/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wijesekara.stores.pos;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Samintha
 */
public class Product extends MySqlDBConnect{
    public boolean isBarcodeTaken(String brcde){
        boolean barcodeTaken = false;
        try{
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM products WHERE barcode='"+brcde+"'");
            while(rs.next()){
                barcodeTaken = true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        }
        return barcodeTaken;
    }
    
    public boolean isItemNumberTaken(String itemnum){
        boolean itemNumberTaken = false;
        try{
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM products WHERE barcode='"+itemnum+"'");
            while(rs.next()){
                itemNumberTaken = true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        }
        return itemNumberTaken;
    }
}
