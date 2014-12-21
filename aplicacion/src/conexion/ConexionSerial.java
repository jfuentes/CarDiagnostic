package conexion;

import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.TooManyListenersException;

import javax.comm.CommPortIdentifier;
import javax.comm.CommPortOwnershipListener;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;

import modelo.ErrorDTC;

/**
 *  maneja una conexión serial.
 */

public class ConexionSerial extends Conexion implements SerialPortEventListener,
                                                        CommPortOwnershipListener {
    private CommPortIdentifier puertoId; // id del puerto
    private SerialPort puertoSerial; // puerto
    private OutputStream os; //salida
    private InputStream is; //entrada

    private boolean abierto = false, despertado = false, disponible = true;
    protected Parametros paramentrosConexion;
    private boolean bloqueante;
     

     
    private ConexionSerial() {
        super();
        //paramentrosConexion = new Parametros();
    }

    public ConexionSerial(String nombrePuerto, int velocidad,
                          int controlFlujoEntrada, int controlFlujoSalida,
                          int bitsDatos, int bitsParada, int paridad) {

        paramentrosConexion =
                new Parametros(nombrePuerto, velocidad, controlFlujoEntrada,
                               controlFlujoSalida, bitsDatos, bitsParada,
                               paridad);


    }

    public ConexionSerial(String nombrePuerto, int velocidad) {
        paramentrosConexion = new Parametros(nombrePuerto, velocidad);
    }

    /**
     *abre una conexión
     * @throws ConexionException
     */
    public void abrirConexion() throws ConexionException {
        // Obtain a CommPortIdentifier object for the port you want to abierto.
        try {
            puertoId =
                    CommPortIdentifier.getPortIdentifier(paramentrosConexion.getNombrePuerto());
        } catch (NoSuchPortException e) {
            throw new ConexionException(ConexionException.NO_OBTENER_ID_PUERTO);
        }

        try {
            System.out.println("dueño:"+puertoId.getCurrentOwner());
            puertoSerial = (SerialPort)puertoId.open("CarDiagnostic", 30);
        } catch (PortInUseException e) {
            throw new ConexionException(ConexionException.NO_ABRIR_PUERTO_SERIAL);

        }


        try {
            puertoSerial.setSerialPortParams(paramentrosConexion.getVelocidad(),
                                             SerialPort.DATABITS_8,
                                             SerialPort.STOPBITS_1,
                                             SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {
            //paramentrosConexion.setBaudRate(oldBaudRate);
            //paramentrosConexion.setDatabits(oldDatabits);
            //paramentrosConexion.setStopbits(oldStopbits);
            // paramentrosConexion.setParity(oldParity);
            puertoSerial.close();
            throw new ConexionException(ConexionException.NO_PARAMETROS_SOPORTADOS);
        }
        try {
            puertoSerial.setFlowControlMode(SerialPort.FLOWCONTROL_NONE |
                                            SerialPort.FLOWCONTROL_NONE);
        } catch (UnsupportedCommOperationException e) {
            throw new ConexionException(ConexionException.NO_SOPORTADO_cONTROL_FLUJO);
        }
        //setParametrosConexion();

        try {
            
            os = puertoSerial.getOutputStream();
            is = puertoSerial.getInputStream();
        } catch (IOException e) {
            puertoSerial.close();
            throw new ConexionException(ConexionException.NO_ABRIR_STREAM_PUERTO_SERIAL);
        }

        try {
            puertoSerial.addEventListener(this);
        } catch (TooManyListenersException e) {
            puertoSerial.close();
            throw new ConexionException(ConexionException.NO_MAS_LISTENER);
        }
        puertoSerial.notifyOnDataAvailable(true);
        puertoSerial.notifyOnBreakInterrupt(true);


        /*
         * OJO CON ESTO ----------------- PENDIENTE ------ REVISAR
         */

        try {
            puertoSerial.enableReceiveTimeout(80);
        } catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
        }

        puertoId.addPortOwnershipListener(this);
        abierto = true;
    }


    /**
     *cierra la conexión
     * @throws ConexionException
     */
    public   void cerrarConexion() throws ConexionException {
        // if el puerto ya esta cerrado
        if(hilo!=null){
            System.out.println("hilo estado +::::::::::::::::::::::::::::::::::::::::"+hilo.getState().toString());
               hilo.stop();
         }else{
            System.out.println("hilo estado +:::::::::::::::::::::::::::::::::::::NULL");

        }
        // asegurarse que el puerto tiene una referecia para evitar NPE.
        if (puertoSerial != null) {
            puertoSerial.removeEventListener();
            puertoId.removePortOwnershipListener(this);
            puertoSerial.sendBreak(10);
            puertoSerial.disableReceiveFraming();
            puertoSerial.disableReceiveTimeout();
            try {
                os.close();
                is.close();
                System.out.println("cerrrando puerto");
            } catch (IOException e) {
                throw new ConexionException(ConexionException.NO_CERRAR_PUERTO);
            }

            // cerrar
            puertoSerial.close();
            
        }  

        abierto = false;
    }

    public void enviarBreak() {
        puertoSerial.sendBreak(1000);
    }

    /**
   Reports the abierto status of the port.
   @return true if port is abierto, false if port is closed.
     */
    public boolean isAbierto() {
        return abierto;
    }

    /**
     * luego de ser lanzado el evento leee del puerto y lo deja en el buffer
     * @param serialPortEvent
     */
    public synchronized void serialEvent(SerialPortEvent serialPortEvent) {
      /*   System.out.println("leyendo puertp bajo nivel:" +
                           Thread.currentThread().getName());
        */
        StringBuffer bufferEntrada = new StringBuffer();
       int nuevoDato = 0;
        switch (serialPortEvent.getEventType()) {
        case SerialPortEvent.DATA_AVAILABLE:
            // leer hasta el -1
            do {
                try {
                    nuevoDato = is.read();
                    if (((char)nuevoDato) == '>') {
                        bufferEntrada.append((char)nuevoDato);
                        break;
                    }
               // System.out.print("dato leido"+nuevoDato);
                //    System.out.println(":" +(char) nuevoDato);
                    if ('\r' == (char)nuevoDato) {
                        bufferEntrada.append('\n');
                    } else {
                        if (nuevoDato != -1)
                        bufferEntrada.append((char)nuevoDato);

                    }

                } catch (IOException ex) {
                    System.out.println(ex);
                    return;
                }
            } while (true);

            break;

            // If break event append BREAK RECEIVED message.
        case SerialPortEvent.BI:

        }
        // System.out.println("leido:" + bufferEntrada.toString());
        //t.stop();


        buffer.escribirBufferEntrada(bufferEntrada.toString());
        disponible = true;
        despertado = true;
       
        this.notify();
        //System.out.println("notyfy--------------------------------------------");

        


    }

    public void ownershipChange(int type) {
        if (type == CommPortOwnershipListener.PORT_OWNERSHIP_REQUESTED) {
            //    PortRequestedDialog prd = new PortRequestedDialog(parent);
        }
    }

    /**
     * escribe en el puerto
     * @param datos
     * @throws IOException
     */
    public synchronized void escribirPuerto(String datos,boolean bloqueante) throws IOException,
                                                                 ConexionException,
                                                                       TimeOutException {

       
        if (disponible) {
           os.write(datos.getBytes());
                 
            disponible = false;
            despertado = false;
            this.bloqueante=bloqueante;
            if(bloqueante){
                try {
                    //System.out.println("wait--------------------------------------------");
                    hilo = Thread.currentThread();
                    this.wait(15000 ); // no deberia pasar
                    if (!despertado) {
                        disponible = true;
                        throw new TimeOutException("SIN RESPUESTA- time out agotado");
                    }
    
                } catch (InterruptedException e) {
                    throw new TimeOutException("SIN RESPUESTA- time out agotado");
                } finally {
                    disponible=true;
                    hilo=null;
                     this.notify();
                }
            }else{
                disponible = true;

            }
        } else
            throw new ConexionException(ConexionException.MULTIPLES_LECTORES);
        

    }

    /**
     * lista los puertos seriales disponibles
     * @return List con los CommPortIdentifier
     */
    public static List<CommPortIdentifier> getPuertosSerialSistema() {
        System.out.println("listando los puertos serial del sistema");
        List<CommPortIdentifier> lista = new LinkedList<CommPortIdentifier>();
        Enumeration en;
        CommPortIdentifier puertoId;

        en = CommPortIdentifier.getPortIdentifiers();
        if (!en.hasMoreElements()) {
            return lista;
        }
        while (en.hasMoreElements()) {
            puertoId = (CommPortIdentifier)en.nextElement();
              System.out.println("puerto encontrado:"+ puertoId.getName());
            if (puertoId.getPortType() == CommPortIdentifier.PORT_SERIAL && !puertoId.isCurrentlyOwned()) {
                lista.add(puertoId);

            }
        }
        return lista;
    }
     public synchronized void cancelarEscrituraLectura() {
        //if (hilo!=null && (hilo.getState()==Thread.State.BLOCKED || hilo.getState()==Thread.State.RUNNABLE || hilo.getState()==Thread.State.WAITING || hilo.getState()==Thread.State.TIMED_WAITING))
         if(hilo!=null){
         hilo.stop();
           
        }
        
        hilo = null;
        disponible = true;
    }

    
}
