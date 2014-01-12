package com.seguim;

import javax.swing.*;

public class Launcher {
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainWindow ex = new MainWindow();
                ex.setVisible(true);
            }
        });
    }
}