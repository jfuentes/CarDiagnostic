package vista;


import conexion.ConexionException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.WindowAdapter;

import java.awt.event.WindowEvent;

import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import modelo.ControladorModelo;

import modelo.DecodificadorPIDs2;

import oracle.jdeveloper.layout.PaneConstraints;
import oracle.jdeveloper.layout.PaneLayout;
import oracle.jdeveloper.layout.VerticalFlowLayout;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXGraph;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXImagePanel;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.JXTitledSeparator;
import org.jdesktop.swingx.painter.Painter;

import vista.asistenteConexion.AsistenteConexionPanel;

import vista.asistenteConexion.PanelConexionBluetooth;

import vista.asistenteConexion.PanelConexionSerial;

import vista.ayuda.AyudaPanel;

import vista.estadisticas.PanelEstadisticas;

public class FramePrincipal extends JFrame {
    private BorderLayout layoutMain = new BorderLayout();
    private JPanel panelCenter = new JPanel();
    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuFile = new JMenu();
    private JMenuItem menuFileExit = new JMenuItem();
    private JMenu menuHelp = new JMenu();
    private JMenuItem menuHelpAbout = new JMenuItem();
    private JLabel statusBar = new JLabel();
    private JSplitPane splitPanelCentral = new JSplitPane();
    private JPanel jPanel1 = new JPanel();
    private JXTaskPaneContainer jXTaskPaneContainer1 =
        new JXTaskPaneContainer();
    private BorderLayout borderLayout2 = new BorderLayout();
    private JXTitledSeparator jXTitledSeparator1 = new JXTitledSeparator();
    private JXTaskPane jXTaskPane1 = new JXTaskPane();
    private JXTaskPane jXTaskPane2 = new JXTaskPane();
    private JXTaskPane jXTaskPane3 = new JXTaskPane();
    private JPanel panelEjemplo = new JPanel();
    private JToolBar jToolBar1 = new JToolBar();
   
    private JButton conectarBoton = new JButton();
    private JButton desconectarBoton = new JButton();
    private JToolBar jToolBar2 = new JToolBar();
    private JButton codigosDeFallasBoton = new JButton();
    private VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
    private JButton causasCodigosFallasBoton = new JButton();
    private JButton pidsBoton = new JButton();
    private JToolBar jToolBar4 = new JToolBar();
    private JButton sensorOxigenoBoton = new JButton();
    private VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
    private JButton informacionVehiculoBoton = new JButton();
    private JButton sensorOxigenoCanBoton = new JButton();
    private JButton testearMonitoreoBoton = new JButton();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JButton mostrarTableroBoton = new JButton();

    // private  AsistenteConexionPanel asistenteConexion;
    Icon icon = new ImageIcon("imagenes/process.png");
    Icon icon2 = new ImageIcon("imagenes/remove.png");
    Icon tacometro = new ImageIcon("imagenes/tacometro2.png");
    Icon iconDtc = new ImageIcon("imagenes/dtc.png");
    Icon iconOt = new ImageIcon("imagenes/conf.png");
    Icon iconAuto = new ImageIcon("imagenes/auto.png");
    Icon iconTestearMonitoreo = new ImageIcon("imagenes/toco.png");
    Icon iconInfo = new ImageIcon("imagenes/llave.png");
    Icon iconEstad = new ImageIcon("imagenes/estad.png");
    Icon iconConsola = new ImageIcon("imagenes/cont.png");
    Icon iconCasa = new ImageIcon("imagenes/casa.png");
    Icon iconSalir = new ImageIcon("imagenes/home_next.png");
    Icon iconAcercaDe = new ImageIcon("imagenes/users.png");
    Icon iconManual = new ImageIcon("imagenes/help.png");
    Icon iconHome = new ImageIcon("imagenes/home.png");
    private Image img=(new ImageIcon("imagenes/index2.png")).getImage();

    public static final FramePrincipal instancia;
    private JToolBar jToolBar5 = new JToolBar();
    private JButton consola = new JButton();
    private VerticalFlowLayout verticalFlowLayout4 = new VerticalFlowLayout();
    private JButton consola2Panel = new JButton();
    private JMenuItem contenidoMenuItem = new JMenuItem();
    private JXTitledPanel jXTitledPanel1 = new JXTitledPanel();
    private BorderLayout borderLayout1 = new BorderLayout();
    private JButton estadisticasBoton = new JButton();
    private JButton inicioBoton = new JButton();
    private JXImagePanel jXImagePanel1 = new JXImagePanel();
    private BorderLayout borderLayout3 = new BorderLayout();
    private BorderLayout borderLayout4 = new BorderLayout();
    private JButton jButton1 = new JButton();
    static {
        try {

            DecodificadorPIDs2.decodificar("?");
            System.out.println("deberia haber cargado el archivo");
        } catch (Exception e) {
        }
    }

    static {
       instancia=new FramePrincipal();
   }
    
    

    private FramePrincipal() {
        try {
            jbInit();
            consola.setVisible(false);
            sensorOxigenoBoton.setVisible(false);
            sensorOxigenoCanBoton.setVisible(false);
            //jXHyperlink1.setURI(URI.create("http://200.0.144.73"));
            //AsistenteConexionPanel.createInstancia(this, "Asistente de conexion", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
    public static FramePrincipal getInstance(){
        
        return instancia;
    }

    private void jbInit() throws Exception {
        this.setJMenuBar( menuBar );
        this.getContentPane().setLayout( layoutMain );
        panelCenter.setLayout(borderLayout1);
        this.setSize(new Dimension(1251, 626));
        menuFile.setText("Archivo");
        menuFileExit.setText("Salir");
        menuFileExit.setIcon(this.iconSalir);
        
        menuFileExit.addActionListener( new ActionListener() { public void actionPerformed( ActionEvent ae ) { fileExit_ActionPerformed( ae ); } } );
        menuHelp.setText("Ayuda");
       
        menuHelpAbout.setText("Acerca del Software");
        menuHelpAbout.setIcon(this.iconAcercaDe);
        menuHelpAbout.addActionListener( new ActionListener() { public void actionPerformed( ActionEvent e ) {
                    helpAbout_ActionPerformed(e);
                } } );
        statusBar.setText( "" );
        splitPanelCentral.setDividerLocation(290);
        splitPanelCentral.setContinuousLayout(true);
        splitPanelCentral.setOneTouchExpandable(true);
        
        jPanel1.setLayout(borderLayout2);
        jPanel1.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        jPanel1.setMinimumSize(new Dimension(199, 36));
        jPanel1.setMaximumSize(new Dimension(300, 1000));
        jXTaskPaneContainer1.setBackground(new Color(127, 166, 245));
        jXTaskPaneContainer1.setBorder(BorderFactory.createEmptyBorder(9, 11,
                                                                       0, 11));
        jXTaskPaneContainer1.setScrollableTracksViewportHeight(true);
        jXTaskPaneContainer1.setMaximumSize(new Dimension(300, 1000));
        jXTitledSeparator1.setTitle("Menú");
        jXTaskPane1.setTitle("Utilidades");

        jXTaskPane1.setAnimated(false);
        jXTaskPane1.setCollapsed(true);
        jXTaskPane2.setBounds(new Rectangle(11, 158, 173, 131));
        jXTaskPane2.setTitle("Diagnosticar");
        jXTaskPane2.setAnimated(false);
        jXTaskPane3.setTitle("Monitoriar");
        jXTaskPane3.setAnimated(false);
        panelEjemplo.setLayout(borderLayout3);
       
        conectarBoton.setText("Conectar");

        conectarBoton.setIcon(icon);
        conectarBoton.setPreferredSize(new Dimension(100, 30));
        conectarBoton.setMinimumSize(new Dimension(100, 30));
        conectarBoton.setMaximumSize(new Dimension(100, 30));
        conectarBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    conectarBoton_actionPerformed(e);
                }
            });
        desconectarBoton.setText("Desconectar");
        desconectarBoton.setEnabled(false);

        desconectarBoton.setIcon(icon2);
        desconectarBoton.setMaximumSize(new Dimension(120, 30));
        desconectarBoton.setMinimumSize(new Dimension(120, 30));
        desconectarBoton.setPreferredSize(new Dimension(120, 30));
        desconectarBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    desconectarBoton_actionPerformed(e);
                }
            });
        jToolBar2.setLayout(verticalFlowLayout1);
        jToolBar2.setFloatable(false);
        jToolBar2.setBorderPainted(false);
        codigosDeFallasBoton.setText("Fallas Presentes");
        codigosDeFallasBoton.setIcon(iconDtc);
        codigosDeFallasBoton.setHorizontalAlignment(SwingConstants.LEFT);
       
        codigosDeFallasBoton.setVerticalAlignment(SwingConstants.TOP);
        codigosDeFallasBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    codigosDeFallasBoton_actionPerformed(e);
                }
            });
        causasCodigosFallasBoton.setText("Sensores al momento de falla");
        causasCodigosFallasBoton.setIcon(iconOt);
        causasCodigosFallasBoton.setHorizontalAlignment(SwingConstants.LEFT);
        causasCodigosFallasBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    causasCodigosFallasBoton_actionPerformed(e);
                }
            });
        pidsBoton.setText("Medir Sensores");
        pidsBoton.setIcon(iconAuto);
       // pidsBoton.setContentAreaFilled(false);
       // pidsBoton.setBorderPainted(false);

        pidsBoton.setHorizontalAlignment(SwingConstants.LEFT);
        pidsBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    pidsBoton_actionPerformed(e);
                }
            });
        jToolBar4.setLayout(verticalFlowLayout2);
        jToolBar4.setFloatable(false);
        jToolBar4.setBorderPainted(false);
        sensorOxigenoBoton.setText("Sensor de Oxígeno");
        sensorOxigenoBoton.setHorizontalAlignment(SwingConstants.LEFT);
        sensorOxigenoBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    sensorOxigenoBoton_actionPerformed(e);
                }
            });
        informacionVehiculoBoton.setText("Información del Vehículo");
        informacionVehiculoBoton.setIcon(iconInfo);
        informacionVehiculoBoton.setHorizontalAlignment(SwingConstants.LEFT);
        informacionVehiculoBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    informacionVehiculoBoton_actionPerformed(e);
                }
            });
        sensorOxigenoCanBoton.setText("Sensor Oxígeno CAN");
        sensorOxigenoCanBoton.setHorizontalAlignment(SwingConstants.LEFT);
        testearMonitoreoBoton.setText("Estado ECU");
        testearMonitoreoBoton.setHorizontalAlignment(SwingConstants.LEFT);
        testearMonitoreoBoton.setIcon(iconTestearMonitoreo);
        testearMonitoreoBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    testearMonitoreoBoton_actionPerformed(e);
                }
            });
        jScrollPane1.setMaximumSize(new Dimension(300, 1000));
        mostrarTableroBoton.setText("Mostrar Tablero");
        mostrarTableroBoton.setIcon(tacometro);
        mostrarTableroBoton.setHorizontalAlignment(SwingConstants.LEFT);
        mostrarTableroBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mostrarTableroBoton_actionPerformed(e);
                }
            });
        jToolBar5.setLayout(verticalFlowLayout4);
        jToolBar5.setBorderPainted(false);
        jToolBar5.setFloatable(false);
        consola.setText("log");
        consola.setContentAreaFilled(false);
        consola.setHorizontalAlignment(SwingConstants.LEFT);
        consola.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    consola_actionPerformed(e);
                }
            });
        consola2Panel.setText("Consola");
        consola2Panel.setActionCommand("Consola2");
        consola2Panel.setIcon(iconConsola);
        consola2Panel.setHorizontalAlignment(SwingConstants.LEFT);
        consola2Panel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    consola2Panel_actionPerformed(e);
                }
            });
        contenidoMenuItem.setText("Manual de usuario");
        contenidoMenuItem.setIcon(this.iconManual);
        contenidoMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    contenidoMenuItem_actionPerformed(e);
                }
            });
        jXTitledPanel1.setTitle("Diagnóstico OBD II v 1.0  Universidad del Bío-Bío 2010");
        jXTitledPanel1.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        jXTitledPanel1.setTitleForeground(new Color(126, 0, 0));
        jXTitledPanel1.setEnabled(false);
        jXTitledPanel1.setDoubleBuffered(false);
        jXTitledPanel1.setOpaque(true);
        jXTitledPanel1.setScrollableTracksViewportWidth(false);
        jXTitledPanel1.setScrollableTracksViewportHeight(false);
        estadisticasBoton.setText("Estadísticas");
        estadisticasBoton.setIcon(iconEstad);
        estadisticasBoton.setHorizontalAlignment(SwingConstants.LEFT);
        estadisticasBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    estadisticasBoton_actionPerformed(e);
                }
            });

        inicioBoton.setIcon(iconCasa);
        jXImagePanel1.setImage(img);
        jXImagePanel1.setLayout(borderLayout4);
        jButton1.setMaximumSize(new Dimension(47, 30));
        jButton1.setMinimumSize(new Dimension(47, 30));
        jButton1.setPreferredSize(new Dimension(100, 30));
        jButton1.setIcon(iconHome);
        jButton1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jButton1_actionPerformed(e);
                }
            });
        menuFile.add( menuFileExit );
        menuBar.add( menuFile );
        menuHelp.add(menuHelpAbout);
        menuHelp.add(contenidoMenuItem);
        menuBar.add(menuHelp);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        jXTaskPane2.add(jToolBar2, BorderLayout.CENTER);
        jXTaskPaneContainer1.add(jXTaskPane2, null);
        jXTaskPane3.add(jToolBar4, BorderLayout.CENTER);
        jXTaskPaneContainer1.add(jXTaskPane3, null);
        jXTaskPane1.add(jToolBar5, BorderLayout.SOUTH);
        jXTaskPaneContainer1.add(jXTaskPane1, null);
        jPanel1.add(jXTitledSeparator1, BorderLayout.NORTH);
        jScrollPane1.getViewport().add(jXTaskPaneContainer1, null);
        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
        splitPanelCentral.add(jPanel1, JSplitPane.LEFT);
        jToolBar2.add(codigosDeFallasBoton, BorderLayout.CENTER);
        jToolBar2.add(causasCodigosFallasBoton, null);
        jToolBar2.add(pidsBoton, null);
        jToolBar4.add(sensorOxigenoBoton, null);
        jToolBar4.add(informacionVehiculoBoton, null);
        jToolBar4.add(sensorOxigenoCanBoton, null);
        jToolBar4.add(testearMonitoreoBoton, null);
        jToolBar4.add(mostrarTableroBoton, null);
        jToolBar4.add(estadisticasBoton, null);
        jToolBar5.add(consola, null);
        jToolBar5.add(consola2Panel, null);


        panelEjemplo.add(jXImagePanel1, BorderLayout.CENTER);
        splitPanelCentral.add(panelEjemplo, JSplitPane.RIGHT);
        panelCenter.add(jXTitledPanel1, BorderLayout.NORTH);
        panelCenter.add(splitPanelCentral, BorderLayout.CENTER);
        this.getContentPane().add(panelCenter, BorderLayout.CENTER);
        this.getContentPane().add(jToolBar1, BorderLayout.NORTH);

        jToolBar1.add(jButton1, null);
        jToolBar1.add(conectarBoton,
                      new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                             GridBagConstraints.CENTER,
                                             GridBagConstraints.NONE,
                                             new Insets(0, 0, 0, 0), 130, 0));
        jToolBar1.add(desconectarBoton,
                      new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                                             GridBagConstraints.CENTER,
                                             GridBagConstraints.NONE,
                                             new Insets(0, 0, 0, 1056), 31,
                                             0));

        this.setVisible(true);
        this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    this_windowClosing(e);
                }
            });
    }

    void fileExit_ActionPerformed(ActionEvent e) {
        System.exit(0);
    }

    void helpAbout_ActionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(this, new FramePrincipal_AboutBoxPanel1(), "About", JOptionPane.PLAIN_MESSAGE);
    }
    public static void main(String[] args) {
        //new FramePrincipal();
    }

    private void conexionSerialBoton_actionPerformed(ActionEvent e) {
        detenerPeticionesELM();
        splitPanelCentral.add(PanelConexionSerial.getInstancia(), JSplitPane.RIGHT);
    }

    private void consola_actionPerformed(ActionEvent e) {
        detenerPeticionesELM();
        splitPanelCentral.add(ConsolaPanel.getInstancia(), JSplitPane.RIGHT);
       
    }

    private void conexionBluetoothBoton_actionPerformed(ActionEvent e) {
        detenerPeticionesELM();
        splitPanelCentral.add(PanelConexionBluetooth.getInstancia() , JSplitPane.RIGHT);
    }

    private void mostrarTableroBoton_actionPerformed(ActionEvent e) {
        detenerPeticionesELM();
        splitPanelCentral.add(PanelTablero.getInstancia() , JSplitPane.RIGHT);
    }

/**
     * Muestra un esistente para la conexion
     * @param e
     * 
     */
    private void conectarBoton_actionPerformed(ActionEvent e) {
       AsistenteConexionPanel ac= new AsistenteConexionPanel(this, "Asistente de conexion", true);
       ac.setVisible(true);
    }

    private void desconectarBoton_actionPerformed(ActionEvent e) {
        if(JOptionPane.showConfirmDialog(this, "¿Está seguro que desea cerrar la conexión?")==0){
        detenerPeticionesELM();
        log("cerrando conexion");
        try {
            conectarBoton.setEnabled(true);
            desconectarBoton.setEnabled(false);
            ControladorVista.getInstancia().cerrarConexion();
           
        } catch (ConexionException f) {
            log(f.toString());
           JOptionPane.showMessageDialog(this,f.getMessage(), "Excepciòn", JOptionPane.ERROR);
        }
        }
    }

    private void log(String dato) {
        ConsolaPanel.getInstancia().log("FRame Principal", dato);
    }
   
    public void habilitarBotonesConectarDesconectar(boolean b){
        conectarBoton.setEnabled(b);
        desconectarBoton.setEnabled(!b);
                
    }

    private void this_windowClosing(WindowEvent e) {
        System.exit(0);
    }

    private void codigosDeFallasBoton_actionPerformed(ActionEvent e) {
        detenerPeticionesELM();
        splitPanelCentral.add(PanelCodigosFallasDTC.getInstancia() , JSplitPane.RIGHT);
    }

    private void causasCodigosFallasBoton_actionPerformed(ActionEvent e) {
        detenerPeticionesELM();
        splitPanelCentral.add(PanelFreezeFrame.getInstancia() , JSplitPane.RIGHT);
    }

    private void sensorOxigenoBoton_actionPerformed(ActionEvent e) {
        detenerPeticionesELM();
        splitPanelCentral.add(PanelSensorOxigeno.getInstancia() , JSplitPane.RIGHT);
    }

    private void informacionVehiculoBoton_actionPerformed(ActionEvent e) {
        detenerPeticionesELM();
        splitPanelCentral.add(PanelInformacionVehiculo.getInstancia() , JSplitPane.RIGHT);
    }

    private void consola2Panel_actionPerformed(ActionEvent e) {
        detenerPeticionesELM();
        splitPanelCentral.add(ConsolaPanel2.getInstancia() , JSplitPane.RIGHT);
    }

    private void contenidoMenuItem_actionPerformed(ActionEvent e) {
        detenerPeticionesELM();
        JOptionPane.showMessageDialog(this, new AyudaPanel(), "Ayuda", JOptionPane.PLAIN_MESSAGE);
    }

    private void pidsBoton_actionPerformed(ActionEvent e) {
        detenerPeticionesELM();
        splitPanelCentral.add(TodosPIDPanel.getInstancia(), JSplitPane.RIGHT);
    }
    private void detenerPeticionesELM(){
        Component comp[]=splitPanelCentral.getComponents();
       for (Component compo : comp) {
            if(compo instanceof Manipulable)
                ((Manipulable)compo).detenerMuestreo(true);
            
        }
       
    }

    private void testearMonitoreoBoton_actionPerformed(ActionEvent e) {
        detenerPeticionesELM();
        splitPanelCentral.add(PanelMonitorTest.getInstancia(), JSplitPane.RIGHT);
    }

    private void estadisticasBoton_actionPerformed(ActionEvent e) {
        detenerPeticionesELM();
        splitPanelCentral.add(PanelEstadisticas.getInstancia(), JSplitPane.RIGHT);
    }

    private void jButton1_actionPerformed(ActionEvent e) {
        detenerPeticionesELM();
        splitPanelCentral.add(panelEjemplo,JSplitPane.RIGHT);
    }
}
