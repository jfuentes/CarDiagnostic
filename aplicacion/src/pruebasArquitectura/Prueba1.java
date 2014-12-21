package pruebasArquitectura;

import conexion.BufferEvent;
import conexion.BufferEventListener;

import conexion.Conexion;
import conexion.ConexionBluetooth;
import conexion.ConexionException;
import conexion.ConexionSerial;

import conexion.MuchosListenerException;
import conexion.OperacionNoSoportadaException;

import java.awt.Dimension;

import java.awt.Rectangle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.MouseAdapter;

import java.awt.event.MouseEvent;

import java.io.IOException;

import java.util.List;

import java.util.StringTokenizer;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;

import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

import javax.comm.CommPortIdentifier;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Prueba1 extends JFrame implements BufferEventListener,
                                               DiscoveryListener {
    private JTextArea consola = new JTextArea();
    private JButton listarPuertosBoton = new JButton();
    private JButton listarBluetoothBoton = new JButton();
    private JEditorPane lineaComando = new JEditorPane();
    private JButton enviarBoton = new JButton();
    private JButton conectarBoton = new JButton();
    private JComboBox puertos = new JComboBox();



    // para la conexion
    Conexion co;
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JScrollPane jScrollPane2 = new JScrollPane();
    RemoteDevice remoteDevice;
    private JComboBox servicios = new JComboBox();
    private JButton conectarServiciosBoton = new JButton();
    private JButton listarBluetoothConocidosBoton = new JButton();
    private JButton cancelarBusquedaBoton = new JButton();
    private JButton jButton1 = new JButton();

    public Prueba1() {
        try {
            jbInit();
           /*  String s;
            s="\n 41 0C 3E 3F \n >";
            //s="NO DATA";
            log(s);
            s=s.trim();
            log(s);
            s=s.replace("\n","");
            s=s.replace(">","");
            if(s.matches("4[0-9][A-Z|a-z|0-9|>| |\n]*"))log("valida");
            s=s.replace(" ", "");
            s=s.replaceFirst("4[0-9]{1}[0-9]{1}[0-9|a-z|A-Z]{1}", "");
            log(s); */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(null);
        this.setSize(new Dimension(699, 608));
        listarPuertosBoton.setText("listar puertos");
        listarPuertosBoton.setBounds(new Rectangle(510, 20, 160, 20));
        listarPuertosBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    listarPuertosBoton_actionPerformed(e);
                }
            });
        listarBluetoothBoton.setText("listar Bluetooth");
        listarBluetoothBoton.setBounds(new Rectangle(510, 60, 165, 20));
        listarBluetoothBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    listarBluetoothBoton_actionPerformed(e);
                }
            });
        lineaComando.setBounds(new Rectangle(10, 515, 520, 20));
        enviarBoton.setText("jButton3");
        enviarBoton.setBounds(new Rectangle(590, 220, 75, 21));
        enviarBoton.setText("enviar");
        enviarBoton.setBounds(new Rectangle(560, 515, 75, 21));
        enviarBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    enviarBoton_actionPerformed(e);
                }
            });
        conectarBoton.setText("conectar");
        conectarBoton.setBounds(new Rectangle(235, 415, 115, 20));
        conectarBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    conectarBoton_actionPerformed(e);
                }
            });
        puertos.setBounds(new Rectangle(30, 415, 180, 20));
        jScrollPane2.setBounds(new Rectangle(25, 15, 470, 380));
        servicios.setBounds(new Rectangle(30, 455, 180, 20));
        conectarServiciosBoton.setText("conectar");
        conectarServiciosBoton.setBounds(new Rectangle(235, 455, 115, 20));
        conectarServiciosBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    conectarServiciosBoton_actionPerformed(e);
                }
            });
        listarBluetoothConocidosBoton.setText("listar Bluetooth conocidos");
        listarBluetoothConocidosBoton.setBounds(new Rectangle(510, 95, 180,
                                                              20));
        listarBluetoothConocidosBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    listarBluetoothConocidosBoton_actionPerformed(e);
                }
            });
        cancelarBusquedaBoton.setText("cancelar Busqueda");
        cancelarBusquedaBoton.setBounds(new Rectangle(510, 130, 155, 35));
        cancelarBusquedaBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cancelarBusquedaBoton_actionPerformed(e);
                }
            });
        jButton1.setText("ConsultarRPM");
        jButton1.setBounds(new Rectangle(560, 550, 115, 20));
        jButton1.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    jButton1_mousePressed(e);
                }
            });
        jScrollPane2.getViewport().add(consola, null);
        this.getContentPane().add(jButton1, null);
        this.getContentPane().add(cancelarBusquedaBoton, null);
        this.getContentPane().add(listarBluetoothConocidosBoton, null);
        this.getContentPane().add(conectarServiciosBoton, null);
        this.getContentPane().add(servicios, null);
        this.getContentPane().add(jScrollPane2, null);
        this.getContentPane().add(puertos, null);
        this.getContentPane().add(conectarBoton, null);
        this.getContentPane().add(enviarBoton, null);
        this.getContentPane().add(lineaComando, null);
        this.getContentPane().add(listarBluetoothBoton, null);
        this.getContentPane().add(listarPuertosBoton, null);
        this.setVisible(true);
        servicios.addItem("btspp://00066601E6C1:1;authenticate=false;encrypt=false;master=false");
    }

    private void listarPuertosBoton_actionPerformed(ActionEvent e) {
        // listar los puertos
        List<CommPortIdentifier> puertos =
            ConexionSerial.getPuertosSerialSistema();
        for (CommPortIdentifier puerto : puertos) {
            consola.append("puerto :" + puerto.getName() + "\n");
            this.puertos.addItem(puerto.getName());
        }
    }

    private void listarBluetoothBoton_actionPerformed(ActionEvent e) {
        // listar dispositivos bluetooth

        try {
            ConexionBluetooth.listarDispositivos(this);
            log("buscando dispositivos ........");
        } catch (ConexionException f) {
            consola.append(f.toString() + "\n");
        }
    }

    private void enviarBoton_actionPerformed(ActionEvent e) {
        // enviar comando
        String str = lineaComando.getText();
       /*  if (co != null) {
            try {
                co.escribirBuffer(str + "\r");
            } catch (IOException f) {
                consola.append(f.toString());
            }
        } */

    }

    public void bufferSalidaEscrito(BufferEvent e) {
        consola.append("buffer de salida escrito con:");
       /*  if (co != null) {
            consola.append(co.getBufferConexion().leerBufferSalidaSB() + "\n");
        } */
    }

    public void bufferEntradaEscrito(BufferEvent e) {
        consola.append("buffer de entrada escrito con:");
        if (co != null) {
            consola.append(co.getBufferConexion().leerBufferEntrada() + "\n");
        }
    }

    public void deviceDiscovered(RemoteDevice remoteDevice,
                                 DeviceClass deviceClass) {
        String dispositivo = "";
        String nombre = "";
        try {
            nombre = remoteDevice.getFriendlyName(false);
        } catch (IOException e) {
            log(e.toString());
        }
        if (this.remoteDevice == null &&
            nombre.equalsIgnoreCase("FireFly-E6C!1")) {
            this.remoteDevice = remoteDevice;
        }
        try {
            dispositivo += remoteDevice.getFriendlyName(false);
        } catch (IOException e) {
            consola.append(e + "\n");
        }

        dispositivo += " - 0x" + remoteDevice.getBluetoothAddress();
        consola.append("dispositivo encontrado :" + dispositivo + "\n");
    }

    private void log(String s) {
        consola.append(s + "\n");
    }

    public void servicesDiscovered(int i, ServiceRecord[] serviceRecords) {
        log("Servicio encontrado.");
        for (int j = 0; j < serviceRecords.length; j++) {
            ServiceRecord rec = serviceRecords[j];
            String url =
                rec.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT,
                                     false);
            servicios.addItem(url);
            log(url);

            //handleConnection(url);
        }
    }

    public void serviceSearchCompleted(int transID, int respCode) {
        String msg = null;
        switch (respCode) {
        case SERVICE_SEARCH_COMPLETED:
            msg = "la busqueda de servicio termino normalmente";
            break;
        case SERVICE_SEARCH_TERMINATED:
            msg = "concelado por DiscoveryAgent.cancelServiceSearch()";
            break;
        case SERVICE_SEARCH_ERROR:
            msg = "un error a ocurrido mientras se procesaba el requerimiento";
            break;
        case SERVICE_SEARCH_NO_RECORDS:
            msg = "no se encontraron servicios almacenados";
            break;
        case SERVICE_SEARCH_DEVICE_NOT_REACHABLE:
            msg = "no se puede establecer comunicacion con el dispositivo";
            break;
        }
        log(msg);
    }

    public void inquiryCompleted(int i) {
        log("busqueda completada");
        log("buscando servicios");

        try {
            ConexionBluetooth.buscarServicios(remoteDevice, this);
        } catch (ConexionException e) {
            log("una exception" + e);
        }
    }

    public static void main(String[] args) {
        new Prueba1();
    }

    private void conectarBoton_actionPerformed(ActionEvent e) {
        // conectar con el puerto
        String puerto = (String)puertos.getItemAt(puertos.getSelectedIndex());
        co = new ConexionSerial(puerto, 9600);
        try {
            co.addBufferEventListener(this);
        } catch (MuchosListenerException f) {
            consola.append(f.toString() + "\n");
        } catch (OperacionNoSoportadaException f) {
            consola.append(f.toString() + "\n");
        }
        try {
            co.abrirConexion();
            consola.append("conexion abierta a:" + puerto + "\n");
        }   catch (ConexionException f) {
            log(f.toString());
        }

    }

    private void conectarServiciosBoton_actionPerformed(ActionEvent e) {
       co=new ConexionBluetooth((String)servicios.getSelectedItem());
        try {
            co.addBufferEventListener(this);
        } catch (MuchosListenerException f) {
            log(f.toString());
        } catch (OperacionNoSoportadaException f) {
            log(f.toString());
        }
        try {
            co.abrirConexion();
            log("conexion abierta a:" + (String)servicios.getSelectedItem());
        }    catch (ConexionException f) {
            log(f.toString());
        }
    }

    private void listarBluetoothConocidosBoton_actionPerformed(ActionEvent e) {
        try {
            log("buscando dispositivos ........");
          RemoteDevice[] r=  ConexionBluetooth.listarDispositivosConocidos();
            for (RemoteDevice rd : r) {
                try {
                    log(rd.getFriendlyName(false));
                } catch (IOException f) {
                    log(f.toString());
                }
            }
           
        } catch (ConexionException f) {
            consola.append(f.toString() + "\n");
        }
    }

    private void cancelarBusquedaBoton_actionPerformed(ActionEvent e) {
        try {
            ConexionBluetooth.cancelarListarDispositivos(this);
            log("busqueda concelada");
        } catch (ConexionException f) {
            log(f.toString());
        }
    }

    private void jButton1_mousePressed(MouseEvent e) {
        // enviar comando
       
      /*   if (co != null) {
            try {
                co.escribirBuffer( "\r");
            } catch (IOException f) {
                consola.append(f.toString());
            }
        } */
    }
}
