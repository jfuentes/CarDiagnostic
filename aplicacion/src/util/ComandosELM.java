package util;

public abstract class ComandosELM {
    
    // camandos at
    public static final String REPETIR_ULTIMO_COMANDO_AT="\r";
    public static final String SETEAR_TPDO_POR_DEFECTO_AT="atd\r";
    public static final String IMPRIMIR_VERSION_ELM_AT="ati\r";
    public static final String RESETEAR_ELM_AT="atws\r";
    
    public static final String RESETEAR_TODO_AT="atz\r";
    public static final String PROTOCOLO_ACTUAL_AT="atdp\r";
    
    public static final String CERRAR_PROTOCOLO_AT="atpc\r";
    public static final String ESPACIO_OFF_AT="ats0\r";
    public static final String ESPACIO_ON_AT="ats1\r";
    public static final String ECHO_OFF_AT="ate0\r";
    public static final String ECHO_ON_AT="ate1\r";
    
    public static final String SETEAR_PROTOCOLO_X_AT="atsp\r";
    public static final String SETEAR_PROTOCOLO_X_AUTO_AT="atspa\r";
    public static final String INTENTAR_PROTOCOLO_X_AT="attp\r";
    public static final String INTENTAR_PROTOCOLO_X_AUTO_AT="attpa\r";
    
    
    //SERVICIOS
    public static final String PID_4_20_SOPORTADOS_0100="0100\r";
    public static final String PID_21_40_SOPORTADOS_0120="0120\r";
    public static final String PID_41_60_SOPORTADOS_0140="0140\r";
    
    public static final String PID_4_20_SOPORTADOS_0200="0200\r";
    public static final String PID_21_40_SOPORTADOS_0220="0220\r";
    public static final String PID_41_60_SOPORTADOS_0240="0240\r";
    
    public static final String ESTADO_DTC_0101="0101\r";
    
    public static final String TEMPERATURA_0105="0105\r";
    public static final String RMP_010C="010C\r";
    public static final String VELOCIDAD_010D="010D\r";
    
    public static final String FLUJO_AIRE_0110="0110\r";
    public static final String SENSORES_OXIGENOS_PRESENTE="0113\r";
    public static final String SENSOR_OXIGENO_BANK1_S1_0114="0114\r";
    public static final String ESTANDAR_VEHICULO_011C="011C\r";
    public static final String TIEMPO_ARRANQUE_011F="011F\r";
    
   
    //public static final String ESTADO_DTC_0204="0101\r";
    
    public static final String TEMPERATURA_0205="0205\r";
    public static final String RMP_020C="020C\r";
    public static final String FLUJO_AIRE_0210="0210\r";
    //public static final String SENSORES_OXIGENOS_PRESENTE="0113\r";
    public static final String SENSOR_OXIGENO_BANK1_S1_0214="0214\r";
   // public static final String ESTANDAR_VEHICULO_021C="011C\r";
    public static final String TIEMPO_ARRANQUE_021F="021F\r";
    
    
    public static final String FREE_FRAME_0202="0202\r";
    public static final String CODIGOS_LANZABLES_03="03\r";
    public static final String ESTADO_DTC_COMANDO="0100\r";
    
    public static final String BORRAR_CODIGO_ALMACENADOS_04="04\r";
    
    public static final String MOSTRAR_CODIGOS_PENDIENTES_07="07\r";
    
    public static final String INFORMACION_VEHICULO_09="09\r";
    public static final String MOSTRAR_CODIGOS_PERMANENTES_0A="0A\r";


   
}
