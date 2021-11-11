package group2;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VehicleImagePanel {
    private JLabel imageLabel;
    private JButton rightButton;
    private JButton leftButton;
    private List<String> imageList;
    private int index = 0;

    VehicleImagePanel(List<String> images) {
        this.imageList = images;
        rightButton = getIncButton();
        leftButton = getLeftButton();
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(480,500));
        imageLabel.setMinimumSize(new Dimension(480,500));
        imageLabel.setMaximumSize(new Dimension(480,500));
    }

    public JPanel imagePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        imageLabel.setIcon(getImageByIndex(index));
//        imageLabel.setBorder(new EmptyBorder(10, 10, 20, 10));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        if (leftButton != null)
            panel.add(leftButton);
        panel.add(imageLabel);
        if (rightButton != null)
            panel.add(rightButton);
        //panel.setBackground(Color.white);
//        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setPreferredSize(new Dimension(540, 500));
        panel.setMinimumSize(new Dimension(540, 500));
        panel.setMaximumSize(new Dimension(540, 500));
        return panel;
    }

    public JButton getLeftButton() {
        if (imageList == null || imageList.size() <= 1) {
            return getButton();
        }
        leftButton = getButton();
        leftButton.setText("<");
        leftButton.setBorder(new LineBorder(new Color(255,255,255)));

        leftButton.setPreferredSize(new Dimension(30,500));
        leftButton.setMinimumSize(new Dimension(30,500));
        leftButton.setMaximumSize(new Dimension(30,500));

        leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (index == 0)
                    return;
                index--;
                imageLabel.setIcon(getImageByIndex(index));
            }
        });
        return leftButton;
    }

    public JButton getIncButton() {
        if (imageList == null || imageList.size() <= 1) {
            return getButton();
        }
        rightButton = getButton();
        rightButton.setText(">");
        rightButton.setBorder(new LineBorder(new Color(255,255,255)));

        rightButton.setPreferredSize(new Dimension(25,500));
        rightButton.setMinimumSize(new Dimension(25,500));
        rightButton.setMaximumSize(new Dimension(25,500));

        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (index == imageList.size() - 1)
                    return;
                index++;
                imageLabel.setIcon(getImageByIndex(index));
            }
        });
        return rightButton;
    }

    public JButton getButton() {
        JButton button = new JButton();
        Font newButtonFont = new Font(button.getFont().getName(), button.getFont().getStyle(), 25);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(newButtonFont);
        return button;
    }

    public ImageIcon getImageByIndex(int index) {
        if (imageList != null && imageList.size() > 0) {
            return getUpdateImage(imageList.get(index), 500, 400);
        }
        return getUpdateImage("img/default_car_260.jpg", 500, 400);
    }

    public ImageIcon getUpdateImage(String url, int width, int height) {
        Image newImage = new ImageIcon(url).getImage().getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
        return new ImageIcon(newImage);
    }

}
