package modelo;

import java.util.LinkedList;
import java.util.List;

public class PIDSoportados extends RespuestaOBD {
    
    /* esta clase deberia contar con una lista, con los numeros de pid soportados
     * eje: [0x00,0x01,0x04,0x0c]
     */
    private List <Integer> pidSoportados;
    public PIDSoportados(int servicio,int pid, String descripcion) {
        super(servicio,pid,descripcion);
        pidSoportados=new LinkedList<Integer> ();
    }
    public void putPIDSoportado(int i){
        pidSoportados.add(i);
    }
    public boolean isSoportado(int i){
        return pidSoportados.contains(i);
    }

    public void setPidSoportados(List<Integer> pidSoportados) {
        this.pidSoportados = pidSoportados;
    }

    public List<Integer> getPidSoportados() {
        return pidSoportados;
    }
    public int compareTo(Object o) {
        RespuestaOBD so = (RespuestaOBD)o;
        if (this.getServicio() == so.getServicio() &&
            this.getPid() == so.getPid()  ) {
            return 0;
        }
        return 1;
    }

    
}
