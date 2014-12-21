package modelo;

public class InformacionVehiculo extends RespuestaOBD{
    private String info;
    public InformacionVehiculo(int servicio,int pid,String descripcion, String info) {
        super(servicio,pid,descripcion);
        this.info=info;
    }
    public int compareTo(Object o) {
        InformacionVehiculo so = (InformacionVehiculo)o;
        if (this.getServicio() == so.getServicio() &&
            this.getPid() == so.getPid() ) {
            return 0;
        }
        return 1;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
