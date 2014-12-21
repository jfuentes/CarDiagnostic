package modelo;
/**
 * duda si usar o no esto
 */
public class DTC extends RespuestaOBD{
   
    private String codigo;
    
    public DTC(int servicio,int pid,String codigo, String descripcion) {
        super(servicio,pid,descripcion);
        this.codigo=codigo;
         
    }


    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
    public int compareTo(Object o) {
        DTC so = (DTC)o;
        if (this.getServicio() == so.getServicio() &&
            this.getPid() == so.getPid() && this.codigo.equals(so.getCodigo())) {
            return 0;
        }
        return 1;
    }
}
