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

        System.out.println(db_urlport + "\n" + db_name + "\n" + db_uname + "\n" + db_password + "\n");
    }

    public void showcon() {
        System.out.println("hello    " + db_urlport + "\n" + db_name + "\n" + db_uname + "\n" + db_password + "\n");
    }

    public void connectDB() {
//        if (testInet("http://www.google.com")) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://" + db_urlport + "/" + db_name + "", db_uname, db_password);

        } catch (CommunicationsException e) {
            JOptionPane.showMessageDialog(null, "Connection Error");
        } catch (Exception e) {
            System.out.println(e);
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
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
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

            ResultSet rs = stmt.executeQuery("select * from wpos_users");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
            }
            //stmt.close();
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        }

    }

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
