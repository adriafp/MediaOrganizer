package com.seguim;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * User: adria
 * Date: 12/01/14
 * Time: 14:42
 */
public class MainWindow extends JFrame {

    public MainWindow() {
        initUI();
    }

    private void initUI() {

        JPanel panel = new JPanel();
        getContentPane().add(panel);

        panel.setLayout(null);

        JButton btn = new JButton("Organize");
        btn.setBounds(100, 60, 100, 30);
        btn.setToolTipText("Click to start");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("MainWindow.actionPerformed");
                try {
                    MediaOrganizer mediaOrganizer = new MediaOrganizer();
                    mediaOrganizer.organize();
                    JOptionPane.showMessageDialog(null, mediaOrganizer.getResult());
                } catch (Exception e) {
                    StringWriter errors = new StringWriter();
                    e.printStackTrace(new PrintWriter(errors));
                    JOptionPane.showMessageDialog(null, errors.toString());
                }
            }
        });
        panel.add(btn);

        setTitle("Media Organizer");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

}
