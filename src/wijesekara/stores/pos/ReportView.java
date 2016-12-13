/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wijesekara.stores.pos;

import java.awt.Container;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author Samintha
 */
public class ReportView extends JFrame {

    public ReportView(String fileNameJasper, HashMap paraPassing) {
        super("Wijesekara Stores (Bill)");


        try {
            DefaultTableModel model = (DefaultTableModel) MainClassUI.productTable.getModel();
            JasperPrint print = JasperFillManager.fillReport(fileNameJasper, paraPassing, new JRTableModelDataSource(model));

            JRViewer viewer = new JRViewer(print);
            Container c = getContentPane();
            c.add(viewer);
        } catch (JRException jRException) {

        }
        setBounds(10, 10, 900, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public ReportView(String fileNameJasper, HashMap paraPassing, String savePath) {
        try {

            DefaultTableModel model = (DefaultTableModel) MainClassUI.productTable.getModel();
            JasperPrint print = JasperFillManager.fillReport(fileNameJasper, paraPassing, new JRTableModelDataSource(model));

            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(print));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));
            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            configuration.setMetadataAuthor("WijesekaraPOS");  //why not set some config as we like
            exporter.setConfiguration(configuration);
            exporter.exportReport();

        } catch (JRException jRException) {
        }
    }

}
