package MidProject;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PhoneBookGUI extends JFrame implements ActionListener {

    private JPanel mainPanel, leftContactsPanel, leftInnerPanel;
    private RightInfoPanel rightInfoPanel;
    private Controller controller;

   public PhoneBookGUI(Controller controller){
       super("Java Swing Contact");
       this.controller = controller;

       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setPreferredSize(CommonConst.GUI_SIZE);
       pack();
       setLocationRelativeTo(null);
       setResizable(false);
       setLayout(null);

       createGui();
   }

   private void createGui(){

       mainPanel = new JPanel();
       mainPanel.setBounds(0, 0, CommonConst.GUI_SIZE.width, CommonConst.GUI_SIZE.height);
       mainPanel.setLayout(null);

       leftContactsPanel = new JPanel();
       Border leftBlackLine = BorderFactory.createTitledBorder("Contacts");

       leftInnerPanel = new JPanel();
       rightInfoPanel = new RightInfoPanel(leftInnerPanel, controller);

       leftContactsPanel.add(leftInnerPanel);

       // add scrolling to the left contacts panel
       JScrollPane scrollPane = new JScrollPane(leftInnerPanel);
       scrollPane.setBounds(12, 20, CommonConst.CONTACTS_PANEL.width - 100, CommonConst.CONTACTS_PANEL.height);
       Border blackline = BorderFactory.createLineBorder(Color.black);
       scrollPane.setBorder(blackline);
       scrollPane.setBackground(Color.white);
       scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
       scrollPane.setMaximumSize(CommonConst.CONTACTS_PANEL);
       scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
       scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
       leftContactsPanel.add(scrollPane);
       leftContactsPanel.setLayout(null);
       rightInfoPanel.setLayout(null);

       leftContactsPanel.setBounds(15, 15, CommonConst.LEFT_CONTACTS_PANEL.width, CommonConst.LEFT_CONTACTS_PANEL.height);
       leftContactsPanel.setBorder(leftBlackLine);
       mainPanel.add(leftContactsPanel);
       mainPanel.add(rightInfoPanel);

       this.getContentPane().add(mainPanel);

       uploadContactsFromBook();

   }

   private boolean uploadContactsFromBook(){
       ArrayList<Contact> contacts = (ArrayList<Contact>) controller.getContactsFromBook();
       if(contacts == null){
           System.out.println("Failed to upload contacts to left contacts panel");
           return false;
       }

       ContactComponent cc = null;
       for(Contact contact : contacts){
           cc = new ContactComponent(contact, rightInfoPanel);
           leftInnerPanel.add(cc);
       }
       leftInnerPanel.setLayout(new BoxLayout(leftInnerPanel, BoxLayout.Y_AXIS));
       leftInnerPanel.repaint();
       leftInnerPanel.revalidate();
       return true;
   }
    @Override
    public void actionPerformed(ActionEvent e) {
        //currently not used
    }
}
