package conexion;

public class OperacionNoSoportadaException extends Exception {
    public static final String INTERESADO_NULL="BluetoothPortEventListener no puede ser null";
    
    public static final String BUFFER_NULL="BufferListener no puede ser null";
    public static final String MAS_BUFFER_LISTENER="No soporta más de un BufferListener";
    
    
    
    public OperacionNoSoportadaException(Throwable throwable) {
        super(throwable);
    }

    public OperacionNoSoportadaException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public OperacionNoSoportadaException(String string) {
        super(string);
    }

    public OperacionNoSoportadaException() {
        super();
    }
}
