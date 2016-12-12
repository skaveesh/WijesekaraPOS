/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wijesekara.stores.pos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.TimerTask;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Samintha
 *
 * This file will check barcode.dat file in the User Document/WPOS folder and
 * get the barcode value and set the barcode.dat file to null. This run() method
 * will execute every 1000 milliseconds from MainClass.java
 *
 */
public class BarcodeReader extends TimerTask {

    JFileChooser fr = new JFileChooser();
    FileSystemView fw = fr.getFileSystemView();

    @Override
    public void run() {
        File file_in_library_ms_dir = new File(fw.getDefaultDirectory() + "\\WPOS\\barcode.dat");
        File library_ms_dir = new File(fw.getDefaultDirectory() + "\\WPOS");

        if (library_ms_dir.exists() && file_in_library_ms_dir.isFile()) {

            String barcode_line;

            try {
                FileReader fr = new FileReader(file_in_library_ms_dir);

                BufferedReader br = null;
                if (file_in_library_ms_dir.exists()) {
                    br = new BufferedReader(fr);
                }

                while ((barcode_line = br.readLine()) != null) {
                    MainClassUI.barcode_txt.setText(barcode_line);
                }
                //fetching details from the barcode file
                fr.close();
                br.close();

            } catch (Exception ex) {

            } finally {
                
                try {
                    PrintWriter writer = new PrintWriter(file_in_library_ms_dir);
                    writer.print("");
                } catch (Exception ex) {
                }
            }
        }
    }
}
