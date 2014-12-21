package conexion;

import javax.comm.*;

public class Parametros {
    private String nombrePuerto;
    private int velocidad;
    private int controlFlujoEntrada;
    private int controlFlujoSalida;
    private int bitsDatos;
    private int bitsParada;
    private int paridad;

    public Parametros() {
        this("COM11", 9600, SerialPort.FLOWCONTROL_NONE,
             SerialPort.FLOWCONTROL_NONE, SerialPort.DATABITS_8,
             SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

    }
    public Parametros(String nombrePuerto,int velocidad){
        this(nombrePuerto, velocidad, SerialPort.FLOWCONTROL_NONE,
             SerialPort.FLOWCONTROL_NONE, SerialPort.DATABITS_8,
             SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
    }

    public Parametros(String nombrePuerto, int velocidad, int controlFlujoEntrada,
                            int controlFlujoSalida, int bitsDatos, int bitsParada,
                            int paridad) {

        this.nombrePuerto = nombrePuerto;
        this.velocidad = velocidad;
        this.controlFlujoEntrada = controlFlujoEntrada;
        this.controlFlujoSalida = controlFlujoSalida;
        this.bitsDatos = bitsDatos;
        this.bitsParada = bitsParada;
        this.paridad = paridad;
    }

    public void setNombrePuerto(String nombrePuerto) {
        this.nombrePuerto = nombrePuerto;
    }

    /**
        Obtiene el nombre del puerto
        @return nombrePuerto.
     */
    public String getNombrePuerto() {
        return nombrePuerto;
    }

    /**
        establece la velocidad en baud
        @param nueva velocidad.
     */
    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    /**
     establece la velocidad en baud
     @param nueva velocidad.
     */
    public void setVelocidad(String velocidad) {
        this.velocidad = Integer.parseInt(velocidad);
    }

    /**
        Obtiene la velocidad actual <code>int</code>.
        @return velocidad.
     */
    public int getVelocidad() {
        return velocidad;
    }

    /**
        Obtiene la velocidad como <code>String</code>.
        @return velocidad actual.
     */
    public String getVelocidadString() {
        return Integer.toString(velocidad);
    }

    /**
        Sets flow control for reading.
        @param controlFlujoEntrada New flow control for reading type.
     */
    public void setControlFlujoEntrada(int controlFlujoEntrada) {
        this.controlFlujoEntrada = controlFlujoEntrada;
    }

    /**
        Sets flow control for reading.
        @param controlFlujoEntrada New flow control for reading type.
     */
    public void setControlFlujoEntrada(String controlFlujoEntrada) {
        this.controlFlujoEntrada = stringToFlow(controlFlujoEntrada);
    }

    /**
        Gets flow control for reading as an <code>int</code>.
        @return Current flow control type.
     */
    public int getControlFlujoEntrada() {
        return controlFlujoEntrada;
    }

    /**
        Gets flow control for reading as a <code>String</code>.
        @return Current flow control type.
     */
    public String getControlFlujoEntradaString() {
        return flowToString(controlFlujoEntrada);
    }

    /**
        Sets flow control for writing.
        @param controlFlujoEntrada New flow control for writing type.
     */
    public void setControlFlujoSalida(int controlFlujoSalida) {
        this.controlFlujoSalida = controlFlujoSalida;
    }

    /**
        Sets flow control for writing.
        @param controlFlujoEntrada New flow control for writing type.
     */
    public void setControlFlujoSalida(String controlFlujoSalida) {
        this.controlFlujoSalida = stringToFlow(controlFlujoSalida);
    }

    /**
        Gets flow control for writing as an <code>int</code>.
        @return Current flow control type.
     */
    public int getControlFlujoSalida() {
        return controlFlujoSalida;
    }

    /**
        Gets flow control for writing as a <code>String</code>.
        @return Current flow control type.
     */
    public String getControlFlujoSalidaString() {
        return flowToString(controlFlujoSalida);
    }

    /**
        Sets data bits.
        @param bitsDatos New data bits setting.
     */
    public void setBitsDatos(int bitsDatos) {
        this.bitsDatos = bitsDatos;
    }

    /**
        Sets data bits.
        @param bitsDatos New data bits setting.
     */
    public void setBitsDatos(String bitsDatos) {
        if (bitsDatos.equals("5")) {
            this.bitsDatos = SerialPort.DATABITS_5;
        }
        if (bitsDatos.equals("6")) {
            this.bitsDatos = SerialPort.DATABITS_6;
        }
        if (bitsDatos.equals("7")) {
            this.bitsDatos = SerialPort.DATABITS_7;
        }
        if (bitsDatos.equals("8")) {
            this.bitsDatos = SerialPort.DATABITS_8;
        }
    }

    /**
        Gets data bits as an <code>int</code>.
        @return Current data bits setting.
     */
    public int getBitsDatos() {
        return bitsDatos;
    }

    /**
        Gets data bits as a <code>String</code>.
        @return Current data bits setting.
     */
    public String getBitsDatosString() {
        switch (bitsDatos) {
        case SerialPort.DATABITS_5:
            return "5";
        case SerialPort.DATABITS_6:
            return "6";
        case SerialPort.DATABITS_7:
            return "7";
        case SerialPort.DATABITS_8:
            return "8";
        default:
            return "8";
        }
    }

    /**
        Sets stop bits.
        @param bitsParada New stop bits setting.
     */
    public void setBitsParada(int bitsParada) {
        this.bitsParada = bitsParada;
    }

    /**
        Sets stop bits.
        @param bitsParada New stop bits setting.
     */
    public void setBitsParada(String bitsParada) {
        if (bitsParada.equals("1")) {
            this.bitsParada = SerialPort.STOPBITS_1;
        }
        if (bitsParada.equals("1.5")) {
            this.bitsParada = SerialPort.STOPBITS_1_5;
        }
        if (bitsParada.equals("2")) {
            this.bitsParada = SerialPort.STOPBITS_2;
        }
    }

    /**
        Gets stop bits setting as an <code>int</code>.
        @return Current stop bits setting.
     */
    public int getBitsParada() {
        return bitsParada;
    }

    /**
        Gets stop bits setting as a <code>String</code>.
        @return Current stop bits setting.
     */
    public String getBitsParadaString() {
        switch (bitsParada) {
        case SerialPort.STOPBITS_1:
            return "1";
        case SerialPort.STOPBITS_1_5:
            return "1.5";
        case SerialPort.STOPBITS_2:
            return "2";
        default:
            return "1";
        }
    }

    /**
        Sets paridad setting.
        @param paridad New paridad setting.
     */
    public void setParidad(int paridad) {
        this.paridad = paridad;
    }

    /**
        Sets paridad setting.
        @param paridad New paridad setting.
     */
    public void setParidad(String paridad) {
        if (paridad.equals("None")) {
            this.paridad = SerialPort.PARITY_NONE;
        }
        if (paridad.equals("Even")) {
            this.paridad = SerialPort.PARITY_EVEN;
        }
        if (paridad.equals("Odd")) {
            this.paridad = SerialPort.PARITY_ODD;
        }
    }

    /**
        Gets paridad setting as an <code>int</code>.
        @return Current paridad setting.
     */
    public int getParidad() {
        return paridad;
    }

    /**
        Gets paridad setting as a <code>String</code>.
        @return Current paridad setting.
     */
    public String getParidadString() {
        switch (paridad) {
        case SerialPort.PARITY_NONE:
            return "None";
        case SerialPort.PARITY_EVEN:
            return "Even";
        case SerialPort.PARITY_ODD:
            return "Odd";
        default:
            return "None";
        }
    }

    /**
        Converts a <code>String</code> describing a flow control type to an
        <code>int</code> type defined in <code>SerialPort</code>.
        @param flowControl A <code>string</code> describing a flow control type.
        @return An <code>int</code> describing a flow control type.
     */
    private int stringToFlow(String flowControl) {
        if (flowControl.equals("None")) {
            return SerialPort.FLOWCONTROL_NONE;
        }
        if (flowControl.equals("Xon/Xoff Out")) {
            return SerialPort.FLOWCONTROL_XONXOFF_OUT;
        }
        if (flowControl.equals("Xon/Xoff In")) {
            return SerialPort.FLOWCONTROL_XONXOFF_IN;
        }
        if (flowControl.equals("RTS/CTS In")) {
            return SerialPort.FLOWCONTROL_RTSCTS_IN;
        }
        if (flowControl.equals("RTS/CTS Out")) {
            return SerialPort.FLOWCONTROL_RTSCTS_OUT;
        }
        return SerialPort.FLOWCONTROL_NONE;
    }

    /**
        Converts an <code>int</code> describing a flow control type to a
        <code>String</code> describing a flow control type.
        @param flowControl An <code>int</code> describing a flow control type.
        @return A <code>String</code> describing a flow control type.
     */
    String flowToString(int flowControl) {
        switch (flowControl) {
        case SerialPort.FLOWCONTROL_NONE:
            return "None";
        case SerialPort.FLOWCONTROL_XONXOFF_OUT:
            return "Xon/Xoff Out";
        case SerialPort.FLOWCONTROL_XONXOFF_IN:
            return "Xon/Xoff In";
        case SerialPort.FLOWCONTROL_RTSCTS_IN:
            return "RTS/CTS In";
        case SerialPort.FLOWCONTROL_RTSCTS_OUT:
            return "RTS/CTS Out";
        default:
            return "None";
        }
    }
}
