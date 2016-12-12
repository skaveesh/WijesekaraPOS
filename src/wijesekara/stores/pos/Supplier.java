/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wijesekara.stores.pos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Samintha
 */
public class Supplier extends MySqlDBConnect{
    
    public List LoadSuppliers(){
        List rowValues = new ArrayList();
        
        try {
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT name FROM supplier");
            while (rs.next()) {
                rowValues.add(rs.getString("name"));
            }
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        }
        
        return rowValues;
    }
    
    public boolean isSupplierAvailable(String supplierID){
        boolean supplierAvailable = false;
        try{
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM supplier WHERE id='"+supplierID+"'");
            while(rs.next()){
                supplierAvailable = true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        }
        return supplierAvailable;
    }
    
    public List getSupplierDetails(String supplierID){
        List rowValues = new ArrayList();
        
        try {
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM supplier WHERE id='"+supplierID+"'");
            while (rs.next()) {
                rowValues.add(rs.getInt("id"));
                rowValues.add(rs.getString("name"));
                rowValues.add(rs.getString("contact"));
                rowValues.add(rs.getString("address"));
                rowValues.add(rs.getString("email"));
            }
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        }
        return rowValues;
    }
    
    public void createSupplier(String name, String contact, String address, String email){
        try {
            stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO supplier(name,contact,address,email) values('"+name+"','"+contact+"','"+address+"','"+email+"')");
            JOptionPane.showMessageDialog(null, "Successfully Inserted!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        }
    }
    
    public void updateSupplier(String supID, String supName, String supCont, String supAddr, String supMail){
        if(isSupplierAvailable(supID)){
            try {
            stmt = con.createStatement();
            stmt.executeUpdate("UPDATE supplier SET name='"+supName+"' ,contact='"+supCont+"', address='"+supAddr+"', email='"+supMail+"' WHERE id='"+supID+"'");
            JOptionPane.showMessageDialog(null, "Update Successful!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        }
        }else{
            JOptionPane.showMessageDialog(null, "Supplier ID Error!");
        }
    }
    
    public void deleteSupplier(String supID){
        if(isSupplierAvailable(supID)){
            try {
            stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM supplier WHERE id='"+supID+"'");
            JOptionPane.showMessageDialog(null, "Delete Successful!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error! \n"+"(Delete products that supplier supplies\n before deleting a supplier.)");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        }
        }else{
            JOptionPane.showMessageDialog(null, "Supplier ID Error!");
        }
    }
}
