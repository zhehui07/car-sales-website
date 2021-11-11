package group2;

import group2.dao.VehicleDAO;
import group2.utils.Utils;
import group6.FormActionDirectory;
import group6.LeadFormController;
import group8.*;
import group8.data.NewJDBC;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewVehicleDetails {
    private JPanel innerPanel;
    private JPanel vehicleShortDescriptionPanel;
    private JPanel vehicleDetailsPanel;
    private JPanel vehicleImageAndLeadFormPanel;
    private JPanel vehicleInfoPanel;
    private JPanel vehicleInfoAndIncentivePanel;
    private JScrollPane vehicleDescriptionPane;
    private JPanel vehicleDescriptionPanel;
    private JScrollPane dealersInformationPane;
    private JPanel dealersInformationPanel;
    private JLabel vehicleMSRPLabel;
    private JPanel vehicleImagePanel;
    private JButton leadFormButton;
    private JLabel vehicleInfoLabel;
    private JLabel vehicleDescriptionLabel;
    private JLabel dealersInformationLabel;
    private JTextArea vehicleDescriptionTextArea;
    private JTextArea dealersInformationTextArea;
    private JLabel vehicleNameLabel;
    private JScrollPane scrollPane;
    private Dimension iconLabelDimension = new Dimension(60, 45);
    private Dimension imageLabelDimension = new Dimension(120, 45);
    private Dimension dataLabelDimension = new Dimension(196, 45);
    private Dimension labelPanelDimension = new Dimension(376, 45);
    private ImageIcon carImage;
    private ImageIcon engineImage;
    private ImageIcon transmissionImage;
    private ImageIcon vinImage;
    private ImageIcon fuelImage;
    private ImageIcon colorImage;
    private ImageIcon stockNumberImage;
    private ImageIcon mileageImage;
    private ImageIcon seatCountImage;
    private ImageIcon ratingsImage;
    private JPanel requestLeadFormPanel;
    private JPanel vehicleInfoLabelPanel;
    private JPanel vehicleConditionLabelPanel;
    private JPanel vehicleEngineLabelPanel;
    private JPanel vehicleTransmissionLabelPanel;
    private JPanel vehicleVINLabelPanel;
    private JPanel vehicleFuelLabelPanel;
    private JPanel vehicleColorLabelPanel;
    private JPanel vehicleStockNumberLabelPanel;
    private JPanel vehicleMileageLabelPanel;
    private JPanel vehicleSeatCountLabelPanel;
    private JPanel vehicleRatingsLabelPanel;
    private JPanel discountedPricePanel;
    private JLabel discountPriceLabel;
    private JFrame frame;
    private Car car;
    private Dealer dealer;
    private List<Incentive> incentives;
    private String discountedPrice = "";
    private static final Font vehiclePriceFont = new Font("Calibri", Font.BOLD, 26);
    private static final Font cashIncentiveFont = new Font("Calibri", Font.BOLD, 26);
    private static final Font nonCashIncentiveFont = new Font("Calibri", Font.BOLD, 16);
    private JLabel discountStaticLabel;


    public ViewVehicleDetails(String vehicleId) {
        VehicleDAO vehicleDAO = new VehicleDAO();

        try {
            final List<Map<String, Object>> res = vehicleDAO.getById(Integer.parseInt(vehicleId));
            this.car = Utils.transToCarV2(res.get(0));
            this.dealer = Utils.transToDealer(res.get(0));
            incentives = NewJDBC.getInstance().getAllIncentiveByCarVIN(this.car.getVIN());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void showVehicleDetails() {
        createVehicleShortDescriptionPanel(this.car);
        createVehicleDetailsPanel(this.car);
        createVehicleDescriptionPanel(this.car);
        createDealersInformationPanel(this.dealer);
        createOuterPanel();
    }

    public void createOuterPanel() {
        frame = new JFrame();

        scrollPane = new JScrollPane();
        scrollPane.setBackground(new Color(-4473925));
        scrollPane.setMaximumSize(new Dimension(916, 970));
        scrollPane.setMinimumSize(new Dimension(916, 970));
        scrollPane.setPreferredSize(new Dimension(916, 970));
//        scrollPane.setBorder(new LineBorder(new Color(255,0,0)));

        innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
        innerPanel.setPreferredSize(new Dimension(916, 970));
        innerPanel.setMaximumSize(new Dimension(916, 970));
        innerPanel.setMinimumSize(new Dimension(916, 970));
//        innerPanel.setBorder(new LineBorder(new Color(0,255,0)
//        ));
        scrollPane.setViewportView(innerPanel);

        innerPanel.add(vehicleShortDescriptionPanel, 0);
        innerPanel.add(vehicleDetailsPanel, 1);
        innerPanel.add(vehicleDescriptionPanel, 2);
        innerPanel.add(dealersInformationPanel, 3);
        JPanel additionalBlankPanel = new JPanel();
        additionalBlankPanel.setBorder(new LineBorder(Color.BLACK));
        additionalBlankPanel.add(Box.createRigidArea(new Dimension(916, 1)));
        additionalBlankPanel.setPreferredSize(new Dimension(916, 1));
        additionalBlankPanel.setMaximumSize(new Dimension(916, 1));
        additionalBlankPanel.setMinimumSize(new Dimension(916, 1));
        innerPanel.add(additionalBlankPanel);

        frame.pack();
        frame.setSize(new Dimension(950, 980));
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setVisible(true);
        frame.setContentPane(scrollPane);
    }

    public void createVehicleShortDescriptionPanel(Car myCar) {

        vehicleShortDescriptionPanel = new JPanel();
        vehicleShortDescriptionPanel.setPreferredSize(new Dimension(916, 74));
        vehicleShortDescriptionPanel.setMaximumSize(new Dimension(916, 74));
        vehicleShortDescriptionPanel.setMinimumSize(new Dimension(916, 74));
        vehicleShortDescriptionPanel.setLayout(new BoxLayout(vehicleShortDescriptionPanel, BoxLayout.X_AXIS));
        vehicleShortDescriptionPanel.setBorder(new LineBorder(Color.BLACK));

        vehicleNameLabel = new JLabel();

        if (myCar.getMake() == null || myCar.getModel() == null) {
            vehicleNameLabel.setText("--");
        } else {
            vehicleNameLabel.setText(myCar.getYear() + " " + myCar.getMake() + " " + myCar.getModel());
        }
        vehicleNameLabel.setFont(new Font("Calibri", Font.BOLD, 36));
        vehicleNameLabel.setMinimumSize(new Dimension(540, 74));
        vehicleNameLabel.setMaximumSize(new Dimension(540, 74));
        vehicleNameLabel.setPreferredSize(new Dimension(540, 74));
//        vehicleNameLabel.setBorder(new LineBorder(Color.BLACK));

        JPanel priceDetailsPanel = new JPanel();
        priceDetailsPanel.setLayout(new BoxLayout(priceDetailsPanel, BoxLayout.X_AXIS));
        priceDetailsPanel.setPreferredSize(new Dimension(376, 74));
        priceDetailsPanel.setMinimumSize(new Dimension(376, 74));
        priceDetailsPanel.setMaximumSize(new Dimension(376, 74));

        discountedPricePanel = new JPanel();
        discountedPricePanel.setLayout(new BoxLayout(discountedPricePanel, BoxLayout.Y_AXIS));
        discountedPricePanel.setPreferredSize(new Dimension(200, 74));
        discountedPricePanel.setMinimumSize(new Dimension(200, 74));
        discountedPricePanel.setMaximumSize(new Dimension(200, 74));

        discountStaticLabel = new JLabel();
        discountStaticLabel.setText("After Incentive");
        discountStaticLabel.setFont(new Font("Calibri", Font.PLAIN, 16));
        discountStaticLabel.setPreferredSize(new Dimension(200, 30));
        discountStaticLabel.setMinimumSize(new Dimension(200, 30));
        discountStaticLabel.setMaximumSize(new Dimension(200, 30));

        discountPriceLabel = new JLabel();
        discountPriceLabel.setText(discountedPrice);
        discountPriceLabel.setPreferredSize(new Dimension(200, 44));
        discountPriceLabel.setMinimumSize(new Dimension(200, 44));
        discountPriceLabel.setMaximumSize(new Dimension(200, 74));
        discountPriceLabel.setFont(vehiclePriceFont);

        discountedPricePanel.add(discountStaticLabel);
        discountedPricePanel.add(discountPriceLabel);

        JPanel msrpPanel = new JPanel();
        msrpPanel.setLayout(new BoxLayout(msrpPanel, BoxLayout.Y_AXIS));
        msrpPanel.setPreferredSize(new Dimension(176, 74));
        msrpPanel.setMinimumSize(new Dimension(176, 74));
        msrpPanel.setMaximumSize(new Dimension(176, 74));

        JLabel msrpStaticLabel = new JLabel();
        msrpStaticLabel.setText("MSRP");
        msrpStaticLabel.setFont(new Font("Calibri", Font.PLAIN, 16));
        msrpStaticLabel.setPreferredSize(new Dimension(176, 30));
        msrpStaticLabel.setMinimumSize(new Dimension(176, 30));
        msrpStaticLabel.setMaximumSize(new Dimension(176, 30));

        vehicleMSRPLabel = new JLabel();
        if (myCar.getMSRP() == 0) {
            vehicleMSRPLabel.setText("--");
        } else {
            vehicleMSRPLabel.setText("$ " + myCar.getMSRP());
        }
        vehicleMSRPLabel.setFont(vehiclePriceFont);
        vehicleMSRPLabel.setMinimumSize(new Dimension(176, 44));
        vehicleMSRPLabel.setMaximumSize(new Dimension(176, 44));
        vehicleMSRPLabel.setPreferredSize(new Dimension(176, 44));

        msrpPanel.add(msrpStaticLabel);
        msrpPanel.add(vehicleMSRPLabel);

        priceDetailsPanel.add(msrpPanel);
        discountedPricePanel.setVisible(false);
        priceDetailsPanel.add(discountedPricePanel);

        vehicleShortDescriptionPanel.add(vehicleNameLabel);
        vehicleShortDescriptionPanel.add(priceDetailsPanel);
    }

    public void createVehicleDetailsPanel(Car myCar) {
        vehicleDetailsPanel = new JPanel();
        vehicleDetailsPanel.setLayout(new BoxLayout(vehicleDetailsPanel, BoxLayout.X_AXIS));
        vehicleDetailsPanel.setPreferredSize(new Dimension(916, 600));
        vehicleDetailsPanel.setMaximumSize(new Dimension(916, 600));
        vehicleDetailsPanel.setMinimumSize(new Dimension(916, 600));
//        vehicleDetailsPanel.setBorder(new LineBorder(new Color(0, 0, 0)));

        createVehicleImageAndLeadFormPanel(myCar);
        createVehicleInfoDetailsPanel(myCar);
        vehicleDetailsPanel.add(vehicleImageAndLeadFormPanel, 0);
        vehicleDetailsPanel.add(vehicleInfoAndIncentivePanel, 1);
    }

    public void createVehicleImageAndLeadFormPanel(Car myCar) {
        vehicleImageAndLeadFormPanel = new JPanel();
        vehicleImageAndLeadFormPanel.setLayout(new BoxLayout(vehicleImageAndLeadFormPanel, BoxLayout.Y_AXIS));
        vehicleImageAndLeadFormPanel.setPreferredSize(new Dimension(540, 600));
        vehicleImageAndLeadFormPanel.setMinimumSize(new Dimension(540, 600));
        vehicleImageAndLeadFormPanel.setMaximumSize(new Dimension(540, 600));
//        vehicleImageAndLeadFormPanel.setBorder(new LineBorder(new Color(0,0,190)));

        vehicleImagePanel = new VehicleImagePanel(car.getImages()).imagePanel();
        requestLeadFormPanel = new JPanel();
        requestLeadFormPanel.setLayout(new BoxLayout(requestLeadFormPanel, BoxLayout.X_AXIS));
        requestLeadFormPanel.setPreferredSize(new Dimension(540, 100));
        requestLeadFormPanel.setMinimumSize(new Dimension(540, 100));
        requestLeadFormPanel.setMaximumSize(new Dimension(540, 100));
        requestLeadFormPanel.setBorder(new LineBorder(Color.black));

        leadFormButton = new JButton();
        leadFormButton.setText("Request Lead Form");
        leadFormButton.setFont(new Font(Font.SERIF, Font.PLAIN, 16));
        leadFormButton.setBorder(new BevelBorder(0));
        leadFormButton.setPreferredSize(new Dimension(200, 30));
        leadFormButton.setMaximumSize(new Dimension(200, 30));
        leadFormButton.setMinimumSize(new Dimension(200, 30));
        leadFormButton.setBackground(new Color(185, 185, 185));
        leadFormButton.setFocusPainted(false);

        leadFormButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FormActionDirectory formAction = new FormActionDirectory(myCar);
                LeadFormController leadFormRequest = new LeadFormController(myCar, formAction);
                leadFormRequest.showLeadForm();
            }
        });

        requestLeadFormPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leadFormButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        requestLeadFormPanel.add(Box.createRigidArea(new Dimension(170, 100)));
        requestLeadFormPanel.add(leadFormButton);
        requestLeadFormPanel.add(Box.createRigidArea(new Dimension(170, 100)));

        vehicleImageAndLeadFormPanel.add(vehicleImagePanel, 0);
        vehicleImageAndLeadFormPanel.add(requestLeadFormPanel, 1);
    }

    public void createVehicleInfoDetailsPanel(Car myCar) {

        vehicleInfoAndIncentivePanel = new JPanel();
        vehicleInfoAndIncentivePanel.setLayout(new BoxLayout(vehicleInfoAndIncentivePanel, BoxLayout.Y_AXIS));
        vehicleInfoAndIncentivePanel.setPreferredSize(new Dimension(376, 600));
        vehicleInfoAndIncentivePanel.setMinimumSize(new Dimension(376, 600));
        vehicleInfoAndIncentivePanel.setMaximumSize(new Dimension(376, 600));

        vehicleInfoPanel = new JPanel();
        vehicleInfoPanel.setLayout(new BoxLayout(vehicleInfoPanel, BoxLayout.Y_AXIS));

        vehicleInfoPanel.setBorder(new LineBorder(Color.BLACK));
        vehicleInfoPanel.setPreferredSize(new Dimension(376, 500));
        vehicleInfoPanel.setMaximumSize(new Dimension(376, 500));
        vehicleInfoPanel.setMaximumSize(new Dimension(376, 500));
        vehicleInfoPanel.setBorder(new LineBorder(Color.BLACK));

        vehicleInfoLabelPanel = new JPanel();
        vehicleInfoLabelPanel.setLayout(new BoxLayout(vehicleInfoLabelPanel, BoxLayout.Y_AXIS));
        vehicleInfoLabelPanel.setPreferredSize(new Dimension(376, 45));
        vehicleInfoLabelPanel.setMaximumSize(new Dimension(376, 45));
        vehicleInfoLabelPanel.setMinimumSize(new Dimension(376, 45));

        vehicleInfoLabel = new JLabel();
        vehicleInfoLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        vehicleInfoLabel.setText("Vehicle Info");
        vehicleInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        vehicleInfoLabelPanel.add(vehicleInfoLabel, 0);

        vehicleInfoPanel.add(vehicleInfoLabelPanel);

        carImage = new ImageIcon("src/group2/Icons/carCondition.jpg");
        engineImage = new ImageIcon("src/group2/Icons/CarEngine.jpg");
        transmissionImage = new ImageIcon("src/group2/Icons/Transmission.jpg");
        vinImage = new ImageIcon("src/group2/Icons/VIN.jpg");
        fuelImage = new ImageIcon("src/group2/Icons/Fuel.jpg");
        colorImage = new ImageIcon("src/group2/Icons/CarColor.jpg");
        stockNumberImage = new ImageIcon("src/group2/Icons/vehicleid.jpg");
        mileageImage = new ImageIcon("src/group2/Icons/Mileage.jpg");
        seatCountImage = new ImageIcon("src/group2/Icons/carseat.jpg");
        ratingsImage = new ImageIcon("src/group2/Icons/Ratings.jpg");

        if (myCar.getCarCategory() != null && validateCarInfoDetails(myCar.getCarCategory().toString())) {
            vehicleConditionLabelPanel = createVehicleSubInfoPanel(carImage, "Car Condition", myCar.getCarCategory().toString());
            vehicleInfoPanel.add(vehicleConditionLabelPanel);
        }
        if (validateCarInfoDetails(myCar.getEngine())) {
            vehicleEngineLabelPanel = createVehicleSubInfoPanel(engineImage, "Engine", myCar.getEngine());
            vehicleInfoPanel.add(vehicleEngineLabelPanel);
        }
        if (validateCarInfoDetails(myCar.getTransmission())) {
            vehicleTransmissionLabelPanel = createVehicleSubInfoPanel(transmissionImage, "Transmission", myCar.getTransmission());
            vehicleInfoPanel.add(vehicleTransmissionLabelPanel);
        }
        if (validateCarInfoDetails(myCar.getVIN())) {
            vehicleVINLabelPanel = createVehicleSubInfoPanel(vinImage, "VIN", myCar.getVIN());
            vehicleInfoPanel.add(vehicleVINLabelPanel);
        }
        if (validateCarInfoDetails(myCar.getFuel())) {
            vehicleFuelLabelPanel = createVehicleSubInfoPanel(fuelImage, "Fuel", myCar.getFuel());
            vehicleInfoPanel.add(vehicleFuelLabelPanel);
        }
        if (validateCarInfoDetails(myCar.getColor())) {
            vehicleColorLabelPanel = createVehicleSubInfoPanel(colorImage, "Color", myCar.getColor());
            vehicleInfoPanel.add(vehicleColorLabelPanel);
        }
        if (validateCarInfoDetails(myCar.getstockNum())) {
            vehicleStockNumberLabelPanel = createVehicleSubInfoPanel(stockNumberImage, "Stock #", myCar.getStockNum());
            vehicleInfoPanel.add(vehicleStockNumberLabelPanel);
        }
        if (myCar.getMileage() >= 0) {
            vehicleMileageLabelPanel = createVehicleSubInfoPanel(mileageImage, "Mileage", Integer.toString(myCar.getMileage()));
            vehicleInfoPanel.add(vehicleMileageLabelPanel);
        }
        if (myCar.getSeatCount() > 0) {
            vehicleSeatCountLabelPanel = createVehicleSubInfoPanel(seatCountImage, "No of Seats", Integer.toString(myCar.getSeatCount()));
            vehicleInfoPanel.add(vehicleSeatCountLabelPanel);
        }
        if (myCar.getRating() >= 0) {
            vehicleRatingsLabelPanel = createVehicleSubInfoPanel(ratingsImage, "Ratings", Integer.toString(myCar.getRating()));
            vehicleInfoPanel.add(vehicleRatingsLabelPanel);
        }

        JPanel incentivePanel = new JPanel();
        incentivePanel.setLayout(new BoxLayout(incentivePanel, BoxLayout.X_AXIS));
        incentivePanel.setMaximumSize(new Dimension(376, 100));
        incentivePanel.setMinimumSize(new Dimension(376, 100));
        incentivePanel.setPreferredSize(new Dimension(376, 100));
        incentivePanel.setBorder(new LineBorder(Color.BLACK));

        JLabel incentiveStaticLabel = new JLabel();
        incentiveStaticLabel.setText("  Incentive Options");
        incentiveStaticLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        incentiveStaticLabel.setPreferredSize(new Dimension(178, 45));
        incentiveStaticLabel.setMaximumSize(new Dimension(178, 45));
        incentiveStaticLabel.setMinimumSize(new Dimension(178, 45));
        incentivePanel.add(incentiveStaticLabel);

        List<String> incentiveDescription = new ArrayList<>();
        incentiveDescription.add("Select Incentive type");
        for (Incentive incentive : incentives) {
            incentiveDescription.add(incentive.getDescription());
        }

        JComboBox incentiveOptions = new JComboBox(incentiveDescription.toArray());
        incentiveOptions.setFont(new Font("Calibri", Font.BOLD, 16));
        incentiveOptions.setPreferredSize(new Dimension(178, 42));
        incentiveOptions.setMaximumSize(new Dimension(178, 42));
        incentiveOptions.setMinimumSize(new Dimension(178, 42));
        incentivePanel.add(incentiveOptions);

        ActionListener incentiveOptionsActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Incentive incentive = null;
                if (incentiveOptions.getSelectedIndex() == 0) {
                    discountedPricePanel.setVisible(false);
                    return;
                }
                try {
                    incentive = incentives.get(incentiveOptions.getSelectedIndex() - 1);
                    discountedPrice = NewJDBC.getInstance().applyDiscount(incentive, car);
//                    System.out.println(discountedPrice);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                discountedPricePanel.setVisible(true);
                if (discountedPrice != null && discountedPrice.trim().length() > 0 && incentive != null) {
                    if(incentive instanceof CashDiscountIncentive) {
                        discountStaticLabel.setVisible(true);
                        discountPriceLabel.setFont(cashIncentiveFont);
                    } else {
                        discountStaticLabel.setVisible(false);
                        discountedPrice = "<html>" + discountedPrice + "</html>";
                        discountedPrice = discountedPrice.replaceAll("\n", "<br>");
                        discountPriceLabel.setFont(nonCashIncentiveFont);
                    }
                    discountPriceLabel.setText(discountedPrice);
                } else {
                    discountPriceLabel.setText("--");
                }
            }
        };

        incentiveOptions.addActionListener(incentiveOptionsActionListener);

        vehicleInfoAndIncentivePanel.add(vehicleInfoPanel);
        vehicleInfoAndIncentivePanel.add(incentivePanel);
    }

    private JPanel createVehicleSubInfoPanel(ImageIcon image, String imageText, String dataText) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setMinimumSize(labelPanelDimension);
        panel.setMaximumSize(labelPanelDimension);
        panel.setPreferredSize(labelPanelDimension);
        panel.add(Box.createRigidArea(new Dimension(5, 0)));
        panel.add(createVehicleIconLabel(image));
        panel.add(createVehicleImageLabel(imageText));
        panel.add(createVehicleDataLabel(dataText));
        return panel;
    }

    private boolean validateCarInfoDetails(String s) {
        if (s == null || s.trim().length() == 0) {
            return false;
        }
        return true;
    }

    private JLabel createVehicleIconLabel(ImageIcon image) {
        JLabel label = new JLabel();
        label.setIcon(image);
        label.setPreferredSize(iconLabelDimension);
        label.setMaximumSize(iconLabelDimension);
        label.setMinimumSize(iconLabelDimension);
        return label;
    }

    private JLabel createVehicleDataLabel(String text) {
        JLabel label = new JLabel();
        label.setText(text);
        label.setPreferredSize(dataLabelDimension);
        label.setMinimumSize(dataLabelDimension);
        label.setMinimumSize(dataLabelDimension);
        label.setFont(new Font("Calibri", Font.BOLD, 16));
        return label;
    }

    private JLabel createVehicleImageLabel(String text) {
        JLabel label = new JLabel();
        label.setText(text);
        label.setPreferredSize(imageLabelDimension);
        label.setMaximumSize(imageLabelDimension);
        label.setMinimumSize(imageLabelDimension);
        label.setFont(new Font("Calibri", Font.BOLD, 16));
        return label;
    }

    public void createVehicleDescriptionPanel(Car myCar) {
        //vehicleDescriptionPanel start
        vehicleDescriptionPanel = new JPanel();
        vehicleDescriptionPanel.setLayout(new BoxLayout(vehicleDescriptionPanel, BoxLayout.Y_AXIS));
        vehicleDescriptionPanel.setMaximumSize(new Dimension(916, 140));
        vehicleDescriptionPanel.setMinimumSize(new Dimension(916, 140));
        vehicleDescriptionPanel.setPreferredSize(new Dimension(916, 140));

        vehicleDescriptionPanel.setBackground(new Color(187, 187, 187));
        vehicleDescriptionPanel.setBorder(new LineBorder(Color.BLACK));

        vehicleDescriptionLabel = new JLabel();
        vehicleDescriptionLabel.setText("Vehicle Description");
        vehicleDescriptionLabel.setFont(new Font("Calibri", Font.BOLD, 16));
        vehicleDescriptionPane = new JScrollPane();
        vehicleDescriptionPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        vehicleDescriptionPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        vehicleDescriptionPane.setMaximumSize(new Dimension(916, 120));
        vehicleDescriptionPane.setMinimumSize(new Dimension(916, 120));
        vehicleDescriptionPane.setPreferredSize(new Dimension(916, 120));
        vehicleDescriptionPane.setBackground(new Color(187, 187, 187));
//        vehicleDescriptionPane1.setBorder(new LineBorder(new Color(0, 0, 0)));

        vehicleDescriptionTextArea = new JTextArea();
        vehicleDescriptionTextArea.setMaximumSize(new Dimension(916, 120));
        vehicleDescriptionTextArea.setMinimumSize(new Dimension(916, 120));
        vehicleDescriptionTextArea.setPreferredSize(new Dimension(916, 120));
        vehicleDescriptionTextArea.setEditable(false);
        vehicleDescriptionTextArea.setBorder(new EmptyBorder(10,10,10,10));
        vehicleDescriptionTextArea.setWrapStyleWord(true);
        vehicleDescriptionTextArea.setVisible(true);
        vehicleDescriptionTextArea.setLineWrap(true);
        vehicleDescriptionTextArea.setBackground(new Color(238, 238, 238));
        if(myCar.getDescription().equals("")){
            vehicleDescriptionTextArea.setText(defaultDescription());
        }else{
            vehicleDescriptionTextArea.setText(myCar.getDescription());
        }
        vehicleDescriptionTextArea.setFont(new Font("Calibri", Font.PLAIN, 13));

        vehicleDescriptionPane.setViewportView(vehicleDescriptionTextArea);

        vehicleDescriptionPanel.add(vehicleDescriptionLabel, 0);
        vehicleDescriptionPanel.add(vehicleDescriptionPane, 1);

        //vehicleDescriptionPanel ends here

    }

    private String defaultDescription(){
        StringBuilder desc = new StringBuilder();
        desc.append(car.getYear() + " " + car.getMake() + " " + car.getModel()).append("\n");
        if(car.getType() != null){
            desc.append(car.getType()).append("\n");
        }
        if(car.getMileage() != 0){
            desc.append(car.getMileage()).append(" mileage").append("\n");
        }
        if(car.getTransmission() != null && car.getEngine() != null){
            desc.append("The vehicle has a ").append(car.getTransmission()).append(" transmission and a ").
                    append(car.getEngine()).append(" engine. ").append("\n");
        }
        if(dealer.getStreetAddress() != null || dealer.getCity() != null || dealer.getStateID() != null){
            desc.append("Available at location ");
            if(dealer.getStreetAddress() != null){
                desc.append(dealer.getStreetAddress()).append(", ");
            }if(dealer.getCity() != null){
                desc.append(dealer.getCity()).append(", ");
            }if(dealer.getStateID() != null){
                desc.append(dealer.getStateID());
            }
        }
        return desc.toString();
    }

    public void createDealersInformationPanel(Dealer myDealer) {
        dealersInformationPanel = new JPanel();
        dealersInformationPanel.setLayout(new BoxLayout(dealersInformationPanel, BoxLayout.Y_AXIS));
        dealersInformationPanel.setMaximumSize(new Dimension(916, 100));
        dealersInformationPanel.setMinimumSize(new Dimension(916, 100));
        dealersInformationPanel.setPreferredSize(new Dimension(916, 100));

        dealersInformationPanel.setBackground(new Color(187, 187, 187));
        dealersInformationPanel.setBorder(new LineBorder(new Color(0, 0, 0)));

        dealersInformationLabel = new JLabel();
        dealersInformationLabel.setText("Dealership Information");
        dealersInformationLabel.setFont(new Font("Calibri", Font.BOLD, 16));

        dealersInformationPane = new JScrollPane();
        dealersInformationPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        dealersInformationPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        dealersInformationPane.setMaximumSize(new Dimension(916, 90));
        dealersInformationPane.setMinimumSize(new Dimension(916, 90));
        dealersInformationPane.setPreferredSize(new Dimension(916, 90));
        dealersInformationPane.setBackground(new Color(187, 187, 187));

        dealersInformationTextArea = new JTextArea();
        dealersInformationTextArea.setMaximumSize(new Dimension(916, 90));
        dealersInformationTextArea.setMinimumSize(new Dimension(916, 90));
        dealersInformationTextArea.setPreferredSize(new Dimension(916, 90));
        dealersInformationTextArea.setEditable(false);
        dealersInformationTextArea.setBorder(new EmptyBorder(0,10,0,10));
        dealersInformationTextArea.setBackground(new Color(238, 238, 238));
        String dealerInfo = "";
        if (myDealer.getName() != null && myDealer.getName().trim().length() != 0) {
            dealerInfo += myDealer.getName();
        }
        if (myDealer.getStreetAddress() != null && myDealer.getName().trim().length() != 0) {
            dealerInfo += "\n" + myDealer.getStreetAddress();
        }
        if (myDealer.getCity() != null && myDealer.getCity().trim().length() != 0) {
            dealerInfo += "\n" + myDealer.getCity();
        }
        if (myDealer.getStateID() != null && myDealer.getStateID().trim().length() != 0) {
            dealerInfo += ", " + myDealer.getStateID();
        }
        if (myDealer.getZipcode() != null && myDealer.getZipcode().trim().length() != 0) {
            dealerInfo += " " + myDealer.getZipcode();
        }
        if (myDealer.getPhoneNumber() != null && myDealer.getPhoneNumber().trim().length() != 0) {
            dealerInfo += "\nPhone " + myDealer.getPhoneNumber();
        }
        dealersInformationTextArea.setText(dealerInfo);
        dealersInformationTextArea.setFont(new Font("Calibri", Font.PLAIN, 13));

        dealersInformationPane.setViewportView(dealersInformationTextArea);

        dealersInformationPanel.add(dealersInformationLabel, 0);
        dealersInformationPanel.add(dealersInformationPane, 1);
    }

    public static void main(String[] args) {
//        Car myCar = new Car();
//        myCar.setID("1234");
//        myCar.setMake("Audi");
//        myCar.setModel("Acura MDX SH-AWD w/Advance");
//        myCar.setYear(2010);
//        myCar.setMSRP(13955);
//        myCar.setCarCategory(CarCategory.USED);
//        myCar.setColor("Grey");
//        myCar.setEngine("3.7L V6");
//        myCar.setTransmission("Automatic 6-Speed");
//        myCar.setFuel("Gasoline");
//        myCar.setVIN("2HNYD2H59AH533278");
//        myCar.setSeatCount(4);
//        myCar.setMileage(1174);
//        myCar.setRating(4);
//        myCar.setDescription("Beautiful White Frost. Abel Chevrolet Buick. Many Financing Options available. Credit Challenged? We can help! We have great relationships with many lenders which allows us to offer financing that many others can't! We're here to help you get in the vehicle you want! At Abel, we do our best to offer you an unique experience when purchasing a New or Pre-Owned vehicle. Unlike traditional car dealers, we offer a non-pressured environment giving you the time and space to make an informed decision. Our advertised prices are our best deal upfront. No Games, just fair prices and outstanding customer service. We won't waste your time! Once you've found the Abel Vehicle you're looking for, on average, you'll go from test drive to driving home in less than an hour!");

//        Dealer myDealer = new Dealer();
//        myDealer.setDealerID("D1234");
//        myDealer.setName("Rairdons Honda of Sumner");
//        myDealer.setStreetAddress("16302 Auto Lane");
//        myDealer.setCity("Sumner");
//        myDealer.setState("Washington");
//        myDealer.setStateID("WA");
//        myDealer.setPhoneNumber("(855) 661-4448");

//        ViewVehicleDetails carDetails = new ViewVehicleDetails(myCar, myDealer);
        ViewVehicleDetails carDetails = new ViewVehicleDetails("28");
        carDetails.showVehicleDetails();
    }

}