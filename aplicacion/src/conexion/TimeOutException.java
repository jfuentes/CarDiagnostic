package conexion;

public class TimeOutException extends Exception {
    public TimeOutException(Throwable throwable) {
        super(throwable);
    }

    public TimeOutException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public TimeOutException(String string) {
        super(string);
    }

    public TimeOutException() {
        super();
    }
}
