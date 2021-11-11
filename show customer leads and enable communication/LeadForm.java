package group3;
/**
 * @author  Zhehui Yang
 * @date: 2021/4/15
 */
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class LeadForm extends JComponent implements MouseListener {
    private JPanel mainPanel;
    private JFrame mainFrame;
    private Lead lead;
    private JTextField fn;
    private JTextField ln;
    private JTextField em;
    private JTextField phone;
    private JTextField zipCode;
    private JTextField carModel;
    private JTextField carColor;
    private JTextField carStock;
    private JTextArea setMessage;
    private JLabel firstName;
    private JLabel lastNameLabel;
    private JButton respondButton;
    private JLabel message;
    private JLabel carModelLabel;
    private JLabel colorLabel;
    private JButton respondShort;
    private JLabel emailLabel;
    private JLabel phoneLabel;
    private JLabel zipCodeLabel;
    private JTextField carVinTextField;
    private JLabel carVINLabel;
    private JLabel carStockLabel;

    private boolean isExpanded;

    public void init(JFrame mainFrame, Lead lead) {
        isExpanded = true;
        this.lead = lead;
        this.mainFrame = mainFrame;
        firstName.addMouseListener(this);
        mouseClicked(null);
        setText();
        implementRespond();
    }

    public void setText() {
        fn.setText(lead.getFirstName());
        ln.setText(lead.getLastName());
        em.setText(lead.getEmail());
        phone.setText(lead.getPhoneNumber());
        zipCode.setText(lead.getZipCode());
        carModel.setText(lead.getCarModel());
        carColor.setText(lead.getCarColor());
        carVinTextField.setText(lead.getCarVin());
        carStock.setText(lead.getCarStock());
        setMessage.setText(lead.getMessage());
    }

    public void implementRespond() {
        respondButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RespondDetailsUI r = new RespondDetailsUI(lead);
                r.buildGUI();
            }
        });
        respondShort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RespondDetailsUI r = new RespondDetailsUI(lead);
                r.buildGUI();
            }
        });
    }

    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);
        if (isVisible == true) {
            isExpanded = false;
            fn.setVisible(true);
            ln.setVisible(true);
            firstName.setVisible(true);
            lastNameLabel.setVisible(true);
            mouseClicked(null);
            return;
        }
        fn.setVisible(false);
        ln.setVisible(false);
        em.setVisible(false);
        phone.setVisible(false);
        zipCode.setVisible(false);
        carModel.setVisible(false);
        carColor.setVisible(false);
        carStock.setVisible(false);
        setMessage.setVisible(false);
        firstName.setVisible(false);
        lastNameLabel.setVisible(false);
        respondButton.setVisible(false);
        message.setVisible(false);
        carModelLabel.setVisible(false);
        colorLabel.setVisible(false);
        respondShort.setVisible(false);
        emailLabel.setVisible(false);
        phoneLabel.setVisible(false);
        zipCodeLabel.setVisible(false);
        carVinTextField.setVisible(false);
        carVINLabel.setVisible(false);
        carStockLabel.setVisible(false);
        return;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        isExpanded = !isExpanded;
        zipCodeLabel.setVisible(isExpanded);
        carModelLabel.setVisible(isExpanded);
        carStockLabel.setVisible(isExpanded);
        carVINLabel.setVisible(isExpanded);
        colorLabel.setVisible(isExpanded);
        carVinTextField.setVisible(isExpanded);
        message.setVisible(isExpanded);
        zipCode.setVisible(isExpanded);
        carModel.setVisible(isExpanded);
        carColor.setVisible(isExpanded);
        carStock.setVisible(isExpanded);
        setMessage.setVisible(isExpanded);
        respondButton.setVisible(isExpanded);
        respondShort.setVisible(!isExpanded);
        if (isExpanded == false) {
            //mainPanel.setSize(700, 100);
            if (mainFrame != null) {
                mainFrame.setSize(750, -300);
            }
        } else {
            //mainPanel.setSize(700, 400);
            if (mainFrame != null) {
                mainFrame.setSize(750, 300);
            }
        }
        mainPanel.updateUI();
    }

    public JTextField getFirstName() {
        return fn;
    }

    public JTextField getLastName() {
        return ln;
    }

    public JTextField getEmail() {
        return em;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
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
        mainPanel.setLayout(new GridLayoutManager(8, 6, new Insets(20, 20, 20, 20), -1, -1));
        mainPanel.setPreferredSize(new Dimension(702, 279));
        firstName = new JLabel();
        firstName.setText("First Name: ");
        mainPanel.add(firstName, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(6, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        fn = new JTextField();
        mainPanel.add(fn, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        lastNameLabel = new JLabel();
        lastNameLabel.setText("Last Name: ");
        mainPanel.add(lastNameLabel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ln = new JTextField();
        mainPanel.add(ln, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        emailLabel = new JLabel();
        emailLabel.setText("Email: ");
        mainPanel.add(emailLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        em = new JTextField();
        mainPanel.add(em, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        phone = new JTextField();
        mainPanel.add(phone, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        phoneLabel = new JLabel();
        phoneLabel.setText("Phone #");
        mainPanel.add(phoneLabel, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        zipCodeLabel = new JLabel();
        zipCodeLabel.setText("Zip Code :");
        mainPanel.add(zipCodeLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        zipCode = new JTextField();
        mainPanel.add(zipCode, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        carModelLabel = new JLabel();
        carModelLabel.setText("Car Model : ");
        mainPanel.add(carModelLabel, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        carModel = new JTextField();
        mainPanel.add(carModel, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        colorLabel = new JLabel();
        colorLabel.setText("Color :");
        mainPanel.add(colorLabel, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        carColor = new JTextField();
        mainPanel.add(carColor, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        message = new JLabel();
        message.setText("Message : ");
        mainPanel.add(message, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        respondButton = new JButton();
        respondButton.setText("Respond");
        mainPanel.add(respondButton, new GridConstraints(6, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        mainPanel.add(spacer2, new GridConstraints(7, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        respondShort = new JButton();
        respondShort.setText("Respond");
        mainPanel.add(respondShort, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        setMessage = new JTextArea();
        setMessage.setText("");
        mainPanel.add(setMessage, new GridConstraints(5, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        carStockLabel = new JLabel();
        carStockLabel.setText("Car Stock #");
        mainPanel.add(carStockLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        carStock = new JTextField();
        mainPanel.add(carStock, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        carVINLabel = new JLabel();
        carVINLabel.setText("Car VIN #");
        mainPanel.add(carVINLabel, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        carVinTextField = new JTextField();
        mainPanel.add(carVinTextField, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
