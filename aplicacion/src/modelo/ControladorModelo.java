package modelo;

import conexion.BufferConexion;
import conexion.BufferEventListener;
import conexion.Conexion;

import conexion.ConexionBluetooth;
import conexion.ConexionException;
import conexion.ConexionSerial;

import conexion.MuchosListenerException;
import conexion.OperacionNoSoportadaException;

import conexion.TimeOutException;

import java.io.IOException;

import java.util.LinkedList;
import java.util.List;

import javax.bluetooth.DiscoveryListener;

import javax.bluetooth.RemoteDevice;

import javax.comm.CommPortIdentifier;

public class ControladorModelo {
    private static ControladorModelo instancia;
    private Conexion conexion;
    private StackC<Integer> pilaN0FRame;
   
    
    class StackC <Integer> {
        List <Integer> lista=new LinkedList<Integer>();
        void pushFinal(Integer i){
            lista.add(i);
        }
        Integer popFIFO(){
            if(!lista.isEmpty()){
                Integer i=lista.get(0);
               lista.remove(0);
               return i;
            }
           return null;
            
        }
        void vaciar(){
            lista.clear();
        }
    }

    private ControladorModelo() {
        super();
        pilaN0FRame=new StackC();
    }

    public static ControladorModelo getInstance() {
        if(instancia==null)instancia=new ControladorModelo();
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
    public void abrirConexion(BufferEventListener interesado,String nombrePuerto,
                              int velocidad) throws ConexionException,
                                                    MuchosListenerException,
                                                    OperacionNoSoportadaException {
        if (conexion == null)
            conexion = new ConexionSerial(nombrePuerto, velocidad);
        else
            throw new ConexionException(ConexionException.CONEXION_ABIERTA);
        
        conexion.addBufferEventListener(interesado);
        try{
            conexion.abrirConexion();
        }catch(ConexionException e){
            conexion=null;
            throw e;
        }
    }
     
    /**
     *abre una conexion bluetooth
     * @param interesado
     * @param url
     * @throws ConexionException
     * @throws MuchosListenerException
     * @throws OperacionNoSoportadaException
     */
    public void abrirConexion(BufferEventListener interesado,String url) throws ConexionException,
                                                 MuchosListenerException,
                                                 OperacionNoSoportadaException {
        if (conexion == null)
            conexion = new ConexionBluetooth(url);
        else
            throw new ConexionException(ConexionException.CONEXION_ABIERTA);
        try{
        conexion.addBufferEventListener(interesado);
        }catch(MuchosListenerException e){
            conexion=null;
            throw e;
            }catch(OperacionNoSoportadaException e){
                conexion=null;
                throw e;
            }
        try{
            conexion.abrirConexion();
        }catch(ConexionException e){
            conexion=null;
            throw e;
        }
    }
    public void cambiarBufferListener(BufferEventListener interesado) throws OperacionNoSoportadaException,
                                                                             ConexionException {
        if(conexion!=null)
            conexion.cambiarBufferEventListener(interesado);
        else
            throw new ConexionException(ConexionException.CONEXION_NULL);
            
            
    }

    public void cerrarConexion() throws ConexionException {
        if(conexion!=null)
        conexion.cerrarConexion();
        conexion=null;
    }

    public void escribirPuertoConexion(String datos, int numeroFrame,boolean bloqueante) throws IOException,
                                                            ConexionException,
                                                                  TimeOutException {
       
        if(conexion!=null){
            conexion.escribirPuerto(datos,bloqueante);
           // pilaN0FRame.pushFinal(numeroFrame);
        }
        else throw new ConexionException(ConexionException.CONEXION_NULL);
        
            
    }

    public void getDispositivosBluetoothEntorno(DiscoveryListener interesado) throws ConexionException {
        ConexionBluetooth.listarDispositivos(interesado);
    }
    public  RemoteDevice[] getDispositivosBluetoothConocidos() throws ConexionException {
        return ConexionBluetooth.listarDispositivosConocidos();
    }
    public void cancelarGetDispositivosBluetoothEntorno(DiscoveryListener interesado) throws ConexionException {
        ConexionBluetooth.cancelarListarDispositivos(interesado);
    }
    public void buscarServiciosDispositivoBluetooth(RemoteDevice bluetooth,DiscoveryListener interesado) throws ConexionException {
        ConexionBluetooth.buscarServicios(bluetooth, interesado);
    }

    public void cacelarGetServiciosBluetooth(int i) throws ConexionException {
        ConexionBluetooth.cancelarGetServiciosBluetooth(i);
    }

    private String leerBuffer() throws ConexionException {
        if(conexion!=null)
        return conexion.getBufferConexion().leerBufferEntrada();
        throw new ConexionException(ConexionException.CONEXION_NULL);
    } 
    public RespuestaOBD leerRespuesta() throws ConexionException, Exception {
        synchronized(this){
        String s=leerBuffer();
       RespuestaOBD o=null;

        try {
            o = DecodificadorPIDs2.decodificar(s);
            //int d=  pilaN0FRame.popFIFO();
            o.setNumeroFrame(0);
        } catch (Exception e) {
             e.printStackTrace();
            throw e;
        }
        if(o==null){
            System.err.println("ERROR FATAL NUNCA DEBE PASAR--------------------------------------");
        }
        return o;
        }
    }

    public boolean estaConectado() {
        return (conexion!=null);
    }
    
    public void cancelarEscrituraLectura(){
        if(conexion!=null)
        conexion.cancelarEscrituraLectura();
    }
      
}
