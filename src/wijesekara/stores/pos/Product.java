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
            ResultSet rs = stmt.executeQuery("SELECT * FROM products WHERE itemnum='"+itemnum+"'");
            while(rs.next()){
                itemNumberTaken = true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error1!");
            //ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error2!");
            //ex.printStackTrace();
        }
        return itemNumberTaken;
    }
    
    public boolean isProductAvailable(int productID){
        boolean productAvailable = false;
        try{
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM products WHERE id="+productID+"");
            while(rs.next()){
                productAvailable = true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            ex.printStackTrace();
        }
        return productAvailable;
    }
    
    public List getProductDetails(String productID){
        List rowValues = new ArrayList();
        
        try {
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM products WHERE id='"+productID+"'");
            while (rs.next()) {
                rowValues.add(rs.getString("barcode"));
                rowValues.add(rs.getString("itemnum"));
                rowValues.add(rs.getString("descr"));
                rowValues.add(rs.getString("price"));
                rowValues.add(rs.getString("discount"));
                //get supplier name by getSupplierName method
                rowValues.add(rs.getInt("available_items"));
            }
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        }
        return rowValues;
    }
    
    public void insertProduct(String barcode, String itemNum, String desc, Float price, int discount, String supplierName, int availableItems){
        try {
            stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO products(barcode,itemnum,descr,price,discount,supplier_id,available_items) "
                    + "values('"+barcode+"','"+itemNum+"','"+desc+"','"+price+"','"+discount+"','"+getSupplierID(supplierName)+"','"+availableItems+"')");
            JOptionPane.showMessageDialog(null, "Successfully Inserted!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        }
    }
    
    public void updateProduct(int prdId, String prdBarcode, String itemNum, String description, Float price, int discount,String supplierName, int availableItems){
        if(isProductAvailable(prdId)){
            try {
            stmt = con.createStatement();
            stmt.executeUpdate("UPDATE products SET barcode='"+prdBarcode+"' ,itemnum='"+itemNum+"', descr='"+description+"', price='"+price+"', discount='"+discount+"', supplier_id='"+getSupplierID(supplierName)+"', available_items='"+availableItems+"' WHERE id='"+prdId+"'");
            JOptionPane.showMessageDialog(null, "Update Successful!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        }
        }else{
            JOptionPane.showMessageDialog(null, "Product ID Error!");
        }
    }
    
    public void deleteProduct(int productID){
        if(isProductAvailable(productID)){
            try {
            stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM products WHERE id='"+productID+"'");
            JOptionPane.showMessageDialog(null, "Delete Successful!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        }
        }else{
            JOptionPane.showMessageDialog(null, "Product ID Error!");
        }
    }
    
    public String getSupplierName(String productID){
        int supplierID_DB = 0;
        String supplierName_DB = "";
        try{
            stmt = con.createStatement();
            ResultSet rs1 = stmt.executeQuery("SELECT supplier_id FROM products WHERE id='"+productID+"' LIMIT 1");
            while(rs1.next()){
                supplierID_DB = rs1.getInt(1);
            }
            
            ResultSet rs2 = stmt.executeQuery("SELECT name FROM supplier WHERE id='"+supplierID_DB+"' LIMIT 1");
            while(rs2.next()){
                supplierName_DB = rs2.getString(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        }
        return supplierName_DB;
    }
    
    public int getSupplierID(String supplierName){
        int supplierID_DB = 0;
        try{
            stmt = con.createStatement();
            ResultSet rs1 = stmt.executeQuery("SELECT id FROM supplier WHERE name='"+supplierName+"' LIMIT 1");
            while(rs1.next()){
                supplierID_DB = rs1.getInt(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        }
        return supplierID_DB;
    }
}
