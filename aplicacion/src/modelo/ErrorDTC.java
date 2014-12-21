package modelo;

public class ErrorDTC extends RespuestaOBD{
    public ErrorDTC(String descripcion) {
        super(0x0, 0x0,descripcion);
    }
    public int compareTo(Object o) {
        ErrorDTC so = (ErrorDTC)o;
        if (this.getServicio() == so.getServicio() &&
            this.getPid() == so.getPid() ) {
            return 0;
        }
        return 1;
    }
}
