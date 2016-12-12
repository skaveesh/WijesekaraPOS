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
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Samintha
 */
public class Salesman extends MySqlDBConnect {

    public boolean isSalesmanAvailable(String salesmanID) {
        boolean salesmanAvailable = false;
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM salesman WHERE id='" + salesmanID + "'");
            while (rs.next()) {
                salesmanAvailable = true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
            //ex.printStackTrace();
        }
        return salesmanAvailable;
    }

    public List getSalesmanDetails(String salesmanID) {
        List rowValues = new ArrayList();

        try {
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM salesman WHERE id='" + salesmanID + "'");
            while (rs.next()) {
                rowValues.add(rs.getInt("id"));
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

    public void createSalesman(String name) {
        try {
            stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO salesman(display_name) values('" + name + "')");
            JOptionPane.showMessageDialog(null, "Successfully Inserted!");

            //re-load salesmans
            MainClassUI.loadSalesmanToComboAndAllTables();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database error!");
        }
    }

    public void updateSalesman(String salID, String salName) {
        if (isSalesmanAvailable(salID)) {
            try {
                stmt = con.createStatement();
                stmt.executeUpdate("UPDATE salesman SET display_name='" + salName + "' WHERE id='"+salID+"'");
                JOptionPane.showMessageDialog(null, "Update Successful!");

                //re-load salesmans
                MainClassUI.loadSalesmanToComboAndAllTables();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Database error!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Database error!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Salesman ID Error!");
        }
    }

    public void deleteSalesman(String salID) {
        if (isSalesmanAvailable(salID)) {
            try {
                stmt = con.createStatement();
                stmt.executeUpdate("DELETE FROM salesman WHERE id='" + salID + "'");
                JOptionPane.showMessageDialog(null, "Delete Successful!");

                //re-load salesmans
                MainClassUI.loadSalesmanToComboAndAllTables();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Database error!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Database error!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Salesman ID Error!");
        }
    }
}
