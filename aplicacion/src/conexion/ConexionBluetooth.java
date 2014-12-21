package conexion;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStream;

import java.io.InputStreamReader;
import java.io.OutputStream;


import java.util.Vector;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

import javax.bluetooth.UUID;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;


public class ConexionBluetooth extends Conexion implements BluetoothPortEventListener {
    // protected static UUID _UUID = new UUID(0x1101)UUID = new UUID("0003", true); // serial port profile
    // tipo de UUID para el bluetooth, representa una conexion serial.
    protected static UUID _UUID = new UUID("0003", true);
     protected static final int OPCIONES_CONEXION =
        ServiceRecord.NOAUTHENTICATE_NOENCRYPT;
    protected static final int TOKEN_PARADA = 255;

    protected String nombreServicio;
    private OutputStream os; //salida
    private InputStream is; //entrada
    private InputStreamReader isr; // para mayores prestaciones
     private StreamConnection stream = null;
    private boolean abierto = false, despertado = false, disponible = true;
  

    /**
     *
     */
    private ManejadorEntradaBluetooth mb;
    private boolean bloqueante;


    public ConexionBluetooth(String nombreServicio) {
        super();
        this.nombreServicio = nombreServicio;
       
    }

    /**
     * método estatico que busca por todos los dispositivos bluetooth que hay en el entorno
     * @param responder
     * @throws ConexionException
     */
    public static void listarDispositivos(DiscoveryListener responder) throws ConexionException {
        DiscoveryAgent agente = null;
        try {
            agente = LocalDevice.getLocalDevice().getDiscoveryAgent();
        } catch (Exception e) {
            throw new ConexionException(ConexionException.NO_AGENTE);
        }
        try {
            agente.startInquiry(DiscoveryAgent.GIAC, responder);
        } catch (Exception e) {
            throw new ConexionException(ConexionException.NO_INICIAR_BUSQUEDA);
        }
    }

    /**
     * cancela la búsqueda de dispositivos
     * @param responder
     * @throws ConexionException
     */
    public static void cancelarListarDispositivos(DiscoveryListener responder) throws ConexionException {
        DiscoveryAgent agente = null;
        try {
            agente = LocalDevice.getLocalDevice().getDiscoveryAgent();
        } catch (Exception e) {
            throw new ConexionException(ConexionException.NO_AGENTE);
        }
        
        agente.cancelInquiry(responder);
    }

    /**
     *lista los dispositivos que estan incluidos en el pc como conocidos
     * @return  RemoteDevice[]
     * @throws ConexionException
     */
    public static RemoteDevice[] listarDispositivosConocidos() throws ConexionException {
        DiscoveryAgent agente = null;
        try {
            agente = LocalDevice.getLocalDevice().getDiscoveryAgent();
        } catch (Exception e) {
            throw new ConexionException(ConexionException.NO_AGENTE);
        }
        try {
            return agente.retrieveDevices(DiscoveryAgent.PREKNOWN);
        } catch (Exception e) {
            throw new ConexionException(ConexionException.NO_INICIAR_BUSQUEDA);
        }
    }

    /**
     * busca los servicios que brinda cierto dispositivo
     * @param device
     * @param manejador
     * @throws ConexionException
     */
    public static void buscarServicios(RemoteDevice device,
                                       DiscoveryListener manejador) throws ConexionException {
        DiscoveryAgent agente = null;
        try {
            agente = LocalDevice.getLocalDevice().getDiscoveryAgent();
        } catch (Exception e) {
            throw new ConexionException(ConexionException.NO_AGENTE);
        }
        try {
            UUID uuids[] = new UUID[] { _UUID };
            agente.searchServices(null, uuids, device, manejador);
        } catch (Exception e) {
            throw new ConexionException(ConexionException.NO_INICIAR_BUSQUEDA_SERVICIO);
        }
    }
    
   
    /**
     * abre una conexion con el dispositivo bluetooth
     * @throws IOException
     * @throws MuchosListenerException
     * @throws OperacionNoSoportadaException
     */
    public void abrirConexion() throws  ConexionException {

        try {
            stream = (StreamConnection)Connector.open(nombreServicio);
        } catch (IOException e) {
            throw new  ConexionException( ConexionException.NO_ABRIR_PUERTO_SERIAL);
        }
        try {
            is = stream.openInputStream();
            os = stream.openOutputStream();
        } catch (IOException e) {
            throw new  ConexionException( ConexionException.NO_ABRIR_STREAM_PUERTO_SERIAL);
        }
       
        isr = new InputStreamReader(is);
        mb = new ManejadorEntradaBluetooth(isr);
        try {
            mb.addBluetoothPortEventListener(this);
        } catch (MuchosListenerException e) {
            throw new ConexionException(ConexionException.NO_MAS_LISTENER);
        } catch (OperacionNoSoportadaException e) {
            throw new ConexionException(ConexionException.NO_SOPORTADA);         
        }
        mb.start();
        abierto = true;

    }

    public void cerrarConexion() throws ConexionException {
        mb.stop();
        try {
            os.close();
            isr.close();
            stream.close();
            stream=null;
            isr=null;
            abierto=false;
        } catch (IOException e) {
            throw new ConexionException(ConexionException.NO_CERRAR_PUERTO);
        }
        
    }

     

    public synchronized void bluetoothEvent(BluetoothPortEvent e) {
        // System.out.println("leer dato y poner en el buffer");
        StringBuffer bufferTemp = new StringBuffer();
        int nuevoDato = 0;
        mb.suspender(); // suspende la ejecución del hilo lector
        boolean b;
        do {
            try {

                nuevoDato = isr.read();
                if (((char)nuevoDato) == '>') {
                    bufferTemp.append((char)nuevoDato);
                    break;
                }
                if ('\r' == (char)nuevoDato) {
                    bufferTemp.append('\n');
                } else {
                    if (nuevoDato != -1)
                    bufferTemp.append((char)nuevoDato);
                     
                }
                //Thread.sleep(15);
            } catch (IOException f) {
                System.out.println(f.toString());
            } /* catch (InterruptedException f) {
            } */

           /*  try {
                b = isr.ready();
            } catch (IOException f) {
                break;
            } */

        } while (true);
        mb.despertar();
        buffer.escribirBufferEntrada(bufferTemp.toString());
    }

    public static void cancelarGetServiciosBluetooth(int i) throws ConexionException {
        DiscoveryAgent agente = null;
        try {
            agente = LocalDevice.getLocalDevice().getDiscoveryAgent();
        } catch (Exception e) {
            throw new ConexionException(ConexionException.NO_AGENTE);
        }
        
        agente.cancelServiceSearch(i);
    }

    public void cancelarEscrituraLectura() {
        if (hilo.isAlive())
            hilo.stop();
        hilo = null;
        disponible = true;
    }

    public synchronized void escribirPuerto(String datos, boolean bloqueante)throws IOException,
                                                          ConexionException,
                                                          TimeOutException {

        if (disponible) {
            os.write(datos.getBytes());

            disponible = false;
            despertado = false;
            this.bloqueante = bloqueante;
            if (bloqueante) {
                try {
                    //System.out.println("wait--------------------------------------------");
                    hilo = Thread.currentThread();
                    this.wait(15000); // no deberia pasar
                    if (!despertado) {
                        disponible = true;
                        throw new TimeOutException("SIN RESPUESTA- time out agotado");
                    }

                } catch (InterruptedException e) {
                    throw new TimeOutException("SIN RESPUESTA- time out agotado");
                } finally {
                    disponible = true;
                    hilo = null;
                    this.notify();
                }
            } else {
                disponible = true;

            }
        } else
            throw new ConexionException(ConexionException.MULTIPLES_LECTORES);


    }
}
