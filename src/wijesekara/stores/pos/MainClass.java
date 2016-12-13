/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wijesekara.stores.pos;

import java.io.File;
import java.io.PrintWriter;
import java.util.Timer;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Samintha
 */
public class MainClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {

        }

        LoginWindow login = new LoginWindow();
        login.pack();
        login.setLocationRelativeTo(null);
        login.setVisible(true);
    }
}
