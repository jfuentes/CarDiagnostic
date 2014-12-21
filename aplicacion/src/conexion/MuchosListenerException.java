package conexion;

public class MuchosListenerException extends Exception {
    public static final String MAS_BUFFER_LISTENER="No soporta más de un Listener";
    
    public MuchosListenerException(Throwable throwable) {
        super(throwable);
    }

    public MuchosListenerException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public MuchosListenerException(String string) {
        super(string);
    }

    public MuchosListenerException() {
        super();
    }
}
