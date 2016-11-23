/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wijesekara.stores.pos;

import com.mysql.jdbc.CommunicationsException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Samintha
 */
public class MySqlDBConnect {

    Connection con;
    Statement stmt;

    static String db_urlport, db_name, db_uname, db_password;

    public void getDBFromFile(String cdb_urlport, String cdb_name, String cdb_uname, String cdb_password) {
        db_urlport = cdb_urlport;
        db_name = cdb_name;
        db_uname = cdb_uname;
        db_password = cdb_password;

        //System.out.println(db_urlport + "\n" + db_name + "\n" + db_uname + "\n" + db_password + "\n");
    }

//    public void showcon() {
//        System.out.println("hello    " + db_urlport + "\n" + db_name + "\n" + db_uname + "\n" + db_password + "\n");
//    }

    public void connectDB() {
//        if (testInet("http://www.google.com")) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://" + db_urlport + "/" + db_name + "", db_uname, db_password);

        } catch (CommunicationsException e) {
            JOptionPane.showMessageDialog(null, "Connection Error");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection Error");
            //e.printStackTrace();
        }
//        } else {
//            JOptionPane.showMessageDialog(null, "Server not found.");
//        }
    }

    public boolean loginUser(String username, String password) {

        String username_fromdb = "", password_fromdb = "";

        try {
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT username,password FROM wpos_users WHERE username='" + username + "'");
            while (rs.next()) {
                username_fromdb = rs.getString(1);
                password_fromdb = rs.getString(2);
            }
            //stmt.close();
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        }

        if (username.equals(username_fromdb) && password.equals(password_fromdb)) {
            return true;
        } else {
            return false;
        }
    }

    public void showUsers() {
        try {
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM wpos_users");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
            }
            //stmt.close();
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        }

    }
    
    public List BarcodeEntryAdd(String brcde){
        List rowValues = new ArrayList();
        
        try {
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT barcode,itemnum,descr,price,discount,available_items FROM products WHERE barcode='"+brcde+"'");
            while (rs.next()) {
                rowValues.add(rs.getString("barcode"));
                rowValues.add(rs.getString("itemnum"));
                rowValues.add(rs.getString("descr"));
                rowValues.add(rs.getFloat("price"));
                rowValues.add(rs.getInt("discount"));
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
    
    public int productQuantity(String brcde){
        int available = 0;
        try {
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT available_items FROM products WHERE barcode='"+brcde+"'");
            while (rs.next()) {
                available = rs.getInt("available_items");
            }
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        }
        return available;
    }
    
    public int productDiscount(String brcde){
        int discount = 0;
        try {
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT discount FROM products WHERE barcode='"+brcde+"'");
            while (rs.next()) {
                discount = rs.getInt("discount");
            }
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        }
        return discount;
    }
    
    public List LoadSalesmans(){
        List rowValues = new ArrayList();
        
        try {
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT display_name FROM salesman");
            while (rs.next()) {
                rowValues.add(rs.getString("display_name"));
            }
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        }
        
        return rowValues;
    }
    
    public int getNextOrderId(){
        int nextOrderID = 0;
        try {
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT order_id FROM transactions WHERE order_id = (SELECT MAX(order_id) FROM transactions)");
            while (rs.next()) {
                nextOrderID = rs.getInt("order_id") + 1;
            }
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        }
        
        return nextOrderID;
    }
    
    public void substractProductQuantity(String brcde, int quantityToSubstract){
        
        int finalQuantity = productQuantity(brcde) - quantityToSubstract;
        
        try {
            stmt = con.createStatement();
            stmt.executeUpdate("UPDATE products SET available_items="+finalQuantity+" WHERE barcode='"+brcde+"'");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        }
    }
    
    public int getSalesmanID(String salesmanName){
        int salesmanID = 0;
        try {
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT id FROM salesman WHERE display_name = '"+salesmanName+"' LIMIT 1");
            while (rs.next()) {
                salesmanID = rs.getInt("id");
            }
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        }
        
        return salesmanID;
    }
    
    public int getProductID(String brcde){
        int PID = 0;
        try {
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT id FROM products WHERE barcode = '"+brcde+"' LIMIT 1");
            while (rs.next()) {
                PID = rs.getInt("id");
            }
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        }
        
        return PID;
    }
    
    public void insertTransaction(int orderID, int productID, float price, int discount, int quantity, float total, int smid){
        //insert transactions into the database.
        try {
            stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO transactions(order_id,pid,price,discount,quantity,total,smid) VALUES('"+orderID+"','"+productID+"','"+price+"','"+discount+"','"+quantity+"','"+total+"','"+smid+"')");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        }
    }
    
    //close database connection
    public void closeConnection() {
        try {
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        } catch (Exception ex) {
        }
    }

    public boolean testInet(String site) {
        try {
            final URL url = new URL(site);
            final URLConnection conn = url.openConnection();
            conn.connect();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }

}
