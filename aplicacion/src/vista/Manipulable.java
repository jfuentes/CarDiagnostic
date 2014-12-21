package vista;

import modelo.RespuestaOBD;

/**
 * interfaze que deben implementar los paneles que leen del obd miestras estan visible
 */
public interface Manipulable {
    
    void detenerMuestreo(boolean detener);
    void tratarRespuesta(RespuestaOBD r);
}
