package conexion;
import java.util.EventObject;

/**
 * evento que es lanzado cuando se detecta un dato disponible en el bluetooth.
 */

/*
 * revisada
 */
public class BluetoothPortEvent extends EventObject{
    private int tipo;
    public static final int DATO_DISPONIBLE=1;
    public BluetoothPortEvent(ManejadorEntradaBluetooth fuente,int tipo) {
        super(fuente);
        this.tipo=tipo;
    }
    /**
     * obtiene el tipo del evento
     * @return tipo
     */
    public int getTipo(){
        return tipo;
    }
    
}
