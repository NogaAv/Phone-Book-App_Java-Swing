package MidProject;

import javax.swing.*;
import java.util.List;

public class Controller {

    private PhoneBook phoneBook;
    private JFrame phoneBookGUI;

    public Controller(){
        phoneBook = new PhoneBook();
    }

    public Controller(String fileName){
        phoneBook = new PhoneBook(fileName);
    }

    public void appInit(){
        phoneBookGUI = new PhoneBookGUI(this);
        //read from file to gui (Read should not be delegated to thread, since it should be serial)
        phoneBookGUI.setVisible(true);
    }

    public boolean addContactToBook(Contact contact){
        //note: controller adds a new copied Contact object to the secured backend ArrayList
        return this.phoneBook.addContact(new Contact(contact));
    }

    public boolean updateContactInBook(Contact oldContact, Contact updatedContact){
        //note: controller adds a new copied Contact object to the secured backend ArrayList
        return this.phoneBook.updateContact(oldContact, new Contact(updatedContact));
    }

    public boolean removeContactFromBook(String phoneNumber){
        return this.phoneBook.removeContact(phoneNumber);
    }

    public List<Contact> getContactsFromBook() {
        return phoneBook.getContacts();
    }
}
