package MidProject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class WritingThread implements Runnable{

    private String fileName;
    private LinkedList<PhoneBook.Request> writeRequests;
    private ReentrantLock writeLock;
    private Condition condition;
    private PhoneBook.Request currentRequest;

    public WritingThread(String fileName, List<PhoneBook.Request> writeRequests, ReentrantLock writeLock, Condition condition){
        this.fileName = fileName;
        this.writeRequests = (LinkedList<PhoneBook.Request>)writeRequests;
        this.writeLock = writeLock;
        this.condition = condition;
        this.currentRequest = null;
    }

    @Override
    public void run() {
        //Create file to write to, in case it doesn't exist:
        File file = new File(fileName);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Failed to create file: " + fileName +
                        ". Writing thread cannot operate");
                return;
            }
        }

        while(true){
            writeLock.lock();
            while(writeRequests.isEmpty()){
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    System.out.println("Writing thread was interrupted");
                }
            }
            try {
                writeLock.lock();
                //acquiring lock for the removal from the thread-unsafe LinkedList,
                //and protecting this resource that is also modified by the main thread.
                currentRequest = writeRequests.removeFirst();
            }catch(NoSuchElementException e){
                System.out.println("No request found to process");
                continue;
            }finally {
                writeLock.unlock();
            }

            processRequest();
        }
    }

    private void processRequest(){
        switch(currentRequest.getOperation()){
            case ADD:
                if(addToFile(currentRequest.getContact()) == false)
                    System.out.println("Faild: processing request for adding contact to file");
                break;
            case REMOVE:
                if(removeFromFile(currentRequest.getContact()) == false)
                    System.out.println("Failed: processing request for removing contact from file");
                break;
            case UPDATE:
                if(updateInFile(currentRequest.getOldContact(), currentRequest.getContact()) == false)
                    System.out.println("Failed: processing request for updating contact in file");
                break;
            default:{
                System.out.println("Error: Invalid process request operation.");
            }
        }
    }

    private boolean addToFile(Contact contact){
        BufferedWriter bw = null;
        String lineToAdd = "";

            if(fileName.endsWith(".csv")) {
                //Building the csv line to be inserted
                lineToAdd = createLine(contact, ",") + System.lineSeparator();

                try {
                    bw = new BufferedWriter(new FileWriter(fileName, true));
                    bw.append(lineToAdd);
                } catch (IOException e) {
                    System.out.println("Failed to add line to csv file: " + e.getMessage());
                    return false;
                } finally {
                    try {
                        if (bw != null)
                            bw.close();
                    } catch (IOException e) {
                        System.out.println("Failed to close file " + fileName + ": " + e.getMessage());
                        return false;
                    }
                }
            }else if(fileName.endsWith(".json")){
                //Creating JSONObject to be inserted to file:
                JSONObject jContact = new JSONObject();

                //putting data to JSONObject  => JSONObject is UNORDERED collection
                // (i.e- order of insertions are not guaranteed)
                jContact.put(JsonKeyConst.firstName, contact.getFirstName() );
                jContact.put(JsonKeyConst.lastName, contact.getLastName());
                jContact.put(JsonKeyConst.phoneNumber, contact.getPhoneNumber());
                jContact.put(JsonKeyConst.email, contact.getEmail());
                jContact.put(JsonKeyConst.webAddress, contact.getWebAddress());
                jContact.put(JsonKeyConst.address, contact.getAddress());
                jContact.put(JsonKeyConst.notes, contact.getNotes());

                File inputFile = null;
                File tempFile = null;
                BufferedReader br = null;
            try{
                inputFile = new File(fileName);
                tempFile = new File("Temp" + fileName);

                br = new BufferedReader(new FileReader(inputFile));
                bw = new BufferedWriter(new FileWriter(tempFile));

                //parsing array of json-objects (-contacts) from file:
                JSONObject jo = null;
                JSONArray jarray = null;
                Object obj = null;
                boolean parsSuccess = false;

                try{
                    obj = new JSONParser().parse(new FileReader(fileName));
                    jo = (JSONObject) obj;
                    jarray = (JSONArray)jo.get(JsonKeyConst.contacts);
                    parsSuccess = true;
                }catch(ParseException e){
                    parsSuccess = false;
                }

                if(!parsSuccess){
                     jo = new JSONObject();
                     jarray = new JSONArray();
                }
                jarray.add(jContact);
                jo.put(JsonKeyConst.contacts, jarray);
                bw.write(jo.toJSONString() + System.lineSeparator());
                boolean success = tempFile.renameTo(inputFile);

            } catch (IOException e) {
                System.out.println("Failed to add line to json file: " + e.getMessage());
                return false;
            } finally {
                if (bw != null){
                    try{
                        bw.close();
                        br.close();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    private boolean removeFromFile(Contact contact){
        String lineToRemove = "";
        String currentLine = "";
        BufferedReader br = null;
        BufferedWriter bw = null;
        File inputFile = null;
        File tempFile = null;
        if(fileName.endsWith(".csv")) {
            lineToRemove = createLine(contact, ",");

            try{
                inputFile = new File(fileName);
                tempFile = new File("Temp" + fileName);

                br = new BufferedReader(new FileReader(inputFile));
                bw = new BufferedWriter(new FileWriter(tempFile));

                while((currentLine = br.readLine()) != null) {
                    String trimmedLine = currentLine.trim();
                    if(trimmedLine.equals(lineToRemove))
                        continue;
                    bw.write(/*System.lineSeparator() +*/ currentLine + System.lineSeparator());
                }
                br.close();
                bw.close();
                br = new BufferedReader(new FileReader(tempFile));
                bw = new BufferedWriter(new FileWriter(inputFile));

                while((currentLine = br.readLine()) != null) {
                    bw.write(currentLine + System.lineSeparator());
                }
                return true;
            }catch(IOException e){
                System.out.println("Failed removing contact from file: " + e.getMessage());
                return false;
            }finally{
                if(bw != null)
                    try{
                        bw.close();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                if(br != null)
                    try{
                        br.close();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
//                tempFile.renameTo(inputFile);
            }
            //TODO:Remove contact from json file
        }else if(fileName.endsWith(".json")){

        }
        return true;
    }

    //Note: Update of contact can be performed on his phone-number, Therefore
    //      we also need to pass the old phone-number
    private boolean updateInFile(Contact oldContact, Contact newContact){
        String lineToReplace = "";
        String lineUpdated = "";

        String currentLine = "";
        BufferedReader br = null;
        BufferedWriter bw = null;

        if(fileName.endsWith(".csv")) {
            lineToReplace = createLine(oldContact, ",");
            lineUpdated = createLine(newContact, ",");

            if(lineToReplace.equals(lineUpdated)){
                System.out.println("No update needed in file. Contact data not changed");
                return true;
            }

            try{
                File inputFile = new File(fileName);
                File tempFile = new File("Temp" + fileName);

                br = new BufferedReader(new FileReader(inputFile));
                bw = new BufferedWriter(new FileWriter(tempFile));

                while((currentLine = br.readLine()) != null) {
                    String trimmedLine = currentLine.trim();
                    if(trimmedLine.equals(lineToReplace)){
                        bw.write(lineUpdated + System.lineSeparator());
                        continue;
                    }else if(currentLine != ""){
                        bw.write(currentLine + System.lineSeparator());
                    }
                }
                bw.close();
                br.close();
             //   tempFile.renameTo(inputFile);  //-This may issue a permission problem and file will not rename
                br = new BufferedReader(new FileReader(tempFile));
                bw = new BufferedWriter(new FileWriter(inputFile));

                while((currentLine = br.readLine()) != null) {
                    bw.write(currentLine + System.lineSeparator());
                }

                return true;
            }catch(IOException e){
                System.out.println("Failed updating contact from file: " + e.getMessage());
                return false;
            }finally{
                if(bw != null)
                    try{
                        bw.close();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                if(br != null)
                    try{
                        br.close();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
            }
            //TODO:Update contact from json file
        }else if(fileName.endsWith(".json")){

        }
        return true;
    }

    private String createLine(Contact contact, String delimeter){
        return   contact.getFirstName().trim() + delimeter + contact.getLastName().trim() +
                 delimeter + contact.getPhoneNumber().trim() + delimeter + contact.getEmail().trim() + delimeter +
                 contact.getWebAddress().trim() + delimeter + contact.getAddress().trim() +
                 delimeter + contact.getNotes().trim();
    }
}
