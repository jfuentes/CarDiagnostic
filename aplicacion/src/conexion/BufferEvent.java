package conexion;

import java.util.EventObject;

/**
 * evento que es lanzado cuando se detecta un dato disponible en el buffer.
 */
public class BufferEvent extends EventObject {
    protected int tipo;
    public static final int DATO_DISPONIBLE=1;
    public BufferEvent(BufferConexion fuente, int tipo) {
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
