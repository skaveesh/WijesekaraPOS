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
}
