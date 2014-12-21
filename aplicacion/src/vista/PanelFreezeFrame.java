package vista;

//91

import conexion.BufferEvent;
import conexion.BufferEventListener;

import conexion.ConexionException;
import conexion.MuchosListenerException;
import conexion.OperacionNoSoportadaException;

import conexion.TimeOutException;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import modelo.DTC;
import modelo.ErrorDTC;
import modelo.MonitoreoPID;
import modelo.PIDSoportados;
import modelo.RespuestaOBD;

import org.jdesktop.swingx.JXTable;

import util.ComandosELM;
import util.ModelTable;

public class PanelFreezeFrame extends JPanel implements Manipulable {

    /** USA NUMERO DE FRAME DE 200-300.
     */
    private JPanel jPanel2 = new JPanel();
    private JTextField dtcCausaTextField = new JTextField();
    private BorderLayout borderLayout1 = new BorderLayout();
    private BorderLayout borderLayout5 = new BorderLayout();
    private Icon iconRefrezcar = new ImageIcon("imagenes/repeat.png");


    class Servicio_00 {
        int puntero = 0;
        List<Integer> pidSoportados = null;

        Servicio_00(List<Integer> pid) {

            pidSoportados = pid;
        }

        /**
         retorna un comando
         * @return
         */
        String getNext() {
            // acttua como un ronda
            if (pidSoportados != null) {
                int pid = 0;
                do {
                    pid = pidSoportados.get(puntero);
                    puntero = (puntero + 1) % pidSoportados.size();


                } while (pid == 0x13 || pid == 0x00 || pid == 0x02);
                return "02" +
                    ((pid < 0x10) ? "0" + Integer.toHexString(pid) : Integer.toHexString(pid)) +
                    "\r"; // 0100\r es un ejemplo
            }
            return "\r"; // repitira solo el ultimo comando
        }
    }

    class Servicio_20 {
        int puntero = 0;
        List<Integer> pidSoportados = null;

        Servicio_20(List<Integer> pid) {

            pidSoportados = pid;
        }

        /**
         retorna un comando
         * @return
         */
        String getNext() {
            // acttua como un ronda
            if (pidSoportados != null) {
                int pid = 0;
                do {
                    pid = pidSoportados.get(puntero);
                    puntero = (puntero + 1) % pidSoportados.size();


                } while (pid == 0x13 || pid == 0x00 || pid == 0x02);

                return "02" +
                    ((pid < 0x10) ? "0" + Integer.toHexString(pid) : Integer.toHexString(pid)) +
                    "\r"; // 0100\r es un ejemplo
            }
            return "\r"; // repitira solo el ultimo comando
        }
    }

    class Servicio_40 {
        int puntero = 0;
        List<Integer> pidSoportados = null;

        Servicio_40(List<Integer> pid) {

            pidSoportados = pid;
        }

        /**
         retorna un comando
         * @return
         */
        String getNext() {
            // acttua como un ronda
            if (pidSoportados != null) {
                int pid = 0;
                do {
                    pid = pidSoportados.get(puntero);
                    puntero = (puntero + 1) % pidSoportados.size();


                } while (pid == 0x13);

                return "02" +
                    ((pid < 0x10) ? "0" + Integer.toHexString(pid) : Integer.toHexString(pid)) +
                    "\r"; // 0100\r es un ejemplo
            }
            return "\r"; // repitira solo el ultimo comando
        }
    }
    Servicio_00 servicio00;
    Servicio_20 servicio20;
    Servicio_40 servicio40;

    class HiloLector extends Thread {
        Servicio_00 s00;
        Servicio_20 s20;
        Servicio_40 s40;
        boolean parar = false;
 

        public void run() {
            System.out.println("comenzando el hilo todos pid");
            int i=180; // son 60 pid 3 veces cada uno
            try {
                if(!parar)
                ControladorVista.getInstancia().escribirPuertoConexion("0102\r",
                                                                       266);
                if (!parar)
                ControladorVista.getInstancia().escribirPuertoConexion("0200\r",
                                                                      200);
                /*  ControladorVista.getInstancia().escribirPuertoConexion("0220\r",
                                                                       220);
                 ControladorVista.getInstancia().escribirPuertoConexion("0240\r",
                                                                       240); */
                /*
                 * este ultimo código borra los codigos de error
                 */
                while (i-->0 && !parar) {

                    if (s00 != null) {
                        ControladorVista.getInstancia().escribirPuertoConexion(s00.getNext(),
                                                                               210);
                    }
                    if (s20 != null) {
                        ControladorVista.getInstancia().escribirPuertoConexion(s20.getNext(),
                                                                               230);
                    }
                    if (s40 != null) {
                        ControladorVista.getInstancia().escribirPuertoConexion(s20.getNext(),
                                                                               250);
                    }
                    if (s00 == null && s20 == null && s40 == null) {
                        System.out.println("son todos nulos");
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            } catch (IOException e) {
                mostrarException(e);
            } catch (ConexionException e) {
                mostrarException(e);
            } catch (TimeOutException e) {
                mostrarException(e);
            }

        }

        public void parar(boolean p) {
            parar = p;
        }
        public void setServicio(Servicio_00 s) {
            System.out.println("servicio 00%%%%%%%%%%%%%%%%%%%%%%%%%");
            s00 = s;
        }

        public void setServicio(Servicio_20 s) {
            s20 = s;
        }

        public void setServicio(Servicio_40 s) {
            s40 = s;
        }


    }
    HiloLector hilo;

    private void mostrarException(Exception ee) {
        if (ee instanceof NullPointerException) {
            JOptionPane.showMessageDialog(this,
                                          "Excepción interna del programa, intente más tarde",
                                          "Excepción",
                                          JOptionPane.ERROR_MESSAGE);
        }
        if (ee instanceof ConexionException) {
            JOptionPane.showMessageDialog(this,
                                          ((ConexionException)ee).getMessage(),
                                          "Excepción de conexión",
                                          JOptionPane.ERROR_MESSAGE);

        }
        if (ee instanceof MuchosListenerException) {
            JOptionPane.showMessageDialog(this,
                                          ((MuchosListenerException)ee).getMessage(),
                                          "Excepción interna ",
                                          JOptionPane.ERROR_MESSAGE);

        }
        if (ee instanceof OperacionNoSoportadaException) {
            JOptionPane.showMessageDialog(this,
                                          ((OperacionNoSoportadaException)ee).getMessage(),
                                          "Excepción aporación no soportada",
                                          JOptionPane.ERROR_MESSAGE);

        }
        if (ee instanceof TimeOutException) {
            JOptionPane.showMessageDialog(this,
                                          "La Interfaz no reponde, verifique la conexión",
                                          "Excepción Tiempo de espera agota",
                                          JOptionPane.ERROR_MESSAGE);
        }

    }

    int cont = 0;
    LinkedHashMap<Integer, Integer> direccionesTabla =
        new LinkedHashMap<Integer, Integer>();

    private JToolBar jToolBar1 = new JToolBar();
    private JButton actualizaBoton = new JButton();
    private JTabbedPane jTabbedPane1 = new JTabbedPane();
    private JPanel jPanel1 = new JPanel();
    private JPanel panelTablaPIDs21_39 = new JPanel();
    private JPanel jPanel3 = new JPanel();
    // --------------------------------------
    private static final PanelFreezeFrame instancia = new PanelFreezeFrame();
    //---------------------------------------------------
    ModelTable modeloTablaPIDs2_19 = new ModelTable();
    //---------------------------------------------------
    ModelTable modeloTablaPIDs21_39 = new ModelTable();
    //---------------------------------------------------
    ModelTable modeloTablaPIDs41_59 = new ModelTable();
    private boolean detenido = false;


    private JScrollPane jScrollPane1 = new JScrollPane();
    private JScrollPane jScrollPane2 = new JScrollPane();
    private JScrollPane jScrollPane3 = new JScrollPane();
    private BorderLayout borderLayout2 = new BorderLayout();
    private BorderLayout borderLayout3 = new BorderLayout();
    private BorderLayout borderLayout4 = new BorderLayout();
    private JXTable tablaPids2_19 = new JXTable();
    private JXTable tablaPIDs21_39 = new JXTable();
    private JXTable tablaPIDs41_60 = new JXTable();

    public static PanelFreezeFrame getInstancia() {
        ControladorVista control = ControladorVista.getInstancia();
        control.setInterfaz(instancia);
        instancia.detenido = false;

        //instancia.getTodosPIDs();


        return instancia;
    }

    private PanelFreezeFrame() {
        try {
            jbInit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        modeloTablaPIDs2_19.addColumn("PID");
        modeloTablaPIDs2_19.addColumn("Descripción");
        modeloTablaPIDs2_19.addColumn("Valor");
        modeloTablaPIDs2_19.addColumn("Unidad de Medida");

        modeloTablaPIDs21_39.addColumn("PID");
        modeloTablaPIDs21_39.addColumn("Descripción");
        modeloTablaPIDs21_39.addColumn("Valor");
        modeloTablaPIDs21_39.addColumn("Unidad de Medida");

        modeloTablaPIDs41_59.addColumn("PID");
        modeloTablaPIDs41_59.addColumn("Descripción");
        modeloTablaPIDs41_59.addColumn("Valor");
        modeloTablaPIDs41_59.addColumn("Unidad de Medida");
        this.tablaPids2_19.setModel(modeloTablaPIDs2_19);
        this.tablaPIDs21_39.setModel(modeloTablaPIDs21_39);
        this.tablaPIDs41_60.setModel(modeloTablaPIDs41_59);


        jPanel2.setLayout(borderLayout1);
        dtcCausaTextField.setEditable(false);
        dtcCausaTextField.setMinimumSize(new Dimension(6, 19));
        dtcCausaTextField.setPreferredSize(new Dimension(6, 19));
        this.setLayout(borderLayout5);
        this.setSize(new Dimension(463, 300));
        actualizaBoton.setText("Actualizar");
        actualizaBoton.setIcon(iconRefrezcar);
        actualizaBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jButton1_actionPerformed(e);
                }
            });
        jPanel1.setLayout(borderLayout2);
        panelTablaPIDs21_39.setLayout(borderLayout3);
        jPanel3.setLayout(borderLayout4);
        jPanel3.setEnabled(false);
        jScrollPane2.setEnabled(false);
        jToolBar1.add(actualizaBoton, null);
        jScrollPane1.getViewport().add(tablaPids2_19, null);
        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
        jTabbedPane1.addTab("PIDs 4- 19 servicio $02", jPanel1);
        jScrollPane2.getViewport().add(tablaPIDs21_39, null);
        panelTablaPIDs21_39.add(jScrollPane2, BorderLayout.CENTER);
        jTabbedPane1.addTab("PIDs 21-39 servicio $02", panelTablaPIDs21_39);
        jScrollPane3.getViewport().add(tablaPIDs41_60, null);
        jPanel3.add(jScrollPane3, BorderLayout.CENTER);
        jTabbedPane1.addTab("PIDs 41-60 servicio $02", jPanel3);
        jPanel2.add(dtcCausaTextField, BorderLayout.CENTER);
        this.add(jPanel2, BorderLayout.SOUTH);
        this.add(jTabbedPane1, BorderLayout.CENTER);
        this.add(jToolBar1, BorderLayout.NORTH);
    }

    private void jButton1_actionPerformed(ActionEvent e) {
        getTodosPIDs();
    }

    private void mostrarExceptionConexion(ConexionException f) {
        JOptionPane.showMessageDialog(this, f.getMessage(),
                                      "Excepción de conexión",
                                      JOptionPane.ERROR_MESSAGE);
    }


    private void getTodosPIDs() {
        ControladorVista control = ControladorVista.getInstancia();
        //direccionesTabla = new LinkedHashMap<Integer, Integer>();
        direccionesTabla.clear();
        cont = 0;

        while (modeloTablaPIDs2_19.getRowCount() > 0)
            modeloTablaPIDs2_19.removeRow(0);
        while (modeloTablaPIDs21_39.getRowCount() > 0)
            modeloTablaPIDs21_39.removeRow(0);
        while (modeloTablaPIDs41_59.getRowCount() > 0)
            modeloTablaPIDs41_59.removeRow(0);
        modeloTablaPIDs21_39.addRow(new String[] { "SIN DATOS" });
        modeloTablaPIDs41_59.addRow(new String[] { "SIN DATOS" });

       // ControladorVista controladorVista = ControladorVista.getInstancia();
        detenerMuestreo(true);
        hilo = new HiloLector();
        hilo.start();
    }

    public void detenerMuestreo(boolean detener) {
        if(hilo!=null)
        hilo.parar(true);
        ControladorVista controladorVista = ControladorVista.getInstancia();
        controladorVista.cancelarEscrituraLectura();
       

    }

    public void tratarRespuesta(RespuestaOBD r) {
        RespuestaOBD respuesta = r;

        /*  if (respuesta.getNumeroFrame() < 200 ||
            respuesta.getNumeroFrame() >= 300) {

            return;
        } */
        if (respuesta != null && respuesta instanceof MonitoreoPID) {
            MonitoreoPID mpid = (MonitoreoPID)respuesta;
            if (!direccionesTabla.containsKey(mpid.getPid())) {
                direccionesTabla.put(mpid.getPid(), cont++);
                modeloTablaPIDs2_19.addRow(new String[] { "", "" });

            }

            // la inserta en una fila asignada
            int row = direccionesTabla.get(mpid.getPid());
            String servicio = Integer.toHexString(mpid.getServicio());
            String pid =
                (mpid.getPid() < 0x10) ? "0" + Integer.toHexString(mpid.getPid()) :
                Integer.toHexString(mpid.getPid());

            modeloTablaPIDs2_19.setValueAt("" + servicio + pid, row, 0);
            modeloTablaPIDs2_19.setValueAt(mpid.getDescripcion(), row, 1);
            modeloTablaPIDs2_19.setValueAt(mpid.getValor(), row, 2);
            modeloTablaPIDs2_19.setValueAt(mpid.getMedicion(), row, 3);
        }
        
        if (respuesta != null && respuesta instanceof PIDSoportados) {
            switch (respuesta.getPid()) {
            case 0x00:
                servicio00 =
                        new Servicio_00(((PIDSoportados)respuesta).getPidSoportados());
                hilo.setServicio(servicio00);
                break;
            case 0x20:
                servicio20 =
                        new Servicio_20(((PIDSoportados)respuesta).getPidSoportados());
                hilo.setServicio(servicio20);
                break;
            case 0x40:
                servicio40 =
                        new Servicio_40(((PIDSoportados)respuesta).getPidSoportados());
                hilo.setServicio(servicio40);
                break;
            }
        }

        if (respuesta != null && respuesta instanceof DTC) {
            DTC d = (DTC)respuesta;
            dtcCausaTextField.setText("Causados por:" + d.getCodigo() + "  " +
                                      d.getDescripcion());

        } else if (respuesta != null && respuesta instanceof ErrorDTC) {
            if (respuesta.getNumeroFrame() == 220) {
                modeloTablaPIDs21_39.addRow(new String[] { "SIN DATOS" });
            } else if (respuesta.getNumeroFrame() == 240) {
                modeloTablaPIDs41_59.addRow(new String[] { "SIN DATOS" });
            }
        }

    }

}
