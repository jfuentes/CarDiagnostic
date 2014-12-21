package modelo;

public class MonitoreoPID extends RespuestaOBD{
    
    private int conversion;
    private String medicion;
    private double valor;
    
    public MonitoreoPID() {
        super();
    }
    public MonitoreoPID(int servicio, int pid, String descripcion, int conversion, String medicion) {
        super(servicio,pid,descripcion);
        this.conversion=conversion;
        this.medicion=medicion;
        valor=0.0;
    }
    public MonitoreoPID(String servicio, String pid, String descripcion, int conversion, String medicion) {
        super(servicio,pid,descripcion);
        this.conversion=conversion;
        this.medicion=medicion;
        valor=0.0;
    }
    

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getValor() {
        return valor;
    }


    public void setConversion(int conversion) {
        this.conversion = conversion;
    }

    public int getConversion() {
        return conversion;
    }

    public void setMedicion(String medicion) {
        this.medicion = medicion;
    }

    public String getMedicion() {
        return medicion;
    }
    public boolean isPID00_20(){
        return (super.getPid()==0x00||super.getPid()==0x20 || super.getPid()==0x40||super.getPid()==0x60);
    }
    public int compareTo(Object o) {
        MonitoreoPID so = (MonitoreoPID)o;
        if (this.getServicio() == so.getServicio() &&
            this.getPid() == so.getPid() && this.valor == so.getValor()) {
            return 0;
        }
        return 1;
    }
}
