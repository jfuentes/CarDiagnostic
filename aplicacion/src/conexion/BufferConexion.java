package conexion;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

/**
 * Buffer de entrada
 */

public final class BufferConexion {
    private StringBuffer bufferEntrada;
    private StringBuffer bufferSalida;
    private BufferEventListener bufferListener;

    private StackC<Integer> pilaN0FRame;

    class StackC<Integer> {
        List<Integer> lista = new LinkedList<Integer>();

        void pushFinal(Integer i) {
            lista.add(i);
        }

        Integer popFIFO() {
            if (!lista.isEmpty()) {
                Integer i = lista.get(0);
                lista.remove(0);
                return i;
            }
            return null;

        }

        void vaciar() {
            lista.clear();
        }
    }

    public BufferConexion() {
        super();
        bufferSalida = new StringBuffer();
        bufferEntrada = new StringBuffer();
        pilaN0FRame = new StackC<Integer>();
    }

    /**
     * add un Listener a quien enviar los eventos
     * @param BufferEventListener e
     * @throws MuchosListenerException
     * @throws OperacionNoSoportadaException
     */
    public void addBufferEventListener(BufferEventListener e) throws MuchosListenerException,
                                                                     OperacionNoSoportadaException {
        if (e == null)
            throw new OperacionNoSoportadaException(OperacionNoSoportadaException.BUFFER_NULL);
        if (bufferListener != null)
            throw new MuchosListenerException(OperacionNoSoportadaException.MAS_BUFFER_LISTENER);
        bufferListener = e;
    }

    public void cambiarBufferEventListener(BufferEventListener e) throws OperacionNoSoportadaException {
        if (e == null)
            throw new OperacionNoSoportadaException(OperacionNoSoportadaException.BUFFER_NULL);
        pilaN0FRame.vaciar();
        bufferEntrada.delete(0, bufferEntrada.length());
        bufferListener = e;


    }

    public void eliminarBufferEventListener() {
        bufferListener = null;
    }
    /**
     *escribe en el buffer de salida lo que se escribirá en el puerto
     * @param dato
     * @return true si el dato fue correctamente escrito
     */
    /*  public boolean escribirBufferSalida(String str){
       bufferSalida.append(str);
      if(bufferListener!=null){
          bufferListener.bufferSalidaEscrito(new BufferEvent(this,BufferEvent.DATO_DISPONIBLE));
      }
      return true;
    } */

    /**
     * escribe en el buffer de entrada lo que se escribio en el puerto de la conexión
     * @param str
     * @return true si el dato fue correctamente escrito en el buffer
     */
    public boolean escribirBufferEntrada(String str) {

        bufferEntrada.append(str);
        //System.out.println("Escrito buffer entrada:" + str);
        if (bufferListener != null)
            bufferListener.bufferEntradaEscrito(new BufferEvent(BufferConexion.this,
                                                                BufferEvent.DATO_DISPONIBLE));
        else
            this.leerBufferEntrada(); // lo vacia el mismo

        return true;

    }
    /**
     *
     * @return
     */
    /*  public String leerBufferSalida(){
        // automaticamente vacia el buffer
        return bufferSalida.toString();
    } */

    /**
     *
     * @return
     */
    public StringBuffer leerBufferSalidaSB() {
        return bufferSalida;
    }

    /**
     * retorna el buffer entero como un String
     * @return String lo que contiene el buffer
     */
    public String leerBufferEntrada() {
        String s = bufferEntrada.toString();
        bufferEntrada.delete(0, bufferEntrada.length());
       // System.out.println("Leido buffer entrada:" + s);
        return s;
    }

    public void pushPila(int i) {
        this.pilaN0FRame.pushFinal(i);
    }

    public int popPila() {
        Integer r = pilaN0FRame.popFIFO();
        if (r == null)
            return -2;
        // pilaN0FRame.remove(pilaN0FRame.size()-1);
        return r;
    }
}
