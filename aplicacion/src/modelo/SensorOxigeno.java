package modelo;

public class SensorOxigeno extends RespuestaOBD {
    double valor;
    double porcentaje;
    

    public SensorOxigeno(int servicio,int pid,double valor, double porcentaje, String descripcion) {
        super(servicio,pid, descripcion);
        this.valor=valor;
        this.porcentaje=porcentaje;
    }

    

    public int compareTo(Object o) {
        SensorOxigeno so = (SensorOxigeno)o;
        if (this.getServicio() == so.getServicio() &&
            this.getPid() == so.getPid() && this.valor == so.getValor()) {
            return 0;
        }
        return 1;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getValor() {
        return valor;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public double getPorcentaje() {
        return porcentaje;
    }
}
