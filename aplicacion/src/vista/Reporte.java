package vista;


import java.io.IOException;

import net.sf.jasperreports.engine.JRException;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.view.JasperViewer;


import net.sf.jasperreports.engine.JasperFillManager;

import net.sf.jasperreports.engine.JasperPrint;

import net.sf.jasperreports.engine.data.JRCsvDataSource;

import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;


public abstract class Reporte {


    public static void displayReport(String reportFile, String [] columnNames, String csv) {

        try {

            
            try {
               
                
                    JRCsvDataSource ds = new JRCsvDataSource(JRLoader.getLocationInputStream(csv));
                    ds.setRecordDelimiter("\r\n");
                ds.setFieldDelimiter('|');
                    ds.setColumnNames(columnNames);
                    
                
                JasperPrint jasperPrint =
                    JasperFillManager.fillReport(reportFile, null,
                                                 ds);
                JasperViewer.viewReport(jasperPrint, false);
            } catch (JRException e) {
                e.printStackTrace();
            }


        } catch (Exception e) {

            e.printStackTrace();

        }

    }


    public static void displayReportEXCEL(String reportFile, String [] columnNames, String csv) {

        try {
           
            
                JRCsvDataSource ds = new JRCsvDataSource(JRLoader.getLocationInputStream(csv));
                ds.setRecordDelimiter("\r\n");
            ds.setFieldDelimiter('|');
                ds.setColumnNames(columnNames);
            JasperPrint jasperPrint =
                JasperFillManager.fillReport(reportFile, null,
                                             ds);


            JRXlsExporter exporter = new JRXlsExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT,
                                  jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
                                  "reportes/reporte_excel.xls");
            exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
                                  Boolean.TRUE);
            exporter.exportReport();
            try {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " +
                                          "reportes/reporte_excel.xls");

            } catch (IOException ee) {
                // TODO Auto-generated catch block
                ee.printStackTrace();
            } 
        } catch (Exception e) {

            System.out.println("Error occurred: " + e.getMessage());

        }

    }

    public static void main(String[] a) {
        String[] columnNames = new String[]{"CODIGO", "DESCRIPCION"};
        Reporte.displayReport("reportes/dtc.jasper", columnNames,"reportes/reporte_fallas.tmp");
    }


}
