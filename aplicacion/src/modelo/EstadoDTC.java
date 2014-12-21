package modelo;

public class EstadoDTC extends RespuestaOBD{
    int numeroDTC;
    int MIL;
    String [][] tablaMonitoreo= new String[4][11];
    public EstadoDTC(int servicio, int pid,String descripcion,int numeroDTC,int MIL) {
        super(servicio,pid,descripcion);
        this.numeroDTC=numeroDTC;
        this.MIL=MIL;
    }

    public void setNumeroDTCs(int numeroDTCs) {
        this.numeroDTC = numeroDTCs;
    }

    public int getNumeroDTCs() {
        return numeroDTC;
    }

    public void setMIL(int MIL) {
        this.MIL = MIL;
    }

    public int getMIL() {
        return MIL;
    }
    public int compareTo(Object o) {
        InformacionVehiculo so = (InformacionVehiculo)o;
        if (this.getServicio() == so.getServicio() &&
            this.getPid() == so.getPid() ) {
            return 0;
        }
        return 1;
    }

    public void setTablaMonitoreo(String[][] tablaMonitoreo) {
        this.tablaMonitoreo = tablaMonitoreo;
    }

    public String[][] getTablaMonitoreo() {
        return tablaMonitoreo;
    }
}
