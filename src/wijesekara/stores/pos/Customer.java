/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wijesekara.stores.pos;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Samintha
 */
public class Customer extends MySqlDBConnect{
    
    public boolean isCustomerAvailable(String customerID){
        boolean customerAvailable = false;
        try{
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM customers WHERE id='"+customerID+"'");
            while(rs.next()){
                customerAvailable = true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        }
        return customerAvailable;
    }
    
    public List getCustomerDetails(String customerID){
        List rowValues = new ArrayList();
        
        try {
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM customers WHERE id='"+customerID+"'");
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
    
    public void createCustomer(String name, String contact, String address, String email){
        try {
            stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO customers(name,contact,address,email) values('"+name+"','"+contact+"','"+address+"','"+email+"')");
            JOptionPane.showMessageDialog(null, "Successfully Inserted!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        }
    }
    
    public void updateCustomer(String cusID, String cusName, String cusCont, String cusAddr, String cusMail){
        if(isCustomerAvailable(cusID)){
            try {
            stmt = con.createStatement();
            stmt.executeUpdate("UPDATE customers SET name='"+cusName+"' ,contact='"+cusCont+"', address='"+cusAddr+"', email='"+cusMail+"' WHERE id='"+cusID+"'");
            JOptionPane.showMessageDialog(null, "Update Successful!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        }
        }else{
            JOptionPane.showMessageDialog(null, "Customer ID Error!");
        }
    }
    
    public void deleteCustomer(String cusID){
        if(isCustomerAvailable(cusID)){
            try {
            stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM customers WHERE id='"+cusID+"'");
            JOptionPane.showMessageDialog(null, "Delete Successful!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        }
        }else{
            JOptionPane.showMessageDialog(null, "Customer ID Error!");
        }
    }
}
