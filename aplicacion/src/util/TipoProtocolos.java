package util;

import java.util.LinkedHashMap;

public abstract class TipoProtocolos {
    public static final String AUTOMATICO = "AUTOMATICO";
    public static final String SAE_PWM = "SAE J1850 PWM (41.6 KBAUD)";
    public static final String SAE_VPW = "SAE J1850 VPW (10.4 KBAUD)";
    public static final String ISO_9141_2 = "ISO 9141-2 (5 BAUD VEL. INICIO)";
    public static final String ISO_14230_4_KWP_5 =
        "ISO 14230-4 (5 BAUD VEL.INCIO)";
    public static final String ISO_14230_4_KWP = "ISO 14230-4 (INCIO RÁPIDO)";
    public static final String ISO_15765_4_CAN_11_500 =
        "ISO 15765-4 CAN (11 BIT ID,250 KBAUD)";
    public static final String ISO_15765_4_CAN_29_500 =
        "ISO 15765-4 CAN (29 BIT ID,500 KBAUD)";
    public static final String ISO_15765_4_CAN_11_250 =
        "ISO 15765-4 CAN (11 BIT ID,250 KBAUD)";
    public static final String ISO_15765_4_CAN_29_250 =
        "ISO 15765-4 CAN (29 BIT ID,250 KBAUD)";
    public static final String SAE_J1939_CAN =
        "SAE J1939 CAN (29 BIT ID,250 KBAUD)";
    public static final String USER1_CAN =
        "USUARIO1 CAN (11 BIT ID,125 KBAUD)";
    public static final String USER2_CAN = "USUARIO2 CAN (11 BIT ID,50 KBAUD)";

    public static final String NUMERO_ELM_AUTOMATICO = "ATTP0\n";
    public static final String NUMERO_ELM_SAE_PWM = "ATTP1\n";
    public static final String NUMERO_ELM_SAE_VPW = "ATTP2\n";
    public static final String NUMERO_ELM_ISO_9141_2 = "ATTP3\n";
    public static final String NUMERO_ELM_ISO_14230_4_KWP_5 = "ATTP4\n";
    public static final String NUMERO_ELM_ISO_14230_4_KWP = "ATTP5\n";
    public static final String NUMERO_ELM_ISO_15765_4_CAN_11_500 = "ATTP6\n";
    public static final String NUMERO_ELM_ISO_15765_4_CAN_29_500 = "ATTP7\n";
    public static final String NUMERO_ELM_ISO_15765_4_CAN_11_250 = "ATTP8\n";
    public static final String NUMERO_ELM_ISO_15765_4_CAN_29_250 = "ATTP9\n";
    public static final String NUMERO_ELM_SAE_J1939_CAN = "ATTP0A\n";
    public static final String NUMERO_ELM_USER1_CAN = "ATTPB\n";
    public static final String NUMERO_ELM_USER2_CAN = "ATTPC\n"; 
    private static final LinkedHashMap<String,String> a=new LinkedHashMap<String,String>();
    static {
        
        a.put(AUTOMATICO, NUMERO_ELM_AUTOMATICO);
        a.put(SAE_PWM, NUMERO_ELM_SAE_PWM);
        a.put(SAE_VPW, NUMERO_ELM_SAE_VPW);
        a.put(ISO_9141_2, NUMERO_ELM_ISO_9141_2);
        a.put(ISO_14230_4_KWP_5, NUMERO_ELM_ISO_14230_4_KWP_5);
        a.put(ISO_14230_4_KWP, NUMERO_ELM_ISO_14230_4_KWP);
        a.put(ISO_15765_4_CAN_11_500, NUMERO_ELM_ISO_15765_4_CAN_11_500);
        a.put(ISO_15765_4_CAN_29_500, NUMERO_ELM_ISO_15765_4_CAN_29_500);
        a.put(ISO_15765_4_CAN_11_250, NUMERO_ELM_ISO_15765_4_CAN_11_250);
        a.put(ISO_15765_4_CAN_29_250, NUMERO_ELM_ISO_15765_4_CAN_29_250);
        a.put(SAE_J1939_CAN, NUMERO_ELM_SAE_J1939_CAN);
        a.put(USER1_CAN, NUMERO_ELM_USER1_CAN);
        a.put(USER2_CAN, NUMERO_ELM_USER2_CAN);
    }

    private TipoProtocolos() {
        super();
    }

    public static String[] getProtocolos() {
        return new String[] { AUTOMATICO, SAE_PWM, SAE_VPW, ISO_9141_2,
                              ISO_14230_4_KWP_5, ISO_14230_4_KWP,
                              ISO_15765_4_CAN_11_500, ISO_15765_4_CAN_29_500,
                              ISO_15765_4_CAN_11_250, ISO_15765_4_CAN_29_250,
                              SAE_J1939_CAN, USER1_CAN, USER2_CAN };
    }
    public static LinkedHashMap getProtocolosHashMap(){
        
        return a;
        
    }
    public static String getComandoELM(String protocolo){
        return a.get(protocolo);
    }
}
