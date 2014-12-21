
package vista;

import conexion.BufferEvent;
import conexion.BufferEventListener;

import conexion.ConexionException;
import conexion.OperacionNoSoportadaException;

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

import java.io.IOException;

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
import modelo.EstadoDTC;
import modelo.RespuestaOBD;

import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXTable;

import util.ComandosELM;
import util.ModelTable;

public class PanelMonitorTest extends JPanel implements   Manipulable {


    private Icon iconRefrezcar = new ImageIcon("imagenes/repeat.png");
    private JPanel jPanel1 = new JPanel();
    private JToolBar jToolBar1 = new JToolBar();
    private JButton refrescarBoton = new JButton();
    private JPanel jPanel2 = new JPanel();
    private JXTable tablaCodigosAlmacenados = new JXTable();

    //---------------------------------------------------
    ModelTable modeloTablaMonitorTest = new ModelTable();
    private static final PanelMonitorTest instancia = new PanelMonitorTest();
    private TreeSet<DTC> hashSetDTC = new TreeSet<DTC>();
    ///-------------------------
    private JScrollPane jScrollPane1 = new JScrollPane();
    private BorderLayout borderLayout2 = new BorderLayout();
    private BorderLayout borderLayout3 = new BorderLayout();
    private JSplitPane jSplitPane1 = new JSplitPane();
    private JTabbedPane jTabbedPane1 = new JTabbedPane();
    private JScrollPane jScrollPane2 = new JScrollPane();
    private JTabbedPane pestanasPanel = new JTabbedPane();
    private BorderLayout borderLayout1 = new BorderLayout();


    private JPanel jPanel3 = new JPanel();
    private JLabel numeroDTCLabel = new JLabel();
    private JLabel numeroDTCLabelO = new JLabel();
    private JLabel milLabelO = new JLabel();
    private JLabel milLabel = new JLabel();
    
    
    boolean detener=false;

    Icon iconOff = new ImageIcon("imagenes/MILoff.png");
    Icon iconOn = new ImageIcon("imagenes/MILon.png");
   

    private PanelMonitorTest() {
        try {
            // debe caragar los ´codigos almacenados
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PanelMonitorTest getInstancia()  {
        ControladorVista control = ControladorVista.getInstancia();
       control.setInterfaz(instancia);
        //instancia.getCodigosFallas();
       return instancia;
    }

    private void jbInit() throws Exception {
        //modeloTablaCodigos.setColumnIdentifiers(nombreColumnas);
        modeloTablaMonitorTest.addColumn("Nombre");
        modeloTablaMonitorTest.addColumn("Continuo");
        modeloTablaMonitorTest.addColumn("Disponible");
        modeloTablaMonitorTest.addColumn("Completo");

        tablaCodigosAlmacenados.setModel(modeloTablaMonitorTest);


        jPanel3.setLayout(null);
        numeroDTCLabel.setText("Número de DTC:");
        numeroDTCLabel.setBounds(new Rectangle(5, 5, 95, 15));
        numeroDTCLabelO.setBounds(new Rectangle(135, 5, 70, 15));
        milLabelO.setBounds(new Rectangle(0, 45, 200, 100));
        milLabelO.setIcon(iconOff);
        milLabel.setText("Estado luz MIL:");
        milLabel.setBounds(new Rectangle(5, 25, 110, 15));
        jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setDividerLocation(250);
        this.setLayout(borderLayout3);
        this.setSize(new Dimension(477, 462));
        jPanel1.setLayout(borderLayout2);
        refrescarBoton.setText("Actualizar");
        refrescarBoton.setActionCommand("Actualizar");
        refrescarBoton.setIcon(iconRefrezcar);
        refrescarBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    refrescarBoton_actionPerformed(e);
                }
            });
        jPanel2.setLayout(borderLayout1);
        tablaCodigosAlmacenados.setSortable(false);
        tablaCodigosAlmacenados.setSortsOnUpdates(false);
        jToolBar1.add(refrescarBoton, null);
        jPanel1.add(jToolBar1, BorderLayout.NORTH);
        jPanel3.add(milLabel, null);
        jPanel3.add(milLabelO,
                    new GridBagConstraints(2, 0, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
                                           GridBagConstraints.NONE,
                                           new Insets(20, 0, 0, 0), 366, 1));
        jPanel3.add(numeroDTCLabelO,
                    new GridBagConstraints(2, 1, 3, 2, 0.0, 0.0, GridBagConstraints.WEST,
                                           GridBagConstraints.NONE,
                                           new Insets(0, 0, 0, 0), 361, 1));
        jPanel3.add(numeroDTCLabel,
                    new GridBagConstraints(0, 1, 1, 2, 0.0, 0.0, GridBagConstraints.WEST,
                                           GridBagConstraints.NONE,
                                           new Insets(0, 10, 0, 0), 1, 0));
        jScrollPane2.getViewport().add(jPanel3, null);
        jTabbedPane1.addTab("Estado de la luz MIL", jScrollPane2);
        jSplitPane1.add(jTabbedPane1, JSplitPane.BOTTOM);
        jScrollPane1.getViewport().add(tablaCodigosAlmacenados, null);
        pestanasPanel.addTab("servicio $01 pid 01", jScrollPane1);
        jSplitPane1.add(pestanasPanel, JSplitPane.TOP);
        jPanel2.add(jSplitPane1, BorderLayout.CENTER);
        jPanel1.add(jPanel2, BorderLayout.CENTER);
        this.add(jPanel1, BorderLayout.CENTER);


    }

    /**
     *para limpiar los codigos de errorS
     * @param e
     */
    private void limpiarBoton_actionPerformed(ActionEvent e) {
        if (JOptionPane.showConfirmDialog(this,
                                          "Realemente desea borrar los códigos almacenados",
                                          "SERVICIO $04",
                                          JOptionPane.WARNING_MESSAGE) == 0) {
            ControladorVista controladorVista =
                ControladorVista.getInstancia();
            try {
                if (controladorVista.estaConectado())
                    controladorVista.escribirPuertoConexion(ComandosELM.BORRAR_CODIGO_ALMACENADOS_04,401);
                else
                    JOptionPane.showMessageDialog(this,
                                                  "Operación fallida, no está conectado a la interfaz");
            } catch (Exception f) {
            }
        }
    }

   

    private void refrescarBoton_actionPerformed(ActionEvent e) {
        getCodigosFallas();
    }

    /**
     * envia un comando para leer los codigos de falla
     */
    private void getCodigosFallas() {
        while (modeloTablaMonitorTest.getRowCount() > 0)
            modeloTablaMonitorTest.removeRow(0);

        milLabelO.setText("");
        numeroDTCLabelO.setText("");
        // sacar lso que esrtaban el el set
        hashSetDTC.clear();
        ControladorVista.getInstancia().cancelarEscrituraLectura();

        Thread t=new Thread(){
            public void run(){
                ControladorVista control = ControladorVista.getInstancia();
                try {
                    if (control.estaConectado()) {
                     control.escribirPuertoConexion(ComandosELM.ESTADO_DTC_0101,
                                                       01);
                 } else {
                        JOptionPane.showMessageDialog(instancia,
                                                      "Operacion Fallida, no está conectado");
                    }

                } catch (Exception e) {

                }
            }
        };
        t.start();
       
    }


    public void detenerMuestreo(boolean detener) {
        
        ControladorVista.getInstancia().cancelarEscrituraLectura();
    }

    public void tratarRespuesta(RespuestaOBD r) {
        /**
        
            /*hay que discriminar si es una dato del servico $03, $07 $09
          * */
            RespuestaOBD respuesta=r;
            
            
            if (respuesta != null && respuesta instanceof EstadoDTC) {
                EstadoDTC dtc = (EstadoDTC)respuesta;
                
                //modeloTablaMonitorTest.addRow();
                String tabla[][]=dtc.getTablaMonitoreo();
                for (int i = 0; i < 11; i++) {
                    modeloTablaMonitorTest.addRow(new String[]{tabla[i][0],tabla[i][1]+" ",tabla[i][2]+" ",tabla[i][3]+" "});
                }
                //milLabelO.setText((dtc.getMIL()==1)?"Encendida":"Apagada");
            
                milLabelO.setIcon((dtc.getMIL() == 1) ? iconOn : iconOff);
                numeroDTCLabelO.setText(dtc.getNumeroDTCs()+"");

            }           
        
    }
}
