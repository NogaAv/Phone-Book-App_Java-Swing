package MidProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class ContactComponent extends JPanel implements ActionListener {

    private Contact contact;
    //holds reference to right info panel in order to display contact info when pressed.
    private RightInfoPanel rightInfoPanel;
    private JLabel fullName;

    public ContactComponent(Contact contact, RightInfoPanel rightInfoPanel){
        this.contact = contact;
        this.rightInfoPanel = rightInfoPanel;
        createEntry();
        add(fullName);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    private void createEntry(){
        fullName = new JLabel(contact.getFirstName() + " " + contact.getLastName());
        fullName.setFont(new Font("Arial", Font.BOLD, 16));
        fullName.setSize(100, 10);

        fullName.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                fullName.setFont(new Font("Arial", Font.PLAIN, 16));
                fullName.setForeground(Color.blue);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                fullName.setFont(new Font("Arial", Font.BOLD, 16));
                fullName.setForeground(Color.black);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                rightInfoPanel.displayContact(contact);
            }
        });
    }

    public String getContactPhone(){
        return contact.getPhoneNumber();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        System.out.println(command);
    }
}
