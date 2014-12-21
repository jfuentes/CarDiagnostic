
package vista;

import conexion.BufferEvent;
import conexion.BufferEventListener;

import conexion.ConexionException;
import conexion.MuchosListenerException;
import conexion.OperacionNoSoportadaException;

import conexion.TimeOutException;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.MouseAdapter;

import java.awt.event.MouseEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.io.PrintWriter;

import java.net.URI;

import java.util.Iterator;
import java.util.LinkedHashMap;

import java.util.Set;

import java.util.SortedSet;

import java.util.TreeSet;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import modelo.DTC;
import modelo.ErrorDTC;
import modelo.RespuestaOBD;

import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXTable;

import util.ComandosELM;
import util.ModelTable;

public class PanelCodigosFallasDTC extends JPanel implements Manipulable {
    private JPanel jPanel1 = new JPanel();
    private JToolBar jToolBar1 = new JToolBar();
    private JButton refrescarBoton = new JButton();
    private JPanel jPanel2 = new JPanel();
    private JXTable tablaCodigosAlmacenados = new JXTable();

    //---------------------------------------------------
    ModelTable modeloTablaCodigosAlmacenados = new ModelTable();
    ModelTable modeloTablaCodigosPendientes = new ModelTable();
    ModelTable modeloTablaDetalle = new ModelTable();
    private static final PanelCodigosFallasDTC instancia =
        new PanelCodigosFallasDTC();
    private TreeSet<DTC> hashSetDTC = new TreeSet<DTC>();
    ///-------------------------
    private JScrollPane jScrollPane1 = new JScrollPane();
    private BorderLayout borderLayout2 = new BorderLayout();
    private JButton limpiarBoton = new JButton();
    private BorderLayout borderLayout3 = new BorderLayout();
    private JSplitPane jSplitPane1 = new JSplitPane();
    private JTabbedPane jTabbedPane1 = new JTabbedPane();
    private JScrollPane jScrollPane2 = new JScrollPane();
    private JTabbedPane pestanasPanel = new JTabbedPane();
    private BorderLayout borderLayout1 = new BorderLayout();
    
      private JScrollPane jScrollPane3 = new JScrollPane();
    private JXTable tablaCodigosPendientes = new JXTable();
    private JPanel jPanel3 = new JPanel();
    private JXHyperlink webLink = new JXHyperlink();
    private JLabel jLabel3 = new JLabel();
    private JLabel jLabel4 = new JLabel();
    private JLabel detalleLabel = new JLabel();
    private JLabel codigoLabel = new JLabel();
    private JLabel jLabel2 = new JLabel();
    private Icon icon = new ImageIcon("imagenes/pdf.png");
    private Icon icon2 = new ImageIcon("imagenes/icon_excel.gif");
    private Icon iconRefrezcar = new ImageIcon("imagenes/repeat.png");
    private Icon iconLimpiar = new ImageIcon("imagenes/window_remove.png");    
    
    private JButton pdfBoton = new JButton(icon);
    private JLabel jLabel1 = new JLabel();
    private JButton excelBoton = new JButton(icon2);

    class HiloLector extends Thread {

        boolean parar = false;

        public void run() {
            try {
                if(!parar)
                ControladorVista.getInstancia().escribirPuertoConexion("0300\r",
                                                                       3);
                if (!parar)
                ControladorVista.getInstancia().escribirPuertoConexion("0700\r",
                                                                       7);
            } catch (IOException e) {
                mostrarException(e);
            } catch (ConexionException e) {
                mostrarException(e);
            } catch (TimeOutException e) {
                mostrarException(e);
            }


        }

        public void parar() {
            parar = true;
        }


    }
    HiloLector hilo;

    private void mostrarException(Exception ee) {
        if (ee instanceof NullPointerException) {
            JOptionPane.showMessageDialog(this,
                                          "Excepción interna del programa, intente más tarde",
                                          "Excepción",
                                          JOptionPane.ERROR_MESSAGE);
        }
        if (ee instanceof ConexionException) {
            JOptionPane.showMessageDialog(this,
                                          ((ConexionException)ee).getMessage(),
                                          "Excepción de conexión",
                                          JOptionPane.ERROR_MESSAGE);

        }
        if (ee instanceof MuchosListenerException) {
            JOptionPane.showMessageDialog(this,
                                          ((MuchosListenerException)ee).getMessage(),
                                          "Excepción interna ",
                                          JOptionPane.ERROR_MESSAGE);

        }
        if (ee instanceof OperacionNoSoportadaException) {
            JOptionPane.showMessageDialog(this,
                                          ((OperacionNoSoportadaException)ee).getMessage(),
                                          "Excepción aporación no soportada",
                                          JOptionPane.ERROR_MESSAGE);

        }
        if (ee instanceof TimeOutException) {
            JOptionPane.showMessageDialog(this,
                                          "La Interfaz no reponde, verifique la conexión",
                                          "Excepción Tiempo de espera agota",
                                          JOptionPane.ERROR_MESSAGE);
        }

    }
    public void detenerMuestreo(boolean detener) {
        if(hilo!=null)
        hilo.parar();
        ControladorVista.getInstancia().cancelarEscrituraLectura();
    }

    public void tratarRespuesta(RespuestaOBD r) {
          
            /*hay que discriminar si es una dato del servico $03, $07 $09
          * */
            RespuestaOBD respuesta = r;
           
           // if(respuesta.getNumeroFrame()<0&&respuesta.getNumeroFrame()>100)return;
            if (respuesta != null && respuesta instanceof DTC) {
                while (modeloTablaDetalle.getRowCount() > 0)
                modeloTablaDetalle.removeRow(0);
                DTC dtc = (DTC)respuesta;
                switch (dtc.getServicio()) {
                case 0x43:
                    while(modeloTablaCodigosAlmacenados.getRowCount()>0) modeloTablaCodigosAlmacenados.removeRow(0);
                    
                    do {

                        modeloTablaCodigosAlmacenados.addRow(new String[] { "" +
                                                                            dtc.getCodigo(),
                                                                            dtc.getDescripcion() });
                        hashSetDTC.add(dtc);
                        dtc = (DTC)dtc.getSiguiente();
                    } while (dtc != null);
                    
                    break;
                case 0x47:
                    while(modeloTablaCodigosPendientes.getRowCount()>0) modeloTablaCodigosPendientes.removeRow(0);
                    
                    do {

                        modeloTablaCodigosPendientes.addRow(new String[] { "" +
                                                                           dtc.getCodigo(),
                                                                           dtc.getDescripcion() });
                        hashSetDTC.add(dtc);
                        dtc = (DTC)dtc.getSiguiente();
                    } while (dtc != null);
                    
                    break;
                 

                }

            } else {
                if (respuesta != null && respuesta.getServicio() == 0x44) {
                    JOptionPane.showMessageDialog(this,
                                                  "Códigos borrados exitosamente");
                    getCodigosFallas();
                } else {
                     if(respuesta != null && respuesta instanceof ErrorDTC){
                    // tratar como si no dataa.
                         if(modeloTablaCodigosPendientes.getRowCount()==0){
                             modeloTablaCodigosPendientes.addRow(new String[] { "NO DATOS"} );
                         }
                         if(modeloTablaCodigosAlmacenados.getRowCount()==0){
                             modeloTablaCodigosAlmacenados.addRow(new String[] { "NO DATOS"} );
                         }
                    }
                }
            }
         

    }

    

    private PanelCodigosFallasDTC() {
        try {
            // debe caragar los ´codigos almacenados
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PanelCodigosFallasDTC getInstancia() {
        ControladorVista control = ControladorVista.getInstancia();
           control.setInterfaz(instancia);
           // instancia.getCodigosFallas();
      return instancia;
    }

    private void jbInit() throws Exception {
        //modeloTablaCodigos.setColumnIdentifiers(nombreColumnas);
        modeloTablaCodigosAlmacenados.addColumn("Código");
        modeloTablaCodigosAlmacenados.addColumn("Descripción");
       // modeloTablaCodigosAlmacenados.addRow(new String[]{"P0100","algo"});
        modeloTablaCodigosPendientes.addColumn("Código");
        modeloTablaCodigosPendientes.addColumn("Descripción");

        modeloTablaDetalle.addColumn("Códido DTC");
        modeloTablaDetalle.addColumn("web");

        tablaCodigosPendientes.addMouseListener(new MouseAdapter() {


                public void mouseClicked(MouseEvent e) {
                    tablaCodigosPendientes_mouseClicked(e);
                }
            });
        tablaCodigosAlmacenados.setModel(modeloTablaCodigosAlmacenados);
        tablaCodigosPendientes.setModel(modeloTablaCodigosPendientes);


        jPanel3.setLayout(null);
        webLink.setBounds(new Rectangle(45, 80, 935, 20));
        webLink.setToolTipText("Revisa más información del código de error en la web");
        jLabel3.setText("Más detalles en la Web");
        jLabel3.setBounds(new Rectangle(5, 60, 200, 20));
        jLabel4.setText("Detalle:");
        jLabel4.setBounds(new Rectangle(5, 40, 60, 15));
        detalleLabel.setBounds(new Rectangle(90, 40, 860, 20));
        codigoLabel.setBounds(new Rectangle(90, 20, 890, 20));
        jLabel2.setText("Código:");
        jLabel2.setBounds(new Rectangle(5, 20, 60, 15));
        
        pdfBoton.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    jButton1_mousePressed(e);
                }
            });
        jLabel1.setText("    Exportar a:  ");
        
        excelBoton.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    jButton2_mousePressed(e);
                }
            });
        limpiarBoton.setText("Borrar Códigos");
        limpiarBoton.setIcon(iconLimpiar);
        limpiarBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    limpiarBoton_actionPerformed(e);
                }
            });
        jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setDividerLocation(200);
        this.setLayout(borderLayout3);
        this.setSize(new Dimension(469, 362));
        jPanel1.setLayout(borderLayout2);
        refrescarBoton.setText("Actualizar");
        refrescarBoton.setIcon(iconRefrezcar);
        
        refrescarBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    refrescarBoton_actionPerformed(e);
                }
            });
        jPanel2.setLayout(borderLayout1);
        tablaCodigosAlmacenados.setSortable(false);
        tablaCodigosAlmacenados.setSortsOnUpdates(false);
        tablaCodigosAlmacenados.addMouseListener(new MouseAdapter() {

                public void mouseClicked(MouseEvent e) {
                    tablaCodigosAlmacenados_mouseClicked(e);
                }
            });
        jToolBar1.add(refrescarBoton, null);
        jToolBar1.add(limpiarBoton, null);
        jToolBar1.add(jLabel1, null);
        jToolBar1.add(pdfBoton, null);
        jToolBar1.add(excelBoton, null);
        jPanel3.add(jLabel2, null);
        jPanel3.add(codigoLabel,
                    new GridBagConstraints(2, 0, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
                                           GridBagConstraints.NONE,
                                           new Insets(20, 0, 0, 0), 366, 1));
        jPanel3.add(detalleLabel,
                    new GridBagConstraints(2, 1, 3, 2, 0.0, 0.0, GridBagConstraints.WEST,
                                           GridBagConstraints.NONE,
                                           new Insets(0, 0, 0, 0), 361, 1));
        jPanel3.add(jLabel4,
                    new GridBagConstraints(0, 1, 1, 2, 0.0, 0.0, GridBagConstraints.WEST,
                                           GridBagConstraints.NONE,
                                           new Insets(0, 10, 0, 0), 1, 0));
        jPanel3.add(jLabel3,
                    new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
                                           GridBagConstraints.NONE,
                                           new Insets(0, 10, 0, 0), 155, 1));
        jPanel3.add(webLink,
                    new GridBagConstraints(1, 4, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                                           GridBagConstraints.NONE,
                                           new Insets(0, 0, 9, 0), 305, 1));
        jScrollPane2.getViewport().add(jPanel3, null);
        jTabbedPane1.addTab("detalle", jScrollPane2);
        jSplitPane1.add(jTabbedPane1, JSplitPane.BOTTOM);
        jScrollPane1.getViewport().add(tablaCodigosAlmacenados, null);
        pestanasPanel.addTab("Códigos Almacenados $03", jScrollPane1);
        jScrollPane3.getViewport().add(tablaCodigosPendientes, null);
        pestanasPanel.addTab("Códigos Pendientes $07", jScrollPane3);
        jSplitPane1.add(pestanasPanel, JSplitPane.TOP);
        jPanel2.add(jSplitPane1, BorderLayout.CENTER);
        jPanel1.add(jToolBar1, BorderLayout.NORTH);
        jPanel1.add(jPanel2, BorderLayout.CENTER);
        this.add(jPanel1, BorderLayout.CENTER);


    }

    /**
     *para limpiar los codigos de errorS
     * @param e
     */
    private void limpiarBoton_actionPerformed(ActionEvent e) {
        if (JOptionPane.showConfirmDialog(this,
                                          "Realmente desea borrar los códigos almacenados",
                                          "SERVICIO $04",
                                          JOptionPane.WARNING_MESSAGE) == 0) {
            Thread t=new Thread(){
                public void run(){
           
                    ControladorVista controladorVista =
                        ControladorVista.getInstancia();
                    try {
                        if (controladorVista.estaConectado())
                            controladorVista.escribirPuertoConexion(ComandosELM.BORRAR_CODIGO_ALMACENADOS_04,4);
                        else
                            JOptionPane.showMessageDialog(PanelCodigosFallasDTC.this,
                                                          "Operación fallida, no está conectado a la interfaz");
                    } catch (Exception f) {
                    }
                    }
             };
            t.start();
        }
    }
   
    private void refrescarBoton_actionPerformed(ActionEvent e) {
        getCodigosFallas();
    }

    /**
     * envia un comando para leer los codigos de falla
     */
    private void getCodigosFallas() {
        while(modeloTablaCodigosAlmacenados.getRowCount()>0) modeloTablaCodigosAlmacenados.removeRow(0);
        while(modeloTablaCodigosPendientes.getRowCount()>0) modeloTablaCodigosPendientes.removeRow(0);
        
        codigoLabel.setText("");
        detalleLabel.setText("");
        webLink.setURI(URI.create(""));
        // sacar lso que esrtaban el el set
        hashSetDTC.clear();
        detenerMuestreo(true);// if(hilo !=null)hilo.stop();
        hilo=new HiloLector();
        hilo.start();
        System.out.println("hilo dtc:"+hilo.getState().toString());
             
    }


    private void tablaCodigosAlmacenados_mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            while (modeloTablaDetalle.getRowCount() > 0)
                modeloTablaDetalle.removeRow(0);
            int row = tablaCodigosAlmacenados.getSelectedRow();
            //JOptionPane.showMessageDialog(this, "click:"+row);
            DTC dtc = null;
            if (row != -1) {
                String d = (String)tablaCodigosAlmacenados.getValueAt(row, 0);

                for (DTC dTC : hashSetDTC) {

                    if (dTC.getCodigo().equalsIgnoreCase(d)) {
                        dtc = dTC;
                        break;
                    }
                }

                if (dtc != null) {
                    //JOptionPane.showMessageDialog(this, "mostrar detalle de"+dtc.getCodigo());
                    codigoLabel.setText(dtc.getCodigo());
                    detalleLabel.setText(dtc.getDescripcion());
                    webLink.setURI(URI.create("http://www.dtcsearch.com/"+dtc.getCodigo()));

                }/* else{
                    codigoLabel.setText("p0100");
                    detalleLabel.setText("");
                    webLink.setURI(URI.create("http://www.dtcsearch.com/P0100"));
                } */
            }
        }
    }

    private void tablaCodigosPendientes_mouseClicked(MouseEvent e) {
        while (modeloTablaDetalle.getRowCount() > 0)
            modeloTablaDetalle.removeRow(0);
        int row = tablaCodigosPendientes.getSelectedRow();
        //JOptionPane.showMessageDialog(this, "click:"+row);
        DTC dtc = null;
        if (row != -1) {
            String d = (String)tablaCodigosPendientes.getValueAt(row, 0);

            for (DTC dTC : hashSetDTC) {

                if (dTC.getCodigo().equals(d)) {
                    dtc = dTC;
                    break;
                }
            }

            if (dtc != null) {
                JOptionPane.showMessageDialog(this,
                                              "mostrar detalle de" + dtc.getCodigo());
            }
        }
    }
  
    private static void mostrarExcepcionConexion(ConexionException e) {
        JOptionPane.showMessageDialog(instancia, e.getMessage(),
                                      "Error de conexión",
                                      JOptionPane.ERROR_MESSAGE);
    }

    private void jButton1_mousePressed(MouseEvent e) {
        String csv="reportes/reporte_fallas.tmp";
        try {
            FileWriter fw = new FileWriter(csv);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter salida = new PrintWriter(bw);
            for (int i = 0; i < this.modeloTablaCodigosAlmacenados.getRowCount(); i++) {
                salida.println(this.modeloTablaCodigosAlmacenados.getValueAt(i, 0)+"|"+this.modeloTablaCodigosAlmacenados.getValueAt(i, 1));
            }

            
            salida.close();
        }
        catch(IOException ioex) {
          System.out.println("Error al generar archivo temporal: "+ioex.toString());
        }
        Reporte.displayReport("reportes/dtc.jasper", new String[]{"CODIGO","DESCRIPCION"}, csv);
        
    }

    private void jButton2_mousePressed(MouseEvent e) {
       
        
        
        String csv="reportes/reporte_fallas.tmp";
        try {
            FileWriter fw = new FileWriter(csv);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter salida = new PrintWriter(bw);
            for (int i = 0; i < this.modeloTablaCodigosAlmacenados.getRowCount(); i++) {
                salida.println(this.modeloTablaCodigosAlmacenados.getValueAt(i, 0)+"|"+this.modeloTablaCodigosAlmacenados.getValueAt(i, 1));
            }

            
            salida.close();
        }
        catch(IOException ioex) {
          System.out.println("Error al generar archivo temporal: "+ioex.toString());
        }
        Reporte.displayReportEXCEL("reportes/dtc_excel.jasper", new String[]{"CODIGO","DESCRIPCION"}, csv);
    }
}
