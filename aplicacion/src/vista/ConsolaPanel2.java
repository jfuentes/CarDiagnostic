package vista;

import conexion.BufferEvent;
import conexion.BufferEventListener;

import conexion.ConexionException;
import conexion.OperacionNoSoportadaException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;

import java.awt.event.KeyAdapter;

import java.awt.event.KeyEvent;

import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import modelo.DTC;
import modelo.ErrorDTC;
import modelo.EstadoDTC;
import modelo.InformacionVehiculo;
import modelo.MonitoreoPID;
import modelo.PIDSoportados;
import modelo.RespuestaOBD;
import modelo.SensorOxigeno;

public class ConsolaPanel2 extends JPanel implements Manipulable {
    private BorderLayout borderLayout1 = new BorderLayout();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JTextArea consolaTextArea = new JTextArea();
    private static final ConsolaPanel2 instancia = new ConsolaPanel2();
    private JPanel jPanel1 = new JPanel();
    private JTextField comandoField = new JTextField();
    private BorderLayout borderLayout2 = new BorderLayout();

    private ConsolaPanel2() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ConsolaPanel2 getInstancia() {
            ControladorVista.getInstancia().setInterfaz(instancia);

       
        return instancia;
    }

    private void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        this.setBorder(BorderFactory.createTitledBorder("Consola de Comandos"));
        consolaTextArea.setBackground(new Color(31, 31, 31));
        consolaTextArea.setCaretColor(Color.white);
        consolaTextArea.setForeground(new Color(0, 205, 0));
        consolaTextArea.setEditable(false);
        jPanel1.setLayout(borderLayout2);
        comandoField.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    comandoField_keyTyped(e);
                }
            });
        jScrollPane1.getViewport().add(consolaTextArea, null);
        this.add(jScrollPane1, BorderLayout.CENTER);
        jPanel1.add(comandoField, BorderLayout.CENTER);
        this.add(jPanel1, BorderLayout.NORTH);
    }

    private void log(String quien, String dato) {
        consolaTextArea.append(quien + " : " + dato + "\n");
    }

    private void comandoField_keyTyped(KeyEvent e) {
        if (e.getKeyChar() == e.VK_ENTER) {
            //log("yo","enter presionado");
            Thread t=new Thread(){
                public void run(){
                    try {
                        ControladorVista.getInstancia().escribirPuertoConexion(comandoField.getText() +
                                                                               "\r");
                        comandoField.setText("");
                    } catch (IOException f) {
                        log("yo", f.getMessage());
                    } catch (Exception f) {
                        log("yo", f.getMessage());
                    }
                }
            };
         t.start();   
           
        }
        /*   try {
       ControladorVista c= ControladorVista.getInstancia();
            if(e.getKeyChar()== e.VK_ENTER){
                comandoField.setText("");
                c.escribirPuertoConexion("\r");
            }
            else
            c.escribirPuertoConexion(""+e.getKeyChar());
        } catch (Exception f) {
            log("yo",f.toString());
        } */
    }


    public void detenerMuestreo(boolean detener) {
    }

    public void tratarRespuesta(RespuestaOBD respuesta) {
             RespuestaOBD s = respuesta;
            
            while (s != null) {
                if(s instanceof ErrorDTC){
                    log("elm",
                        (s != null) ?  s.getDescripcion() :
                        "null");
                }else
                if (s instanceof DTC) {
                    DTC r = (DTC)s;
                    log("elm",
                        (s != null) ? (Integer.toHexString(s.getServicio()) +
                                       ((s.getPid() < 0x10) ?
                                        "0" + Integer.toHexString(s.getPid()) :
                                        Integer.toHexString(s.getPid())) + " " +
                                       r.getCodigo() + " " + s.getDescripcion()) :
                        "null");
                } else

                if (s instanceof InformacionVehiculo) {
                    InformacionVehiculo r = (InformacionVehiculo)s;
                    log("elm",
                        (s != null) ? (Integer.toHexString(s.getServicio()) +
                                       ((s.getPid() < 0x10) ?
                                        "0" + Integer.toHexString(s.getPid()) :
                                        Integer.toHexString(s.getPid())) + " " +
                                       " " + s.getDescripcion()) : "null");
                } else if (s instanceof MonitoreoPID) {
                    MonitoreoPID r = (MonitoreoPID)s;
                    log("elm",
                        (s != null) ? (Integer.toHexString(s.getServicio()) +
                                       ((s.getPid() < 0x10) ?
                                        "0" + Integer.toHexString(s.getPid()) :
                                        Integer.toHexString(s.getPid())) + " " +
                                       r.getValor() + " " + s.getDescripcion()) :
                        "null");
                } else if (s instanceof EstadoDTC) {
                    EstadoDTC r = (EstadoDTC)s;
                    log("elm",
                        (s != null) ? (Integer.toHexString(s.getServicio()) +
                                       ((s.getPid() < 0x10) ?
                                        "0" + Integer.toHexString(s.getPid()) :
                                        Integer.toHexString(s.getPid())) +
                                       " luz:" +
                                       ((r.getMIL() == 1) ? "Encedida" : "Apagada") +
                                       " N° DTC :" + r.getNumeroDTCs() + " " +
                                       s.getDescripcion()) : "null");
                } else if (s instanceof PIDSoportados) {
                    PIDSoportados r = (PIDSoportados)s;
                    log("elm",
                        (s != null) ? (Integer.toHexString(s.getServicio()) +
                                       ((s.getPid() < 0x10) ?
                                        "0" + Integer.toHexString(s.getPid()) :
                                        Integer.toHexString(s.getPid())) + " " +
                                       r.getPidSoportados() + " " +
                                       s.getDescripcion()) : "null");
                } else if (s instanceof SensorOxigeno) {
                    SensorOxigeno r = (SensorOxigeno)s;
                    log("elm",
                        (s != null) ? (Integer.toHexString(s.getServicio()) +
                                       ((s.getPid() < 0x10) ?
                                        "0" + Integer.toHexString(s.getPid()) :
                                        Integer.toHexString(s.getPid())) + " " +
                                       r.getValor() + " Volts " +
                                       r.getPorcentaje() + "  % " +
                                       s.getDescripcion()) : "null");
                }
                s = s.getSiguiente();
            }

         
    }
}
