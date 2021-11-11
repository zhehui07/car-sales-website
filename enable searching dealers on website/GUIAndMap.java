
package group1;
import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.view.swing.BrowserView;
import static com.teamdev.jxbrowser.engine.RenderingMode.HARDWARE_ACCELERATED;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.*;
import java.awt.*;
public class GUIAndMap {

    private final DealerSearch dealerSearch;

    public GUIAndMap() {
        dealerSearch = new DealerSearch();
    }

    private void GUI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        JFrame frame = new JFrame("FIND YOUR DEALER");
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        defaults.putIfAbsent("Table.alternateRowColor", new Color(240, 240, 240));

        frame.setSize(1200, 800);
        ImageIcon logo = new ImageIcon("src/group1/img/5100logo.png");
        logo.setImage(logo.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT));

        //create the header panel that contains logo + search bar
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBorder(BorderFactory.createEtchedBorder());
        JLabel headerLabel = new JLabel();
        headerLabel.setIcon(logo);
        headerLabel.setHorizontalAlignment(headerLabel.CENTER);
        header.add(headerLabel);

        //Search bar
        JPanel searchPanel = dealerSearch.getSearchContainerPanel();
        header.add(searchPanel);

        //create the panel that contains map
        JPanel mapAndOutput = new JPanel(new GridLayout(1, 2));
        mapAndOutput.setBackground(Color.LIGHT_GRAY);
        mapAndOutput.setBorder(BorderFactory.createEtchedBorder());


        //This is a google map
        // Creating and running Chromium engine
        Engine engine = Engine.newInstance(
                EngineOptions.newBuilder(HARDWARE_ACCELERATED)
                        .licenseKey("6P830J66YB340FFLFB8VP9JUX9DSELH7EBMDKZM6TC3X6IRWKD6B0VO6SS72ANETDSF3")
                        .build());

        Browser browser = engine.newBrowser();
        // Loading the required web page

        browser.navigation().loadUrl(new File("src/group1/index.html").getAbsolutePath());

        SwingUtilities.invokeLater(() -> {
            // Creating Swing component for rendering web content
            // loaded in the given Browser instance
            BrowserView view = BrowserView.newInstance(browser);

            mapAndOutput.add(dealerDisplay());
            mapAndOutput.add(view);

            // Creating and displaying Swing app frame
            //JFrame frame = new JFrame("Google Map");
            // Closing the engine when app frame is about to close

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    engine.close();
                }
            });

        });

        frame.setLayout(new BorderLayout());
        frame.add(header, BorderLayout.NORTH);
        frame.add(mapAndOutput, BorderLayout.CENTER);
        SwingUtilities.updateComponentTreeUI(frame);
        frame.setVisible(true);
        frame.setResizable(true);
    }

    private JScrollPane dealerDisplay() {
        JScrollPane output = new JScrollPane(dealerSearch.getListDealersPanel(),
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        output.setBackground(Color.WHITE);
        output.setBorder(BorderFactory.createLoweredBevelBorder());
        output.setPreferredSize(new Dimension(180, 300));
        output.getVerticalScrollBar();
        return output;
    }


    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        GUIAndMap runApp = new GUIAndMap();
        runApp.GUI();
    }
}

