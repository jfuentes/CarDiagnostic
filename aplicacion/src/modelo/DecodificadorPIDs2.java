package modelo;

import java.io.BufferedReader;
import java.io.FileReader;

import java.io.IOException;

import java.math.BigInteger;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;


public class DecodificadorPIDs2 {

    private static final Map<String, MonitoreoPID> MonitoreoHash;
    private static final Map<String, String> dtcHash;
    private static final EstadoDTC estadoDTC;
    static {
        MonitoreoHash = new LinkedHashMap<String, MonitoreoPID>();
        dtcHash = new LinkedHashMap<String, String>();
        construirHash();
        estadoDTC = new EstadoDTC(0x41, 0x01, "Numero de errores DTC", 0, 0);
    }


    /*
     *implementar todos sus metodos staticos.
     * debe leer de una archivo los tipos de Monitoreos y dejarlos en una tabla hash
     */

    /*
     * hasta el momento carga los pid 0-20
     */

    private static void construirHash() {
        FileReader fr;
        BufferedReader bf;
        String linea;
        try {
            fr = new FileReader("arch/CodigosMedicion.txt");
            bf = new BufferedReader(fr);
            while ((linea = bf.readLine()) != null) {
                if(linea.equalsIgnoreCase(""))continue;
                StringTokenizer stk = new StringTokenizer(linea, "|");
                String servicio = stk.nextToken();
                String pid = stk.nextToken();
                String descripcion = stk.nextToken();
                String medicion = stk.nextToken();
                int conversion = Integer.parseInt(stk.nextToken());
                try {
                    MonitoreoPID p =
                        new MonitoreoPID(servicio, pid, descripcion,
                                         conversion, medicion);
                   // System.out.println(servicio + pid);
                    MonitoreoHash.put((servicio + pid), p);
                } catch (Exception ee) {
                    ee.printStackTrace();
                    continue;
                }

            }

        } catch (IOException e) {
            System.err.println("error al leer codigos desde archivo");
        }
        /*
         * carga los codigos de error DTC
         */
        try {
            fr = new FileReader("arch/CodigosError.txt");
            bf = new BufferedReader(fr);
            int totalC=0;
            while ((linea = bf.readLine()) != null) {
                if(linea.equalsIgnoreCase(""))continue;
                StringTokenizer stk = new StringTokenizer(linea, "|");
                String codigo = stk.nextToken();
                String descripcion = stk.nextToken();

                //DTC dtc = new DTC(codigo, descripcion);
                dtcHash.put(codigo, descripcion);
                totalC++;
            }
            System.out.println("total DTC cargados: "+totalC);

        } catch (IOException e) {
            System.err.println("error al leer códigos desde archivo");
        }
    }


    /*
     * casos de decodificacion hasta ahora implementados
    Modo Monitoreo -> 41
    Modo DTC -> 43

    */

    public static RespuestaOBD decodificar(String s) throws Exception {
        // s="\n 41 0C 3E 3F \n  >"; alguns respuestas pueden probvenir en mas de un línea

        //if(s.matches("4[0-9][A-Z|a-z|0-9|>| | \n]*")) // pasa la prueba de ser una respuesta válida


        //s = s.replace("\n", ""); // saca los saltos de linea
        // s = s.replace("\\r", "");
        // s = s.replace("\n", ""); // saca los saltos de linea
       // System.out.println("preparado para decodificar " + s);
        s = s.replace(">", ""); // saca >
        s = s.replace(" ", ""); // elimina los espacios
        
        if (s.equalsIgnoreCase("")||s.contains("OK") || s.contains("DATA") || s.contains("ERROR") ||
            s.contains("UNABLE TO CONNECT") || s.contains("?") ||
            s.contains("SEARCHING") || s.contains("RESPUESTA")) { // SIN RESPUESTA
            return new ErrorDTC(s);
        }
       

        /*
         *1.- decodificar cabeceras
         *2.- de acuerdo al pid 
         * 
         */
        
        if (s.length() >= 4) {
            String comand = s.substring(0, 2);
            MonitoreoPID mpid = null;
            if (comand.equals("43") || comand.equals("44")) {
                mpid = MonitoreoHash.get(comand + "00");
            } else {
                String key = s.substring(0, 4);
                mpid = MonitoreoHash.get(key);
            }
            if (mpid == null)
                return new ErrorDTC(s);
            switch (mpid.getServicio()) {
            case 0x41:
                if (mpid.isPID00_20())
                    return decoficarSoportadosPIDs(mpid, s);
                    // tabla
                else if (mpid.getPid() == 0x01)
                    return decodificarModoEstadoDTC(s);
                else if (mpid.getPid() == 0x02)
                    return decodificarModoDTCFreeze(mpid, s);
                return decodificarModoMonitoreo(mpid, s);

            case 0x42:
                s = mataFrame(s);
                if (mpid.isPID00_20())
                    return decoficarSoportadosPIDs(mpid, s);
                else if (mpid.getPid() == 0x01)
                    return decodificarModoEstadoDTC(s);
                else if (mpid.getPid() == 0x02)
                    return decodificarModoDTCFreeze(mpid, s);
                return decodificarModoMonitoreo(mpid, s);
                //break;
            case 0x43:
                try {
                    return decodificarModoDTC(mpid, s);
                } catch (NumberFormatException e) {
                    System.out.println(e.toString());
                    return new ErrorDTC(s);
                } catch (Exception ee) {
                    System.out.println("El numero de errores no corresponde al los que devuelve 03 o formato respuesta distinto" +
                                       ee.toString());
                    return new ErrorDTC(s);
                }
                //break;
            case 0x44:
                return mpid;
                // break;
            case 0x45:
                return mpid;
                // break;
            case 0x46:
                return mpid;
                // break;
            case 0x47:
                try {
                    return decodificarModoDTC(mpid, s);
                } catch (NumberFormatException e) {
                    System.out.println(e.toString());
                    return new ErrorDTC(s);
                } catch (Exception ee) {
                    System.out.println("El numero de errores no corresponde al los que devuelve 03 o formato respuesta distinto" +
                                       ee.toString());
                    return new ErrorDTC(s);
                }
                //return mpid;
                 //break;
            case 0x49:
                if (mpid.isPID00_20())
                    return decoficarSoportadosPIDs(mpid, s);
                else
                    return decodificarInformacionVehiculo(mpid, s);
                // break;

            }


        }
        return new ErrorDTC(s);
    }

    private static RespuestaOBD decodificarModoMonitoreo(MonitoreoPID pid,
                                                         String s) {
        /*
         * el String pasa en su estado original, sin espacios ni >. ejemplo. 41pidxx
         * o 4100 si no hay datos
         */
        s = s.replace("\n", "");
        s = s.replaceFirst("4[0-9]{1}[0-9]{1}[0-9|a-z|A-Z]{1}", "");
        s = s.trim();
        double v = 0;
        /*FORMULA DE CONVERSION
             * 1= A*100/255
             * 2= A-40
             * 3= (A-128) * 100/128
             * 4= A*3
             * 5= A
             * 6= A/4
             * 7= A/2 - 64
             * 8= A-40
             * 9= ((A*256)+B) / 100
             * 10= A*100/255
             */
        switch (pid.getConversion()) {

        case 1:
            v = Integer.parseInt(s, 16) * 100 / 255;
            break;
        case 2:
            v = (Integer.parseInt(s, 16) - 40);
            break;
        case 3:
            v = (Integer.parseInt(s, 16) - 128) * 100 / 128;
            break;
        case 4:
            v = Integer.parseInt(s, 16) * 3;
            break;
        case 5:
            v = Integer.parseInt(s, 16);
            break;
        case 6:
            v = Integer.parseInt(s, 16) / 4;
            break;
        case 7:
            v = (Integer.parseInt(s, 16) / 2) - 64;
            break;
        case 8:
            if (s.length() > 2) {
                int a = Integer.parseInt(s.substring(0, 2), 16);
                int b = Integer.parseInt(s.substring(2, 4), 16);
                v = ((a * 256) + b) / 100;
            }
            break;
        case 9:
            if (s.length() > 2) {
                int a = Integer.parseInt(s.substring(0, 2), 16);
                int b = Integer.parseInt(s.substring(2, 4), 16);
                //System.out.println("vaslor de oxigeno----------------------------------------"+v+" b:"+b+" a:"+Integer.toHexString(a));
                double va = (a * 0.005);
                double por = (b - 128) * 100 / 128;

                return new SensorOxigeno(pid.getServicio(), pid.getPid(), va,
                                         por, pid.getDescripcion());


            }

            break;
        case 10:
            if (s.length() > 2) {
                int a = Integer.parseInt(s.substring(0, 2), 16);
                int b = Integer.parseInt(s.substring(2, 4), 16);
                v = (a * 256) + b;
            }
            break;
        case 11:
            if (s.length() > 2) {
                int b = Integer.parseInt(s.substring(2, 4), 16);
                int a = Integer.parseInt(s.substring(0, 2), 16);
                v = (((a * 256) + b) * 10) / 128;
            }
            break;
        case 12:
            if (s.length() > 2) {
                int a = Integer.parseInt(s.substring(0, 2), 16);
                int b = Integer.parseInt(s.substring(2, 4), 16);
                v = ((a * 256) + b) * 10;
            }
            break;
        case 13:
            if (s.length() > 4) {
                int c = Integer.parseInt(s.substring(4, 6), 16);
                int d = Integer.parseInt(s.substring(6, 8), 16);
                v = ((c * 256) + d) / 8192;
            }
            break;
        case 14:
            if (s.length() > 2) {
                int a = Integer.parseInt(s.substring(0, 2), 16);
                int b = Integer.parseInt(s.substring(2, 4), 16);
                v = ((a * 256) + b) / 4;
            }
            break;
        case 15:
            if (s.length() > 4) {
                int c = Integer.parseInt(s.substring(4, 6), 16);
                int d = Integer.parseInt(s.substring(6, 8), 16);
                v = (((c * 256) + d) / 256) - 128;
            }
            break;
        case 16:
            if (s.length() > 2) {
                int a = Integer.parseInt(s.substring(0, 2), 16);
                int b = Integer.parseInt(s.substring(2, 4), 16);
                v = ((a * 256) + b) / 10 - 40;
            }
            break;
        case 17:
            if (s.length() > 2) {
                int a = Integer.parseInt(s.substring(0, 2), 16);
                int b = Integer.parseInt(s.substring(2, 4), 16);
                v = ((a * 256) + b) / 1000;
            }
            break;
        case 18:
            if (s.length() > 2) {
                int a = Integer.parseInt(s.substring(0, 2), 16);
                int b = Integer.parseInt(s.substring(2, 4), 16);

                v = ((a * 256) + b) * (100 / 255);
            }
            break;
        case 19:
            int codigo = Integer.parseInt(s, 16);
            switch (codigo) {
            case 0x01:
                pid.setMedicion("Gasolina");
                break;
            case 0x02:
                pid.setMedicion("Methanol");
                break;
            case 0x03:
                pid.setMedicion("Ethanol");
                break;
            case 0x04:
                pid.setMedicion("Diesel");
                break;
            case 0x05:
                pid.setMedicion("LPG");
                break;
            case 0x06:
                pid.setMedicion("CNG");
                break;
            case 0x07:
                pid.setMedicion("Propane");
                break;
            case 0x08:
                pid.setMedicion("Electric");
                break;
            case 0x09:
                pid.setMedicion("Bifuel running Gasoline");
                break;
            case 0x0A:
                pid.setMedicion("Bifuel running Methanol");
                break;
            case 0x0B:
                pid.setMedicion("Bifuel running Ethanol");
                break;
            case 0x0C:
                pid.setMedicion("Bifuel running LPG");
                break;
            case 0x0D:
                pid.setMedicion("Bifuel running CNG");
                break;
            case 0x0E:
                pid.setMedicion("Bifuel running Prop");
                break;
            case 0x0F:
                pid.setMedicion("Bifuel running Electricity");
                break;
            case 0x10:
                pid.setMedicion("Bifuel mixed gas/electric");
                break;
            case 0x11:
                pid.setMedicion("Hybrid gasoline");
                break;
            case 0x12:
                pid.setMedicion("Hybrid Ethanol");
                break;
            case 0x13:
                pid.setMedicion("Hybrid Diesel");
                break;
            case 0x14:
                pid.setMedicion("Hybrid Electric");
                break;
            case 0x15:
                pid.setMedicion("Hybrid Mixed fuel");
                break;
            case 0x16:
                pid.setMedicion("Hybrid Regenerative");
                break;

            }
            break;
        default:
            break;
        }
        pid.setValor(v);

        return pid;
    }

    private static RespuestaOBD decodificarModoDTC(MonitoreoPID pid,
                                          String s) throws Exception {

        // para el standar no can, el numero de byte es impar osea 7, y para el can es par n, donde  es par.
        
        if((s.length()%2)== 0) // can,  es par
          return decodificarModoDTCCAN(pid,s);
        else // impar
            return decodificarModoDTCNOCAN(pid, s);
        
    }

    private static RespuestaOBD decodificarModoDTCCAN(MonitoreoPID pid,
                                                   String s) throws Exception {

        // para el standar no can, el numero de byte es impar osea 7, y para el can es par n, donde  es par.

            s = s.replaceFirst("4[0-9]{1}", "");
            s = s.replace("\n", "");
            System.out.println("decodificar:" + s);
            // aca pueden venir muchos dtc.
            BigInteger valor = new BigInteger(s, 16);
            byte[] valorArreglo =
                valor.toByteArray(); // pimer byte indica el numero de dtc que vienen
            int pos = 2;
            DTC dtc = null;
            System.out.println("n dtc:" + valorArreglo[0]);
            for (int i = 1, j = 0; j < valorArreglo[0]; i += 2, j++) {
                StringBuffer tipo = new StringBuffer();
                int valorOringenDTC = (valorArreglo[i] & 0xC0) >> 6;
                int digito1 = (valorArreglo[i] & 0x20) >> 4;
                int digito2 = valorArreglo[i] & 0x0f;
                int digito3 = (valorArreglo[(i + 1)] & 0xf0) >> 4;
                int digito4 = valorArreglo[(i + 1)] & 0x0f;

                switch (valorOringenDTC) {

                case 0x0:
                    tipo.append("P");
                    break;
                case 0x1:
                    tipo.append("C");
                    break;
                case 0x10:
                    tipo.append("B");
                    break;
                case 0x11:
                    tipo.append("U");
                    break;

                }
                tipo.append(digito1);
                tipo.append(digito2);
                tipo.append(digito3);
                tipo.append(digito4);
                System.out.println(tipo);
                String descDTC = dtcHash.get(tipo.toString());
                DTC dtcAux =
                    new DTC(pid.getServicio(), pid.getPid(), tipo.toString(),
                            descDTC);
                if (dtc == null)
                    dtc = dtcAux;
                else
                    dtc.setSiguiente(dtcAux);

            }

            if (dtc == null)
                return new ErrorDTC("NO DATA");
            return dtc;
        
    }

    private static RespuestaOBD decodificarModoDTCNOCAN(MonitoreoPID pid,
                                                      String s) throws Exception {

        // para el standar no can, el numero de byte es impar osea 7, y para el can es par n, donde  es par.
        DTC dtc = null;

        s = s.replaceFirst("4[0-9]{1}", "");
        s = s.replace("\n", "");
        BigInteger valor = new BigInteger(s, 16);
        byte[] valorArreglo =   valor.toByteArray(); 
        for (int i = 0, j = 0; j < 3; i += 2, j++) {
            StringBuffer tipo = new StringBuffer();
            int valorOringenDTC = (valorArreglo[i] & 0xC0) >> 6;
            int digito1 = (valorArreglo[i] & 0x20) >> 4;
            int digito2 = valorArreglo[i] & 0x0f;
            int digito3 = (valorArreglo[(i + 1)] & 0xf0) >> 4;
            int digito4 = valorArreglo[(i + 1)] & 0x0f;
            if(digito1==0&&digito2==0&& digito3==0 && digito4==0)break;
            switch (valorOringenDTC) {

            case 0x0:
                tipo.append("P");
                break;
            case 0x1:
                tipo.append("C");
                break;
            case 0x10:
                tipo.append("B");
                break;
            case 0x11:
                tipo.append("U");
                break;

            }
            tipo.append(digito1);
            tipo.append(digito2);
            tipo.append(digito3);
            tipo.append(digito4);
            System.out.println(tipo);
            String descDTC = dtcHash.get(tipo.toString());
            DTC dtcAux =
                new DTC(pid.getServicio(), pid.getPid(), tipo.toString(),
                        descDTC);
            if (dtc == null)
                dtc = dtcAux;
            else
                dtc.setSiguiente(dtcAux);

        }

        if (dtc == null)
            return new ErrorDTC("NO DATA");
        return dtc;

    }


    private static RespuestaOBD decodificarModoEstadoDTC(String s) {
        /*
         * el String pasa en su estado original, sin espacios ni >. ejemplo. 4101ABCD
         * o 4100 si no hay datos
         */
        s = s.replaceFirst("41[0-9]{1}[0-9|a-z|A-Z]{1}", "");
        s = s.replace("\n", "");
        Long valorDec = Long.parseLong(s, 16); // todo a un int
        int MIL = (int)((valorDec >> 31) & 0x01);
        String descripcion;
        int numeroDTC;

        if (MIL == 0) {
            descripcion = "No hay errores guardados. MIL=OFF";
            System.out.println("mil off");
            numeroDTC = 0;
            MIL = 0;
        } else {
            numeroDTC = (int)(valorDec & 0x7f000000) >> 24;
            descripcion =
                    "Actualmente hay " + numeroDTC + " guardados en la ECU. MIL=ON";
            System.out.println("Actualmente hay " + numeroDTC +
                               " guardados en la ECU. MIL=ON");
            MIL = 1;
        }

        System.out.println("valor a decodigficar:"+Long.toBinaryString(valorDec)+" he"+Long.toHexString(valorDec));
        
        String[][] matr =
        { { "Misfire monitoring", "si", "", "" }, 
          { "Fuel system monitoring", "si", "", "" },
          { "Comprehensive component monitoring", "si", "", "" },
          { "Catalyst monitoring", "no", "", "" },
          { "Heated catalyst monitoring", "no", "", "" },
          { "Evaporative system monitoring", "no", "", "" },
          { "Secondary air system monitoring", "no", "", "" },
          { "A/C system refrigerant monitoring", "no", "", "" },
          { "Oxigen sensor monitoring", "no", "", "" },
        { "Oxigen sensor heater monitoring", "no", "", "" },
          { "EGR system monitoring", "no", "", "" }, 
          { "EGR system monitoring", "no", "", "" }};

        int j=0;
        
        for (int i = 3; i > 0; i--, j++) {
            int b = (int)(valorDec >> (20 + i)) & 0x01;
            matr[j][2] = (b == 1) ? "si" : "no";
        }
        j=0;
        for (int i = 3; i >= 0; i--, j++) {
            int b = (int)(valorDec >> (16+i)) & 0x01;
            matr[j][3] = (b == 1) ? "si" : "no";
        }
        int k=--j;
        for (int i = 7; i >=0; i--, j++) {
            int b = (int)(valorDec >> (8 + i)) & 0x01;
            matr[j][2] = (b == 1) ? "si" : "no";
        }
        j=k;
        for (int i = 7; i >=0; i--, j++) {
            int b = (int)(valorDec >> (i)) & 0x01;
            matr[j][3] = (b == 1) ? "si" : "no";
        }


        estadoDTC.setMIL(MIL);
        estadoDTC.setNumeroDTCs(numeroDTC);
        estadoDTC.setTablaMonitoreo(matr);
        //falta
        return estadoDTC;
    }

    private static RespuestaOBD decoficarSoportadosPIDs(MonitoreoPID mpid,
                                                        String s) {
        //llega entero el string sin espacios ni > ejemplo 4100ABCD.
        // idea decodificar los ABCD
        s = s.trim();
        s =
  s.replaceFirst("4[0-9]{3}", ""); // le saca el 4100 0 4120 etc
        
        StringTokenizer st = new StringTokenizer(s, "\n");
        PIDSoportados ps = null;
        while (st.hasMoreTokens()) {
            String deco = st.nextToken();
            deco.replace("\n", "");
            deco = deco.trim();
            System.out.println("estring::" + deco + "#");

            Long pids =
                Long.parseLong(deco, 16); // ABCD 4 byte deberia calzar justo.


            PIDSoportados ps2 =
                new PIDSoportados(mpid.getServicio(), mpid.getPid(),
                                  mpid.getDescripcion());
            long mask = 0x01;
            long p = pids;
            for (int i = 32; i > 0; i--, pids >>= 1) {
                if ((pids & mask) == 1) { // pid soportado
                    ps2.putPIDSoportado(i);
                    //System.out.print(Integer.toHexString(i) + " ");
                }
                // System.out.print(pids&mask);


            }
           // System.out.println();
           // System.out.println("DECODIFICANDO SERVICIO 0100" +
           //                    pids.toBinaryString(p));
            // System.out.println("DECODIFICANDO SERVICIO 0100"+Long.toHexString(0));
            if (ps == null)
                ps = ps2;
            else
                ps.setSiguiente(ps2);

        }

        if(ps==null)return new ErrorDTC("NO DATA");
        return ps;
    }

    private static RespuestaOBD decodificarModoDTCFreeze(MonitoreoPID pid,
                                                         String s) {
        s = s.replaceFirst("4[0-9]{1}02", "");
        s = s.replace("\n", "");
        System.out.println("decodificar:" + s);
        // aca pueden venir muchos dtc.
        Integer valor = Integer.parseInt(s, 16);
        DTC dtc = null;
        if (valor > 0) {


            StringBuffer tipo = new StringBuffer();
            int valorOringenDTC = (valor & 0xC000) >> 14;
            int digito1 = (valor & 0x2000) >> 12;
            int digito2 = (valor & 0x0f00) >> 8;
            int digito3 = (valor & 0xf0) >> 4;
            int digito4 = valor & 0x0f;

            switch (valorOringenDTC) {

            case 0x0:
                tipo.append("P");
                break;
            case 0x1:
                tipo.append("C");
                break;
            case 0x10:
                tipo.append("B");
                break;
            case 0x11:
                tipo.append("U");
                break;

            }
            tipo.append(digito1);
            tipo.append(digito2);
            tipo.append(digito3);
            tipo.append(digito4);
            System.out.println(tipo);
            String descDTC = dtcHash.get(tipo.toString());
            return new DTC(pid.getServicio(), pid.getPid(), tipo.toString(),
                           descDTC);

        }

        if(dtc==null)return new ErrorDTC("NO DATA");
        return dtc;
    }

    private static String mataFrame(String s) {
        return s.substring(0, 4) + s.substring(6, s.length());
    }

    private static RespuestaOBD decodificarInformacionVehiculo(MonitoreoPID pid,
                                                               String s) {
        s =
  s.replaceFirst("49[0-9]{1}[0-9|a-z|A-Z]{1}", ""); // le saca el 4900 0 4902 etc
        s = s.trim();
        switch (pid.getPid()) {

        case 0x02:
            int datas = Integer.parseInt(s.substring(0, 2));
            StringBuffer sb = new StringBuffer();
            int contData = 0;
            while (contData < datas) {

                for (int pos = 2; pos < 36; pos += 2) {
                    char[] c =
                        Character.toChars(Integer.parseInt(s.substring(pos,
                                                                       pos +
                                                                       2),
                                                           16));
                    sb.append(c);
                }
                sb.append(" - ");
                contData++;
            }
            return new InformacionVehiculo(pid.getServicio(), pid.getPid(),
                                           pid.getDescripcion(),
                                           sb.toString());

        }


        return new ErrorDTC("NO DATA");
    }
}
