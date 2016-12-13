/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wijesekara.stores.pos;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Samintha
 */
public class MainClassUI extends javax.swing.JFrame {

    int productTableID = 0;
    public static String whereToRedirect = "";

    private class MainClassUIKeyManager implements KeyEventDispatcher {

        //this class controls all the key events
        //key press events goes here
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                //F1
                if (e.getKeyCode() == KeyEvent.VK_F1) {
                    // executre clearWholeWindow() on F1 key press
                    int reply = JOptionPane.showConfirmDialog(null,
                            "Are You Sure You Want to Clear All and Go for New Transaction?", "New", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        clearWholeWindow();
                    }
                    loadSalesmanToComboAndAllTables();
                }

                //F2
                if (e.getKeyCode() == KeyEvent.VK_F2) {
                    // save current transaction to database
                    int reply = JOptionPane.showConfirmDialog(null,
                            "Press Yes to Proceed with Save?", "Save", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        if (productTable.getRowCount() > 0) {
                            if (paid_txt.getText().trim().equals("") || Float.parseFloat(paid_txt.getText().trim()) <= Float.parseFloat(total_lbl.getText().trim())) {
                                JOptionPane.showMessageDialog(null, "Please enter appropriate paid amount");
                            } else {
                                save_transaction();
                                loadSalesmanToComboAndAllTables();
                                clearWholeWindow();
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Please insert items to the order list to proceed.");
                        }
                    }
                }

                //F3
                if (e.getKeyCode() == KeyEvent.VK_F3) {
                    // save current transaction to database and save order as pdf
                    int reply = JOptionPane.showConfirmDialog(null,
                            "Press Yes to Proceed with Save to a PDF?", "Save", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        if (productTable.getRowCount() > 0) {
                            if (paid_txt.getText().trim().equals("") || Float.parseFloat(paid_txt.getText().trim()) <= Float.parseFloat(total_lbl.getText().trim())) {
                                JOptionPane.showMessageDialog(null, "Please enter appropriate paid amount");
                            } else {

                                // generating bill for the order
                                HashMap parametersToReport = new HashMap();
                                parametersToReport.put("order_id", orderid_txt.getText());
                                parametersToReport.put("order_date", date_txt.getText());
                                parametersToReport.put("order_time", time_txt.getText());
                                parametersToReport.put("sal_name_jas", salesman_combo.getSelectedItem().toString());
                                parametersToReport.put("order_total", total_lbl.getText());
                                parametersToReport.put("order_paid", paid_txt.getText());
                                parametersToReport.put("order_balance", due_txt.getText());

                                JFileChooser chooser = new JFileChooser();
                                chooser.setFileFilter(new FileNameExtensionFilter("PDF File", "pdf"));

                                int result = chooser.showSaveDialog(null);
                                if (result == JFileChooser.APPROVE_OPTION) {
                                    String path = chooser.getSelectedFile().getAbsolutePath();
                                    String final_name = path + ".pdf";
                                    ReportView r = new ReportView("jasperreport\\report1.jasper", parametersToReport, final_name);

                                    save_transaction();
                                    loadSalesmanToComboAndAllTables();
                                    clearWholeWindow();
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Please insert items to the order list to proceed.");
                        }
                    }

                }

                //F5
                if (e.getKeyCode() == KeyEvent.VK_F5) {
                    // save current transaction to database and save order as pdf
                    int reply = JOptionPane.showConfirmDialog(null,
                            "Press Yes to Proceed with Save Order and Print?", "Save and Print", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        if (productTable.getRowCount() > 0) {
                            if (paid_txt.getText().trim().equals("") || Float.parseFloat(paid_txt.getText().trim()) <= Float.parseFloat(total_lbl.getText().trim())) {
                                JOptionPane.showMessageDialog(null, "Please enter appropriate paid amount");
                            } else {

                                // generating bill for the order
                                HashMap parametersToReport = new HashMap();
                                parametersToReport.put("order_id", orderid_txt.getText());
                                parametersToReport.put("order_date", date_txt.getText());
                                parametersToReport.put("order_time", time_txt.getText());
                                parametersToReport.put("sal_name_jas", salesman_combo.getSelectedItem().toString());
                                parametersToReport.put("order_total", total_lbl.getText());
                                parametersToReport.put("order_paid", paid_txt.getText());
                                parametersToReport.put("order_balance", due_txt.getText());

                                ReportView r = new ReportView("jasperreport\\report1.jasper", parametersToReport);
                                r.setVisible(true);

                                save_transaction();
                                loadSalesmanToComboAndAllTables();
                                clearWholeWindow();

                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Please insert items to the order list to proceed.");
                        }
                    }

                }

                //F9
                if (e.getKeyCode() == KeyEvent.VK_F9) {
                    //refresh all
                    loadSalesmanToComboAndAllTables();
                }
            }
            return false;
        }
    }

    /**
     * Creates new form MainClassUI
     */
    public MainClassUI() {
        initComponents();
        autoStart();
        loadSalesmanToComboAndAllTables();

        //key events invokes
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new MainClassUIKeyManager());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        newOdr_btn = new javax.swing.JButton();
        saveOdr_btn = new javax.swing.JButton();
        saveToPDF_btn = new javax.swing.JButton();
        saveAndPrint_btn = new javax.swing.JButton();
        refreshAll_btn = new javax.swing.JButton();
        user_btn = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        productTable = new javax.swing.JTable();
        editRowOrder_btn = new javax.swing.JButton();
        removeRowOrder_btn = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        transactionsDisplayTable = new javax.swing.JTable();
        trans_srch_txt = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        trans_srch_btn = new javax.swing.JButton();
        trans_reset_btn = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        productDisplayTable = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        prdct_srch_txt = new javax.swing.JTextField();
        prdct_srch_btn = new javax.swing.JButton();
        prdct_reset_btn = new javax.swing.JButton();
        prdct_srch_cmb = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        supplierDisplayTable = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();
        sup_srch_txt = new javax.swing.JTextField();
        sup_srch_btn = new javax.swing.JButton();
        sup_reset_btn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        customerDisplayTable = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        srch_cus_txt = new javax.swing.JTextField();
        srch_cus_btn = new javax.swing.JButton();
        srch_cusreset_btn = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        date_txt = new javax.swing.JTextField();
        time_txt = new javax.swing.JTextField();
        salesman_combo = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        orderid_txt = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        barcode_txt = new javax.swing.JTextField();
        quantity_txt = new javax.swing.JTextField();
        discount_txt = new javax.swing.JTextField();
        barcodeadd_btn = new javax.swing.JButton();
        barcodecancel_btn = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel8 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        total_lbl = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        total_txt = new javax.swing.JTextField();
        paid_txt = new javax.swing.JTextField();
        due_txt = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        addUpdateProductMenu = new javax.swing.JMenuItem();
        addUpdateSupplierMenu = new javax.swing.JMenuItem();
        addUpdateSalesmanMenu = new javax.swing.JMenuItem();
        addUpdateCustomerMenu = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        AboutMenu = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Wijesekara POS");
        setLocation(new java.awt.Point(0, 0));
        setName("mainFrame"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setPreferredSize(new java.awt.Dimension(1200, 650));

        newOdr_btn.setLabel("New (F1)");
        newOdr_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newOdr_btnActionPerformed(evt);
            }
        });

        saveOdr_btn.setLabel("Save (F2)");
        saveOdr_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveOdr_btnActionPerformed(evt);
            }
        });

        saveToPDF_btn.setText("Doc (F3)");
        saveToPDF_btn.setToolTipText("Save as Document");
        saveToPDF_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveToPDF_btnActionPerformed(evt);
            }
        });

        saveAndPrint_btn.setText("Print (F5)");
        saveAndPrint_btn.setToolTipText("Save and Print ");
        saveAndPrint_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAndPrint_btnActionPerformed(evt);
            }
        });

        refreshAll_btn.setText("Refr. (F9)");
        refreshAll_btn.setToolTipText("Refresh All the Tables");
        refreshAll_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshAll_btnActionPerformed(evt);
            }
        });

        user_btn.setText("User Mgt.");
        user_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                user_btnActionPerformed(evt);
            }
        });

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(1220, 477));

        productTable.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        productTable.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        productTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Barcode Entry", "Item Number", "Description", "Discount %", "Qty", "Each", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        productTable.setRowHeight(20);
        jScrollPane1.setViewportView(productTable);

        editRowOrder_btn.setText("Edit");
        editRowOrder_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editRowOrder_btnActionPerformed(evt);
            }
        });

        removeRowOrder_btn.setText("Remove");
        removeRowOrder_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeRowOrder_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 983, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(removeRowOrder_btn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                    .addComponent(editRowOrder_btn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(editRowOrder_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)
                        .addComponent(removeRowOrder_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(137, 137, 137))
        );

        jTabbedPane1.addTab("Order", jPanel3);

        transactionsDisplayTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Transaction ID", "Order ID", "Product Item Num", "Sold Price", "On Discount", "Quantity", "Total On Product", "Salesman", "Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(transactionsDisplayTable);

        jLabel10.setText("<html>Search<br />Order :</html>");

        trans_srch_btn.setText("Search");
        trans_srch_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trans_srch_btnActionPerformed(evt);
            }
        });

        trans_reset_btn.setText("Reset");
        trans_reset_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trans_reset_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 983, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(trans_srch_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(trans_srch_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(trans_reset_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(trans_srch_txt)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(trans_srch_btn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(trans_reset_btn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(124, 124, 124))
        );

        jTabbedPane1.addTab("Transactions", jPanel4);

        productDisplayTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID", "Barcode", "Item Number", "Description", "Price", "Discount", "Supplier ID", "Available Items"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(productDisplayTable);

        jLabel14.setText("Search Item By");

        prdct_srch_btn.setText("Search");
        prdct_srch_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prdct_srch_btnActionPerformed(evt);
            }
        });

        prdct_reset_btn.setText("Reset");
        prdct_reset_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prdct_reset_btnActionPerformed(evt);
            }
        });

        prdct_srch_cmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Barcode", "Item Number" }));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 983, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(prdct_srch_txt)
                    .addComponent(prdct_srch_btn, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                    .addComponent(prdct_reset_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(prdct_srch_cmb, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(prdct_srch_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(prdct_srch_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(prdct_srch_btn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(prdct_reset_btn)
                .addContainerGap(96, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Products", jPanel9);

        supplierDisplayTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Supplier ID", "Name", "Contact", "Address", "Email"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(supplierDisplayTable);

        jLabel16.setText("<html>Search Supplier<br />by Name</html>");

        sup_srch_btn.setText("Search");
        sup_srch_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sup_srch_btnActionPerformed(evt);
            }
        });

        sup_reset_btn.setText("Reset");
        sup_reset_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sup_reset_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 983, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sup_srch_txt)
                    .addComponent(sup_srch_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 24, Short.MAX_VALUE))
                    .addComponent(sup_reset_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sup_srch_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sup_srch_btn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sup_reset_btn)
                .addContainerGap(124, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Suppliers", jPanel5);

        customerDisplayTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Customer ID", "Customer Name", "Contact", "Address", "Email"
            }
        ));
        jScrollPane5.setViewportView(customerDisplayTable);

        jLabel13.setText("<html>Search Customer<br />by Name:</html>");

        srch_cus_btn.setText("Search");
        srch_cus_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                srch_cus_btnActionPerformed(evt);
            }
        });

        srch_cusreset_btn.setText("Reset");
        srch_cusreset_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                srch_cusreset_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 983, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(srch_cus_txt)
                    .addComponent(srch_cus_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 15, Short.MAX_VALUE))
                    .addComponent(srch_cusreset_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(srch_cus_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(srch_cus_btn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(srch_cusreset_btn)
                .addContainerGap(124, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Customer", jPanel2);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Information"));

        jLabel1.setText("Date :");

        jLabel2.setText("Time :");

        jLabel3.setText("Salesman :");

        date_txt.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        date_txt.setEnabled(false);

        time_txt.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        time_txt.setEnabled(false);

        salesman_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel17.setText("Order ID :");

        orderid_txt.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        orderid_txt.setEnabled(false);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel17))
                .addGap(30, 30, 30)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(date_txt)
                    .addComponent(time_txt)
                    .addComponent(salesman_combo, 0, 110, Short.MAX_VALUE)
                    .addComponent(orderid_txt))
                .addContainerGap(116, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(date_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(time_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(salesman_combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(orderid_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        date_txt.getAccessibleContext().setAccessibleName("date_text");
        time_txt.getAccessibleContext().setAccessibleName("time_text");

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Entry"));

        jLabel4.setText("Barcode Entry :");

        jLabel5.setText("Quantity :");

        jLabel6.setText("Discount for Each :");

        quantity_txt.setToolTipText("");
        quantity_txt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                quantity_txtKeyTyped(evt);
            }
        });

        discount_txt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                discount_txtKeyTyped(evt);
            }
        });

        barcodeadd_btn.setText("Add");
        barcodeadd_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barcodeadd_btnActionPerformed(evt);
            }
        });

        barcodecancel_btn.setText("Cancel");
        barcodecancel_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barcodecancel_btnActionPerformed(evt);
            }
        });

        jLabel12.setText("%");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(barcode_txt)
                            .addComponent(quantity_txt)
                            .addComponent(discount_txt, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel12))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(barcodeadd_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(barcodecancel_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(barcode_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(quantity_txt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(discount_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(barcodeadd_btn)
                    .addComponent(barcodecancel_btn))
                .addContainerGap())
        );

        barcode_txt.getAccessibleContext().setAccessibleName("sku_text");
        quantity_txt.getAccessibleContext().setAccessibleName("qty_text");
        discount_txt.getAccessibleContext().setAccessibleName("disc_text");

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Total"));

        jLabel7.setText("Total :");

        jLabel8.setText("Paid :");

        jLabel9.setText("Due :");

        total_lbl.setFont(new java.awt.Font("Tahoma", 0, 28)); // NOI18N
        total_lbl.setText("0.0");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 28)); // NOI18N
        jLabel11.setText("LKR");

        total_txt.setText("0.0");
        total_txt.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        total_txt.setEnabled(false);

        paid_txt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                paid_txtKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                paid_txtKeyTyped(evt);
            }
        });

        due_txt.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        due_txt.setEnabled(false);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(total_txt, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                    .addComponent(paid_txt)
                    .addComponent(due_txt))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addComponent(total_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel11)
                .addGap(110, 110, 110))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(total_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(paid_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(due_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(total_lbl)
                    .addComponent(jLabel11))
                .addContainerGap())
        );

        total_lbl.getAccessibleContext().setAccessibleName("total_label");
        total_txt.getAccessibleContext().setAccessibleName("total_text");
        paid_txt.getAccessibleContext().setAccessibleName("paid_text");
        due_txt.getAccessibleContext().setAccessibleName("due_text");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(newOdr_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(saveOdr_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(saveToPDF_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(saveAndPrint_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(refreshAll_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(user_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1124, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 8, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(refreshAll_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saveAndPrint_btn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saveToPDF_btn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saveOdr_btn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(newOdr_btn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(user_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 339, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("Order");

        jMenu1.setText("File");

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(exitMenuItem);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Add/Update");

        addUpdateProductMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        addUpdateProductMenu.setText("Add/Update Product");
        addUpdateProductMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addUpdateProductMenuActionPerformed(evt);
            }
        });
        jMenu2.add(addUpdateProductMenu);

        addUpdateSupplierMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        addUpdateSupplierMenu.setText("Add/Update Supplier");
        addUpdateSupplierMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addUpdateSupplierMenuActionPerformed(evt);
            }
        });
        jMenu2.add(addUpdateSupplierMenu);

        addUpdateSalesmanMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        addUpdateSalesmanMenu.setText("Add/Update Salesman");
        addUpdateSalesmanMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addUpdateSalesmanMenuActionPerformed(evt);
            }
        });
        jMenu2.add(addUpdateSalesmanMenu);

        addUpdateCustomerMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        addUpdateCustomerMenu.setText("Add/Update Customer");
        addUpdateCustomerMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addUpdateCustomerMenuActionPerformed(evt);
            }
        });
        jMenu2.add(addUpdateCustomerMenu);

        jMenuBar1.add(jMenu2);

        jMenu6.setText("Help");

        AboutMenu.setText("About");
        AboutMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AboutMenuActionPerformed(evt);
            }
        });
        jMenu6.add(AboutMenu);

        jMenuBar1.add(jMenu6);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1144, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, 629, 629, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void autoStart() {
        //set table to only select one record at a time
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //right align the total label
        total_lbl.setHorizontalAlignment(SwingConstants.RIGHT);

        //set text on date_txt and time_txt
        t.start();

    }

    public static void loadSalesmanToComboAndAllTables() {
        //load salesmans
        MySqlDBConnect db = new MySqlDBConnect();
        db.connectDB();

        List<String> ls = db.LoadSalesmans();
        salesman_combo.setModel(new DefaultComboBoxModel(ls.toArray()));

        db.refreshAllTables();

        db.closeConnection();
    }

    DateFormat timeF = new SimpleDateFormat("HH:mm:ss");
    DateFormat dateF = new SimpleDateFormat("dd/MM/yyyy");
    Timer t = new Timer(500, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Date date = new Date();
            time_txt.setText(timeF.format(date));
            date_txt.setText(dateF.format(date));
            repaint();
        }
    });

    public static void countTotalAndDisplay() {
        float total = 0;
        for (int i = 0; i < productTable.getRowCount(); i++) {
            total = total + (Float) productTable.getModel().getValueAt(i, 7);
        }
        total_lbl.setText(String.format("%.02f", total) + "");
        total_txt.setText(String.format("%.02f", total) + "");
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int reply = JOptionPane.showConfirmDialog(null,
                "Are You Really Want to Quit ?", "Quit", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_formWindowClosing

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        int reply = JOptionPane.showConfirmDialog(null,
                "Are You Really Want to Quit ?", "Quit", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void barcodeadd_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barcodeadd_btnActionPerformed

        // Enter bacode and press Add button    
        MySqlDBConnect db = new MySqlDBConnect();
        db.connectDB();

        //if user didn't enter quantity it will automatically be one
        //if user didn't enter discount it will automatically be discount value
        //in the database.
        int auto_quantity = 1, auto_discount = db.productDiscount(barcode_txt.getText());

        if (!discount_txt.getText().equals("") && Integer.parseInt(discount_txt.getText()) <= 50) {
            auto_discount = Integer.parseInt(discount_txt.getText());
        } else if (!discount_txt.getText().equals("") && Integer.parseInt(discount_txt.getText()) > 50) {
            JOptionPane.showMessageDialog(null, "Set the discount below 50%. Default value will be set for now!");
        }

        if (quantity_txt.getText().equals("")) {
            auto_quantity = 1;
        } else {
            auto_quantity = Integer.parseInt(quantity_txt.getText());
        }

        List list = db.BarcodeEntryAdd(barcode_txt.getText());
        if (!list.isEmpty()) //list.forEach(System.out::println);
        {
            //check if items are available and then add to the list
            if (db.productQuantity(barcode_txt.getText()) > 0 && db.productQuantity(barcode_txt.getText()) >= auto_quantity) {
                boolean previousRecordFound = false;
                //insert record for the first time
                if (productTable.getRowCount() == 0) {
                    DefaultTableModel model = (DefaultTableModel) productTable.getModel();
                    model.addRow(new Object[]{" " + (++productTableID), list.get(0), list.get(1), list.get(2), auto_discount, auto_quantity, (Float) list.get(3), (((Float) list.get(3) * auto_quantity) - (((Float) list.get(3) * auto_quantity * auto_discount) / 100))});
                } else if (productTable.getRowCount() > 0) {
                    //if the same barcode entered before, this will look through the table and edit previously endtered record.
                    for (int i = 0; i < productTable.getRowCount(); i++) {
                        if (productTable.getModel().getValueAt(i, 1).equals(barcode_txt.getText())) {
                            // Edit row if the entry already exists
                            EditOrderWindow edito = new EditOrderWindow(barcode_txt.getText(), Integer.parseInt(productTable.getModel().getValueAt(i, 5).toString()), Integer.parseInt(productTable.getModel().getValueAt(i, 4).toString()), Float.parseFloat(productTable.getModel().getValueAt(i, 6).toString()));
                            edito.pack();
                            edito.setLocationRelativeTo(null);
                            edito.setVisible(true);
                            previousRecordFound = true;
                        }
                    }
                    if (!previousRecordFound) {
                        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
                        model.addRow(new Object[]{" " + (++productTableID), list.get(0), list.get(1), list.get(2), auto_discount, auto_quantity, (Float) list.get(3), (((Float) list.get(3) * auto_quantity) - (((Float) list.get(3) * auto_quantity * auto_discount) / 100))});
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Products are not available for entered quantity!");
            }

            //System.out.println("barcode " + list.get(0) + " itemnum " + list.get(1) + " decription " + list.get(2) + " price " + (Float) list.get(3) + " discount " + (Integer) list.get(4) + " available_items " + (Integer) list.get(5));
        } else {
            JOptionPane.showMessageDialog(null, "Barcode entry error!");
        }

        //show total on text fields
        countTotalAndDisplay();

        //clear text fields
        barcode_txt.setText("");
        quantity_txt.setText("");
        discount_txt.setText("");
        paid_txt.setText("");
        due_txt.setText("");

        db.closeConnection();
    }//GEN-LAST:event_barcodeadd_btnActionPerformed

    private void editRowOrder_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editRowOrder_btnActionPerformed
        // Edit row of product orders
        if (productTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Please select a record to edit.");
        } else {
            EditOrderWindow edito = new EditOrderWindow(productTable.getValueAt(productTable.getSelectedRow(), 1).toString(), Integer.parseInt(productTable.getModel().getValueAt(productTable.getSelectedRow(), 5).toString()), Integer.parseInt(productTable.getModel().getValueAt(productTable.getSelectedRow(), 4).toString()), Float.parseFloat(productTable.getModel().getValueAt(productTable.getSelectedRow(), 6).toString()));
            edito.pack();
            edito.setLocationRelativeTo(null);
            edito.setVisible(true);
        }
    }//GEN-LAST:event_editRowOrder_btnActionPerformed

    private void quantity_txtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_quantity_txtKeyTyped
        char c = evt.getKeyChar();
        if (!((c >= '0') && (c <= '9')
                || (c == KeyEvent.VK_BACK_SPACE)
                || (c == KeyEvent.VK_DELETE))) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_quantity_txtKeyTyped

    private void discount_txtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_discount_txtKeyTyped
        char c = evt.getKeyChar();
        if (!((c >= '0') && (c <= '9')
                || (c == KeyEvent.VK_BACK_SPACE)
                || (c == KeyEvent.VK_DELETE))) {
            
                getToolkit().beep();
                evt.consume();
        }
    }//GEN-LAST:event_discount_txtKeyTyped

    private void removeRowOrder_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeRowOrder_btnActionPerformed
        // remove selected row
        if (productTable.getSelectedRow() != -1) {
            // remove selected row from the model
            DefaultTableModel model = (DefaultTableModel) productTable.getModel();
            model.removeRow(productTable.getSelectedRow());
            paid_txt.setText("");
            due_txt.setText("");
            countTotalAndDisplay();
        } else {
            JOptionPane.showMessageDialog(null, "Please select a record to delete.");
        }
    }//GEN-LAST:event_removeRowOrder_btnActionPerformed

    private void paid_txtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paid_txtKeyTyped
        if (paid_txt.getText().contains(".")) {
            char c = evt.getKeyChar();
            if (!((c >= '0') && (c <= '9')
                    || (c == KeyEvent.VK_BACK_SPACE)
                    || (c == KeyEvent.VK_DELETE))) {
                getToolkit().beep();
                evt.consume();
            }
        } else if (paid_txt.getText().trim().equals("")) {
            char c = evt.getKeyChar();
            if (!((c >= '0') && (c <= '9')
                    || (c == KeyEvent.VK_BACK_SPACE)
                    || (c == KeyEvent.VK_DELETE))) {
                getToolkit().beep();
                evt.consume();
            }
        } else {
            char c = evt.getKeyChar();
            if (!((c >= '0') && (c <= '9')
                    || (c == KeyEvent.VK_BACK_SPACE)
                    || (c == KeyEvent.VK_DELETE)
                    || (c == '.'))) {
                getToolkit().beep();
                evt.consume();
            }
        }
    }//GEN-LAST:event_paid_txtKeyTyped

    private void paid_txtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paid_txtKeyReleased
        if ((!paid_txt.getText().trim().equals("") && (Double.parseDouble(total_txt.getText()) <= Double.parseDouble(paid_txt.getText())))) {
            double due_amount = Double.parseDouble(paid_txt.getText()) - Double.parseDouble(total_txt.getText());
            due_txt.setText("" + String.format("%.02f", due_amount));
        } else {
            due_txt.setText("");
        }
    }//GEN-LAST:event_paid_txtKeyReleased

    private void saveOdr_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveOdr_btnActionPerformed
        // save current transaction to database
        int reply = JOptionPane.showConfirmDialog(null,
                "Press Yes to Proceed with Save?", "Save", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            if (productTable.getRowCount() > 0) {
                if (paid_txt.getText().trim().equals("") || Float.parseFloat(paid_txt.getText().trim()) <= Float.parseFloat(total_lbl.getText().trim())) {
                    JOptionPane.showMessageDialog(null, "Please enter appropriate paid amount");
                } else {
                    save_transaction();
                    loadSalesmanToComboAndAllTables();
                    clearWholeWindow();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please insert items to the order list to proceed.");
            }
        }
    }//GEN-LAST:event_saveOdr_btnActionPerformed

    private void barcodecancel_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barcodecancel_btnActionPerformed

        barcode_txt.setText("");
        quantity_txt.setText("");
        discount_txt.setText("");
    }//GEN-LAST:event_barcodecancel_btnActionPerformed

    private void newOdr_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newOdr_btnActionPerformed
        int reply = JOptionPane.showConfirmDialog(null,
                "Are You Sure You Want to Clear All and Go for New Transaction?", "New", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            clearWholeWindow();
        }
        loadSalesmanToComboAndAllTables();
    }//GEN-LAST:event_newOdr_btnActionPerformed

    private void addUpdateCustomerMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addUpdateCustomerMenuActionPerformed
        AddUpdateCustomerUI auc = new AddUpdateCustomerUI();
        auc.pack();
        auc.setLocationRelativeTo(null);
        auc.setVisible(true);
    }//GEN-LAST:event_addUpdateCustomerMenuActionPerformed

    private void addUpdateProductMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addUpdateProductMenuActionPerformed
        AdminAuthenticateUI admin = new AdminAuthenticateUI();
        admin.pack();
        admin.setLocationRelativeTo(null);
        admin.setVisible(true);

        //this will redirect to product add update UI from admin password window
        whereToRedirect = "ProductUI";
    }//GEN-LAST:event_addUpdateProductMenuActionPerformed

    private void addUpdateSupplierMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addUpdateSupplierMenuActionPerformed
        AdminAuthenticateUI admin = new AdminAuthenticateUI();
        admin.pack();
        admin.setLocationRelativeTo(null);
        admin.setVisible(true);

        //this will redirect to product add update UI from admin password window
        whereToRedirect = "SupplierUI";

    }//GEN-LAST:event_addUpdateSupplierMenuActionPerformed

    private void addUpdateSalesmanMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addUpdateSalesmanMenuActionPerformed
        AdminAuthenticateUI admin = new AdminAuthenticateUI();
        admin.pack();
        admin.setLocationRelativeTo(null);
        admin.setVisible(true);

        whereToRedirect = "SalesmanUI";

    }//GEN-LAST:event_addUpdateSalesmanMenuActionPerformed

    private void saveToPDF_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveToPDF_btnActionPerformed
        // save current transaction to database and save order as pdf
        int reply = JOptionPane.showConfirmDialog(null,
                "Press Yes to Proceed with Save to a PDF?", "Save", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            if (productTable.getRowCount() > 0) {
                if (paid_txt.getText().trim().equals("") || Float.parseFloat(paid_txt.getText().trim()) <= Float.parseFloat(total_lbl.getText().trim())) {
                    JOptionPane.showMessageDialog(null, "Please enter appropriate paid amount");
                } else {

                    // generating bill for the order
                    HashMap parametersToReport = new HashMap();
                    parametersToReport.put("order_id", orderid_txt.getText());
                    parametersToReport.put("order_date", date_txt.getText());
                    parametersToReport.put("order_time", time_txt.getText());
                    parametersToReport.put("sal_name_jas", salesman_combo.getSelectedItem().toString());
                    parametersToReport.put("order_total", total_lbl.getText());
                    parametersToReport.put("order_paid", paid_txt.getText());
                    parametersToReport.put("order_balance", due_txt.getText());

                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileFilter(new FileNameExtensionFilter("PDF File", "pdf"));

                    int result = chooser.showSaveDialog(this);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        String path = chooser.getSelectedFile().getAbsolutePath();
                        String final_name = path + ".pdf";
                        ReportView r = new ReportView("jasperreport\\report1.jasper", parametersToReport, final_name);

                        save_transaction();
                        loadSalesmanToComboAndAllTables();
                        clearWholeWindow();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please insert items to the order list to proceed.");
            }
        }

    }//GEN-LAST:event_saveToPDF_btnActionPerformed

    private void refreshAll_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshAll_btnActionPerformed
        //refresh all
        loadSalesmanToComboAndAllTables();
    }//GEN-LAST:event_refreshAll_btnActionPerformed

    private void saveAndPrint_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAndPrint_btnActionPerformed
        // save current transaction to database and save order as pdf
        int reply = JOptionPane.showConfirmDialog(null,
                "Press Yes to Proceed with Save Order and Print?", "Save and Print", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            if (productTable.getRowCount() > 0) {
                if (paid_txt.getText().trim().equals("") || Float.parseFloat(paid_txt.getText().trim()) <= Float.parseFloat(total_lbl.getText().trim())) {
                    JOptionPane.showMessageDialog(null, "Please enter appropriate paid amount");
                } else {

                    // generating bill for the order
                    HashMap parametersToReport = new HashMap();
                    parametersToReport.put("order_id", orderid_txt.getText());
                    parametersToReport.put("order_date", date_txt.getText());
                    parametersToReport.put("order_time", time_txt.getText());
                    parametersToReport.put("sal_name_jas", salesman_combo.getSelectedItem().toString());
                    parametersToReport.put("order_total", total_lbl.getText());
                    parametersToReport.put("order_paid", paid_txt.getText());
                    parametersToReport.put("order_balance", due_txt.getText());

                    ReportView r = new ReportView("jasperreport\\report1.jasper", parametersToReport);
                    r.setVisible(true);

                    save_transaction();
                    loadSalesmanToComboAndAllTables();
                    clearWholeWindow();

                }
            } else {
                JOptionPane.showMessageDialog(null, "Please insert items to the order list to proceed.");
            }
        }

    }//GEN-LAST:event_saveAndPrint_btnActionPerformed

    private void trans_srch_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trans_srch_btnActionPerformed
        MySqlDBConnect db = new MySqlDBConnect();
        db.connectDB();
        db.searchTransactionsTable(trans_srch_txt.getText());
        db.closeConnection();
    }//GEN-LAST:event_trans_srch_btnActionPerformed


    private void trans_reset_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trans_reset_btnActionPerformed
        loadSalesmanToComboAndAllTables();
        trans_srch_txt.setText("");
    }//GEN-LAST:event_trans_reset_btnActionPerformed

    private void prdct_srch_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prdct_srch_btnActionPerformed
        MySqlDBConnect db = new MySqlDBConnect();
        db.connectDB();
        if (prdct_srch_cmb.getSelectedItem().toString().equals("Barcode")) {
            db.searchProductTable(prdct_srch_txt.getText(), "barcode");
        } else {
            db.searchProductTable(prdct_srch_txt.getText(), "itemnum");
        }
        db.closeConnection();
    }//GEN-LAST:event_prdct_srch_btnActionPerformed

    private void prdct_reset_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prdct_reset_btnActionPerformed
        loadSalesmanToComboAndAllTables();
        prdct_srch_txt.setText("");
    }//GEN-LAST:event_prdct_reset_btnActionPerformed

    private void sup_srch_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sup_srch_btnActionPerformed
        MySqlDBConnect db = new MySqlDBConnect();
        db.connectDB();
        db.searchSupplierTable(sup_srch_txt.getText());
        db.closeConnection();
    }//GEN-LAST:event_sup_srch_btnActionPerformed

    private void sup_reset_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sup_reset_btnActionPerformed
        loadSalesmanToComboAndAllTables();
        sup_srch_txt.setText("");
    }//GEN-LAST:event_sup_reset_btnActionPerformed

    private void srch_cus_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_srch_cus_btnActionPerformed
        MySqlDBConnect db = new MySqlDBConnect();
        db.connectDB();
        db.searchCustomerTable(srch_cus_txt.getText());
        db.closeConnection();
    }//GEN-LAST:event_srch_cus_btnActionPerformed

    private void srch_cusreset_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_srch_cusreset_btnActionPerformed
        loadSalesmanToComboAndAllTables();
        srch_cus_txt.setText("");
    }//GEN-LAST:event_srch_cusreset_btnActionPerformed

    private void user_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_user_btnActionPerformed
        UserUI uui = new UserUI();
        uui.pack();
        uui.setLocationRelativeTo(null);
        uui.setVisible(true);
    }//GEN-LAST:event_user_btnActionPerformed

    private void AboutMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AboutMenuActionPerformed
        JOptionPane.showMessageDialog(null, "WijesekaraPOS\n2016\nSamintha Kaveesh\nChulan Kotudurage\nUmayanga Wijesinghe","About WijesekaraPOS", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_AboutMenuActionPerformed

    public void save_transaction() {
        try {
            MySqlDBConnect db = new MySqlDBConnect();
            db.connectDB();

            int orderID = db.getNextOrderId();
            int smid = db.getSalesmanID((String) salesman_combo.getSelectedItem());

            for (int i = 0; i < productTable.getRowCount(); i++) {

                int productID = db.getProductID((String) productTable.getModel().getValueAt(i, 1));
                int discount = (int) productTable.getModel().getValueAt(i, 4);
                int quantity = (int) productTable.getModel().getValueAt(i, 5);
                float price = (float) productTable.getModel().getValueAt(i, 6);
                float total = (float) productTable.getModel().getValueAt(i, 7);

                db.substractProductQuantity((String) productTable.getModel().getValueAt(i, 1), (int) productTable.getModel().getValueAt(i, 5));
                db.insertTransaction(orderID, productID, price, discount, quantity, total, smid);
            }

            db.closeConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Fatal Error!");
        }
    }

    public static void editRecord(String barcode, int quantity, int discount, float price) {
        //this method invoke from the EditOrderWindow class
        MySqlDBConnect db = new MySqlDBConnect();
        db.connectDB();

        //check if entered quantity available in the database
        if (db.productQuantity(barcode) >= quantity && discount <= 50) {
            for (int i = 0; i < productTable.getRowCount(); i++) {
                if (productTable.getModel().getValueAt(i, 1).equals(barcode)) {
                    // Search for the row and edit it
                    DefaultTableModel model = (DefaultTableModel) productTable.getModel();
                    model.setValueAt(quantity, i, 5);
                    model.setValueAt(discount, i, 4);
                    model.setValueAt(price, i, 6);
                    model.setValueAt(((price * quantity) - ((price * quantity * discount) / 100)), i, 7);
                }
            }
        } else if (discount > 50) {
            JOptionPane.showMessageDialog(null, "Set discount lower than 50%!");
        } else {
            JOptionPane.showMessageDialog(null, "Products are not available for entered quantity!");
        }
        paid_txt.setText("");
        due_txt.setText("");
        countTotalAndDisplay();
        db.closeConnection();
    }

    public void clearWholeWindow() {

        DefaultTableModel tMOdel = (DefaultTableModel) productTable.getModel();
        tMOdel.setRowCount(0);

        countTotalAndDisplay();

        barcode_txt.setText("");
        quantity_txt.setText("");
        discount_txt.setText("");
        paid_txt.setText("");
        due_txt.setText("");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainClassUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem AboutMenu;
    private javax.swing.JMenuItem addUpdateCustomerMenu;
    private javax.swing.JMenuItem addUpdateProductMenu;
    private javax.swing.JMenuItem addUpdateSalesmanMenu;
    private javax.swing.JMenuItem addUpdateSupplierMenu;
    public static javax.swing.JTextField barcode_txt;
    private javax.swing.JButton barcodeadd_btn;
    private javax.swing.JButton barcodecancel_btn;
    private javax.swing.ButtonGroup buttonGroup1;
    public static javax.swing.JTable customerDisplayTable;
    private javax.swing.JTextField date_txt;
    private javax.swing.JTextField discount_txt;
    private static javax.swing.JTextField due_txt;
    private javax.swing.JButton editRowOrder_btn;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton newOdr_btn;
    public static javax.swing.JTextField orderid_txt;
    private static javax.swing.JTextField paid_txt;
    private javax.swing.JButton prdct_reset_btn;
    private javax.swing.JButton prdct_srch_btn;
    private javax.swing.JComboBox<String> prdct_srch_cmb;
    private javax.swing.JTextField prdct_srch_txt;
    public static javax.swing.JTable productDisplayTable;
    public static javax.swing.JTable productTable;
    private javax.swing.JTextField quantity_txt;
    private javax.swing.JButton refreshAll_btn;
    private javax.swing.JButton removeRowOrder_btn;
    private static javax.swing.JComboBox<String> salesman_combo;
    private javax.swing.JButton saveAndPrint_btn;
    private javax.swing.JButton saveOdr_btn;
    private javax.swing.JButton saveToPDF_btn;
    private javax.swing.JButton srch_cus_btn;
    private javax.swing.JTextField srch_cus_txt;
    private javax.swing.JButton srch_cusreset_btn;
    private javax.swing.JButton sup_reset_btn;
    private javax.swing.JButton sup_srch_btn;
    private javax.swing.JTextField sup_srch_txt;
    public static javax.swing.JTable supplierDisplayTable;
    private javax.swing.JTextField time_txt;
    private static javax.swing.JLabel total_lbl;
    private static javax.swing.JTextField total_txt;
    private javax.swing.JButton trans_reset_btn;
    private javax.swing.JButton trans_srch_btn;
    private javax.swing.JTextField trans_srch_txt;
    public static javax.swing.JTable transactionsDisplayTable;
    private javax.swing.JButton user_btn;
    // End of variables declaration//GEN-END:variables

}
