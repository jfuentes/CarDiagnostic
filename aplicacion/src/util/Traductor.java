package util;

import java.util.HashMap;

public final class Traductor {
   public static final HashMap<String,String> tra=new HashMap<String,String>();
   static{
       iniciar();
   }
   public static void iniciar(){
       tra.put("finalizar", "Finalizar");
       tra.put("ayudaConexion","Seleccione el tipo de conexión,\n luego el asistente de guiará para conectar con la interfaz");
        tra.put("conectar", "Conectar");
   }

    public static String getDes(String clave) {
        return tra.get(clave);
    }
}
