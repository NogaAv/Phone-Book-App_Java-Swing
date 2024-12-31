package MidProject;


public class Contact {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String WebAddress;
    private String Address;
    private String notes;

    private Contact(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    private Contact(String firstName, String lastName, String email, String phoneNumber, String webAddress, String address, String notes) {
        this(firstName, lastName, phoneNumber);
        this.email = email;
        WebAddress = webAddress;
        Address = address;
        this.notes = notes;
    }
    //Copy Ctor
    public Contact(Contact contact){
        this.firstName = contact.getFirstName();
        this.lastName = contact.getLastName();
        this.phoneNumber = contact.getPhoneNumber();
        this.email = contact.getEmail();
        this.Address = contact.getAddress();
        this.WebAddress = contact.getWebAddress();
        this.notes = contact.getNotes();
    }

    //Factory method - to guarantee providing at least "firstName", "lastName" and "PhoneNumber"
    public static Contact createContact(String firstName, String lastName, String phoneNumber){
        return createContact(firstName, lastName, phoneNumber, "", "", "", "" );
    }

    public static Contact createContact(String firstName, String lastName, String phoneNumber, String email, String webAddress, String address, String notes){
        firstName = firstName.trim();
        lastName = lastName.trim();
        phoneNumber = phoneNumber.trim();
        email = email.trim();
        webAddress = webAddress.trim();
        address = address.trim();
        notes = notes.trim();

        InputErrCode firstNameValidation = InputValidator.validateTextInput(firstName);
        InputErrCode lastNameValidation = InputValidator.validateTextInput(lastName);

        if( firstNameValidation != InputErrCode.VALID_INPUT ||
            lastNameValidation != InputErrCode.VALID_INPUT){
            System.out.println("createContact() failed in 'InputValidator.validateTextInput()' with Error codes: "
                               + firstNameValidation.name() +", " + lastNameValidation.name());
            return null;
        }

        InputErrCode phonevalidation = InputValidator.validatePhoneNumber(phoneNumber);
        if( phonevalidation != InputErrCode.VALID_INPUT){
            System.out.println("createContact() failed in 'InputValidator.validatePhoneNumber()' with Error code: "
                               + phonevalidation.name());
            return null;
        }

        if(!email.isEmpty()){
            InputErrCode emailValidation = InputValidator.validateEmail(email);
            if( emailValidation != InputErrCode.VALID_INPUT){
                System.out.println("createContact() failed in 'InputValidator.validateEmail()' with Error code: "
                        + emailValidation.name());
                return null;
            }
        }
        return new Contact(firstName, lastName, email, phoneNumber, webAddress, address, notes);
    }

    //setters and getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getWebAddress() {
        return WebAddress;
    }

    public String getAddress() {
        return Address;
    }

    public String getNotes() {
        return notes;
    }

    //setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setWebAddress(String webAddress) {
        WebAddress = webAddress;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
