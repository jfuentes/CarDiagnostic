package vista;

import conexion.BufferEvent;
import conexion.BufferEventListener;
import conexion.ConexionBluetooth;
import conexion.ConexionException;
import conexion.ConexionSerial;
import conexion.MuchosListenerException;
import conexion.OperacionNoSoportadaException;

import conexion.TimeOutException;

import java.io.IOException;

import java.util.List;

import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;

import javax.comm.CommPortIdentifier;

import javax.swing.JOptionPane;

import modelo.ControladorModelo;
import modelo.RespuestaOBD;

public class ControladorVista implements   BufferEventListener{
    private static ControladorVista instancia;
    private static Manipulable interfaz;
    private ControladorVista() { 
        super();
    }
    
    
    public static ControladorVista getInstancia(){
        if(instancia==null) instancia=new ControladorVista();
        return instancia;
    }


    /*
     * servicios para el puerto serial
     */

    public List<CommPortIdentifier> getPuertosSerialSistema() {
        return ConexionSerial.getPuertosSerialSistema();
    }

    /**
     * abre una conexion serial
     * @param interesado
     * @param nombrePuerto
     * @param velocidad
     * @throws ConexionException
     * @throws MuchosListenerException
     * @throws OperacionNoSoportadaException
     */
    public void abrirConexion(String nombrePuerto,
                              int velocidad) throws ConexionException,
                                                    MuchosListenerException,
                                                    OperacionNoSoportadaException {
        ControladorModelo controladorModelo = ControladorModelo.getInstance();
        controladorModelo.abrirConexion(this, nombrePuerto, velocidad);
    }
   
    /**
     *abre una conexion bluetooth
     * @param interesado
     * @param url
     * @throws ConexionException
     * @throws MuchosListenerException
     * @throws OperacionNoSoportadaException
     */
    public void abrirConexion(String url) throws ConexionException,
                                                 MuchosListenerException,
                                                 OperacionNoSoportadaException {
        ControladorModelo.getInstance().abrirConexion(this, url);
    }
    private void cambiarBufferListener(BufferEventListener interesado) throws OperacionNoSoportadaException,
                                                                             ConexionException {
        ControladorModelo.getInstance().cambiarBufferListener(interesado);
            
    }

    public void cerrarConexion() throws ConexionException {
        ControladorModelo.getInstance().cerrarConexion();
    }

    public void escribirPuertoConexion(String datos) throws IOException,
                                                            ConexionException,
                                                            TimeOutException {
        try{
        ControladorModelo.getInstance().escribirPuertoConexion(datos,-1,true);
        } catch (IOException e) {
            //System.out.println("################# el manso error");
            e.printStackTrace();
            throw e;
        } catch (ConexionException e) {
           //System.out.println("################# el manso error Nop");
            e.printStackTrace();
            throw e;
        } catch (TimeOutException e) {
            
            //System.out.println("tiempo agotado");
            throw e;
        }
    }
     public void escribirPuertoConexion(String datos,int nFrame)   throws IOException,
                                                              ConexionException,
                                                          TimeOutException {

        //   System.out.println("numero de frame escrito------################:"+nFrame);
        try {
            ControladorModelo.getInstance().escribirPuertoConexion(datos,nFrame,true);
        } catch (IOException e) {
           //System.out.println("################# el manso error");
              throw e;
        } catch (ConexionException e) {
          // System.out.println("################# el manso error sip"+e.toString());
            throw e;
        }catch (TimeOutException e) {
          //  System.out.println("tiempo agotado");
            throw e;
        }
    } 
    public void escribirPuertoConexion(String datos,int nFrame,boolean bloqueante)   throws IOException,
                                                             ConexionException,
                                                                  TimeOutException {
        //   System.out.println("numero de frame escrito------################:"+nFrame);
        try {
            ControladorModelo.getInstance().escribirPuertoConexion(datos,
                                                                   nFrame,
                                                                   bloqueante);
        } catch (IOException e) {
           // System.out.println("################# el manso error");
            throw e;
        } catch (ConexionException e) {
           // System.out.println("################# el manso error sip");
            throw e;
        }catch (TimeOutException e) {
          //  System.out.println("tiempo agotado");
            throw e;
        }
    }

    public void getDispositivosBluetoothEntorno(DiscoveryListener interesado) throws ConexionException {
        ControladorModelo.getInstance().getDispositivosBluetoothEntorno(interesado);
    }
    public  RemoteDevice[] getDispositivosBluetoothConocidos() throws ConexionException {
        return  ControladorModelo.getInstance().getDispositivosBluetoothConocidos();
    }
    public void cancelarGetDispositivosBluetoothEntorno(DiscoveryListener interesado) throws ConexionException {
        ControladorModelo.getInstance().cancelarGetDispositivosBluetoothEntorno(interesado);
    }
    public void buscarServiciosDispositivoBluetooth(RemoteDevice bluetooth,DiscoveryListener interesado) throws ConexionException {
        ControladorModelo.getInstance().buscarServiciosDispositivoBluetooth(bluetooth, interesado);
    }

    public void cancelarGetServiciosBluetooth(int i) throws ConexionException {
        ControladorModelo.getInstance().cacelarGetServiciosBluetooth( i);
    }

    private RespuestaOBD leerRespuesta() throws ConexionException, Exception {
       RespuestaOBD r= ControladorModelo.getInstance().leerRespuesta();
       //System.out.println("numero de frame leido------------####-"+r.getNumeroFrame());
        return r;
    }

   public boolean estaConectado() {
        return ControladorModelo.getInstance().estaConectado();
    }


    public synchronized void bufferEntradaEscrito(BufferEvent e) {
        RespuestaOBD respuesta = null;
        //System.out.println("plop");
        try {
            respuesta = ControladorVista.getInstancia().leerRespuesta();
            if(interfaz!=null) interfaz.tratarRespuesta(respuesta);
        } catch (ConexionException f) {
            
            System.out.println("//////////////////////////////////////////// el manso eerror");
        } catch (Exception f) { // se recupera del error
        System.out.println("//////////////////////////////////////////// el manso eerror");
        
            
        }
      
        
    }

    public synchronized   void setInterfaz(Manipulable interfaz) {
        ControladorVista.interfaz = interfaz;
    }

    public static Manipulable getInterfaz() {
        return interfaz;
    }

    public void cancelarEscrituraLectura() {
        ControladorModelo.getInstance().cancelarEscrituraLectura();
    }
}
