package MidProject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

enum FileExt{CSV, JSON};
enum ReqOperation{ADD, UPDATE, REMOVE};

public class PhoneBook {

    private List<Contact> contacts;
    private String phoneBookFileName;
    //instance fields for writing to file:
    private FileExt fileExt;
    private LinkedList<Request> writeRequests;
    private ReentrantLock writeLock;
    private Condition condition;
    private Thread writingThread;


    public PhoneBook(String phoneBookFileName) {
        this.contacts = new ArrayList<Contact>();
        this.phoneBookFileName = phoneBookFileName;
        fileExt = phoneBookFileName.endsWith(".csv") ? FileExt.CSV : FileExt.JSON;
        uploadContactsFromFile();
        writeRequests = new LinkedList<Request>();
        writeLock = new ReentrantLock();
        condition = writeLock.newCondition();
        writingThread = null;
    }
    public PhoneBook() {
        //File type defaults to .csv
        this("data.csv");
    }

    private void uploadContactsFromFile(){
        switch(fileExt){
            case CSV:
                if(uploadCSVfile() == false){
                    System.out.println("Failed to upload data from file: " + phoneBookFileName);
                }
                break;
            case JSON:
                if(uploadJSONfile() == false){
                    System.out.println("Failed to upload data from file: " + phoneBookFileName);
                }
                break;
            default:{
                System.out.println("Invalid file extension");
                }
        }
    }

    public boolean uploadCSVfile(){
        String line = "";
        String splitBy = ",";
        BufferedReader br = null;
        try
        {
            File file = new File(this.phoneBookFileName);
            if(!file.exists()){
                if(file.createNewFile())
                    System.out.println("File " + phoneBookFileName + " was created successfully!");
                return true;
            }

            //parsing a CSV file into BufferedReader class constructor
            br = new BufferedReader(new FileReader(file));

            //First line in CSV file is skipped, since it contains headers and not the data.
            br.readLine();

            //Columns in csv file should be arranged in the following order:
            // String firstName, String lastName, String phoneNumber, String email, String webAddress, String address, String notes
            while ((line = br.readLine()) != null){   //returns a Boolean value
                if(line.isEmpty())
                    continue;
                String[] contactInfo = line.split(splitBy);    // use comma as separator
                if(contactInfo.length < 7)
                    continue;
                Contact contactRead = Contact.createContact(contactInfo[0], contactInfo[1], contactInfo[2], contactInfo[3],
                                     contactInfo[4], contactInfo[5], contactInfo[6]);
                if(contactRead != null){
                    contacts.add(contactRead);
                }
                System.out.println("Date Read: " + contactInfo[0] + ", " + contactInfo[1] + "," + contactInfo[2] + "," +
                        ", " + contactInfo[3] + ", " + contactInfo[4] + ", " + contactInfo[5] + "," + ", " + contactInfo[6]);
            }
            return true;
        }catch (IOException e ){
            e.printStackTrace();
            return false;
        }finally {
            try{
                if(br != null)
                    br.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public boolean uploadJSONfile(){

        BufferedReader br = null;
        try
        {
            File file = new File(this.phoneBookFileName);
            if(!file.exists()){
                if(file.createNewFile())
                    System.out.println("File " + phoneBookFileName + " was created successfully!");
                return true;
            }

            //parsing array of json-objects (-contacts) from file:
            Object obj = new JSONParser().parse(new FileReader(phoneBookFileName));

            //typecasting obj to JSONObject
            JSONObject jo = (JSONObject) obj;
            JSONArray jarray = (JSONArray)jo.get("contacts");

            //iterating contacts objects:
            Iterator itr = jarray.iterator();

            while (itr.hasNext()){
                JSONObject jobj = (JSONObject) itr.next();
                Contact contact = Contact.createContact((String)jobj.get(JsonKeyConst.firstName),
                        (String)jobj.get(JsonKeyConst.lastName), (String)jobj.get(JsonKeyConst.phoneNumber),
                        (String)jobj.get(JsonKeyConst.email), (String)jobj.get(JsonKeyConst.webAddress),
                        (String)jobj.get(JsonKeyConst.address), (String)jobj.get(JsonKeyConst.notes));
                if(contact != null){
                    contacts.add(contact);
                }else{
                    System.out.println("Failed to parse contact from JSON file");
                };
            }
            return true;
        }catch (IOException e){
            System.out.println("Failed uploading data from file");
            return false;
        }catch(ParseException e){
            System.out.println("File empty. No data to parse");
            return true;
        } finally {
            try{
                if(br != null)
                    br.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public boolean addContact(Contact newContact){

        if(findContactByPhone(newContact.getPhoneNumber()) != null){
            System.out.println("Contact was not added. Contact with the same phone number already exists.");
            return false;
        }
        this.contacts.add(newContact);
        writeContactToFile(new Request(newContact, ReqOperation.ADD));

        return true;
    }

    public Contact findContactByPhone(String phoneNumber){
        for(Contact c : contacts){
            if(c.getPhoneNumber().equals(phoneNumber)){
                return c;
            }
        }
        return null;
    }

    public boolean updateContact(Contact oldContact, Contact updatedContact){
        boolean updateSuccess = false;
        Contact foundContactInList = findContactByPhone(oldContact.getPhoneNumber());
        if(foundContactInList == null){
            System.out.println("Contact with that phone number doesn't exist in Contacts List");
            return false;
        }
        updateSuccess = contacts.remove(foundContactInList);
        updateSuccess = updateSuccess && contacts.add(updatedContact);
        writeContactToFile(new Request(foundContactInList, updatedContact, ReqOperation.UPDATE));

        return updateSuccess;
    }

    public boolean removeContact(Contact contact){
        if(contact != null){
            return contacts.remove(contact);
        }
        return false;
    }

    public boolean removeContact(String phoneNumber){
        Contact contact = findContactByPhone(phoneNumber);
        if(contact != null){
            writeContactToFile(new Request(contact, ReqOperation.REMOVE));
            return contacts.remove(contact);
        }
        return false;
    }

    public List<Contact> getContacts() {
        return new ArrayList<>(this.contacts);
    }

    public String getPhoneBookFileName() {
        return phoneBookFileName;
    }

    private void writeContactToFile(Request writeRequest) {
        if(writingThread == null){
            writingThread = new Thread(new WritingThread(phoneBookFileName, writeRequests, writeLock, condition));
            writingThread.start();
        }

        //This thread shares lock with the writing thread in order to protect
        //modification of the LinkedList by both threads concurrently
        try{
            writeLock.lock();
            writeRequests.addLast(writeRequest);
        }finally{
            condition.signal();
            writeLock.unlock();
        }
    }


    class Request {
        private final ReqOperation operation;
        private final Contact contact;
        private final Contact oldContact;

        public Request(Contact contact, ReqOperation operation) {
            this.operation = operation;
            this.contact = contact;
            this.oldContact = null;
        }

        //This Ctor will be used for 'UPDATE' operation, since updating contact in file
        //requires finding the former not-updated contact and then replace it with new contact
        public Request(Contact oldContact, Contact newContact, ReqOperation operation) {
            this.operation = operation;
            this.contact = newContact;
            this.oldContact = oldContact;
        }

        public ReqOperation getOperation() {
            return operation;
        }

        public Contact getContact() {
            return contact;
        }

        public Contact getOldContact() {
            return oldContact;
        }
    }

}
