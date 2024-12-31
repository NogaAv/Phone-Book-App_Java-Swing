package MidProject;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static MidProject.InputValidator.*;

public class RightInfoPanel extends JPanel implements ActionListener {

    //holds a reference to the contacts left panel, in order to add/update/remove ContactComponant as needed
    private JPanel leftInnerPanel;
    private JLabel err;
    private Contact displayedContact;

    private JTextField tfirstName, tlastName, temail, tphone, twebAddr, taddr, tnotes;
    private Controller controller;

    public RightInfoPanel(JPanel leftInnerPanel, Controller controller){
        this.controller = controller;
        this.leftInnerPanel = leftInnerPanel;
        setLayout(null);
        setBounds(CommonConst.LEFT_CONTACTS_PANEL.width + 20, 15, CommonConst.RIGHT_INFO_PANEL.width, CommonConst.RIGHT_INFO_PANEL.height);
        Border rightBlackLine = BorderFactory.createTitledBorder("Contact Information");
        setBorder(rightBlackLine);
        createContactForm();

    }

    private void createContactForm(){

        //Error label
        err = new JLabel("", SwingConstants.RIGHT);
        err.setFont(new Font("Arial", Font.BOLD, 16));
        err.setForeground(Color.red);
        err.setSize(CommonConst.RIGHT_INFO_PANEL.width-100, 40);
        err.setLocation(CommonConst.FORM_TEXT_ALIGN - 100 , 0);
        add(err);

        //First Name
        JLabel firstName = new JLabel("First Name: ", SwingConstants.RIGHT);
        firstName.setFont(new Font("Arial", Font.BOLD, 16));
        firstName.setSize(100, 40);
        firstName.setLocation(CommonConst.FORM_TEXT_ALIGN - 100, CommonConst.FORM_ALIGN.height);

        add(firstName);

        tfirstName = new JTextField();
        tfirstName.setFont(new Font("Arial", Font.PLAIN, 15));
        tfirstName.setSize(350, 25);
        tfirstName.setLocation(CommonConst.FORM_TEXT_ALIGN, CommonConst.FORM_ALIGN.height + 10);
        add(tfirstName);

        //Last Name
        JLabel lastName = new JLabel("Last Name: ", SwingConstants.RIGHT);
        lastName.setFont(new Font("Arial", Font.BOLD, 16));
        lastName.setSize(100, 40);
        lastName.setLocation(CommonConst.FORM_TEXT_ALIGN - 100, CommonConst.FORM_ALIGN.height*2 + 10);
        setLayout(null);
        add(lastName);

        tlastName = new JTextField();
        tlastName.setFont(new Font("Arial", Font.PLAIN, 15));
        tlastName.setSize(350, 25);
        tlastName.setLocation(CommonConst.FORM_TEXT_ALIGN, CommonConst.FORM_ALIGN.height*2 + 20);
        add(tlastName);

        //E-mail
        JLabel email = new JLabel("E-mail: ", SwingConstants.RIGHT);
        email.setFont(new Font("Arial", Font.BOLD, 16));
        email.setSize(100, 40);
        email.setLocation(CommonConst.FORM_TEXT_ALIGN - 100, CommonConst.FORM_ALIGN.height*3 + 20);
        setLayout(null);
        add(email);

        temail = new JTextField();
        temail.setFont(new Font("Arial", Font.PLAIN, 15));
        temail.setSize(350, 25);
        temail.setLocation(CommonConst.FORM_TEXT_ALIGN, CommonConst.FORM_ALIGN.height*3 + 30);
        this.add(temail);

        //phone number
        JLabel phone = new JLabel("Phone Number: ", SwingConstants.RIGHT);
        phone.setFont(new Font("Arial", Font.BOLD, 16));
        phone.setSize(200, 40);
        phone.setLocation(CommonConst.FORM_TEXT_ALIGN - 200, CommonConst.FORM_ALIGN.height*4 + 30);
        this.add(phone);

        tphone = new JTextField();
        tphone.setFont(new Font("Arial", Font.PLAIN, 15));
        tphone.setSize(350, 25);
        tphone.setLocation(CommonConst.FORM_TEXT_ALIGN, CommonConst.FORM_ALIGN.height*4 + 40);
        this.add(tphone);

        //web address
        JLabel webAddr = new JLabel("Web Address: ", SwingConstants.RIGHT);
        webAddr.setFont(new Font("Arial", Font.BOLD, 16));
        webAddr.setSize(200, 40);
        webAddr.setLocation(CommonConst.FORM_TEXT_ALIGN - 200, CommonConst.FORM_ALIGN.height*5 + 40);
        this.add(webAddr);

        twebAddr = new JTextField();
        twebAddr.setFont(new Font("Arial", Font.PLAIN, 15));
        twebAddr.setSize(350, 25);
        twebAddr.setLocation(CommonConst.FORM_TEXT_ALIGN, CommonConst.FORM_ALIGN.height*5 + 50);
        this.add(twebAddr);

        //address
        JLabel addr = new JLabel("Address: ", SwingConstants.RIGHT);
        addr.setFont(new Font("Arial", Font.BOLD, 16));
        addr.setSize(100, 40);
        addr.setLocation(CommonConst.FORM_TEXT_ALIGN - 100, CommonConst.FORM_ALIGN.height*6 + 50);
        this.add(addr);

        taddr = new JTextField();
        taddr.setFont(new Font("Arial", Font.PLAIN, 15));
        taddr.setSize(350, 75);
        taddr.setLocation(CommonConst.FORM_TEXT_ALIGN, CommonConst.FORM_ALIGN.height*6 + 60);
        this.add(taddr);

        //notes
        JLabel notes = new JLabel("Notes: ", SwingConstants.RIGHT);
        notes.setFont(new Font("Arial", Font.BOLD, 16));
        notes.setSize(100, 40);
        notes.setLocation(CommonConst.FORM_TEXT_ALIGN - 100, CommonConst.FORM_ALIGN.height*7 + 110);
        this.add(notes);

        tnotes = new JTextField();
        tnotes.setFont(new Font("Arial", Font.PLAIN, 15));
        tnotes.setSize(350, 75);
        tnotes.setLocation(CommonConst.FORM_TEXT_ALIGN, CommonConst.FORM_ALIGN.height*7 + 120);
        this.add(tnotes);

        //buttons
        JButton addBtn = new JButton("Add");
        addBtn.setFont(new Font("Arial", Font.BOLD, 15));
        addBtn.setBounds(CommonConst.FORM_TEXT_ALIGN, CommonConst.RIGHT_INFO_PANEL.height - 50, 100, 30);
        addBtn.setForeground(Color.BLUE);
        addBtn.addActionListener(this);
        this.add(addBtn);

        JButton updateBtn = new JButton("Update");
        updateBtn.setFont(new Font("Arial", Font.BOLD, 15));
        updateBtn.setBounds(CommonConst.FORM_TEXT_ALIGN + 110, CommonConst.RIGHT_INFO_PANEL.height - 50, 100, 30);
        updateBtn.setForeground(Color.BLUE);
        updateBtn.addActionListener(this);
        this.add(updateBtn);


        JButton rmvBtn = new JButton("Remove");
        rmvBtn.setFont(new Font("Arial", Font.BOLD, 15));
        rmvBtn.setBounds(CommonConst.FORM_TEXT_ALIGN + 220, CommonConst.RIGHT_INFO_PANEL.height - 50, 100, 30);
        rmvBtn.setForeground(Color.BLUE);
        rmvBtn.addActionListener(this);
        this.add(rmvBtn);

        //TODO: Add 'reset' button to clear all form data
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch(e.getActionCommand().toLowerCase()){
            case "add":
                if(addContact() == true)
                    err.setText("");
                break;
            case "update":
                if(updateContact() == true)
                    err.setText("");
                break;
            case "remove":
                if(removeContact() == true)
                    err.setText("");
                break;
            default:{
                    System.out.println("RightInfoPanel: Invalid button command");
                }
        }
    }

    private boolean addContact(){

        if(validateInfoInput() == false)
            return false;

        //If we got to here - then all required fields are valid.
        //Check By phone-Number if this contact already exists:
        Contact newContact = Contact.createContact(tfirstName.getText(), tlastName.getText(), tphone.getText(),
                                        temail.getText(), twebAddr.getText(), taddr.getText(), tnotes.getText());
        if(newContact == null){
            err.setText("* Failed to create new contact");
            return false;
        }
        if(controller.addContactToBook(newContact) == false){
            err.setText("* Contact with the same phone already exist!");
            return false;
        }else{
            //adding contact to the left contacts panel:
            ContactComponent cc = new ContactComponent(newContact, this);
            leftInnerPanel.add(cc);
            resetContactDisplayed();
            return true;
        }
    }

    public boolean updateContact(){
        if(validateInfoInput() == false)
            return false;

        Contact updatedContact = Contact.createContact(tfirstName.getText(), tlastName.getText(), tphone.getText(),
                                 temail.getText(), twebAddr.getText(), taddr.getText(), tnotes.getText());


        if(displayedContact == null){
            err.setText("* Choose Contact to update!");
            return false;
        }

        if(controller.updateContactInBook(this.displayedContact, updatedContact) == false){
            err.setText("* Failed to update contact info");
            return false;
        }

        //updating contact on the left contacts panel:
        Component[] contactsComponents = leftInnerPanel.getComponents();
        for(Component cc : contactsComponents){
            if(((ContactComponent)cc).getContactPhone().equals(displayedContact.getPhoneNumber())){
                leftInnerPanel.remove(cc);
                leftInnerPanel.add((new ContactComponent(updatedContact, this)));
                leftInnerPanel.repaint();
                break;
            }
        }
        resetContactDisplayed();
        return true;
    }

    public boolean removeContact(){

        if(displayedContact == null){
            err.setText("* Choose Contact to be removed!");
            return false;
        }
        if(controller.removeContactFromBook(this.tphone.getText()) == false){
            err.setText("* Contact failed to be removed!");
            return false;
        }else{
            //removing contact from the left contacts panel:
            Component[] contactsComponents = leftInnerPanel.getComponents();
            for(Component cc : contactsComponents){
                if(((ContactComponent)cc).getContactPhone().equals(this.tphone.getText())){
                    leftInnerPanel.remove(cc);
                    break;
                }
            }
            resetContactDisplayed();
            return true;
        }
    }

    private boolean validateInfoInput(){
        //Validate phone number:
        String phoneNumber = tphone.getText().trim();

        switch(validatePhoneNumber(phoneNumber)){
            case ERR_EMPTY:
                err.setText("* Phone Number field is required!");
                return false;
            case INVALID_INPUT:
                err.setText("* Invalid Phone Number!");
                return false;
            case INVALID_CHAR_INPUT:
                err.setText("* Phone Number must contain digits only!");
                return false;
        }

        //Validate First Name and Last Name
        String tFirstName = tfirstName.getText();
        if(validateTextInput(tFirstName) == InputErrCode.ERR_EMPTY){
            err.setText("* First Name field is required!");
            return false;
        }

        String tLastName = tlastName.getText();
        if(validateTextInput(tLastName) == InputErrCode.ERR_EMPTY){
            err.setText("* Last Name field is required!");
            return false;
        }

        String email = temail.getText().trim();
        if(!email.isEmpty() && validateEmail(temail.getText()) != InputErrCode.VALID_INPUT){
            err.setText("* Invalid E-mail!");
            return false;
        }

        return true;
    }
    public void displayContact(Contact contact){
        displayedContact = contact;
        tfirstName.setText(contact.getFirstName());
        tlastName.setText(contact.getLastName());
        tphone.setText(contact.getPhoneNumber());
        temail.setText(contact.getEmail());
        twebAddr.setText(contact.getWebAddress());
        taddr.setText(contact.getAddress());
        tnotes.setText(contact.getNotes());

        repaint();
    }

    private void resetContactDisplayed(){
        displayedContact = null;

        this.tphone.setText("");
        this.tfirstName.setText("");
        this.tlastName.setText("");
        this.tnotes.setText("");
        this.taddr.setText("");
        this.temail.setText("");
        this.twebAddr.setText("");

        leftInnerPanel.setLayout(new BoxLayout(leftInnerPanel, BoxLayout.Y_AXIS));
        leftInnerPanel.repaint();
        leftInnerPanel.revalidate();
    }
}
