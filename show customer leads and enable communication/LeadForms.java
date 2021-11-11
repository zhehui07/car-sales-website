package group3;
/**
 * @author  Zhehui Yang
 * @date: 2021/4/15
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class LeadForms extends JFrame implements ItemListener {
    private int numberOfLeads;
    private int height;
    private int width;
    private Map<String, List<LeadForm>> modelToLeadsMap;
    private Map<String, List<LeadForm>> emailToLeadsMap;
    private JPanel mainPanel;
    private JPanel listingPanel;
    private JPanel optionPanel;
    private JComboBox<String> filteredByComboBox;
    public LeadForms(String title) {
        super(title);
        width = 750;
        modelToLeadsMap = new HashMap<>();
        emailToLeadsMap = new HashMap<>();
        listingPanel = new JPanel();
    }

    public void init(List<Lead> leads) {
        super.setSize(300, 300);
        mainPanel = new JPanel();
        super.setContentPane(mainPanel);
        this.numberOfLeads = leads.size();
        height = numberOfLeads * 400;
        listingPanel.setLayout(new GridLayout(numberOfLeads, 1));
        mainPanel.setLayout(new GridBagLayout());
        createLeads(leads);
        setFilter();
        super.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // this.pack();
        this.setVisible(true);
    }

    public void setFilter(){
        optionPanel = new JPanel();
        optionPanel.setSize(500, 10);
        optionPanel.add(new JLabel("Filter By "));
        optionPanel.add(filteredByComboBox);
        optionPanel.setSize(new Dimension(750, 100));
        GridBagConstraints controlLayout = new GridBagConstraints();
        controlLayout.fill = GridBagConstraints.VERTICAL;
        controlLayout.gridy = 0;
        controlLayout.ipadx = 750;
        mainPanel.add(optionPanel, controlLayout);
        controlLayout.ipady = 500;
        controlLayout.gridy = 1;
        controlLayout.gridwidth = 3;
        mainPanel.add(new JScrollPane(listingPanel), controlLayout);
    }

    public void createLeads(List<Lead> leads){
        filteredByComboBox = new JComboBox<String>();
        for (int i = 0; i < numberOfLeads; i++) {
            Lead lead = leads.get(i);
            System.out.println("Current lead name is" + lead.getLastName());
            LeadForm leadForm = new LeadForm();
            leadForm.init(this, lead);
            leadForm.getMainPanel().setSize(750, 100);
            listingPanel.add(leadForm.getMainPanel(), BorderLayout.NORTH);
            if (modelToLeadsMap.get(lead.getCarModel().toUpperCase()) != null) {
                modelToLeadsMap.get(lead.getCarModel().toUpperCase()).add(leadForm);
            } else {
                filteredByComboBox.addItem(lead.getCarModel().toUpperCase());
                List<LeadForm> tmpLeadFormList = new ArrayList<>();
                tmpLeadFormList.add(leadForm);
                modelToLeadsMap.put(lead.getCarModel().toUpperCase(), tmpLeadFormList);
            }
        }
        filteredByComboBox.addItemListener(this);
    }

    public void updateFrame(JFrame frame){
        frame.setSize(750, numberOfLeads * 100);
    }

    @Override
    public void setSize(int width, int height) {
        System.out.println("height is " + this.height);
        this.height += height;
    }

    @Override
    public void itemStateChanged(ItemEvent e){
        if (e.getStateChange() == ItemEvent.SELECTED) {

            listingPanel.removeAll();
            List<LeadForm> selectedLeads = modelToLeadsMap.get(e.getItem());
            listingPanel.setLayout(new GridLayout(selectedLeads.size(), 1));
            for (LeadForm leadForm: selectedLeads) {
                listingPanel.add(leadForm.getMainPanel(), BorderLayout.NORTH);
            }
            listingPanel.updateUI();
        }
    }
}
