package vista;

import java.awt.BorderLayout;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import javax.swing.JToolBar;

import org.jdesktop.swingx.JXTable;

import util.ModelTable;

public class PanelSensorOxigeno extends JPanel {
    private JPanel jPanel1 = new JPanel();
    private JPanel jPanel2 = new JPanel();
    private JTabbedPane jTabbedPane1 = new JTabbedPane();
    private JXTable tablaOxigeno = new JXTable();
    private JSplitPane jSplitPane1 = new JSplitPane();
    private JTabbedPane jTabbedPane2 = new JTabbedPane();
    private JPanel jPanel4 = new JPanel();
    private JPanel jPanel5 = new JPanel();
    private BorderLayout borderLayout2 = new BorderLayout();
    private JSplitPane jSplitPane2 = new JSplitPane();


    //-----------------------------------------
    //---------------------------------------------------
    ModelTable modeloTablaSensor=new ModelTable();
    
    private static final PanelSensorOxigeno instancia=new PanelSensorOxigeno();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private BorderLayout borderLayout1 = new BorderLayout();
    private JToolBar jToolBar1 = new JToolBar();
    private JButton refrescarBoton = new JButton();
    private JButton jButton3 = new JButton();
    private BorderLayout borderLayout3 = new BorderLayout();

    public PanelSensorOxigeno() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static PanelSensorOxigeno getInstancia(){
        return instancia;
    }

    private void jbInit() throws Exception {
        modeloTablaSensor.addColumn("N banco");
        modeloTablaSensor.addColumn("N Sensor");
        modeloTablaSensor.addColumn("Descripción");
        modeloTablaSensor.addColumn("Valor");
        modeloTablaSensor.addColumn("Mínimo");
        modeloTablaSensor.addColumn("Máximo");
        tablaOxigeno.setModel(modeloTablaSensor);
        this.setLayout(borderLayout3);
        jPanel1.setLayout(borderLayout1);
        jPanel2.setLayout(borderLayout2);
        jSplitPane1.setDividerLocation(50);
        jSplitPane2.setOrientation(JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setDividerLocation(200);
        refrescarBoton.setText("Refrescar");
        jButton3.setText("jButton3");
        jSplitPane1.add(jPanel4, JSplitPane.TOP);
        jSplitPane1.add(jPanel5, JSplitPane.BOTTOM);
        jScrollPane1.getViewport().add(tablaOxigeno, null);
        jTabbedPane1.addTab("Sensor de Oxigeno servicio $05 para vehículos no CAN solamente",
                jScrollPane1);
        jPanel1.add(jTabbedPane1, BorderLayout.CENTER);
        jSplitPane2.add(jPanel1, JSplitPane.TOP);
        jTabbedPane2.addTab("Detalle Sensor Oxigeno", jSplitPane1);
        jPanel2.add(jTabbedPane2, BorderLayout.CENTER);
        jSplitPane2.add(jPanel2, JSplitPane.BOTTOM);
        jToolBar1.add(refrescarBoton, null);
        jToolBar1.add(jButton3, null);
        this.add(jToolBar1, BorderLayout.NORTH);
        this.add(jSplitPane2, BorderLayout.CENTER);
    }
}
