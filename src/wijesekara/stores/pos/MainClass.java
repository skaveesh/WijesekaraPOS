/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wijesekara.stores.pos;

import java.util.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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
        
        //this method will execute BarcodeReader.class clss file
        //every 1000 millisecond
        Timer timer = new Timer();
        timer.schedule(new BarcodeReader(), 0, 1000);
    }
}
