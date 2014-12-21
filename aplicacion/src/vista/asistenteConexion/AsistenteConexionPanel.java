    package vista.asistenteConexion;


import conexion.BufferEvent;
import conexion.BufferEventListener;
import conexion.ConexionException;
import conexion.MuchosListenerException;
import conexion.OperacionNoSoportadaException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.WindowAdapter;

import java.awt.event.WindowEvent;

import java.io.IOException;

import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import modelo.MonitoreoPID;
import modelo.RespuestaOBD;

import oracle.jdeveloper.layout.VerticalFlowLayout;

import org.jdesktop.swingx.JXBusyLabel;

import util.ComandosELM;
import util.JRadioButtonGroup;
import util.TipoProtocolos;
import util.Traductor;

import vista.ConsolaPanel;
import vista.ControladorVista;
import vista.FramePrincipal;
import vista.Manipulable;


public class AsistenteConexionPanel extends JDialog implements Manipulable {
    private JPanel panelContenedorBotones = new JPanel();
    private JPanel panelBotones = new JPanel();
    private JButton atrasBoton = new JButton();
    private JButton cancelarBoton = new JButton();
    private BorderLayout borderLayout2 = new BorderLayout();
    private JPanel panelAyuda = new JPanel();
    private JButton ayudaBoton = new JButton();
    private JButton adelanteBoton = new JButton();

    //----------------------------------------------------------
    // private   static  AsistenteConexionPanel instancia;

    private int estado; // etsado del asistente respecto a la conexion
    private int tipoConexion;
    private static final int TIPO_CONEXION_SERIAL = 1;
    private static final int TIPO_CONEXION_BLUETOOTH = 2;

    private static final int ESTADO_MOSTRANDO_TIPO_CONEXION = 1;
    private static final int ESTADO_BUSCANDO = 2;
    private static final int ESTADO_FINALIZAR_CONEXION = 4;

    // para la segunda etapa
    //private int estadoConexionElmAuto; // etsado del asistente respecto a la conexion
    //private int tipoConexionElmAuto;
    private static final int TIPO_CONEXION_ELM_AUTOMATICA = 4;
    private static final int TIPO_CONEXION_ELM_MANUAL = 8;

    private static final int ESTADO_MOSTRANDO_TIPO_CONEXION_ELM = 8;

    private static final int ESTADO_FINALIZAR_CONEXION_ELM = 16;

    private JRadioButtonGroup grupoOpcionesConexion = new JRadioButtonGroup();
    private final PanelConexionSerial conexionSerialPanel =
        new PanelConexionSerial();
    private final PanelConexionBluetooth conexionBluetoothPanel =
        new PanelConexionBluetooth(this);

    private Thread t; // hilo que conecta
      // incializar el hashmap

    //----------------------------------------------------------
    private JPanel panelOpciones = new JPanel();
    private JPanel panelConectando = new JPanel();
    private JPanel jPanel2 = new JPanel();
    private JRadioButton serialRBoton = new JRadioButton();
    private JRadioButton bluetoothRBoton = new JRadioButton();
    private VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
    private JPanel panelCentral = new JPanel();
    private BorderLayout borderLayout3 = new BorderLayout();
    private JPanel tiposProtocolosPanel = new JPanel();
    private JPanel jPanel4 = new JPanel();
    private JLabel tipoConexionLabel = new JLabel();
    private FlowLayout flowLayout1 = new FlowLayout();
    private JLabel estadoConexionLabel = new JLabel();
    private VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
    private JXBusyLabel ocupadoLabel = new JXBusyLabel();
    private BorderLayout borderLayout1 = new BorderLayout();

    private FramePrincipal control;
    private JLabel dispositivosLabel = new JLabel();
    private JLabel estadoConexionFinalLabel = new JLabel();
    private VerticalFlowLayout verticalFlowLayout3 = new VerticalFlowLayout();
    private JComboBox tipoProtocolosComboBox = new JComboBox();

    private JLabel jLabel1 = new JLabel();
    private JPanel jPanel1 = new JPanel();
    private BorderLayout borderLayout4 = new BorderLayout();
    private FlowLayout flowLayout2 = new FlowLayout();
    private JSeparator jSeparator1 = new JSeparator();
    private JLabel tipoConexionLabel2face = new JLabel();
    private JLabel estadoConexion2final = new JLabel();


    { // inicializar el combo box
        String a[] = TipoProtocolos.getProtocolos();
        for (String protocolo : a) {
            tipoProtocolosComboBox.addItem(protocolo);
        }

    } {
        estado = this.ESTADO_MOSTRANDO_TIPO_CONEXION;
        grupoOpcionesConexion.add(serialRBoton);
        grupoOpcionesConexion.add(bluetoothRBoton);
        grupoOpcionesConexion.setSelected(serialRBoton.getModel(), true);
    }

    private AsistenteConexionPanel() {
        this(null, "", false);
    }

    /* por cada vez que se realiza una conenxión se utiliza un asistente objeto distinto*/

    public AsistenteConexionPanel(FramePrincipal parent, String title,
                                  boolean modal) {
        super(parent, title, modal);
        control = parent;
        ControladorVista.getInstancia().setInterfaz(this);
        try {
            jbInit();
            tipoConexionLabel.setForeground(Color.red);
            this.setLocation((int)((parent.getSize().getWidth() / 2) -
                                   (this.getSize().getWidth() / 2)), 50);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *puede retornar null
     * @return
     */
    /*  public static AsistenteConexionPanel getInstacia(){

        return instancia;
    } */
    private void jbInit() throws Exception {
        this.setSize(new Dimension(616, 395));
        this.getContentPane().setLayout(borderLayout3);
        this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    this_windowClosing(e);
                }
            });
        panelContenedorBotones.setLayout(borderLayout2);
        atrasBoton.setText("<< Atras");
        atrasBoton.setEnabled(false);
        atrasBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    atrasBoton_actionPerformed(e);
                }
            });
        cancelarBoton.setText("Cancelar");
        cancelarBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cancelarBoton_actionPerformed(e);
                }
            });
        ayudaBoton.setText("Ayuda");
        ayudaBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    ayudaBoton_actionPerformed(e);
                }
            });
        adelanteBoton.setText("Adelante >>");
        adelanteBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    adelanteBoton_actionPerformed(e);
                }
            });
        panelOpciones.setBorder(BorderFactory.createTitledBorder("Seleccione el medio de Conexión"));
        panelOpciones.setLayout(flowLayout1);
        panelConectando.setBorder(BorderFactory.createTitledBorder("Conectando"));
        panelConectando.setLayout(verticalFlowLayout2);
        jPanel2.setLayout(verticalFlowLayout1);
        serialRBoton.setText("Serial RS-232");
        bluetoothRBoton.setText("Bluetooth");
        panelCentral.setLayout(borderLayout1);
        tiposProtocolosPanel.setLayout(flowLayout2);
        tiposProtocolosPanel.setBorder(BorderFactory.createTitledBorder("Conexión entre la interfaz y el vehículo"));
        jPanel4.setLayout(verticalFlowLayout3);
        tipoConexionLabel.setText("Medio de Conexión");
        estadoConexionLabel.setText(" ");
        estadoConexionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ocupadoLabel.setText(" ");
        ocupadoLabel.setBusy(true);
        ocupadoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dispositivosLabel.setText("Dispositivos");
        estadoConexionFinalLabel.setText("Estado Conexión PC Interfaz");
        tipoProtocolosComboBox.setPreferredSize(new Dimension(251, 21));
        jLabel1.setText("Tipo de protocolo");
        jPanel1.setLayout(borderLayout4);
        tipoConexionLabel2face.setText("Tipo de Protocolo");
        estadoConexion2final.setText("Estado Conexión Interfaz Vehículo");
        panelBotones.add(atrasBoton, null);
        panelBotones.add(adelanteBoton, null);
        panelBotones.add(cancelarBoton, null);
        panelContenedorBotones.add(panelBotones, BorderLayout.EAST);
        panelAyuda.add(ayudaBoton, null);
        panelContenedorBotones.add(panelAyuda, BorderLayout.WEST);

        jPanel4.add(tipoConexionLabel, null);
        jPanel4.add(dispositivosLabel, null);
        jPanel4.add(estadoConexionFinalLabel, null);
        jPanel4.add(jSeparator1, null);
        jPanel4.add(tipoConexionLabel2face, null);
        jPanel4.add(estadoConexion2final, null);
        panelCentral.add(jPanel4, BorderLayout.WEST);
        panelCentral.add(panelContenedorBotones, BorderLayout.SOUTH);
        panelOpciones.add(jPanel2, BorderLayout.CENTER);
        panelCentral.add(panelOpciones, BorderLayout.CENTER);
        this.getContentPane().add(panelCentral, BorderLayout.CENTER);
        jPanel2.add(serialRBoton, null);
        jPanel2.add(bluetoothRBoton, null);
        panelConectando.add(estadoConexionLabel, null);
        panelConectando.add(ocupadoLabel, null);
        jPanel1.add(jLabel1, BorderLayout.NORTH);
        jPanel1.add(tipoProtocolosComboBox, BorderLayout.CENTER);
        tiposProtocolosPanel.add(jPanel1, BorderLayout.CENTER);
        // panelCentral.add(conexionSerialPanel, BorderLayout.CENTER);
    }

    private void adelanteBoton_actionPerformed(ActionEvent e) {
        removerPaneles();
        switch (estado) {
        case ESTADO_MOSTRANDO_TIPO_CONEXION:
            // estado siguiente mostrar los puertos o los bluetooth
            if (grupoOpcionesConexion.getSelectionRadioButton() ==
                serialRBoton) {
                panelCentral.add(conexionSerialPanel, BorderLayout.CENTER);
                tipoConexion = TIPO_CONEXION_SERIAL;
            } else {
                if (grupoOpcionesConexion.getSelectionRadioButton() ==
                    bluetoothRBoton) {
                    panelCentral.add(conexionBluetoothPanel,
                                     BorderLayout.CENTER);
                    tipoConexion = TIPO_CONEXION_BLUETOOTH;
                }
            }

            estado = this.ESTADO_BUSCANDO;
            adelanteBoton.setText(Traductor.getDes("conectar"));
            adelanteBoton.setEnabled(true);
            cancelarBoton.setEnabled(true);
            atrasBoton.setEnabled(true);
            dispositivosLabel.setForeground(Color.red);
            tipoConexionLabel.setForeground(Color.black);

            break;
        case ESTADO_BUSCANDO:
            ocupadoLabel.setVisible(true);
            ocupadoLabel.setBusy(true);
            estadoConexionLabel.setText("Conectando...");
            panelCentral.add(panelConectando, BorderLayout.CENTER);
            switch (tipoConexion) {
            case TIPO_CONEXION_SERIAL:
                conectarSerial();
                break;
           case TIPO_CONEXION_BLUETOOTH:
                conectarBluetooth();
                break;

            }
            estado = ESTADO_FINALIZAR_CONEXION;
            adelanteBoton.setText("Adelante >>");
            estadoConexionFinalLabel.setForeground(Color.red);
            dispositivosLabel.setForeground(Color.black);

            break;

        case ESTADO_FINALIZAR_CONEXION:
            atrasBoton.setEnabled(false);
            adelanteBoton.setText("Conectar");
            cancelarBoton.setEnabled(true);
            estado = ESTADO_MOSTRANDO_TIPO_CONEXION_ELM;
           //estado= ESTADO_FINALIZAR_CONEXION_ELM;
            panelCentral.add(tiposProtocolosPanel, BorderLayout.CENTER);
            tipoConexionLabel2face.setForeground(Color.red);
            estadoConexionFinalLabel.setForeground(Color.black);

            break;
        case ESTADO_MOSTRANDO_TIPO_CONEXION_ELM:
            ocupadoLabel.setVisible(true);
            ocupadoLabel.setBusy(true);
            estadoConexionLabel.setText("Conectando al vehículo...");
            panelCentral.add(panelConectando, BorderLayout.CENTER);
           estadoConexion2final.setForeground(Color.red);
             tipoConexionLabel2face.setForeground(Color.black);

            String tipoProtocolo =
                (String)tipoProtocolosComboBox.getSelectedItem();
            log("tipo protocolo:"+tipoProtocolo);
            String comando=TipoProtocolos.getComandoELM(tipoProtocolo);
            final Thread t=new Thread(){
                public void run(){
                    try {
                        ControladorVista.getInstancia().escribirPuertoConexion("atz\r",0,true);
                         ControladorVista.getInstancia().escribirPuertoConexion("ate0\r",0,true);
                          ControladorVista.getInstancia().escribirPuertoConexion("ats0\r",0,true);
                           ControladorVista.getInstancia().escribirPuertoConexion("010c \r",0,true);
                       
                       
                    } catch (Exception f) {
                        // conexion fallida
                        ocupadoLabel.setVisible(false);
                        ocupadoLabel.setBusy(false);
                        estadoConexionLabel.setText("Conexión Fallida");
                        estado = ESTADO_FINALIZAR_CONEXION_ELM;
                        atrasBoton.setEnabled(true);
                        adelanteBoton.setEnabled(false);
                        adelanteBoton.setText("Finalizar");
                        cancelarBoton.setEnabled(true);
                    }

                }
            };
            t.start();
            Thread v = new Thread() {
                public void run() {
                    try {
                        Thread.sleep(10000);
                        System.out.println("esatdo:" +
                                           t.getState().toString());
                        if (t.getState() == Thread.State.BLOCKED ||
                            t.getState() == Thread.State.TIMED_WAITING) {
                            t.stop();
                            ocupadoLabel.setVisible(false);
                            ocupadoLabel.setBusy(false);
                            estadoConexionLabel.setText("Conexión Fallida");
                            estado = ESTADO_FINALIZAR_CONEXION_ELM;
                            atrasBoton.setEnabled(true);
                            adelanteBoton.setEnabled(false);
                            adelanteBoton.setText("Finalizar");
                            cancelarBoton.setEnabled(true);
                            
                        }
                        //        JOptionPane.showMessageDialog(null, "tube que pitiarme al hilo, estaba bloeuqeado");
                    } catch (InterruptedException e) {
                    }
                }
            };
            v.start();
            
            break;
        case ESTADO_FINALIZAR_CONEXION_ELM:
            this.setVisible(false);
            break;

        }
        panelCentral.updateUI();
    }

    private void ayudaBoton_actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(this, Traductor.getDes("ayudaConexion"));
    }

    private void atrasBoton_actionPerformed(ActionEvent e) {
        switch (estado) {
        case ESTADO_MOSTRANDO_TIPO_CONEXION:
            // no deberia suceder
            atrasBoton.setEnabled(false);
           
            break;
        case ESTADO_BUSCANDO:
            removerPaneles();
            panelCentral.add(panelOpciones, BorderLayout.CENTER);
            panelCentral.updateUI();
            estado = ESTADO_MOSTRANDO_TIPO_CONEXION;
            //adelanteBoton.setText(Traductor.getDes("finalizar"));
            adelanteBoton.setEnabled(true);
            adelanteBoton.setText("Adelante >>");
            atrasBoton.setEnabled(false);
            cancelarBoton.setEnabled(true);
            dispositivosLabel.setForeground(Color.black);
            tipoConexionLabel.setForeground(Color.red);

            
            break;

        case ESTADO_FINALIZAR_CONEXION:
            removerPaneles();
            if (tipoConexion == this.TIPO_CONEXION_SERIAL) {
                panelCentral.add(conexionSerialPanel, BorderLayout.CENTER);
            } else {
                panelCentral.add(conexionBluetoothPanel, BorderLayout.CENTER);
            }
            panelCentral.updateUI();
            adelanteBoton.setEnabled(true);
            adelanteBoton.setText("Conectar");
            atrasBoton.setEnabled(true);
            cancelarBoton.setEnabled(true);
            estadoConexionFinalLabel.setForeground(Color.black);
            dispositivosLabel.setForeground(Color.red);

            

            estado = ESTADO_BUSCANDO;
            break;
        case ESTADO_MOSTRANDO_TIPO_CONEXION_ELM:
            tipoConexionLabel2face.setForeground(Color.black);
            estadoConexionFinalLabel.setForeground(Color.red);

           
            estado = ESTADO_FINALIZAR_CONEXION;

            break;
        case ESTADO_FINALIZAR_CONEXION_ELM:
            removerPaneles();
            atrasBoton.setEnabled(false);
            adelanteBoton.setText("Conectar");
            adelanteBoton.setEnabled(true);
            cancelarBoton.setEnabled(true);
            estadoConexion2final.setForeground(Color.black);
            tipoConexionLabel2face.setForeground(Color.red);

            estado = ESTADO_MOSTRANDO_TIPO_CONEXION_ELM;
            panelCentral.add(tiposProtocolosPanel, BorderLayout.CENTER);
            panelCentral.updateUI();
            
            break;


        }

    }

    private void cancelarBoton_actionPerformed(ActionEvent e) {
        if (tipoConexion == this.TIPO_CONEXION_SERIAL) {
            if (t == null || !t.isAlive())
                this.setVisible(false);
            else
                JOptionPane.showMessageDialog(this,
                                              "Lo sentimos, No se puede cancelar la operación en curso, por favor espere");
        } else {
            try {
                conexionBluetoothPanel.cacelarConexion(0);
                this.setVisible(false);
            } catch (ConexionException f) {
                log(f.toString());
            }
        }

        try {
            ControladorVista.getInstancia().cerrarConexion();
        } catch (ConexionException f) {
        }
        control.habilitarBotonesConectarDesconectar(true);
    }

    private void removerPaneles() {
        panelCentral.remove(panelConectando);
        panelCentral.remove(panelOpciones);
        panelCentral.remove(conexionBluetoothPanel);
        panelCentral.remove(conexionSerialPanel);
        panelCentral.remove(tiposProtocolosPanel);
    }

  
    public boolean conectarSerial() {

        t = new Thread() {
                public void run() {
                    try {
                        ocupadoLabel.setBusy(true);
                        panelCentral.setEnabled(false);
                        atrasBoton.setEnabled(false);
                        adelanteBoton.setEnabled(false);
                        ControladorVista controlador = ControladorVista.getInstancia();
                        String puerto =
                            conexionSerialPanel.getPuertoSeleccionado();
                        String velocidad =
                            conexionSerialPanel.getVelocidadSeleccionada();
                        controlador.abrirConexion( puerto,9600);
                        log("Conectando al:" + puerto);

                        // para conectar con el elm

                        estadoConexionLabel.setText("Conexión Exitosa");
                        cancelarBoton.setEnabled(false);
                        atrasBoton.setEnabled(false);
                        adelanteBoton.setEnabled(true);
                        control.habilitarBotonesConectarDesconectar(false);
                    } catch (NullPointerException ee) {
                        // TODO: Add catch code
                        log(ee.toString());
                        ee.printStackTrace();
                        logShow(ee);
                    } catch (ConexionException f) {
                        log(f.toString());
                        f.printStackTrace();
                        logShow(f);
                        estadoConexionLabel.setText("Conexión fallida");
                        atrasBoton.setEnabled(true);
                        cancelarBoton.setEnabled(true);
                    } catch (MuchosListenerException f) {
                        log(f.toString());
                        f.printStackTrace();
                        logShow(f);
                        estadoConexionLabel.setText("Conexión fallida");
                    } catch (OperacionNoSoportadaException f) {
                        log(f.toString());
                        f.printStackTrace();
                        logShow(f);
                        estadoConexionLabel.setText("Conexión fallida");
                    } finally {
                        //AsistenteConexionPanel.this.panelCentral.setVisible(true);
                        panelCentral.setEnabled(true);
                        ocupadoLabel.setBusy(false);
                        ocupadoLabel.setVisible(false);

                    }

                }


            };
        t.start();
        
       

        return true;

        // controlador.abrirConexion();
    }

    private void log(String dato) {
        ConsolaPanel.getInstancia().log("AsistenteConexionPanel", dato);
    }

    private void logShow(Exception ee) {
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

    }

    private void conectarBluetooth() {
        ocupadoLabel.setBusy(true);
        ocupadoLabel.setVisible(true);
        panelCentral.setEnabled(false);
        atrasBoton.setEnabled(false);
        adelanteBoton.setEnabled(false);
        try {
            conexionBluetoothPanel.conectarBluetoothConParametrosAutoseleccionados();
    } catch (ConexionException e) {
            // TODO: Add catch code
            estadoConexionLabel.setText("Conexión fallida");
            panelCentral.setEnabled(true);
            ocupadoLabel.setBusy(false);
            ocupadoLabel.setVisible(false);
            atrasBoton.setEnabled(true);
            adelanteBoton.setEnabled(false);


            // log("algo pasa");
            log(e.toString());
            e.printStackTrace();
            logShow(e);

        }
    }

    public void notificarConexionExitosaBluetooth(boolean exito) {
        panelCentral.setEnabled(true);
        ocupadoLabel.setBusy(false);
        ocupadoLabel.setVisible(false);

        if (exito) {
            estadoConexionLabel.setText("Conexión Exitosa");
            control.habilitarBotonesConectarDesconectar(false);

            atrasBoton.setEnabled(false);
            adelanteBoton.setEnabled(true);
            cancelarBoton.setEnabled(false);

        } else {
            estadoConexionLabel.setText("Conexión Fallida");
            control.habilitarBotonesConectarDesconectar(true);
            atrasBoton.setEnabled(true);
            adelanteBoton.setEnabled(false);
            cancelarBoton.setEnabled(true);

        }
    }


    public void detenerMuestreo(boolean detener) {
        ControladorVista.getInstancia().cancelarEscrituraLectura();
    }

    public void tratarRespuesta(RespuestaOBD r) {
             // esto debería asegurar la conexión con el automovil
            System.out.println("conectado elm");
            RespuestaOBD respuesta=r;
           // if(respuesta instanceof MonitoreoPID){
            
            ocupadoLabel.setVisible(false);
            ocupadoLabel.setBusy(false);
            estadoConexionLabel.setText("Conexión exitosa");
            estado = ESTADO_FINALIZAR_CONEXION_ELM;
            atrasBoton.setEnabled(false);
            adelanteBoton.setEnabled(true);
            adelanteBoton.setText("Finalizar");
            cancelarBoton.setEnabled(false);
          //  }
        }

    private void this_windowClosing(WindowEvent e) {
        
        // cancelar la conexion
    }
}

