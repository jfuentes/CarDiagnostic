package vista.asistenteConexion;


import conexion.BufferEvent;
import conexion.BufferEventListener;
import conexion.ConexionException;

import conexion.MuchosListenerException;
import conexion.OperacionNoSoportadaException;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;

import java.util.HashMap;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;

import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import oracle.jdeveloper.layout.BoxLayout2;
import oracle.jdeveloper.layout.OverlayLayout2;
import oracle.jdeveloper.layout.VerticalFlowLayout;

import util.JRadioButtonGroup;
import util.Traductor;

import vista.ConsolaPanel;
import vista.ControladorVista;


public class PanelConexionBluetooth extends JPanel implements DiscoveryListener {
    private JPanel jPanel2 = new JPanel();
    private JPanel jPanel4 = new JPanel();
    private JPanel jPanel5 = new JPanel();
    private JButton buscarTodosBluetoothBoton = new JButton();
    private JButton buscarConocidosBluetoothBoton = new JButton();
    private BorderLayout borderLayout1 = new BorderLayout();
    private BorderLayout borderLayout2 = new BorderLayout();
    private FlowLayout flowLayout1 = new FlowLayout();


    // private RemoteDevice remoteDevice;
    private JRadioButtonGroup grupoBluetoothsRBoton = new JRadioButtonGroup();
    private JRadioButtonGroup grupoServiciosRBoton = new JRadioButtonGroup();

    private static final PanelConexionBluetooth instancia =
        new PanelConexionBluetooth(null);
    private JScrollPane jScrollPane1 = new JScrollPane();
    private VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
    private JPanel panelBluetooths = new JPanel();
    private VerticalFlowLayout verticalFlowLayout3 = new VerticalFlowLayout();


    private Cursor cursorEsperar = new Cursor(Cursor.WAIT_CURSOR);
    private Cursor cursorDefecto = new Cursor(Cursor.DEFAULT_CURSOR);
    private BoxLayout2 boxLayout21 = new BoxLayout2();


    //-------------------------------------------------------------------------
    private ServiceRecord[] serviciosEncontrados;
    private HashMap<JRadioButton, RemoteDevice> dispositivos =
        new HashMap<JRadioButton, RemoteDevice>();
    private JButton cancelarBoton = new JButton();
    AsistenteConexionPanel control;
    public boolean cancelado = false;

    public PanelConexionBluetooth(AsistenteConexionPanel ac) {
        try {
            jbInit();
            control = ac;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PanelConexionBluetooth getInstancia() {
        return instancia;
    }

    private void jbInit() throws Exception {
        this.setLayout(borderLayout2);
        this.setSize(new Dimension(602, 373));
        this.setBorder(BorderFactory.createTitledBorder("Dispositivos Bluetooth"));
        jPanel2.setLayout(boxLayout21);
          jPanel4.setLayout(borderLayout1);
        jPanel5.setLayout(flowLayout1);
       
        buscarTodosBluetoothBoton.setText("Listar Todos");
        buscarTodosBluetoothBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    buscarTodosBluetoothBoton_actionPerformed(e);
                }
            });
        buscarConocidosBluetoothBoton.setText("Listar Conocidos");
        buscarConocidosBluetoothBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    buscarConocidosBluetoothBoton_actionPerformed(e);
                }
            });
         panelBluetooths.setLayout(verticalFlowLayout3);
         cancelarBoton.setText("Cancelar");
        cancelarBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cancelarBoton_actionPerformed(e);
                }
            });
        jPanel5.add(buscarTodosBluetoothBoton, null);
        jPanel5.add(buscarConocidosBluetoothBoton, null);
        jPanel5.add(cancelarBoton, null);
        jPanel4.add(jPanel5, BorderLayout.SOUTH);
        jScrollPane1.getViewport().add(panelBluetooths, null);
        jPanel4.add(jScrollPane1, BorderLayout.CENTER);
        jPanel2.add(jPanel4, null);
        this.add(jPanel2, BorderLayout.CENTER);
    }

    private void buscarTodosBluetoothBoton_actionPerformed(ActionEvent e) {
        this.setCursor(cursorEsperar);
        panelBluetooths.removeAll();
        grupoBluetoothsRBoton.removeAll();
        buscarTodosBluetoothBoton.setEnabled(false);
        try {
            log("buscando bluetooth...");
            ControladorVista.getInstancia().getDispositivosBluetoothEntorno(this);
        } catch (ConexionException f) {
            log(f.toString());
            f.printStackTrace();
        }
    }

    public void deviceDiscovered(final RemoteDevice remoteDevice,
                                 DeviceClass deviceClass) {
        String dispositivo = "";
        String nombre = "";

        try {
            nombre = remoteDevice.getFriendlyName(false);
            if (nombre.equalsIgnoreCase(""))
                nombre = "SIN NOMBRE";
        } catch (IOException e) {
            nombre = "SIN NOMBRE";
            log(e.toString());
            e.printStackTrace();
        }
        try {
            dispositivo += remoteDevice.getFriendlyName(false);
        } catch (IOException e) {
            log(e.toString());
            e.printStackTrace();
        }

        dispositivo += " - 0x" + remoteDevice.getBluetoothAddress();
        log("dispositivo encontrado :" + dispositivo);
        JRadioButton pRB = new JRadioButton();
        pRB.setText(nombre);
        panelBluetooths.add(pRB, null);
        grupoBluetoothsRBoton.add(pRB);
        dispositivos.put(pRB, remoteDevice);
        panelBluetooths.updateUI();
    }

    public void servicesDiscovered(int i, ServiceRecord[] serviceRecords) {
        log("Servicio encontrado.");
        for (ServiceRecord sr : serviceRecords) {
            log(sr.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT,
                                    false));
        }
        serviciosEncontrados = serviceRecords;

    }

    public void serviceSearchCompleted(int transID, int respCode) {
        String msg = null;
        if (!cancelado) {
            switch (respCode) {
            case SERVICE_SEARCH_COMPLETED:
                msg = "la busqueda de servicio termino normalmente";
                ControladorVista controlador = ControladorVista.getInstancia();

                try {
                    // JRadioButton seleccionado=grupoServiciosRBoton.getSelectionRadioButton();
                    String puerto = "";
                    if (serviciosEncontrados.length > 0) {
                        puerto =
                                serviciosEncontrados[0].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT,
                                                                         false);

                    } else {
                        JOptionPane.showMessageDialog(this,
                                                      "No se puede conectar al bluetooth");
                        control.notificarConexionExitosaBluetooth(false);
                        return;
                    }
                    /* String velocidad =
                    grupoVelocidadRBoton.getSelectionRadioButton().getText();*/
                    controlador.abrirConexion( puerto);
                    log("servicio seleccionado:" + puerto);
                } catch (NullPointerException ee) {
                    // TODO: Add catch code
                    log(ee.toString());
                    ee.printStackTrace();
                    JOptionPane.showMessageDialog(this, ee.toString());
                    control.notificarConexionExitosaBluetooth(false);
                } catch (ConexionException f) {
                    log(f.toString());
                    f.printStackTrace();
                    JOptionPane.showMessageDialog(this, f.toString());
                    control.notificarConexionExitosaBluetooth(false);
                } catch (MuchosListenerException f) {
                    log(f.toString());
                    f.printStackTrace();
                    JOptionPane.showMessageDialog(this, f.toString());
                    control.notificarConexionExitosaBluetooth(false);
                } catch (OperacionNoSoportadaException f) {
                    log(f.toString());
                    f.printStackTrace();
                    JOptionPane.showMessageDialog(this, f.toString());
                    control.notificarConexionExitosaBluetooth(false);
                } finally {
                    this.setCursor(cursorDefecto);

                }
                control.notificarConexionExitosaBluetooth(true);
                break;
            case SERVICE_SEARCH_TERMINATED:
                msg = "concelado por DiscoveryAgent.cancelServiceSearch()";
                JOptionPane.showMessageDialog(this, "cancelado");
                control.notificarConexionExitosaBluetooth(false);
                break;
            case SERVICE_SEARCH_ERROR:
                msg =
"un error a ocurrido mientras se procesaba el requerimiento";
                JOptionPane.showMessageDialog(this,
                                              "error no se puede conectar");
                control.notificarConexionExitosaBluetooth(false);
                break;
            case SERVICE_SEARCH_NO_RECORDS:
                msg = "no se encontraron servicios almacenados";
                JOptionPane.showMessageDialog(this,
                                              "no se encontraron servicios");
                control.notificarConexionExitosaBluetooth(false);
                break;
            case SERVICE_SEARCH_DEVICE_NOT_REACHABLE:
                msg = "no se puede establecer comunicacion con el dispositivo";
                JOptionPane.showMessageDialog(this,
                                              "no se puede establecer comunicacion");
                control.notificarConexionExitosaBluetooth(false);
                break;
            }
            log(msg);
        }


    }

    public void inquiryCompleted(int i) {
        this.setCursor(cursorDefecto);
        buscarTodosBluetoothBoton.setEnabled(true);

    }

    public void log(String dato) {
        ConsolaPanel.getInstancia().log("PanelConexionBluetooth:", dato);
    }

    public void conectarBluetoothConParametrosAutoseleccionados() throws ConexionException {
        log("conectar----");
        //this.setCursor(cursorEsperar);
        JRadioButton rd = grupoBluetoothsRBoton.getSelectionRadioButton();
        RemoteDevice remoteDevice = dispositivos.get(rd);
        ControladorVista.getInstancia().buscarServiciosDispositivoBluetooth(remoteDevice,
                                                                            this);

    }

    public void bufferEntradaEscrito(BufferEvent e) {
    }

    private void buscarConocidosBluetoothBoton_actionPerformed(ActionEvent e) {
        panelBluetooths.removeAll();
        grupoBluetoothsRBoton.removeAll();
        RemoteDevice[] rd;
        try {
            rd = ControladorVista.getInstancia().getDispositivosBluetoothConocidos();
            for (RemoteDevice remo : rd) {
                String dispositivo = "";
                String nombre = "";
                try {
                    nombre = remo.getFriendlyName(false);
                } catch (IOException ee) {
                    log(ee.toString());
                    ee.printStackTrace();
                }
                try {
                    dispositivo += remo.getFriendlyName(false);
                } catch (IOException ee) {
                    log(ee.toString());
                    ee.printStackTrace();
                }

                dispositivo += " - 0x" + remo.getBluetoothAddress();
                log("dispositivo encontrado :" + dispositivo);
                JRadioButton pRB = new JRadioButton();
                pRB.setText(nombre);
                panelBluetooths.add(pRB, null);
                grupoBluetoothsRBoton.add(pRB);
                dispositivos.put(pRB, remo);

                /* pRB.addActionListener(new ActionListener(){

                        public void actionPerformed(ActionEvent e) {
                            try {
                                log("buscando servicios");
                                ControladorVista.getInstancia().buscarServiciosDispositivoBluetooth(remoteDevice, PanelConexionBluetooth.this);
                            } catch (ConexionException f) {
                                log(f.toString());
                                f.printStackTrace();
                            }
                        }
                    });
                */
                panelBluetooths.updateUI();
            }
        } catch (ConexionException f) {
            log(f.toString());
            f.printStackTrace();
        }
    }

    private void cancelarBoton_actionPerformed(ActionEvent e) {
        try {
            ControladorVista.getInstancia().cancelarGetDispositivosBluetoothEntorno(this);
        } catch (ConexionException f) {
            JOptionPane.showMessageDialog(this,
                                          "No es posble cancelar la búsqueda");
        }
    }
    /*  public RemoteDevice getsDispositivoSeleccionado(){
        JRadioButton rd=grupoBluetoothsRBoton.getSelectionRadioButton();
        RemoteDevice remoteDevice=dispositivos.get(rd);
        return remoteDevice;
    } */

    public void cacelarConexion(int i)throws ConexionException {
        if (i == 0)
            cancelado = true;
        else
            cancelado = false;
    }
}
