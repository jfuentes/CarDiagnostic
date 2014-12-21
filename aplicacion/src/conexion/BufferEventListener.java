package conexion;
/**
 * Interfaz que debe implementar un objeto que desee capturar eventos del bufferConexión
 */
public interface BufferEventListener {
    //void bufferSalidaEscrito(BufferEvent e);
    void bufferEntradaEscrito(BufferEvent e);
}
