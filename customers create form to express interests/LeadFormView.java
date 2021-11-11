package group6;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import group8.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

public class LeadFormView extends JFrame {
    private JPanel mainPanel;
    private JLabel requestQuoteLabel;
    private JLabel imageLabel;
    private JLabel carModelLabel;
    private JLabel carColorLabel;
    private JLabel carStockLabel;
    private JLabel carVinLabel;
    private JLabel instructionLabel;
    private JTextField fName;
    private JRadioButton personalUseRadioButton;
    private JRadioButton businessUseRadioButton;
    private JTextArea textArea1;
    private JLabel commentLabel;
    private JLabel zipCodeLabel;
    private JLabel phoneNo;
    private JLabel emailLabel;
    private JLabel lastNameLabel;
    private JLabel firstNameLabel;
    private JButton submitButton;
    private JTextField lName;
    private JTextField eMail;
    private JTextField ph_No;
    private JTextField zipCode;
    private JLabel termsConditionLabel;
    private JLabel mileageLabel;
    private JLabel priceLabel;
    private JLabel errorLabel = new JLabel();
    Car car;
    LeadFormController controller;
    private ButtonGroup buttonGroup;

    private String[] info = new String[11];
    public static File file = new File("LEAD.csv");
    private boolean isErrorDialogShown = false;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Constructor
     * @param car
     * @param formActionDirectory
     */
    public LeadFormView(Car car, FormActionDirectory formActionDirectory) {
        this.car = car;
        this.setContentPane(this.mainPanel);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.pack();
        setProperties();
        this.setValue();
        setSize(new Dimension(510, 800));
        setMaximumSize(new Dimension(510, 800));

        buttonGroup = new ButtonGroup();
        buttonGroup.add(businessUseRadioButton);
        buttonGroup.add(personalUseRadioButton);

        radioButtonActionListener();

        textFieldFocusListener();

        submitButtonActionListener(car);
    }

    private void submitButtonActionListener(Car car) {
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String firstName = fName.getText();
                if (firstName.isEmpty()) {
                    showErrorMessage("Please enter First Name", fName);
                    return;
                }
                String lastName = lName.getText();
                if (lastName.isEmpty()) {
                    showErrorMessage("Please enter Last Name", lName);
                    return;
                }
                String emailId = eMail.getText();
                if (emailId.isEmpty() || !isValidEmail(emailId)) {
                    showErrorMessage("Please enter Valid email", eMail);
                    return;
                }
                String phoneNo = ph_No.getText();
                if (phoneNo.isEmpty() || !isValidPhone(phoneNo)) {
                    showErrorMessage("Please enter Valid Phone Number", ph_No);
                    return;
                }
                String zip = zipCode.getText();
                if (zip.isEmpty() || !isValidZipCode(zip)) {
                    showErrorMessage("Please enter Valid ZipCode", zipCode);
                    return;
                }
                User user = new User(firstName, lastName, emailId, phoneNo, zip);

                String message;
                LeadModel optional;
                if (!textArea1.getText().matches("")) {
                    message = textArea1.getText();
                } else {
                    message = "No Comment";
                }
                if (personalUseRadioButton.isSelected()) {
                    optional = new LeadModel(message, LeadModel.UseType.PERSONAL, user);

                } else if (businessUseRadioButton.isSelected()) {
                    optional = new LeadModel(message, LeadModel.UseType.BUSINESS, user);

                } else {
                    optional = new LeadModel(message, LeadModel.UseType.NO_USE_TYPE, user);
                }
                String encodedMessage = Base64.getEncoder().encodeToString(message.getBytes());

                user.setOptional(optional);
                controller.submitLeadForm(user);
                //write into file
                info[0] = firstName;
                info[1] = lastName;
                info[2] = emailId;
                info[3] = phoneNo;
                info[4] = zip;
                info[5] = optional.getUserType().toString();
                info[6] = encodedMessage;
                info[7] = car.getModel();
                info[8] = car.getColor();
                info[9] = car.getVIN();
                info[10] = car.getStockNum();
                try {
                    write(info);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                //add a notification
                JOptionPane.showMessageDialog(null, "Submit Successfully!");
                clear();
                dispose();
            }
        });
    }

    private void textFieldFocusListener() {
        fName.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                setDefaultBorder(fName);
            }
        });
        lName.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                setDefaultBorder(lName);
            }
        });
        eMail.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (!isValidEmail(eMail.getText())) {
                    showErrorMessage("Enter a valid E-mail ", eMail);
                }
            }

            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                setDefaultBorder(eMail);
            }
        });

        ph_No.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (!isValidPhone(ph_No.getText())) {
                    showErrorMessage("Enter a valid Phone No", ph_No);
                }
            }

            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                setDefaultBorder(ph_No);
            }
        });

        zipCode.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (!isValidZipCode(zipCode.getText())) {
                    showErrorMessage("Enter a valid Zip Code", zipCode);
                }
            }

            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                setDefaultBorder(zipCode);
            }
        });
    }

    private void radioButtonActionListener() {
        businessUseRadioButton.addActionListener(new ActionListener() {
            boolean flag = true;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (businessUseRadioButton.isSelected() == flag) {
                    flag = !(flag);
                } else {
                    buttonGroup.clearSelection();
                    flag = true;

                }
            }
        });

        personalUseRadioButton.addActionListener(new ActionListener() {
            boolean flag = true;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (personalUseRadioButton.isSelected() == flag) {
                    flag = !(flag);
                } else {
                    buttonGroup.clearSelection();
                    flag = true;
                }
            }
        });
    }

    boolean isValidEmail(String eMail) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return eMail.matches(regex);
    }

    boolean isValidPhone(String phoneNo) {
        String regex = "^\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$";
        return phoneNo.matches(regex);
    }

    boolean isValidZipCode(String zipCode) {
        String regex = "\\d{5}";
        return zipCode.matches(regex);
    }

    void setDefaultBorder(JTextField textField) {
        textField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    }

    void setProperties() {
        for (JTextField textField : new JTextField[]{lName, fName, eMail, ph_No, zipCode}) {
            setDefaultBorder(textField);
        }
        requestQuoteLabel.setFont(DealerFont.getTitleFont());
        carModelLabel.setFont(DealerFont.getHeaderFont());
        carColorLabel.setFont(DealerFont.getSubSubTitleFont());
        carStockLabel.setFont(DealerFont.getSubSubTitleFont());
        carVinLabel.setFont(DealerFont.getSubSubTitleFont());
        mileageLabel.setFont(DealerFont.getSubSubTitleFont());
        instructionLabel.setFont(DealerFont.getDescriptionFont());
        firstNameLabel.setFont(DealerFont.getNormalFont());
        fName.setFont(DealerFont.getNormalFont());
        lastNameLabel.setFont(DealerFont.getNormalFont());
        lName.setFont(DealerFont.getNormalFont());
        emailLabel.setFont(DealerFont.getNormalFont());
        eMail.setFont(DealerFont.getNormalFont());
        phoneNo.setFont(DealerFont.getNormalFont());
        ph_No.setFont(DealerFont.getNormalFont());
        zipCodeLabel.setFont(DealerFont.getNormalFont());
        zipCode.setFont(DealerFont.getNormalFont());
        commentLabel.setFont(DealerFont.getNormalFont());
        textArea1.setFont(DealerFont.getNormalFont());
        personalUseRadioButton.setFont(DealerFont.getNormalFont());
        businessUseRadioButton.setFont(DealerFont.getNormalFont());
        submitButton.setFont(DealerFont.getSubTitleFont());
        priceLabel.setFont(DealerFont.getSubSubTitleFont());

        termsConditionLabel.setFont(DealerFont.getDescriptionFont());
    }
    // Car details
    private void setValue() {
        carModelLabel.setText(car.getYear() + " " + car.getMake() + " " + car.getModel());
        carColorLabel.setText("Color: " + car.getColor());
        priceLabel.setText("Price: $" + car.getMSRP());
        carStockLabel.setText(String.format("Stock#: %s", car.getStockNum()));
        carVinLabel.setText("Vin: " + car.getVIN());
        mileageLabel.setText("Mileage: " + car.getMileage() + "Miles");
        instructionLabel.setText("<html>Fill out the contact form below and one of our friendly helpful sales staff will answer <br/> any questions you have about this vehicle.</html>");
        termsConditionLabel.setText("<html>By submitting your request, you consent to be contacted at the phone number you <br/> provided-which may include auto-dials,text messages and/or pre-recorded calls.By <br/> subscribing to receive  recurring SMS offers, you consent to receive text messages <br/> sent  through an automatic telephone dialing system, and message and data rates<br/>may apply. This consent is not a condition of purchase. You may opt out at any time<br/>by replying STOP to the received text message to have your telephone number<br/>removed from our system.</html>");

        String imageName;
        imageName = "src/group6/car_placeholder.png";
        if (car.getImages() != null && car.getImages().size() > 0) {
            imageName = car.getImages().get(0);
        }
        try {
            BufferedImage img = ImageIO.read(new File(imageName));
            Image scaledImage = img.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(),
                    Image.SCALE_SMOOTH);
            ImageIcon image = new ImageIcon(scaledImage);
            imageLabel.setIcon(image);
            imageLabel.setBackground(Color.white);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageLabel.setText("");
    }

    //after error message, border will change to Red color
    private void showErrorMessage(String error, JTextField textField) {
        if (isErrorDialogShown)
            return;
        isErrorDialogShown = true;
        textField.setBorder(BorderFactory.createLineBorder(Color.RED));
        JOptionPane.showMessageDialog(null, error);
        isErrorDialogShown = false;
    }

    //add clear method
    private void clear() {
        carModelLabel.setText("");
        carColorLabel.setText("");
        priceLabel.setText("");
        carStockLabel.setText("");
        carVinLabel.setText("");
        mileageLabel.setText("");
        fName.setText("");
        lName.setText("");
        eMail.setText("");
        ph_No.setText("");
        zipCode.setText("");
        textArea1.setText("");
        imageLabel.setIcon(null);
        buttonGroup.clearSelection();
        info = new String[11];
    }

    // write in file
    public void write(String[] info) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
        StringBuilder sb = new StringBuilder();
        if (!file.exists()) {
            file.createNewFile();
        }
        bw.newLine();
        for (String element : info) {
            sb.append(element);
            sb.append(",");
        }
        bw.write(sb.toString());
        bw.close();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(12, 1, new Insets(20, 20, 20, 20), -1, -1));
        final JScrollPane scrollPane1 = new JScrollPane();
        mainPanel.add(scrollPane1, new GridConstraints(0, 0, 12, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(12, 1, new Insets(0, 0, 0, 0), -1, -1));
        scrollPane1.setViewportView(panel1);
        requestQuoteLabel = new JLabel();
        requestQuoteLabel.setText("Request Quote");
        panel1.add(requestQuoteLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        imageLabel = new JLabel();
        imageLabel.setText("Image View");
        panel2.add(imageLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, 200), new Dimension(200, 200), 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(8, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        carModelLabel = new JLabel();
        carModelLabel.setText("Label");
        panel3.add(carModelLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        carColorLabel = new JLabel();
        carColorLabel.setText("Label");
        panel3.add(carColorLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        carStockLabel = new JLabel();
        carStockLabel.setText("Label");
        panel3.add(carStockLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        carVinLabel = new JLabel();
        carVinLabel.setText("Label");
        panel3.add(carVinLabel, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mileageLabel = new JLabel();
        mileageLabel.setText("Label");
        panel3.add(mileageLabel, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 10), new Dimension(-1, 10), 0, false));
        priceLabel = new JLabel();
        priceLabel.setText("Label");
        panel3.add(priceLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        instructionLabel = new JLabel();
        instructionLabel.setText("Label");
        panel1.add(instructionLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        firstNameLabel = new JLabel();
        firstNameLabel.setText("First Name *");
        panel4.add(firstNameLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        fName = new JTextField();
        fName.setToolTipText("First Name");
        panel4.add(fName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        lastNameLabel = new JLabel();
        lastNameLabel.setText("Last Name *");
        lastNameLabel.setToolTipText("");
        panel5.add(lastNameLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        lName = new JTextField();
        lName.setToolTipText("Last Name");
        panel5.add(lName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel6, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        emailLabel = new JLabel();
        emailLabel.setText("Email *");
        emailLabel.setToolTipText("");
        panel6.add(emailLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        eMail = new JTextField();
        eMail.setToolTipText("Email");
        panel6.add(eMail, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel7, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        phoneNo = new JLabel();
        phoneNo.setText("Phone No *");
        phoneNo.setToolTipText("");
        panel7.add(phoneNo, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        ph_No = new JTextField();
        ph_No.setToolTipText("Phone No");
        panel7.add(ph_No, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel8, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        zipCodeLabel = new JLabel();
        zipCodeLabel.setText("Zip Code *");
        panel8.add(zipCodeLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        zipCode = new JTextField();
        zipCode.setToolTipText("Zip Code");
        panel8.add(zipCode, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel9, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        personalUseRadioButton = new JRadioButton();
        personalUseRadioButton.setText("Personal Use");
        panel9.add(personalUseRadioButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        businessUseRadioButton = new JRadioButton();
        businessUseRadioButton.setText("Business Use");
        panel9.add(businessUseRadioButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel9.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel9.add(spacer4, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel10, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        commentLabel = new JLabel();
        commentLabel.setText("Comments");
        panel10.add(commentLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textArea1 = new JTextArea();
        textArea1.setLineWrap(true);
        panel10.add(textArea1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel11, new GridConstraints(11, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        submitButton = new JButton();
        submitButton.setText("Submit");
        panel11.add(submitButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel11.add(spacer5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel11.add(spacer6, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        termsConditionLabel = new JLabel();
        termsConditionLabel.setText("Label");
        panel1.add(termsConditionLabel, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(personalUseRadioButton);
        buttonGroup.add(businessUseRadioButton);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}

