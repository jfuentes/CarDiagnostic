package modelo;

public abstract class RespuestaOBD implements Comparable {
    private int servicio; // pueden ser de $01- $0A
    private int pid;
    private String descripcion;
    private RespuestaOBD siguiente;
    int numeroFrame;
    public RespuestaOBD() {
        super();
    }
    public RespuestaOBD(int servicio,int pid, String descripcion) {
        this.servicio=servicio;
        this.pid=pid;
        this.descripcion=descripcion;
    }
    public RespuestaOBD(String servicio,String pid, String descripcion) {
        this.servicio=Integer.parseInt(servicio, 16);
        this.pid=Integer.parseInt(pid, 16);
        this.descripcion=descripcion;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getPid() {
        return pid;
    }

    public void setDescripcion(String descripción) {
        this.descripcion = descripción;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setServicio(int servicio) {
        this.servicio = servicio;
    }

    public int getServicio() {
        return servicio;
    }

    public void setSiguiente(RespuestaOBD siguiente) {
       if(this.siguiente!=null) siguiente.setSiguiente(siguiente);
       else this.siguiente=siguiente;
    }

    public RespuestaOBD getSiguiente() {
        
        return siguiente;
    }

    public void setNumeroFrame(int numeroFrame) {
        this.numeroFrame = numeroFrame;
    }

    public int getNumeroFrame() {
        return numeroFrame;
    }
}
