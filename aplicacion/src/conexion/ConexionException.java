package conexion;

public class ConexionException extends Exception {
    public static final String NO_AGENTE="NO SE PUEDE OBTNER EL AGENTE DE BÚSQUEDA";
    public static final String NO_INICIAR_BUSQUEDA="NO SE PUEDE INICIAR LA BÚSQUEDA";
    public static final String NO_INICIAR_BUSQUEDA_SERVICIO="NO SE PUEDE INICIAR LA BÚSQUEDA DE SERVICIOS";
    public static final String NO_OBTENER_ID_PUERTO="NO SE PUEDE OBTENER EL ID DEL PUERTO";
    public static final String NO_ABRIR_PUERTO_SERIAL="NO SE PUEDE ABRIR EL PUERTO";
    public static final String NO_ABRIR_STREAM_PUERTO_SERIAL="NO SE PUEDE ABRIR EL STREAM DEL PUERTO";
    public static final String NO_MAS_LISTENER="ERROR NO SE PERMITE MAS DE UN LISTENER EN EL SERIAL PORT";
    public static final String NO_PARAMETROS_SOPORTADOS="PARAMETROS NO SOPORTADOS POR EL PUERTO SERIAL";
    public static final String NO_SOPORTADO_cONTROL_FLUJO="CONTROL DE FLUJO NO SOPORTADO";
    public static final String NO_CERRAR_PUERTO="EXCEPCION AL CERRAR EL PUERTO";
    public static final String NO_SOPORTADA="OPERACION NO SOPORTADA";
    public static final String CONEXION_ABIERTA="ERROR EXISTE UNA CONEXIÓN ABIERTA";
    public static final String NO_MAS_CONEXIONES="ERROR SOLO SE PERMITE UNA SOLA CONEXIÓN, CIERRE LA ANTERIOR";
    public static final String CONEXION_NULL="ERROR, NO ESTÁ CONECTADO";
     public static final String MULTIPLES_LECTORES="MULTIMPLES THREAD INTENTAN LEER EL BUFFER";
    
    
    
    
    public ConexionException(String str) {
            super(str);
        }
        public ConexionException() {
            super();
        }

}
