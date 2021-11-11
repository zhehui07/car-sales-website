package group3;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

public class Home extends JFrame{
    private JPanel mainPanel;
    private JButton viewLeads;
    public Home(){
        super.setTitle("Dealer's Home Page");
        createComponents();
        // mainPanel.setSize(750,500);
        super.setSize(750,500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // this.pack();
        this.setVisible(true);
    }
    public void createComponents(){
        mainPanel = new JPanel();
        super.setContentPane(mainPanel);
        viewLeads = new JButton("View Leads");
        mainPanel.add(viewLeads);
        implementViewLeads();
    }
    public void implementViewLeads(){
        viewLeads.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LeadForms frame = new LeadForms("Leads List");
                LeadDataAccessor lda = new LeadDataAccessor("Lead.csv");
                frame.init(lda.getLeads());
            }
        });

    }
    public static void main(String[] args){
        Home h = new Home();
    }

}
