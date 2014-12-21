package conexion;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Hilo encargado de chequear si existen datos disponibles en el puerto bluetooth
 */
public final class ManejadorEntradaBluetooth extends Thread {
    private InputStreamReader isr;
    private BluetoothPortEventListener bpel;
    private boolean suspendido=false;
    private Object candado=new Object();
    
    private int tiempoMinimoEventos=50;
    public ManejadorEntradaBluetooth(InputStreamReader is) {
        super();
        isr=is;
    }
    /**
     * suspende la ejecución del hilo
     */
    public   void suspender( ){
        synchronized(candado){
            suspendido=true;
            //System.out.println("suspendiendo");
        }
    }
    /**
     * reanuda la ejecución del hilo
     */
    public void despertar(){
        synchronized(candado){
            suspendido=false;
          //  System.out.println("despertamdo");
        }
    }
    /**
     * sebreescritura del metedo run
     */
    public void run()  {
        //System.out.println("hilo iniciado");
        boolean aaa=false;
        do{
            try {
               
                /* //System.out.println("lnza eevnto");
                synchronized (candado) {
                    aaa = suspendido;
                } */
               // if(!aaa){
                    if(isr.ready()){
                        if(bpel!=null){
                            System.out.println("lnza eevnto");
                            bpel.bluetoothEvent(new BluetoothPortEvent(this,BluetoothPortEvent.DATO_DISPONIBLE));
                            
                        } 
                    
                    }
                //}
                this.sleep(tiempoMinimoEventos);
            } catch (IOException e) {
                System.out.println(e);
            } catch (InterruptedException e) {
            }
           
        }while(true);
        
    }
    
    /**
     *add un interazado a quien enviarle los eventos
     * @param e
     * @throws MuchosListenerException
     * @throws OperacionNoSoportadaException
     */
    public void addBluetoothPortEventListener(BluetoothPortEventListener e)throws MuchosListenerException,
                                                                     OperacionNoSoportadaException {
        if(e==null)
            throw new OperacionNoSoportadaException(OperacionNoSoportadaException.INTERESADO_NULL);
        if(bpel!=null)
            throw new MuchosListenerException(MuchosListenerException.MAS_BUFFER_LISTENER);
        bpel=e;
    }
    public void setTiempoEspera(int tme ){
        tiempoMinimoEventos=tme;
    }
}
