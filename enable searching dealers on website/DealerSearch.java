package group1;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import group5.ui.FilterAndBrowseUI;
import group8.Dealer;
import group8.DealerDirectory;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class DealerSearch extends JFrame {
    private JPanel mainPanel;
    private JButton searchButton;
    private JTextField queryTextField;
    private JRadioButton stateRadioButton;
    private JRadioButton zipCodeRadioButton;
    private JRadioButton dealerNameRadioButton;
    private JTable dealerTable;
    private JLabel validationText;
    private JPanel searchPanel;
    private JPanel searchContainerPanel;
    private JPanel listDealersPanel;

    public DealerSearch() throws HeadlessException {
        $$$setupUI$$$();
        setQueryTextFieldTooltip("State/State code");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                boolean isValidQuery = false;
                DefaultTableModel model = (DefaultTableModel) dealerTable.getModel();
                try {
                    DealerDirectory dealerDirectory = new DealerDirectory();
                    String queryString = queryTextField.getText();
                    List<Dealer> dealers = new ArrayList<>();
                    if (stateRadioButton.isSelected()) {
                        if (queryString.length() > 0 && isValidStateOrStateCode(queryString)) {
                            isValidQuery = true;
                            dealers = dealerDirectory.getDealerByStateOrStateId(queryString);
                        } else {
                            invalidQueryTextFieldEntry("Please enter a valid State or State code");
                        }

                    } else if (zipCodeRadioButton.isSelected()) {
                        if (queryString.length() > 0 && isValidZipCode(queryString)) {
                            isValidQuery = true;
                            dealers = dealerDirectory.getDealerByZipCode(queryString);
                        } else {
                            invalidQueryTextFieldEntry("Please enter a valid zipcode");
                        }
                    } else if (dealerNameRadioButton.isSelected()) {
                        if (queryString.length() > 0 && isValidDealerName(queryString)) {
                            isValidQuery = true;
                            dealers = dealerDirectory.getDealerByDealerName(queryString);
                        } else {
                            invalidQueryTextFieldEntry("Please enter a valid dealer name");
                        }
                    }
                    model.setRowCount(0);
                    listDealersPanel.removeAll();
                    listDealersPanel.setLayout(new BoxLayout(listDealersPanel, BoxLayout.Y_AXIS));

                    for (int i = 0; i < dealers.size(); i++) {
                        listDealersPanel.add(new JLabel("<html><br/></html>"));

                        Dealer dealer = dealers.get(i);
                        JLabel tempLabelName = new JLabel();
                        tempLabelName.setText(dealer.getName());
                        tempLabelName.setAlignmentX(CENTER_ALIGNMENT);
                        listDealersPanel.add(tempLabelName);
                        listDealersPanel.add(new JLabel("<html><br/></html>"));

                        String dealerAddress = dealer.getStreetAddress() + ", " + dealer.getCity() + ", " +  dealer.getZipcode();
                        JLabel tempLabelAddress = new JLabel();
                        tempLabelAddress.setText(dealerAddress);
                        tempLabelAddress.setAlignmentX(CENTER_ALIGNMENT);
                        listDealersPanel.add(tempLabelAddress);
                        listDealersPanel.add(new JLabel("<html><br/></html>"));

                        String dealerPhone = "Ph: " + dealer.getPhoneNumber();
                        JLabel tempDealerPhone = new JLabel();
                        tempDealerPhone.setText(dealerPhone);
                        tempDealerPhone.setAlignmentX(CENTER_ALIGNMENT);
                        listDealersPanel.add(tempDealerPhone);
                        listDealersPanel.add(new JLabel("<html><br/></html>"));

                        JButton inventoryButton = new JButton();
                        inventoryButton.setText("Inventory");
                        inventoryButton.addActionListener(new InventoryButtonActionListener(dealer));
                        inventoryButton.setAlignmentX(CENTER_ALIGNMENT);
                        listDealersPanel.add(inventoryButton);
                        listDealersPanel.add(new JLabel("<html><br/></html>"));
                        listDealersPanel.add(new JLabel("<html><br/></html>"));
                    }
                    listDealersPanel.revalidate();
                    listDealersPanel.repaint();
                    if (dealers.size() == 0 && isValidQuery) {
                        JOptionPane.showMessageDialog(null, "No dealers found. Please try again with a different search parameter", "InfoBox: " + "No Dealers found", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    JOptionPane.showMessageDialog(mainPanel, "Error In Connectivity");
                }
            }
        });
        queryTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                clearValidations();
            }
        });
        stateRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearQueryTextField();
                setQueryTextFieldTooltip("State/State code");
                clearValidations();
            }
        });
        zipCodeRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearQueryTextField();
                setQueryTextFieldTooltip("Zipcode");
                clearValidations();
            }
        });
        dealerNameRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearQueryTextField();
                setQueryTextFieldTooltip("Dealer name");
                clearValidations();
            }
        });
    }

    private void clearValidations() {
        queryTextField.setBorder(new LineBorder(Color.white, 0));
        validationText.setText("");
    }

    private void setQueryTextFieldTooltip(String tooltipText) {
        queryTextField.setToolTipText(tooltipText);
    }

    public static void main(String[] args) {
        DealerSearch formView = new DealerSearch();
        formView.setVisible(true);
        formView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        formView.pack();
    }

    static boolean isValidZipCode(String zipCode) {
        String regex = "\\d{5}";
        return zipCode.matches(regex);
    }

    static boolean isValidStateOrStateCode(String stateOrStateCode) {
        String regex = "^[a-zA-Z]*$";
        return stateOrStateCode.matches(regex);
    }

    static boolean isValidDealerName(String stateOrStateCode) {
        String regex = "\\D*";
        return stateOrStateCode.matches(regex);
    }

    private void invalidQueryTextFieldEntry(String s) {
        validationText.setText(s);
        validationText.setForeground(Color.RED);
        queryTextField.setBorder(new LineBorder(Color.red, 1));
    }

    private void clearQueryTextField() {
        queryTextField.setText("");
    }

    private void createUIComponents() {
        searchButton = new JButton();
        queryTextField = new JTextField();
        stateRadioButton = new JRadioButton();
        zipCodeRadioButton = new JRadioButton();
        dealerNameRadioButton = new JRadioButton();
        listDealersPanel = new JPanel();
        dealerTable = new JTable(new DefaultTableModel(null, new String[]{""}));
        dealerTable.setOpaque(false);
        ((DefaultTableCellRenderer) dealerTable.getDefaultRenderer(Object.class)).setOpaque(false);
    }

    private void ShowErrorMessage(String errorMessage, String title) {
        JOptionPane.showMessageDialog(null, errorMessage, title, JOptionPane.ERROR_MESSAGE);
    }

    public JTable getDealerTable() {
        return dealerTable;
    }

    public JPanel getListDealersPanel() {
        return listDealersPanel;
    }

    public JPanel getSearchPanel() {
        return searchPanel;
    }

    public JPanel getSearchContainerPanel() {
        return searchContainerPanel;
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        searchContainerPanel = new JPanel();
        searchContainerPanel.setLayout(new GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), -1, -1));
        searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        searchContainerPanel.add(searchPanel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        searchButton = new JButton();
        searchButton.setText("Search");
        searchPanel.add(searchButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        queryTextField = new JTextField();
        queryTextField.setText("");
        searchPanel.add(queryTextField, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        stateRadioButton.setSelected(true);
        stateRadioButton.setText("State/State Code");
        searchPanel.add(stateRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        zipCodeRadioButton = new JRadioButton();
        zipCodeRadioButton.setText("ZipCode");
        searchPanel.add(zipCodeRadioButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dealerNameRadioButton = new JRadioButton();
        dealerNameRadioButton.setText("Dealer Name");
        searchPanel.add(dealerNameRadioButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        validationText = new JLabel();
        validationText.setHorizontalAlignment(2);
        validationText.setHorizontalTextPosition(2);
        validationText.setText("");
        searchPanel.add(validationText, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 16), null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        searchContainerPanel.add(spacer1, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, -1), null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        searchContainerPanel.add(spacer2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, -1), null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        searchContainerPanel.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 10), null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        searchContainerPanel.add(spacer4, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 20), null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(stateRadioButton);
        buttonGroup.add(zipCodeRadioButton);
        buttonGroup.add(dealerNameRadioButton);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return searchContainerPanel;
    }

}

class InventoryButtonActionListener implements ActionListener {
    private Dealer dealer;

    public InventoryButtonActionListener(Dealer dealer) {
        this.dealer = dealer;
    }

    public void actionPerformed(ActionEvent e) {
        FilterAndBrowseUI filterAndBrowseUI = null;
        try {
            filterAndBrowseUI = new FilterAndBrowseUI(dealer);
            filterAndBrowseUI.buildUseCase2UI();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
