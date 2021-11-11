package group3;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;


public class RespondDetailsUI {

    private Lead lead;

    private static int SAVE_TIME_INTERVAL = 60;
    private static String[] TAB_NAMES = {"Customer info",  "Comments message"};

    private JTextArea commentsMessageTextArea;
    private JTextArea commentsMessageReplyTextArea;
    private JButton replyButton;
    private JTabbedPane mainPanel;
    private JFrame theFrame;


    public RespondDetailsUI(Lead lead) {
        this.lead = lead;
    }

    /*public static void main (String[] args) {

        // use your own file routes


        LeadDataAccessor dataAccessor = new LeadDataAccessor("D:\\NewVehicleData.csv");
        RespondDetailsUI r = new RespondDetailsUI(dataAccessor.getLeads().get(0));
        r.buildGUI();
    }*/

    public void buildGUI () {
        theFrame = new JFrame("Respond Window");
        theFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainPanel = new JTabbedPane();
        layoutComponents();
        theFrame.getContentPane().add(mainPanel);
        theFrame.setPreferredSize(new Dimension(800, 600));
        theFrame.pack();
        theFrame.setLocationRelativeTo(null);
        theFrame.setVisible(true);

    }

    private void layoutComponents() {
        int tabIndex = 0;



        /**
         * customer info panel
         */
        JPanel leadInfoPanel = new JPanel();
        fillLeadPanel(leadInfoPanel);
        changeFont(leadInfoPanel, new Font("Baskerville", Font.PLAIN, 18));
        mainPanel.addTab(TAB_NAMES[tabIndex++], null, leadInfoPanel, "first");



        /**
         * comments message panel
         */
        JPanel commentsMessagePanel = new JPanel();
        fillCommentsMessagePanel(commentsMessagePanel);
        mainPanel.addTab(TAB_NAMES[tabIndex++], null, commentsMessagePanel, "third");


    }

    private void fillLeadPanel(JPanel leadInfoPanel) {
        leadInfoPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        GridLayout grid = new GridLayout(0,2);
        grid.setHgap(1);
        grid.setVgap(1);
        /**
         * fill first name
         */
        leadInfoPanel.setLayout(grid);
        JLabel firstNameLabel = new JLabel();
        firstNameLabel.setText("First Name: ");
        leadInfoPanel.add(firstNameLabel);
        JLabel firstName = new JLabel();
        firstName.setText(lead.getFirstName());
        leadInfoPanel.add(firstName);
        /**
         * fill last name
         */
        JLabel lastNameLabel = new JLabel();
        lastNameLabel.setText("Last Name: ");
        leadInfoPanel.add(lastNameLabel);
        JLabel lastName = new JLabel();
        lastName.setText(lead.getLastName());
        leadInfoPanel.add(lastName);
        /**
         * fill phone number
         */
        JLabel phoneLabel = new JLabel();
        phoneLabel.setText("Phone Number: ");
        leadInfoPanel.add(phoneLabel);
        JLabel phone = new JLabel();
        phone.setText(lead.getPhoneNumber());
        leadInfoPanel.add(phone);
        /**
         * fill email
         */
        JLabel emailLabel = new JLabel();
        emailLabel.setText("Email: ");
        leadInfoPanel.add(emailLabel);
        JLabel email= new JLabel();
        email.setText(lead.getEmail());
        leadInfoPanel.add(email);
        /**
         * fill zip code
         */
        JLabel zipCodeLabel = new JLabel();
        zipCodeLabel.setText("Zip Code: ");
        leadInfoPanel.add(zipCodeLabel);
        JLabel zipCode= new JLabel();
        zipCode.setText(lead.getZipCode());
        leadInfoPanel.add(zipCode);

    }



    public void fillCommentsMessagePanel(JPanel commentsMessagePanel) {

        commentsMessagePanel.setLayout(new BorderLayout());

        GridLayout commentsMessagegrid = new GridLayout(2,1);
        commentsMessagegrid.setVgap(10);
        commentsMessagegrid.setHgap(1);
        JPanel commentsMessageSubPanel = new JPanel(commentsMessagegrid);
        /**
         * comments Message
         */
        commentsMessageTextArea = new JTextArea(12,40);
        commentsMessageTextArea.setEditable(false);
        commentsMessageTextArea.setLineWrap(true);
        commentsMessageTextArea.setText(lead.getMessage());

        JScrollPane commentsMessageTextScroller = new JScrollPane(commentsMessageTextArea);
        commentsMessageTextScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        commentsMessageTextScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        /**
         * reply notes
         */
        commentsMessageReplyTextArea = new JTextArea();
        commentsMessageReplyTextArea.setLineWrap(true);
        if (lead.getReplyMessage() != null && !lead.getReplyMessage().isEmpty()) {
            commentsMessageReplyTextArea.setText(lead.getReplyMessage());
        }

        JScrollPane commentsMessageReplyTextScroller = new JScrollPane(commentsMessageReplyTextArea);
        commentsMessageReplyTextScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        commentsMessageReplyTextScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        commentsMessageSubPanel.add(commentsMessageTextScroller);
        commentsMessageSubPanel.add(commentsMessageReplyTextScroller);

        commentsMessagePanel.add(BorderLayout.NORTH, commentsMessageSubPanel);
        commentsMessagePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        replyButton = new JButton("Reply");
        replyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (commentsMessageReplyTextArea.getText().trim().isEmpty()) {
                    JDialog dialog = new JDialog();
                    dialog.setAlwaysOnTop(true);
                    JOptionPane.showConfirmDialog(dialog, "The message is empty!",
                            "Warning",JOptionPane.WARNING_MESSAGE);
                    theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    theFrame.setVisible(false);
                } else {
                    int result = JOptionPane.showConfirmDialog(theFrame,
                            "Do you want to continue sending the message",
                            "Reply window",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if(result == JOptionPane.YES_OPTION){
                        lead.setReplyMessage(commentsMessageReplyTextArea.getText());
                        replyButton.setText("message sent");
                        sendMessage(lead, commentsMessageReplyTextArea.getText());
                        JDialog dialog = new JDialog();
                        dialog.setAlwaysOnTop(true);
                        JOptionPane.showConfirmDialog(dialog, "The message is sent",
                                "Message",JOptionPane.DEFAULT_OPTION);
                        lead.setContacted(true);
                        commentsMessageReplyTextArea.setText(null);
                        lead.setReplyMessage("");
                        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        theFrame.setVisible(false);


                    }
                }
            }
        });



        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));

        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(replyButton);

        commentsMessagePanel.add(BorderLayout.PAGE_END, buttonPane);
    }




    private JButton makeNavigationButton(String actionCommand, String toolTipText) {
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        button.setText(actionCommand);
        return button;
    }

    private void sendMessage(Lead lead, String message) {

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileOutputStream("RespondData.csv",true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder csvData = new StringBuilder("");

        // write data
        csvData.append(lead.getFirstName()).append(",");
        csvData.append(lead.getLastName()).append(",");
        csvData.append(lead.getEmail()).append(",");
        csvData.append(lead.getMessage()).append(",");
        csvData.append(lead.getReplyMessage());
        csvData.append('\n');
        pw.write(csvData.toString());
        pw.close();

    }

    private static void changeFont ( Component component, Font font )
    {
        component.setFont ( font );
        if ( component instanceof Container )
        {
            for ( Component child : ( ( Container ) component ).getComponents () )
            {
                changeFont ( child, font );
            }
        }
    }

}
