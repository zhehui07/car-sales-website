package group7.ui;

import group7.datafilter.*;
import group7.validators.*;
import group8.*;
import group8.data.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.List;
import java.util.*;
import java.util.function.Predicate;

public class IncentiveManagerUI extends JFrame {
    private group8.IDataProvider dataProvider;
    private List<Car> carsByDealerId;
    private String dealerId;

    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JPanel detailsPanel;
    private JPanel inventoryPanel;
    private JPanel descriptionPanel;

    private IncentiveType incentiveTypeSelected;
    private Date incentiveStartDate;
    private Date incentiveEndDate;

    // Parameters related to Discount Type Incentive
    private CashDiscountType cashDiscountType;
    private double discountPercentage;
    private double discountFlatAmount;

    // Parameters related to Loan Type Incentive
    private double loanInterestRate;
    private int loanDurationInMonths;

    // Parameters related to Rebate Type Incentive
    private HashMap<String, Double> rebateMap;

    // Parameters related to Lease Type Incentive
    private int leaseDurationInMonths;
    private double leaseSigningAmount;
    private double leaseMonthlyPayment;

    // flag to determine if first page parameters are valid
    private boolean isDetailsPageParametersValid;

    // flag to determine if inventory page parameters are valid
    private boolean isInventoryPageParametersValid;

    // Parameters related to Inventory List to Apply the Incentive
    private List<String> carsVINsToApplyIncentive;

    private JTextField startDateTextBox;
    private CalendarPanel startDateCalendarPanel;
    private JLabel startDateLabel;
    private JTextField endDateTextBox;
    private CalendarPanel endDateCalendarPanel;
    private JLabel endDateLabel;
    private JRadioButton cashDicountSectionRadioButton;
    private JRadioButton flatRateDiscountRadioButton;
    private JRadioButton percentageRateDiscountRadioButton;
    private ButtonGroup cashDiscountSelectionRadioButtonGroup;
    private JTextField flatRateDiscountTextField;
    private JTextField percentageRateDiscountTextField;
    private JRadioButton loanSectionRadioButton;
    private JLabel interestRateLabel;
    private JLabel loanDurationInMonthsLabel;
    private JTextField interestRateTextField;
    private JTextField loanDurationInMonthsTextField;

    private JRadioButton rebateSectionRadioButton;
    private JCheckBox newGradRebateCheckBox;
    private JCheckBox militaryRebateCheckBox;
    private JTextField newGradRebateTextBox;
    private JTextField militaryRebateTextBox;

    private JRadioButton leaseSectionRadioButton;
    private JLabel leaseMonthlyPaymentInDollarsLabel;
    private JLabel leaseDurationInMonthsLabel;
    private JLabel leaseSigningAmountLabel;
    private JTextField leaseMonthlyPaymentInDollarsTextBox;
    private JTextField leaseDurationInMonthsTextBox;
    private JTextField leaseSigningAmountTextBox;
    private JButton detailsPageNextButton;
    private JButton detailsPageCancelButton;

    private ButtonGroup incentiveGroups;

    private JLabel carCategoryLabel;
    private JComboBox<String> carCategoryComboBox;

    private JLabel vinLabel;
    private JTextField vinTextBox;

    private JLabel yearsBetweenAndLabel;
    private JLabel yearsBetweenLabel;
    private JComboBox<String> fromYearsComboBox;
    private JComboBox<String> toYearsComboBox;

    private JLabel makeFilterLabel;
    private JComboBox<String> makeFilterComboBox;

    private JLabel modelFilterLabel;
    private JComboBox<String> modelFilterComboBox;

    private JLabel searchByLabel;
    private JCheckBox retailPriceFilterCheckBox;
    private JComboBox<String> priceComparisonTypeComboxBox;
    private JTextField searchByPriceFilterTextBox;
    private JCheckBox milageFilterCheckBox;
    private JComboBox<String> milageComparisonTypeComboxBox;
    private JTextField searchByMilageFilterTextBox;

    private JButton searchButton;

    private JTable scrollPaneCarTable;
    private JButton clearAllButton;
    private JCheckBox selectAllCheckBox;
    private JScrollPane scrollPane;

    private JButton inventoryPageCancelButton;
    private JButton inventoryPageNextButton;
    private JButton inventoryPagePreviousButton;

    private JButton descriptionPagePreviousButton;
    private JButton descriptionPageCancelButton;
    private JButton descriptionPagePublishButton;
    private JLabel descriptionPageTitleLabel;
    private JEditorPane descriptionPageTitleEditorPane;
    private JLabel descriptionPageDescriptionLabel;
    private JEditorPane descriptionPageDescriptionEditorPane;
    private JLabel descriptionPageDisclaimerLabel;
    private JEditorPane descriptionPageDisclaimerEditorPane;


    public IncentiveManagerUI(IDataProvider dataProvider, String dealerId) {
        this.dataProvider = dataProvider;
        this.dealerId = dealerId;
        this.carsByDealerId = dataProvider.getAllCarsByDealerId(this.dealerId);

        this.setTitle("Create Incentive");

        mainPanel = new JPanel(new GridLayout());
        this.setContentPane(this.mainPanel);
        this.setBounds(100, 100, 897, 578);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // this.pack();
        tabbedPane = new JTabbedPane();

        detailsPanel = new JPanel();
        detailsPanel.setLayout(null);
        createDetailsPanelComponents();
        tabbedPane.addTab("Details", detailsPanel);

        inventoryPanel = new JPanel();
        inventoryPanel.setLayout(null);
        createInventoryPanelComponents();
        tabbedPane.addTab("Inventory", inventoryPanel);

        descriptionPanel = new JPanel();
        descriptionPanel.setLayout(null);
        createDescriptionPanelComponents();
        tabbedPane.addTab("Description", descriptionPanel);

        tabbedPane.setSelectedComponent(detailsPanel);

        mainPanel.add(tabbedPane);
        this.setVisible(true);
    }

    private void createDetailsPanelComponents() {
        // Create the components related to start date of the incentive.
        createStartDateComponents();

        // Create the components related to end date of the incentive.
        createEndDateComponents();

        // Create the components related to cash discount incentive.
        createCashDiscountIncentiveComponents();

        // Create the components related to loan incentive.
        createLoanIncentiveComponents();

        // Create the components related to the rebate incentive.
        createRebateIncentiveComponents();

        // Create the components related to the leasing incentive.
        createLeaseIncentiveComponents();

        // Create the components related to navigation of the pages.
        createNavigationComponentsFromDetailsPage();

        // Add Incentive groups to a radio button group
        createExclusiveIncentiveGroups();

        // Default behaviour
        if (cashDicountSectionRadioButton.isSelected()) {
            enableCashDiscountGroup();

            // Default behavior in Cash Discount group
            flatRateDiscountRadioButton.setSelected(true);
            flatRateDiscountTextField.setEnabled(true);
            flatRateDiscountTextField.setText("");
            percentageRateDiscountTextField.setText("");
            percentageRateDiscountTextField.setEnabled(false);

            disableLoanGroup();
            disableLeaseGroup();
            disableRebateGroup();
        }
    }

    private void createInventoryPanelComponents() {
        // Create the components related to Category Filter
        createCarCategoryComponents();

        // Create the components related to VIN Filter
        createVINFilterComponents();

        // Create the components related to year filter
        createYearsFilterComponents();

        // Create the components related to make filter
        createMakeFilterComponents();

        // Create the components related to model filter
        createModelFilerComponents();

        // Create the components related to price filter
        createPriceFilterComponents();

        // Create the components related to miles filter
        createMilesFilterComponents();

        // Create the search button
        createSearchButton();

        // Create the cleaAll button
        createClearAllButton();

        // Create scroll pane of car table
        createScrollPaneCarTable();

        // create navigation buttons();
        createInventoryPageNavigationButtons();

        // Add Action Listener to Search Button()
        addActionListenerToInventoryPageSearchButton();
    }

    private void addActionListenerToInventoryPageSearchButton() {
        searchButton.addActionListener(e -> {
            List<Predicate<Car>> filtersToApply = new ArrayList<>();

            // Car Category Filtering
            String carCategorySelectedString = (String) carCategoryComboBox.getSelectedItem();
            assert carCategorySelectedString != null;
            boolean isCarCategoryFilterRequired = !carCategorySelectedString.equalsIgnoreCase("All");
            if (isCarCategoryFilterRequired) {
                CarCategory carCategorySelected = CarCategory.fromString(carCategorySelectedString);
                filtersToApply.add(CarsFilter.isCarCategoryEqualTo(carCategorySelected));
            }

            // VIN Filtering
            boolean isVINFilterRequired = !vinTextBox.getText().isEmpty();
            if (isVINFilterRequired) {
                String VINEntered = vinTextBox.getText();
                filtersToApply.add(CarsFilter.isCarVINEqualTo(VINEntered));
            }

            // Years From Filter
            String yearsFromSelectedString = (String) fromYearsComboBox.getSelectedItem();
            assert yearsFromSelectedString != null;
            boolean isYearsFromFilterRequired = !yearsFromSelectedString.equalsIgnoreCase("All");
            if (isYearsFromFilterRequired) {
                int yearsFrom = Integer.parseInt(yearsFromSelectedString);
                filtersToApply.add(CarsFilter.isCarYearGreaterThanOrEqualTo(yearsFrom));
            }

            // Years To Filter
            String yearsToSelectedString = (String) toYearsComboBox.getSelectedItem();
            assert yearsToSelectedString != null;
            boolean isYearsToFilterRequired = !yearsToSelectedString.equalsIgnoreCase("All");
            if (isYearsToFilterRequired) {
                int yearsTo = Integer.parseInt(yearsToSelectedString);
                filtersToApply.add(CarsFilter.isCarYearLesserThanOrEqualTo(yearsTo));
            }

            String carMakeSelected = (String) makeFilterComboBox.getSelectedItem();
            assert carMakeSelected != null;
            boolean isMakeFilterRequired = !carMakeSelected.equalsIgnoreCase("All Makes");
            if (isMakeFilterRequired) {
                filtersToApply.add(CarsFilter.isCarMakeEqual(carMakeSelected));
            }

            String carModelSelected = (String) modelFilterComboBox.getSelectedItem();
            assert carModelSelected != null;
            boolean isModelFilterRequired = !carModelSelected.equalsIgnoreCase("All Models");
            if (isModelFilterRequired) {
                filtersToApply.add(CarsFilter.isCarModelEqual(carModelSelected));
            }

            boolean isPriceFilterRequired = retailPriceFilterCheckBox.isSelected();
            if (isPriceFilterRequired) {
                String priceComparator = (String) priceComparisonTypeComboxBox.getSelectedItem();
                assert priceComparator != null;

                String priceToCompareString = searchByPriceFilterTextBox.getText();
                try {
                    double priceToCompare = Double.parseDouble(priceToCompareString);
                    if (priceComparator.equalsIgnoreCase("<=")) {
                        filtersToApply.add(CarsFilter.isCarPriceLesserThanEqualTo(priceToCompare));
                    } else {
                        filtersToApply.add(CarsFilter.isCarPriceGreaterThanEqualTo(priceToCompare));
                    }
                } catch (NumberFormatException ne) {
                    JOptionPane.showMessageDialog(null, "Please enter valid number for search by price", "Invalid Price", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            boolean isMileageFilterRequired = milageFilterCheckBox.isSelected();
            if (isMileageFilterRequired) {
                String mileageComparator = (String) milageComparisonTypeComboxBox.getSelectedItem();
                assert mileageComparator != null;

                String mileageToCompareString = searchByMilageFilterTextBox.getText();
                try {
                    int mileageToCompare = Integer.parseInt(mileageToCompareString);
                    if (mileageComparator.equalsIgnoreCase("<=")) {
                        filtersToApply.add(CarsFilter.isCarMileageLesserThanOrEqualTo(mileageToCompare));
                    } else {
                        filtersToApply.add(CarsFilter.isCarMileageGreaterThanOrEqualTo(mileageToCompare));
                    }
                } catch (NumberFormatException ne) {
                    JOptionPane.showMessageDialog(null, "Please enter valid number for search by mileage", "Invalid Mileage", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Apply All the filters and Fill the Table
            fillTable(CarsFilter.ApplyFilters(carsByDealerId, filtersToApply));
            selectAllCheckBox.setSelected(false);
        });
    }

    private void createDescriptionPanelComponents() {
        // Create Title Components
        createDescriptionPageTitleComponents();

        // Create Description Components
        createDescriptionPageDescriptionComponents();

        // Create Disclaimer Components
        createDescriptionPageDisclaimerComponents();

        // Default disclaimer content
        createDefaultDisclaimerComponents();

        // Create Description Page Navigation Buttons
        createDescriptionPageNavigationButtons();
    }

    private void createDefaultDisclaimerComponents() {
        String defaultDisclaimerText = "Sales tax does not qualify for this incentive. This incentive is not redeemable for " +
                "cash or gift cards, nor is it valid toward previous purchases. Incentive may not be combined with any " +
                "other coupons, discounts, offers, or promotions. ";

        JCheckBox defaultDisclaimerCheckBox = new JCheckBox("Use Default Disclaimer");
        defaultDisclaimerCheckBox.setBounds(80, 345, 180, 23);
        descriptionPanel.add(defaultDisclaimerCheckBox);
        defaultDisclaimerCheckBox.setSelected(false);

        defaultDisclaimerCheckBox.addActionListener(e -> {
            if (defaultDisclaimerCheckBox.isSelected()) {
                descriptionPageDisclaimerEditorPane.setForeground(Color.BLACK);
                descriptionPageDisclaimerEditorPane.setText(defaultDisclaimerText);
            } else {
                descriptionPageDisclaimerEditorPane.setForeground(Color.GRAY);
                descriptionPageDisclaimerEditorPane.setText("Maximum 250 characters");
            }
        });
    }

    private void createDescriptionPageDisclaimerComponents() {
        descriptionPageDisclaimerLabel = new JLabel("Disclaimer");
        descriptionPageDisclaimerLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        descriptionPageDisclaimerLabel.setBounds(87, 306, 103, 27);
        descriptionPanel.add(descriptionPageDisclaimerLabel);

        descriptionPageDisclaimerEditorPane = new JEditorPane();
        descriptionPageDisclaimerEditorPane.setFont(new Font("Dialog", Font.PLAIN, 12));
        descriptionPageDisclaimerEditorPane.setBounds(247, 295, 420, 88);
        descriptionPanel.add(descriptionPageDisclaimerEditorPane);

        Watermark disclaimerWatermark = new Watermark("Maximum 250 characters", descriptionPageDisclaimerEditorPane);
        descriptionPageDisclaimerEditorPane.addFocusListener(disclaimerWatermark);
    }

    private void createDescriptionPageDescriptionComponents() {
        descriptionPageDescriptionLabel = new JLabel("Description");
        descriptionPageDescriptionLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        descriptionPageDescriptionLabel.setBounds(87, 122, 103, 27);
        descriptionPanel.add(descriptionPageDescriptionLabel);

        descriptionPageDescriptionEditorPane = new JEditorPane();
        descriptionPageDescriptionEditorPane.setFont(new Font("Dialog", Font.PLAIN, 12));
        descriptionPageDescriptionEditorPane.setBounds(247, 122, 420, 127);
        descriptionPanel.add(descriptionPageDescriptionEditorPane);

        Watermark descriptionWatermark = new Watermark("Maximum 350 characters", descriptionPageDescriptionEditorPane);
        descriptionPageDescriptionEditorPane.addFocusListener(descriptionWatermark);
    }

    private void createDescriptionPageTitleComponents() {
        descriptionPageTitleLabel = new JLabel("Title");
        descriptionPageTitleLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        descriptionPageTitleLabel.setBounds(87, 55, 61, 27);
        descriptionPanel.add(descriptionPageTitleLabel);

        descriptionPageTitleEditorPane = new JEditorPane();
        descriptionPageTitleEditorPane.setBounds(247, 42, 420, 40);
        descriptionPageTitleEditorPane.setFont(new Font("Dialog", Font.PLAIN, 12));
        descriptionPanel.add(descriptionPageTitleEditorPane);

        Watermark titleWatermark = new Watermark("Maximum 100 characters", descriptionPageTitleEditorPane);
        descriptionPageTitleEditorPane.addFocusListener(titleWatermark);
    }

    public void createDescriptionPageNavigationButtons() {
        descriptionPagePreviousButton = new JButton("Previous");
        descriptionPagePreviousButton.setBounds(170, 448, 117, 29);
        descriptionPagePreviousButton.setFont(new Font("Dialog", Font.BOLD, 12));
        descriptionPanel.add(descriptionPagePreviousButton);

        descriptionPagePreviousButton.addActionListener(e -> {
            tabbedPane.setSelectedComponent(inventoryPanel);
        });

        descriptionPageCancelButton = new JButton("Cancel");
        descriptionPageCancelButton.setBounds(370, 448, 117, 29);
        descriptionPageCancelButton.setFont(new Font("Dialog", Font.BOLD, 12));
        descriptionPanel.add(descriptionPageCancelButton);

        descriptionPageCancelButton.addActionListener(e -> {
            // Change later
            System.exit(0);
        });

        descriptionPagePublishButton = new JButton("Publish Incentive");
        descriptionPagePublishButton.setBounds(570, 448, 130, 29);
        descriptionPagePublishButton.setFont(new Font("Dialog", Font.BOLD, 12));
        descriptionPanel.add(descriptionPagePublishButton);

        descriptionPagePublishButton.addActionListener(e -> {
            validateCreateAndPublishIncentive();
        });
    }

    private void validateCreateAndPublishIncentive() {
        if (!isDetailsPageParametersValid) {
            JOptionPane.showMessageDialog(null, "Please select valid incentive type details", "Invalid Incentive details", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isInventoryPageParametersValid) {
            JOptionPane.showMessageDialog(null, "Please select valid cars in the inventory page", "Invalid inventory details", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (validateTextLength(descriptionPageTitleEditorPane, 100) && validateTextLength(descriptionPageDescriptionEditorPane, 350) && validateTextLength(descriptionPageDisclaimerEditorPane, 250)) {
            if (JOptionPane.showConfirmDialog(null, "Create new incentive? ", "Confirmation", JOptionPane.OK_CANCEL_OPTION) == 0) {
                createIncentiveObjectAndPublish();
                JOptionPane.showMessageDialog(null, "New Incentive Created", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Incentive Publish Cancelled", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Text field cannot be empty or exceed maximum number of characters.", "Confirmation", 1);
        }
    }

    private void createIncentiveObjectAndPublish() {
        switch (incentiveTypeSelected) {
            case DISCOUNT:
                CashDiscountIncentive cashDiscountIncentive =
                        new CashDiscountIncentive(
                                UUID.randomUUID().toString(),
                                this.dealerId,
                                this.incentiveStartDate,
                                this.incentiveEndDate,
                                descriptionPageTitleEditorPane.getText(),
                                descriptionPageDescriptionEditorPane.getText(),
                                descriptionPageDisclaimerEditorPane.getText(),
                                new HashSet<>(carsVINsToApplyIncentive),
                                cashDiscountType.equals(CashDiscountType.FLATAMOUNT) ? discountFlatAmount : discountPercentage,
                                cashDiscountType);

                dataProvider.persistIncentive(cashDiscountIncentive);

                break;

            case LOAN:
                LoanIncentive loanIncentive =
                        new LoanIncentive(
                                UUID.randomUUID().toString(),
                                this.dealerId,
                                this.incentiveStartDate,
                                this.incentiveEndDate,
                                descriptionPageTitleEditorPane.getText(),
                                descriptionPageDescriptionEditorPane.getText(),
                                descriptionPageDisclaimerEditorPane.getText(),
                                new HashSet<>(carsVINsToApplyIncentive),
                                loanInterestRate,
                                loanDurationInMonths);

                dataProvider.persistIncentive(loanIncentive);

                break;

            case LEASE:
                LeasingIncentive leasingIncentive =
                        new LeasingIncentive(
                                UUID.randomUUID().toString(),
                                this.dealerId,
                                this.incentiveStartDate,
                                this.incentiveEndDate,
                                descriptionPageTitleEditorPane.getText(),
                                descriptionPageDescriptionEditorPane.getText(),
                                descriptionPageDisclaimerEditorPane.getText(),
                                new HashSet<>(carsVINsToApplyIncentive),
                                leaseDurationInMonths,
                                leaseSigningAmount,
                                leaseMonthlyPayment);

                dataProvider.persistIncentive(leasingIncentive);

                break;

            case REBATE:
                RebateIncentive rebateIncentive =
                        new RebateIncentive(
                                UUID.randomUUID().toString(),
                                this.dealerId,
                                this.incentiveStartDate,
                                this.incentiveEndDate,
                                descriptionPageTitleEditorPane.getText(),
                                descriptionPageDescriptionEditorPane.getText(),
                                descriptionPageDisclaimerEditorPane.getText(),
                                new HashSet<>(carsVINsToApplyIncentive),
                                rebateMap);

                dataProvider.persistIncentive(rebateIncentive);

                break;
        }
    }

    // Check if editor planes contains text that is non-empty, and no more than the maximum characters
    // and text cannot be the watermark text
    private boolean validateTextLength(JEditorPane plane, int max) {
        String string = plane.getText();
        String watermarkText = "Maximum " + max + " characters";
        return (string != null && string.trim().length() > 0 && string.trim().length() <= max && !string.equals(watermarkText));
    }

    private void createInventoryPageNavigationButtons() {
        inventoryPagePreviousButton = new JButton("Previous");
        inventoryPagePreviousButton.setBounds(170, 448, 117, 29);
        inventoryPagePreviousButton.setFont(new Font("Dialog", Font.BOLD, 12));
        inventoryPanel.add(inventoryPagePreviousButton);

        inventoryPagePreviousButton.addActionListener(e -> {
            tabbedPane.setSelectedComponent(detailsPanel);
        });

        inventoryPageCancelButton = new JButton("Cancel");
        inventoryPageCancelButton.setBounds(370, 448, 117, 29);
        inventoryPageCancelButton.setFont(new Font("Dialog", Font.BOLD, 12));
        inventoryPanel.add(inventoryPageCancelButton);

        inventoryPageCancelButton.addActionListener(e -> {
            // Change later
            System.exit(0);
        });

        inventoryPageNextButton = new JButton("Apply & Next");
        inventoryPageNextButton.setBounds(570, 448, 117, 29);
        inventoryPageNextButton.setFont(new Font("Dialog", Font.BOLD, 12));
        inventoryPanel.add(inventoryPageNextButton);

        inventoryPageNextButton.addActionListener(e -> {
            validateInventoryDetailsForIncentives();
        });
    }

    private void validateInventoryDetailsForIncentives() {
        DefaultTableModel defaultTableModel = (DefaultTableModel) scrollPaneCarTable.getModel();
        carsVINsToApplyIncentive = new ArrayList<>();
        for (int i = 0; i < defaultTableModel.getRowCount(); i++) {
            if ((Boolean) defaultTableModel.getValueAt(i, 0)) {
                carsVINsToApplyIncentive.add((String) defaultTableModel.getValueAt(i, 1));
            }
        }
        if (carsVINsToApplyIncentive.isEmpty()) {
            isDetailsPageParametersValid = false;
            JOptionPane.showMessageDialog(null, "Please Select atleast one car to apply the incentive", "Empty Car List for Applying Incentive", JOptionPane.ERROR_MESSAGE);
        } else {
            isInventoryPageParametersValid = true;
            tabbedPane.setSelectedComponent(descriptionPanel);
        }
    }

    private void selectAllItemsAction(boolean select) {
        DefaultTableModel defaultTableModel = (DefaultTableModel) scrollPaneCarTable.getModel();
        for (int i = 0; i < defaultTableModel.getRowCount(); i++) {
            defaultTableModel.setValueAt(select, i, 0);
        }
    }

    private void createScrollPaneCarTable() {
        selectAllCheckBox = new JCheckBox("Select All");
        selectAllCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectAllItemsAction(selectAllCheckBox.isSelected());
            }
        });
        selectAllCheckBox.setBounds(25, 425, 128, 23);
        selectAllCheckBox.setFont(new Font("Dialog", Font.BOLD, 12));
        inventoryPanel.add(selectAllCheckBox);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(25, 210, 829, 210);
        inventoryPanel.add(scrollPane);

        Vector<String> headerNames = new Vector<>();
        headerNames.add("Select");
        headerNames.add("VIN");
        headerNames.add("Category");
        headerNames.add("Make");
        headerNames.add("Model");
        headerNames.add("Year");
        headerNames.add("Miles");
        headerNames.add("MSRP");

        Vector v = new Vector();
        scrollPaneCarTable = new JTable(new DefaultTableModel(v, headerNames) {
            public boolean isCellEditable(int row, int column) {
                return column <= 0;
            }

            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
        });

        scrollPane.setViewportView(scrollPaneCarTable);
        fillTable(carsByDealerId);
    }

    private void fillTable(List<Car> cars) {
        DefaultTableModel defaultTableModel = (DefaultTableModel) scrollPaneCarTable.getModel();
        defaultTableModel.setRowCount(0);
        for (Car car : cars) {
            Vector vector = new Vector();
            vector.add(false);
            vector.add(car.getVIN());
            vector.add(car.getCarCategory());
            vector.add(car.getMake());
            vector.add(car.getModel());
            vector.add(car.getYear());
            vector.add(car.getMileage());
            vector.add(car.getMSRP());
            defaultTableModel.addRow(vector);
        }
    }

    private void createClearAllButton() {
        clearAllButton = new JButton("Clear All");
        clearAllButton.setFont(new Font("Dialog", Font.BOLD, 12));
        clearAllButton.setBounds(610, 140, 122, 42);
        inventoryPanel.add(clearAllButton);

        clearAllButton.addActionListener(e -> {
            carCategoryComboBox.setSelectedIndex(0);
            vinTextBox.setText("");
            fromYearsComboBox.setSelectedIndex(0);
            toYearsComboBox.setSelectedIndex(0);
            makeFilterComboBox.setSelectedIndex(0);
            modelFilterComboBox.setSelectedIndex(0);
            retailPriceFilterCheckBox.setSelected(false);
            priceComparisonTypeComboxBox.setEnabled(false);
            searchByPriceFilterTextBox.setEnabled(false);
            searchByPriceFilterTextBox.setText("");
            milageFilterCheckBox.setSelected(false);
            milageComparisonTypeComboxBox.setEnabled(false);
            searchByMilageFilterTextBox.setEnabled(false);
            searchByMilageFilterTextBox.setText("");
            fillTable(carsByDealerId);
        });
    }

    private void createSearchButton() {
        searchButton = new JButton("Search");
        searchButton.setFont(new Font("Dialog", Font.BOLD, 12));
        searchButton.setBounds(440, 140, 122, 42);
        inventoryPanel.add(searchButton);
    }

    private void createMilesFilterComponents() {
        milageFilterCheckBox = new JCheckBox("Mileage");
        milageFilterCheckBox.setFont(new Font("Dialog", Font.BOLD, 12));
        milageFilterCheckBox.setBounds(420, 94, 122, 16);
        milageFilterCheckBox.setFont(new Font("Dialog", Font.BOLD, 12));
        inventoryPanel.add(milageFilterCheckBox);

        milageComparisonTypeComboxBox = new JComboBox<>();
        milageComparisonTypeComboxBox.setModel(new DefaultComboBoxModel<>(new String[]{"<=", ">="}));
        milageComparisonTypeComboxBox.setBounds(550, 91, 75, 27);
        milageComparisonTypeComboxBox.setEnabled(false);
        inventoryPanel.add(milageComparisonTypeComboxBox);

        searchByMilageFilterTextBox = new JTextField();
        searchByMilageFilterTextBox.setBounds(650, 92, 100, 26);
        searchByMilageFilterTextBox.setEnabled(false);
        inventoryPanel.add(searchByMilageFilterTextBox);
        searchByMilageFilterTextBox.setColumns(10);

        milageFilterCheckBox.addActionListener(e -> {
            if (milageFilterCheckBox.isSelected()) {
                milageComparisonTypeComboxBox.setEnabled(true);
                searchByMilageFilterTextBox.setEnabled(true);
            } else {
                milageComparisonTypeComboxBox.setEnabled(false);
                searchByMilageFilterTextBox.setEnabled(false);
            }
            searchByMilageFilterTextBox.setText("");
        });
    }

    private void createPriceFilterComponents() {
        searchByLabel = new JLabel("Search By");
        searchByLabel.setBounds(420, 24, 109, 16);
        searchByLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        inventoryPanel.add(searchByLabel);

        retailPriceFilterCheckBox = new JCheckBox("Retail Price ($)");
        retailPriceFilterCheckBox.setFont(new Font("Dialog", Font.BOLD, 12));
        retailPriceFilterCheckBox.setBounds(420, 59, 120, 16);
        inventoryPanel.add(retailPriceFilterCheckBox);

        priceComparisonTypeComboxBox = new JComboBox<>();
        priceComparisonTypeComboxBox.setModel(new DefaultComboBoxModel<>(new String[]{"<=", ">="}));
        priceComparisonTypeComboxBox.setBounds(550, 55, 75, 27);
        priceComparisonTypeComboxBox.setEnabled(false);
        inventoryPanel.add(priceComparisonTypeComboxBox);

        searchByPriceFilterTextBox = new JTextField();
        searchByPriceFilterTextBox.setBounds(650, 57, 100, 26);
        searchByPriceFilterTextBox.setEnabled(false);
        inventoryPanel.add(searchByPriceFilterTextBox);
        searchByPriceFilterTextBox.setColumns(10);

        retailPriceFilterCheckBox.addActionListener(e -> {
            if (retailPriceFilterCheckBox.isSelected()) {
                priceComparisonTypeComboxBox.setEnabled(true);
                searchByPriceFilterTextBox.setEnabled(true);
            } else {
                priceComparisonTypeComboxBox.setEnabled(false);
                searchByPriceFilterTextBox.setEnabled(false);
            }
            searchByPriceFilterTextBox.setText("");
        });
    }

    private void createModelFilerComponents() {
        modelFilterLabel = new JLabel("Model");
        modelFilterLabel.setBounds(73, 175, 61, 16);
        modelFilterLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        inventoryPanel.add(modelFilterLabel);

        modelFilterComboBox = new JComboBox<>();

        String[] carModels = carsByDealerId
                .stream()
                .map(Car::getModel)
                .distinct()
                .toArray(String[]::new);

        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(carModels);
        comboBoxModel.insertElementAt("All Models", 0);
        modelFilterComboBox.setModel(comboBoxModel);
        modelFilterComboBox.setSelectedIndex(0);
        modelFilterComboBox.setBounds(167, 171, 170, 27);
        inventoryPanel.add(modelFilterComboBox);

        makeFilterComboBox.addActionListener(e -> {
            String carMakeSelected = (String) makeFilterComboBox.getSelectedItem();
            assert carMakeSelected != null;
            DefaultComboBoxModel<String> newComboBoxModel;
            if (carMakeSelected.equalsIgnoreCase("All Makes")) {
                newComboBoxModel = new DefaultComboBoxModel<>(carsByDealerId
                        .stream()
                        .map(Car::getModel)
                        .distinct()
                        .toArray(String[]::new));
            } else {
                newComboBoxModel = new DefaultComboBoxModel<>(carsByDealerId
                        .stream()
                        .filter(car -> car.getMake().equalsIgnoreCase(carMakeSelected))
                        .map(Car::getModel)
                        .distinct()
                        .toArray(String[]::new));
            }
            newComboBoxModel.insertElementAt("All Models", 0);
            modelFilterComboBox.setModel(newComboBoxModel);
            modelFilterComboBox.setSelectedIndex(0);
        });
    }

    private void createMakeFilterComponents() {
        makeFilterLabel = new JLabel("Make");
        makeFilterLabel.setBounds(73, 137, 61, 16);
        makeFilterLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        inventoryPanel.add(makeFilterLabel);

        makeFilterComboBox = new JComboBox<>();

        String[] carMakes = carsByDealerId
                .stream()
                .map(Car::getMake)
                .distinct()
                .toArray(String[]::new);

        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(carMakes);
        comboBoxModel.insertElementAt("All Makes", 0);
        makeFilterComboBox.setModel(comboBoxModel);
        makeFilterComboBox.setSelectedIndex(0);
        makeFilterComboBox.setBounds(167, 133, 170, 27);
        inventoryPanel.add(makeFilterComboBox);
    }

    private void createYearsFilterComponents() {
        yearsBetweenLabel = new JLabel("Years from");
        yearsBetweenLabel.setBounds(73, 97, 82, 16);
        yearsBetweenLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        inventoryPanel.add(yearsBetweenLabel);

        yearsBetweenAndLabel = new JLabel("to");
        yearsBetweenAndLabel.setBounds(245, 97, 25, 16);
        yearsBetweenAndLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        inventoryPanel.add(yearsBetweenAndLabel);

        fromYearsComboBox = new JComboBox<>();
        fromYearsComboBox.setBounds(166, 93, 70, 27);
        fromYearsComboBox.addItem("All");
        int startYear = 2000;
        int endYear = Year.now().getValue();
        for (int i = endYear + 1; i >= startYear; --i) {
            fromYearsComboBox.addItem(String.valueOf(i));
        }
        inventoryPanel.add(fromYearsComboBox);

        toYearsComboBox = new JComboBox<>();
        toYearsComboBox.setBounds(265, 93, 70, 27);
        toYearsComboBox.addItem("All");
        for (int i = endYear + 1; i >= startYear; --i) {
            toYearsComboBox.addItem(String.valueOf(i));
        }
        inventoryPanel.add(toYearsComboBox);
    }

    private void createVINFilterComponents() {
        vinLabel = new JLabel("VIN");
        vinLabel.setBounds(73, 59, 60, 16);
        vinLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        inventoryPanel.add(vinLabel);

        vinTextBox = new JTextField();
        // vinTextBox.setTransferHandler(null);
        vinTextBox.setBounds(167, 54, 170, 26);
        inventoryPanel.add(vinTextBox);
        vinTextBox.setColumns(10);
    }

    private void createCarCategoryComponents() {
        carCategoryLabel = new JLabel("Category");
        carCategoryLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        carCategoryLabel.setBounds(73, 24, 61, 16);
        inventoryPanel.add(carCategoryLabel);

        carCategoryComboBox = new JComboBox<>();
        carCategoryComboBox.addItem("All");
        for (CarCategory carCategory : CarCategory.values()) {
            carCategoryComboBox.addItem(carCategory.toString());
        }

        carCategoryComboBox.setBounds(167, 20, 170, 27);
        inventoryPanel.add(carCategoryComboBox);
    }

    private void validateIncentiveDetailsAndCreateIncentiveInstance() {
        Date startDate = validateAndParseDate(startDateTextBox.getText(), "Start Date");
        Date endDate = validateAndParseDate(endDateTextBox.getText(), "End Date");

        if (IncentiveDataValidator.isNull(startDate) || IncentiveDataValidator.isNull(endDate)) {
            return;
        }

        if (startDate.after(endDate)) {
            JOptionPane.showMessageDialog(null, "Start Date: " + startDate + " cannot be after End Date: " + endDate, "Start Date greater than End Date", JOptionPane.ERROR_MESSAGE);
            return;
        }

        this.incentiveStartDate = startDate;
        this.incentiveEndDate = endDate;
        this.incentiveTypeSelected = IncentiveType.fromString(incentiveGroups.getSelection().getActionCommand());

        assert this.incentiveTypeSelected != null;

        switch (this.incentiveTypeSelected) {
            case DISCOUNT:
                boolean isCashCountParametersValid = validateAndParseCashDiscountIncentiveParameters();
                if (isCashCountParametersValid) {
                    isDetailsPageParametersValid = true;
                    tabbedPane.setSelectedComponent(inventoryPanel);
                }
                break;

            case LOAN:
                boolean isLoanIncentiveParametersValid = validateAndParseLoanIncentiveParameters();
                if (isLoanIncentiveParametersValid) {
                    isDetailsPageParametersValid = true;
                    tabbedPane.setSelectedComponent(inventoryPanel);
                }
                break;
            case REBATE:
                boolean isRebateIncentiveParametersValid = validateAndParseRebateIncentiveParameters();
                if (isRebateIncentiveParametersValid) {
                    isDetailsPageParametersValid = true;
                    tabbedPane.setSelectedComponent(inventoryPanel);
                }
                break;

            case LEASE:
                boolean isLeaseIncentiveParametersValid = validateAndParseLeaseIncentiveParameters();
                if (isLeaseIncentiveParametersValid) {
                    isDetailsPageParametersValid = true;
                    tabbedPane.setSelectedComponent(inventoryPanel);
                }
                break;

            default:
                JOptionPane.showMessageDialog(null, "Please select valid incentive Type", "Invalid Incentive type", JOptionPane.ERROR_MESSAGE);
                isDetailsPageParametersValid = false;
        }
    }

    private boolean validateAndParseLeaseIncentiveParameters() {
        try {
            this.leaseDurationInMonths = Integer.parseInt(leaseDurationInMonthsTextBox.getText());
            if (this.leaseDurationInMonths <= 0 || this.leaseDurationInMonths > 72) {
                JOptionPane.showMessageDialog(null, "Please enter valid number of months for the lease duration (1-72)", "Invalid lease duration", JOptionPane.ERROR_MESSAGE);
                this.leaseSigningAmount = 0.0;
                this.leaseMonthlyPayment = 0.0;
                return false;
            }
        } catch (NumberFormatException ne) {
            JOptionPane.showMessageDialog(null, "Please enter valid number of months for the lease duration", "Invalid lease duration", JOptionPane.ERROR_MESSAGE);
            this.leaseSigningAmount = 0.0;
            this.leaseMonthlyPayment = 0.0;
            return false;
        }

        try {
            this.leaseSigningAmount = Double.parseDouble(leaseSigningAmountTextBox.getText());
        } catch (NumberFormatException ne) {
            JOptionPane.showMessageDialog(null, "Please enter valid number for lease signing amount", "Invalid lease signing amount", JOptionPane.ERROR_MESSAGE);
            this.leaseDurationInMonths = 0;
            this.leaseMonthlyPayment = 0.0;
            return false;
        }

        try {
            this.leaseMonthlyPayment = Double.parseDouble(leaseMonthlyPaymentInDollarsTextBox.getText());
        } catch (NumberFormatException ne) {
            JOptionPane.showMessageDialog(null, "Please enter valid number of for monthly lease payment", "Invalid monthly lease payment", JOptionPane.ERROR_MESSAGE);
            this.leaseDurationInMonths = 0;
            this.leaseSigningAmount = 0.0;
            return false;
        }

        return true;
    }

    private boolean validateAndParseRebateIncentiveParameters() {
        rebateMap = new HashMap<>();
        if (newGradRebateCheckBox.isSelected()) {
            try {
                double newGradRebateAmount = Double.parseDouble(newGradRebateTextBox.getText());
                rebateMap.put("NewGradRebate", newGradRebateAmount);
            } catch (NumberFormatException ne) {
                JOptionPane.showMessageDialog(null, "Please enter valid number for the new grad incentive amount", "Invalid Rebate Incentive Amount", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        if (militaryRebateCheckBox.isSelected()) {
            try {
                double militaryRebateAmount = Double.parseDouble(militaryRebateTextBox.getText());
                rebateMap.put("MilitaryVeteranRebate", militaryRebateAmount);
            } catch (NumberFormatException ne) {
                JOptionPane.showMessageDialog(null, "Please enter valid number for the military veteran incentive amount", "Invalid Rebate Incentive Amount", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        if (rebateMap.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select atleast one type of Rebate incentive", "Invalid Rebate Incentive selection", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean validateAndParseLoanIncentiveParameters() {
        try {
            this.loanInterestRate = Double.parseDouble(interestRateTextField.getText());
            if (this.loanInterestRate < 0.0 || this.loanInterestRate > 100.0) {
                JOptionPane.showMessageDialog(null, "Please enter valid interest rate value between 0.0 to 100.0 in the loan interest field", "Invalid Loan Interest Rate", JOptionPane.ERROR_MESSAGE);
                this.loanInterestRate = 0.0;
                this.loanDurationInMonths = 0;
                return false;
            }
        } catch (NumberFormatException ne) {
            JOptionPane.showMessageDialog(null, "Please enter correct loan interest rate", "Invalid Loan Interest Rate", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            this.loanDurationInMonths = Integer.parseInt(loanDurationInMonthsTextField.getText());
            if (this.loanDurationInMonths <= 0 || this.loanDurationInMonths > 72) {
                JOptionPane.showMessageDialog(null, "Please enter valid loan duration in months between 1-72 months in the loan duration field", "Invalid Loan Duration in months", JOptionPane.ERROR_MESSAGE);
                this.loanInterestRate = 0.0;
                this.loanDurationInMonths = 0;
                return false;
            }
        } catch (NumberFormatException ne) {
            JOptionPane.showMessageDialog(null, "Please enter correct loan duration months", "Invalid Loan Interest Rate", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean validateAndParseCashDiscountIncentiveParameters() {
        this.cashDiscountType = CashDiscountType.fromString(cashDiscountSelectionRadioButtonGroup.getSelection().getActionCommand());
        if (cashDiscountType == null) {
            JOptionPane.showMessageDialog(null, "Please select correct discount type", "Invalid Discount Type", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        switch (cashDiscountType) {
            case PERCENTAGE:
                try {
                    this.discountPercentage = Double.parseDouble(percentageRateDiscountTextField.getText());
                    if (discountPercentage < 0.0 || discountPercentage > 100.0) {
                        this.discountPercentage = 0.0;
                        JOptionPane.showMessageDialog(null, "Please enter valid percentage value between 0.0 to 100.0 in the discount percentage field", "Invalid Discount Percentage", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    this.discountFlatAmount = 0.0;
                } catch (NumberFormatException ne) {
                    JOptionPane.showMessageDialog(null, "Please enter valid percentage value between 0.0 to 100.0 in the discount percentage field", "Invalid Discount Percentage", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                break;

            case FLATAMOUNT:
                try {
                    this.discountFlatAmount = Double.parseDouble(flatRateDiscountTextField.getText());
                    this.discountPercentage = 0.0;
                } catch (NumberFormatException ne) {
                    JOptionPane.showMessageDialog(null, "Please enter valid number for flat amount field", "Invalid Flat Amount", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                break;

            default:
                JOptionPane.showMessageDialog(null, "Please select valid discount type", "Invalid Discount Type", JOptionPane.ERROR_MESSAGE);
                return false;
        }

        return true;
    }

    private Date validateAndParseDate(String date, String title) {
        if (IncentiveDataValidator.isNullOrEmpty(date)) {
            JOptionPane.showMessageDialog(null, title + " field cannot be empty", "Required Field Missing", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        Date startDate = null;
        try {
            startDate = IncentiveDataValidator.parseDateFromString(date);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }

        if (startDate == null) {
            JOptionPane.showMessageDialog(null, title + " needs to be a valid date in the format yyyy/mm/dd", "Invalid StartDate", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date todayWithZeroTime = formatter.parse(formatter.format(new Date()));
            if (startDate.before(todayWithZeroTime)) {
                JOptionPane.showMessageDialog(null, title + " cannot be a past date and needs to be a valid date in the format yyyy/mm/dd", "Invalid StartDate", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        return startDate;
    }

    private void createExclusiveIncentiveGroups() {
        incentiveGroups = new ButtonGroup();
        incentiveGroups.add(cashDicountSectionRadioButton);
        incentiveGroups.add(loanSectionRadioButton);
        incentiveGroups.add(rebateSectionRadioButton);
        incentiveGroups.add(leaseSectionRadioButton);
    }

    private void createNavigationComponentsFromDetailsPage() {
        detailsPageCancelButton = new JButton("Cancel");
        detailsPageCancelButton.setBounds(250, 448, 117, 29);
        detailsPageCancelButton.setFont(new Font("Dialog", Font.BOLD, 12));
        detailsPanel.add(detailsPageCancelButton);

        detailsPageCancelButton.addActionListener(e -> {
            // Change later
            System.exit(0);
        });

        detailsPageNextButton = new JButton("Apply & Next");
        detailsPageNextButton.setBounds(450, 448, 117, 29);
        detailsPageNextButton.setFont(new Font("Dialog", Font.BOLD, 12));
        detailsPanel.add(detailsPageNextButton);

        detailsPageNextButton.addActionListener(e -> validateIncentiveDetailsAndCreateIncentiveInstance());
    }

    public void enableLeaseGroup() {
        leaseMonthlyPaymentInDollarsTextBox.setText("");
        leaseMonthlyPaymentInDollarsTextBox.setEnabled(true);
        leaseDurationInMonthsTextBox.setText("");
        leaseDurationInMonthsTextBox.setEnabled(true);
        leaseSigningAmountTextBox.setText("");
        leaseSigningAmountTextBox.setEnabled(true);
    }

    public void disableLeaseGroup() {
        leaseMonthlyPaymentInDollarsTextBox.setText("");
        leaseMonthlyPaymentInDollarsTextBox.setEnabled(false);
        leaseDurationInMonthsTextBox.setText("");
        leaseDurationInMonthsTextBox.setEnabled(false);
        leaseSigningAmountTextBox.setText("");
        leaseSigningAmountTextBox.setEnabled(false);
    }

    private void createLeaseIncentiveComponents() {
        leaseSectionRadioButton = new JRadioButton("Lease Incentive");
        leaseSectionRadioButton.setFont(new Font("Dialog", Font.BOLD, 14));
        leaseSectionRadioButton.setBounds(432, 271, 204, 23);
        leaseSectionRadioButton.setActionCommand(String.valueOf(IncentiveType.LEASE));
        detailsPanel.add(leaseSectionRadioButton);

        leaseMonthlyPaymentInDollarsLabel = new JLabel("Monthly Payment of ($)");
        leaseMonthlyPaymentInDollarsLabel.setBounds(456, 304, 153, 16);
        detailsPanel.add(leaseMonthlyPaymentInDollarsLabel);

        leaseDurationInMonthsLabel = new JLabel("Number of months");
        leaseDurationInMonthsLabel.setBounds(456, 332, 153, 16);
        detailsPanel.add(leaseDurationInMonthsLabel);

        leaseSigningAmountLabel = new JLabel("Signing Amount of ($)");
        leaseSigningAmountLabel.setBounds(456, 360, 153, 16);
        detailsPanel.add(leaseSigningAmountLabel);

        leaseMonthlyPaymentInDollarsTextBox = new JTextField();
        leaseMonthlyPaymentInDollarsTextBox.setBounds(621, 299, 56, 26);
        detailsPanel.add(leaseMonthlyPaymentInDollarsTextBox);
        leaseMonthlyPaymentInDollarsTextBox.setColumns(10);

        leaseDurationInMonthsTextBox = new JTextField();
        leaseDurationInMonthsTextBox.setBounds(621, 327, 56, 26);
        detailsPanel.add(leaseDurationInMonthsTextBox);
        leaseDurationInMonthsTextBox.setColumns(10);

        leaseSigningAmountTextBox = new JTextField();
        leaseSigningAmountTextBox.setColumns(10);
        leaseSigningAmountTextBox.setBounds(621, 355, 56, 26);
        detailsPanel.add(leaseSigningAmountTextBox);

        leaseSectionRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (leaseSectionRadioButton.isSelected()) {
                    enableLeaseGroup();
                    disableCashDiscountGroup();
                    disableLoanGroup();
                    disableRebateGroup();
                }
            }
        });
    }

    public void enableRebateGroup() {
        newGradRebateCheckBox.setSelected(false);
        newGradRebateCheckBox.setEnabled(true);
        militaryRebateCheckBox.setSelected(false);
        militaryRebateCheckBox.setEnabled(true);
        newGradRebateTextBox.setText("");
        militaryRebateTextBox.setText("");
    }

    public void disableRebateGroup() {
        newGradRebateCheckBox.setSelected(false);
        newGradRebateCheckBox.setEnabled(false);
        militaryRebateCheckBox.setSelected(false);
        militaryRebateCheckBox.setEnabled(false);
        newGradRebateTextBox.setEnabled(false);
        newGradRebateTextBox.setText("");
        militaryRebateTextBox.setEnabled(false);
        militaryRebateTextBox.setText("");
    }

    private void createRebateIncentiveComponents() {
        rebateSectionRadioButton = new JRadioButton("Rebate Incentive");
        rebateSectionRadioButton.setFont(new Font("Dialog", Font.BOLD, 14));
        rebateSectionRadioButton.setBounds(427, 121, 204, 23);
        rebateSectionRadioButton.setActionCommand(String.valueOf(IncentiveType.REBATE));
        detailsPanel.add(rebateSectionRadioButton);

        newGradRebateCheckBox = new JCheckBox("Rebate for new Grads");
        newGradRebateCheckBox.setBounds(456, 156, 165, 23);
        detailsPanel.add(newGradRebateCheckBox);

        militaryRebateCheckBox = new JCheckBox("Rebate for Millitary Veterans");
        militaryRebateCheckBox.setBounds(456, 191, 221, 23);
        detailsPanel.add(militaryRebateCheckBox);

        newGradRebateTextBox = new JTextField();
        newGradRebateTextBox.setBounds(688, 155, 63, 26);
        detailsPanel.add(newGradRebateTextBox);
        newGradRebateTextBox.setColumns(10);

        militaryRebateTextBox = new JTextField();
        militaryRebateTextBox.setColumns(10);
        militaryRebateTextBox.setBounds(688, 190, 63, 26);
        detailsPanel.add(militaryRebateTextBox);

        rebateSectionRadioButton.addActionListener(e -> {
            if (rebateSectionRadioButton.isSelected()) {
                enableRebateGroup();
                disableCashDiscountGroup();
                disableLoanGroup();
                disableLeaseGroup();
            }
        });

        militaryRebateCheckBox.addActionListener(e -> {
            if (militaryRebateCheckBox.isSelected()) {
                militaryRebateTextBox.setEnabled(true);
            } else {
                militaryRebateTextBox.setEnabled(false);
            }
            militaryRebateTextBox.setText("");
        });

        newGradRebateCheckBox.addActionListener(e -> {
            if (newGradRebateCheckBox.isSelected()) {
                newGradRebateTextBox.setEnabled(true);
            } else {
                newGradRebateTextBox.setEnabled(false);
            }
            newGradRebateTextBox.setText("");
        });
    }

    public void disableLoanGroup() {
        interestRateTextField.setText("");
        interestRateTextField.setEnabled(false);
        loanDurationInMonthsTextField.setText("");
        loanDurationInMonthsTextField.setEnabled(false);
    }

    public void enableLoanGroup() {
        interestRateTextField.setText("");
        interestRateTextField.setEnabled(true);
        loanDurationInMonthsTextField.setText("");
        loanDurationInMonthsTextField.setEnabled(true);
    }

    private void createLoanIncentiveComponents() {
        loanSectionRadioButton = new JRadioButton("Loan Incentive");
        loanSectionRadioButton.setFont(new Font("Dialog", Font.BOLD, 14));
        loanSectionRadioButton.setBounds(68, 270, 204, 23);
        loanSectionRadioButton.setActionCommand(String.valueOf(IncentiveType.LOAN));
        detailsPanel.add(loanSectionRadioButton);

        interestRateLabel = new JLabel("Interest Rate (%)");
        interestRateLabel.setBounds(107, 304, 102, 16);
        detailsPanel.add(interestRateLabel);

        loanDurationInMonthsLabel = new JLabel("Number of months");
        loanDurationInMonthsLabel.setBounds(107, 332, 119, 16);
        detailsPanel.add(loanDurationInMonthsLabel);

        interestRateTextField = new JTextField();
        interestRateTextField.setBounds(238, 299, 50, 26);
        detailsPanel.add(interestRateTextField);
        interestRateTextField.setColumns(10);

        loanDurationInMonthsTextField = new JTextField();
        loanDurationInMonthsTextField.setColumns(10);
        loanDurationInMonthsTextField.setBounds(238, 327, 50, 26);
        detailsPanel.add(loanDurationInMonthsTextField);

        loanSectionRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (loanSectionRadioButton.isSelected()) {
                    enableLoanGroup();
                    disableCashDiscountGroup();
                    disableLeaseGroup();
                    disableRebateGroup();
                }
            }
        });
    }

    public void enableCashDiscountGroup() {
        flatRateDiscountRadioButton.setSelected(true);
        flatRateDiscountRadioButton.setEnabled(true);
        percentageRateDiscountRadioButton.setEnabled(true);
        flatRateDiscountTextField.setText("");
        flatRateDiscountTextField.setEnabled(true);
        percentageRateDiscountTextField.setText("");
        percentageRateDiscountTextField.setEnabled(true);
    }

    public void disableCashDiscountGroup() {
        flatRateDiscountRadioButton.setSelected(false);
        flatRateDiscountRadioButton.setEnabled(false);
        percentageRateDiscountRadioButton.setSelected(false);
        percentageRateDiscountRadioButton.setEnabled(false);
        flatRateDiscountTextField.setText("");
        flatRateDiscountTextField.setEnabled(false);
        percentageRateDiscountTextField.setText("");
        percentageRateDiscountTextField.setEnabled(false);
    }

    private void createCashDiscountIncentiveComponents() {
        cashDicountSectionRadioButton = new JRadioButton("Discount Incentive");
        cashDicountSectionRadioButton.setFont(new Font("Dialog", Font.BOLD, 14));
        cashDicountSectionRadioButton.setBounds(68, 121, 204, 23);
        cashDicountSectionRadioButton.setActionCommand(String.valueOf(IncentiveType.DISCOUNT));
        cashDicountSectionRadioButton.setSelected(true);
        detailsPanel.add(cashDicountSectionRadioButton);

        flatRateDiscountRadioButton = new JRadioButton("$");
        flatRateDiscountRadioButton.setBounds(107, 156, 50, 23);
        flatRateDiscountRadioButton.setActionCommand(String.valueOf(CashDiscountType.FLATAMOUNT));
        detailsPanel.add(flatRateDiscountRadioButton);

        percentageRateDiscountRadioButton = new JRadioButton("%");
        percentageRateDiscountRadioButton.setBounds(107, 191, 42, 23);
        percentageRateDiscountRadioButton.setActionCommand(String.valueOf(CashDiscountType.PERCENTAGE));
        detailsPanel.add(percentageRateDiscountRadioButton);

        cashDiscountSelectionRadioButtonGroup = new ButtonGroup();
        cashDiscountSelectionRadioButtonGroup.add(flatRateDiscountRadioButton);
        cashDiscountSelectionRadioButtonGroup.add(percentageRateDiscountRadioButton);


        flatRateDiscountTextField = new JTextField();
        flatRateDiscountTextField.setBounds(161, 156, 75, 26);
        detailsPanel.add(flatRateDiscountTextField);
        flatRateDiscountTextField.setColumns(10);

        percentageRateDiscountTextField = new JTextField();
        percentageRateDiscountTextField.setColumns(10);
        percentageRateDiscountTextField.setBounds(161, 188, 75, 26);
        detailsPanel.add(percentageRateDiscountTextField);

        cashDicountSectionRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cashDicountSectionRadioButton.isSelected()) {
                    enableCashDiscountGroup();
                    disableLoanGroup();
                    disableLeaseGroup();
                    disableRebateGroup();
                }
            }
        });

        flatRateDiscountRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (flatRateDiscountRadioButton.isSelected()) {
                    flatRateDiscountTextField.setEnabled(true);
                    flatRateDiscountTextField.setText("");
                    percentageRateDiscountTextField.setText("");
                    percentageRateDiscountTextField.setEnabled(false);
                }
            }
        });

        percentageRateDiscountRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (percentageRateDiscountRadioButton.isSelected()) {
                    percentageRateDiscountTextField.setText("");
                    percentageRateDiscountTextField.setEnabled(true);
                    flatRateDiscountTextField.setText("");
                    flatRateDiscountTextField.setEnabled(false);
                }
            }
        });

    }

    private void createStartDateComponents() {
        startDateTextBox = new JTextField();
        startDateTextBox.setBounds(184, 26, 89, 30);
        startDateTextBox.setFont(new Font("Dialog", Font.BOLD, 12));

        startDateCalendarPanel = new CalendarPanel(startDateTextBox, "yyyy/MM/dd");
        startDateCalendarPanel.initCalendarPanel();
        detailsPanel.add(startDateCalendarPanel);
        detailsPanel.add(startDateTextBox);

        startDateLabel = new JLabel("Start Date");
        startDateLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        startDateLabel.setBounds(68, 26, 89, 30);
        detailsPanel.add(startDateLabel);
    }

    private void createEndDateComponents() {
        endDateTextBox = new JTextField();
        endDateTextBox.setBounds(551, 26, 89, 30);
        endDateTextBox.setFont(new Font("Dialog", Font.BOLD, 12));

        endDateCalendarPanel = new CalendarPanel(endDateTextBox, "yyyy/MM/dd");
        endDateCalendarPanel.initCalendarPanel();
        detailsPanel.add(endDateCalendarPanel);
        detailsPanel.add(endDateTextBox);

        endDateLabel = new JLabel("End Date");
        endDateLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        endDateLabel.setBounds(432, 26, 89, 30);
        detailsPanel.add(endDateLabel);
    }

    public static void main(String[] args) throws SQLException {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                new IncentiveManagerUI(NewJDBC.getInstance(), "10");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}