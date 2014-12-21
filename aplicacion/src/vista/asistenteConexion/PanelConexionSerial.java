package vista.asistenteConexion;

import conexion.BufferEvent;
import conexion.BufferEventListener;

import conexion.ConexionException;
import conexion.MuchosListenerException;
import conexion.OperacionNoSoportadaException;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;

import javax.comm.CommPortIdentifier;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import javax.swing.JToggleButton;

import oracle.jdeveloper.layout.VerticalFlowLayout;

import util.JRadioButtonGroup;
import util.TipoProtocolos;

import vista.ConsolaPanel;
import vista.ControladorVista;

public class PanelConexionSerial extends JPanel  {
    private JPanel jPanel2 = new JPanel();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JScrollPane jScrollPane2 = new JScrollPane();
    private BorderLayout borderLayout1 = new BorderLayout();
    private GridLayout gridLayout1 = new GridLayout();
    private GridLayout gridLayout2 = new GridLayout();
    private JPanel panelPuertos = new JPanel();
    private BorderLayout borderLayout2 = new BorderLayout();
    private VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
    private JPanel jPanel5 = new JPanel();
    private VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
    private JRadioButton velocidad1RBoton = new JRadioButton();
    private JRadioButton velocidad2RBoton = new JRadioButton();
    
    // variables relevantes
    
    JRadioButtonGroup grupoPuertosRBoton = new JRadioButtonGroup();
    JRadioButtonGroup grupoVelocidadRBoton = new JRadioButtonGroup();

    private static final PanelConexionSerial instancia=new PanelConexionSerial();
    
    public static PanelConexionSerial getInstancia(){
        return instancia;
    }
    public PanelConexionSerial() {
        try {
            
            jbInit();
            getPuertosSerialSistema();
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        this.setSize(new Dimension(275, 246));
        jPanel2.setLayout(gridLayout1);
        jScrollPane1.setBorder(BorderFactory.createTitledBorder("Puertos Disponibles"));
        jScrollPane2.setBorder(BorderFactory.createTitledBorder("Puertos Disponibles"));
        
        panelPuertos.setLayout(verticalFlowLayout1);
        jPanel5.setLayout(verticalFlowLayout2);
        velocidad1RBoton.setText("9600");
        velocidad2RBoton.setText("38400");
        jScrollPane2.getViewport().add(panelPuertos, BorderLayout.CENTER);
        jPanel2.add(jScrollPane2,
                    new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                                           GridBagConstraints.BOTH,
                                           new Insets(25, 55, 144, 88), 130,
                                           61));
        jPanel5.add(velocidad1RBoton, null);
        jPanel5.add(velocidad2RBoton, null);
        jScrollPane1.getViewport().add(jPanel5, null);
        jPanel2.add(jScrollPane1,
                    new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                                           GridBagConstraints.BOTH,
                                           new Insets(25, 30, 144, 0), 110,
                                           56));
        this.add(jPanel2, BorderLayout.CENTER);


        grupoVelocidadRBoton.add(velocidad1RBoton);
        grupoVelocidadRBoton.add(velocidad2RBoton);
        grupoVelocidadRBoton.setSelected(velocidad2RBoton.getModel(),true);
        
    }

     private void getPuertosSerialSistema() {
        log("listando los puertos");
        List <CommPortIdentifier> puertos = ControladorVista.getInstancia().getPuertosSerialSistema();
        JRadioButton pRB=null;
       for (CommPortIdentifier commPortIdentifier : puertos) {
           pRB = new JRadioButton();
           pRB.setText(commPortIdentifier.getName());
            panelPuertos.add(pRB, null);
            grupoPuertosRBoton.add(pRB);
            log( "puerto:"+commPortIdentifier.getName());
           
        }
       if(pRB!=null)
        grupoPuertosRBoton.setSelected(pRB.getModel(), true);
        panelPuertos.updateUI();
    }

    
    public void log(String dato){
        ConsolaPanel.getInstancia().log("PanelConexionSerial:",dato);
    }
    public String getPuertoSeleccionado(){
        return  grupoPuertosRBoton.getSelectionRadioButton().getText();
    }
    public String getVelocidadSeleccionada(){
        return grupoVelocidadRBoton.getSelectionRadioButton().getText();
    }
}
