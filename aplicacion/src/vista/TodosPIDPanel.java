package vista;

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

import java.awt.event.MouseAdapter;

import java.awt.event.MouseEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.io.PrintWriter;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import modelo.ErrorDTC;
import modelo.MonitoreoPID;
import modelo.PIDSoportados;
import modelo.RespuestaOBD;

import modelo.SensorOxigeno;

import org.jdesktop.swingx.JXTable;

import util.ComandosELM;
import util.ModelTable;

public class TodosPIDPanel extends JPanel implements Manipulable {

    private JLabel jLabel1 = new JLabel();
    private Icon icon = new ImageIcon("imagenes/pdf.png");
    private Icon icon2 = new ImageIcon("imagenes/icon_excel.gif");
    private Icon iconRefrezcar = new ImageIcon("imagenes/repeat.png");
    private JButton pdfBoton = new JButton(icon);
    private JButton excelBoton = new JButton(icon2);


    private void mostrarExceptionConexion(ConexionException f) {
        JOptionPane.showMessageDialog(this, f.getMessage(),
                                      "Excepción de conexión",
                                      JOptionPane.ERROR_MESSAGE);
    }

    private void jButton1_mousePressed(MouseEvent e) {

        String csv = "reportes/reporte_pids.tmp";
        try {
            FileWriter fw = new FileWriter(csv);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter salida = new PrintWriter(bw);
            for (int i = 0; i < this.modeloTablaPIDs2_19.getRowCount(); i++) {
                salida.println(this.modeloTablaPIDs2_19.getValueAt(i, 0) +
                               "|" +
                               this.modeloTablaPIDs2_19.getValueAt(i, 1) +
                               "|" +
                               this.modeloTablaPIDs2_19.getValueAt(i, 2) +
                               "|" +
                               this.modeloTablaPIDs2_19.getValueAt(i, 3));
            }
            for (int i = 0; i < this.modeloTablaPIDs21_39.getRowCount(); i++) {
                salida.println(this.modeloTablaPIDs21_39.getValueAt(i, 0) +
                               "|" +
                               this.modeloTablaPIDs21_39.getValueAt(i, 1) +
                               "|" +
                               this.modeloTablaPIDs21_39.getValueAt(i, 2) +
                               "|" +
                               this.modeloTablaPIDs21_39.getValueAt(i, 3));
            }
            for (int i = 0; i < this.modeloTablaPIDs41_59.getRowCount(); i++) {
                salida.println(this.modeloTablaPIDs41_59.getValueAt(i, 0) +
                               "|" +
                               this.modeloTablaPIDs41_59.getValueAt(i, 1) +
                               "|" +
                               this.modeloTablaPIDs41_59.getValueAt(i, 2) +
                               "|" +
                               this.modeloTablaPIDs41_59.getValueAt(i, 3));
            }
            salida.close();
        } catch (IOException ioex) {
            System.out.println("Error al generar archivo temporal: " +
                               ioex.toString());
        }
        Reporte.displayReport("reportes/pids.jasper",
                              new String[] { "PID", "DESCRIPCION", "VALOR",
                                             "UNIDAD" }, csv);
    }

    private void jButton2_mousePressed(MouseEvent e) {
        /*  this.modeloTablaPIDs2_19.addRow(new String[]{"ada","asda","123.4","RMP"});
        this.modeloTablaPIDs21_39.addRow(new String[]{"adasda","asda","123.4","RMP"});
        this.modeloTablaPIDs41_59.addRow(new String[]{"adasda","asda","123.4","RMP"});  */
        String csv = "reportes/reporte_pids.tmp";
        try {
            FileWriter fw = new FileWriter(csv);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter salida = new PrintWriter(bw);
            for (int i = 0; i < this.modeloTablaPIDs2_19.getRowCount(); i++) {
                salida.println(this.modeloTablaPIDs2_19.getValueAt(i, 0) +
                               "|" +
                               this.modeloTablaPIDs2_19.getValueAt(i, 1) +
                               "|" +
                               this.modeloTablaPIDs2_19.getValueAt(i, 2) +
                               "|" +
                               this.modeloTablaPIDs2_19.getValueAt(i, 3));
            }
            for (int i = 0; i < this.modeloTablaPIDs21_39.getRowCount(); i++) {
                salida.println(this.modeloTablaPIDs21_39.getValueAt(i, 0) +
                               "|" +
                               this.modeloTablaPIDs21_39.getValueAt(i, 1) +
                               "|" +
                               this.modeloTablaPIDs21_39.getValueAt(i, 2) +
                               "|" +
                               this.modeloTablaPIDs21_39.getValueAt(i, 3));
            }
            for (int i = 0; i < this.modeloTablaPIDs41_59.getRowCount(); i++) {
                salida.println(this.modeloTablaPIDs41_59.getValueAt(i, 0) +
                               "|" +
                               this.modeloTablaPIDs41_59.getValueAt(i, 1) +
                               "|" +
                               this.modeloTablaPIDs41_59.getValueAt(i, 2) +
                               "|" +
                               this.modeloTablaPIDs41_59.getValueAt(i, 3));
            }
            salida.close();
        } catch (IOException ioex) {
            System.out.println("Error al generar archivo temporal: " +
                               ioex.toString());
        }
        Reporte.displayReportEXCEL("reportes/pids_excel.jasper",
                                   new String[] { "PID", "DESCRIPCION",
                                                  "VALOR", "UNIDAD" }, csv);
    }

    public void tratarRespuesta(RespuestaOBD respuesta) {

        if (respuesta instanceof MonitoreoPID)
            System.out.println("####monitore");
        if (respuesta instanceof SensorOxigeno)
            System.out.println("####oxigeno");
        if (respuesta instanceof PIDSoportados)
            System.out.println("####pid soportados");
        if (respuesta instanceof ErrorDTC)
            System.out.println("####Error");
        // es de otra peticion


        if (respuesta instanceof PIDSoportados) {
            System.out.println(" ##############sip difinitivamente;");
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

            }
        }


        if (respuesta != null && respuesta instanceof ErrorDTC) {
            System.out.println("------ERROR-----------------------------------------frame" +
                               respuesta.getNumeroFrame() + " pis:" +
                               respuesta.getPid());
            if (respuesta.getNumeroFrame() == 120) {
                modeloTablaPIDs21_39.addRow(new String[] { "SIN DATOS" });
            } else if (respuesta.getNumeroFrame() == 140) {
                modeloTablaPIDs41_59.addRow(new String[] { "SIN DATOS" });
            }
        }

        if (respuesta instanceof MonitoreoPID ||
            respuesta instanceof SensorOxigeno) {
            System.out.println(" tipo monitoreo++++++++++++++++++++++++++++++++++++++++++++");

            if (respuesta.getPid() < 0x20) {

                if (!direccionesTabla020.containsKey("" +
                                                     respuesta.getPid())) {
                    direccionesTabla020.put("" + respuesta.getPid(), cont++);
                    modeloTablaPIDs2_19.addRow(new String[] { "", "" });
                }

                // la inserta en una fila asignada
                int row = direccionesTabla020.get("" + respuesta.getPid());
                String servicio = Integer.toHexString(respuesta.getServicio());
                String pid =
                    (respuesta.getPid() < 0x10) ? "0" + Integer.toHexString(respuesta.getPid()) :
                    Integer.toHexString(respuesta.getPid());

                modeloTablaPIDs2_19.setValueAt("" + servicio + pid, row, 0);
                modeloTablaPIDs2_19.setValueAt(respuesta.getDescripcion(), row,
                                               1);
                if (respuesta instanceof MonitoreoPID) {
                    modeloTablaPIDs2_19.setValueAt(((MonitoreoPID)respuesta).getValor(),
                                                   row, 2);
                    modeloTablaPIDs2_19.setValueAt(((MonitoreoPID)respuesta).getMedicion(),
                                                   row, 3);
                } else {
                    modeloTablaPIDs2_19.setValueAt(((SensorOxigeno)respuesta).getValor() +
                                                   " Volt " +
                                                   ((SensorOxigeno)respuesta).getPorcentaje() +
                                                   " %", row, 2);

                }

            } else if (respuesta.getPid() > 0x20 &&
                       respuesta.getPid() < 0x40) {

                if (!direccionesTabla4060.containsKey("" +
                                                      respuesta.getPid())) {
                    direccionesTabla4060.put("" + respuesta.getPid(), cont2++);
                    modeloTablaPIDs21_39.addRow(new String[] { "", "" });
                }

                // la inserta en una fila asignada
                int row = direccionesTabla4060.get("" + respuesta.getPid());
                String servicio = Integer.toHexString(respuesta.getServicio());
                String pid =
                    (respuesta.getPid() < 0x10) ? "0" + Integer.toHexString(respuesta.getPid()) :
                    Integer.toHexString(respuesta.getPid());

                modeloTablaPIDs21_39.setValueAt("" + servicio + pid, row, 0);
                modeloTablaPIDs21_39.setValueAt(respuesta.getDescripcion(),
                                                row, 1);
                if (respuesta instanceof MonitoreoPID) {
                    modeloTablaPIDs21_39.setValueAt(((MonitoreoPID)respuesta).getValor(),
                                                    row, 2);
                    modeloTablaPIDs21_39.setValueAt(((MonitoreoPID)respuesta).getMedicion(),
                                                    row, 3);
                } else {
                    modeloTablaPIDs21_39.setValueAt(((SensorOxigeno)respuesta).getValor() +
                                                    " Volt " +
                                                    ((SensorOxigeno)respuesta).getPorcentaje() +
                                                    " %", row, 2);

                }
            } else if (respuesta.getPid() > 0x40) {
                if (!direccionesTabla6080.containsKey("" +
                                                      respuesta.getPid())) {
                    direccionesTabla6080.put("" + respuesta.getPid(), cont3++);
                    modeloTablaPIDs41_59.addRow(new String[] { "", "" });
                }

                // la inserta en una fila asignada
                int row = direccionesTabla6080.get("" + respuesta.getPid());
                String servicio = Integer.toHexString(respuesta.getServicio());
                String pid =
                    (respuesta.getPid() < 0x10) ? "0" + Integer.toHexString(respuesta.getPid()) :
                    Integer.toHexString(respuesta.getPid());

                modeloTablaPIDs41_59.setValueAt("" + servicio + pid, row, 0);
                modeloTablaPIDs41_59.setValueAt(respuesta.getDescripcion(),
                                                row, 1);
                if (respuesta instanceof MonitoreoPID) {
                    modeloTablaPIDs41_59.setValueAt(((MonitoreoPID)respuesta).getValor(),
                                                    row, 2);
                    modeloTablaPIDs41_59.setValueAt(((MonitoreoPID)respuesta).getMedicion(),
                                                    row, 3);
                } else {
                    modeloTablaPIDs41_59.setValueAt(((SensorOxigeno)respuesta).getValor() +
                                                    " Volt " +
                                                    ((SensorOxigeno)respuesta).getPorcentaje() +
                                                    " %", row, 2);

                }

            }


        }

    }

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


                } while (pid == 0x13 || pid == 0x00 || pid == 0x01);

                System.out.println("pid retornado-----------------------:" +
                                   Integer.toHexString(pid));
                return "01" +
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


                } while (pid == 0x13);

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

    class HiloLector extends Thread {
        Servicio_00 s00;
        Servicio_20 s20;
        Servicio_40 s40;
        boolean parar = false;

        public HiloLector() {

        }

        public void run() {
            System.out.println("comenzando el hilo todos pid");
            try {
                testearNuevamente:
                while(!parar){
                if(!parar)
                ControladorVista.getInstancia().escribirPuertoConexion("0100\r",
                                                                       100);
                if (!parar)
                ControladorVista.getInstancia().escribirPuertoConexion("0120\r",
                                                                       120);
                if (!parar)
                ControladorVista.getInstancia().escribirPuertoConexion("0140\r",
                                                                       140);
                while ( !parar) {

                    if (s00 != null) {
                        ControladorVista.getInstancia().escribirPuertoConexion(s00.getNext(),
                                                                               110);
                    }
                    if (s20 != null) {
                        ControladorVista.getInstancia().escribirPuertoConexion(s20.getNext(),
                                                                               130);
                    }
                    if (s40 != null) {
                        ControladorVista.getInstancia().escribirPuertoConexion(s20.getNext(),
                                                                               150);
                    }
                    if (s00 == null && s20 == null && s40 == null) {
                        System.out.println("son todos nulos");
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                            }
                        continue testearNuevamente;
                     
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

        public void parar(boolean p){
            parar=p;
        }
        public void setServicio(Servicio_00 s) {
            //System.out.println("servicio 00%%%%%%%%%%%%%%%%%%%%%%%%%");
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

    Servicio_00 servicio00;
    Servicio_20 servicio20;
    Servicio_40 servicio40;


    int turno = 0, peticiones = 0;
    int cont = 0, cont2, cont3;
    LinkedHashMap<String, Integer> direccionesTabla020 =
        new LinkedHashMap<String, Integer>();
    LinkedHashMap<String, Integer> direccionesTabla4060 =
        new LinkedHashMap<String, Integer>();
    LinkedHashMap<String, Integer> direccionesTabla6080 =
        new LinkedHashMap<String, Integer>();

    private JToolBar jToolBar1 = new JToolBar();
    private JButton actualizaBoton = new JButton();
    private JTabbedPane jTabbedPane1 = new JTabbedPane();
    private JPanel jPanel1 = new JPanel();
    private JPanel panelTablaPIDs21_39 = new JPanel();
    private JPanel jPanel3 = new JPanel();
    private BorderLayout borderLayout1 = new BorderLayout();
    // --------------------------------------
    private static final TodosPIDPanel instancia = new TodosPIDPanel();
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
    public static TodosPIDPanel getInstancia() {
        ControladorVista control = ControladorVista.getInstancia();
      control.setInterfaz(instancia);
        instancia.detenido = false;
     // instancia.getTodosPIDs();
      return instancia;
    }

    private TodosPIDPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        /* jButton1.setContentAreaFilled(false);

        jButton2.setContentAreaFilled(false); */
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


        excelBoton.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    jButton2_mousePressed(e);
                }
            });

        pdfBoton.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    jButton1_mousePressed(e);
                }
            });
        jLabel1.setText("    Exportar a:   ");
        this.setLayout(borderLayout1);
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
        jToolBar1.add(actualizaBoton, null);
        jToolBar1.add(jLabel1, null);
        jToolBar1.add(pdfBoton, null);
        jToolBar1.add(excelBoton, null);
        jScrollPane1.getViewport().add(tablaPids2_19, null);
        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
        jTabbedPane1.addTab("PIDs 4- 19 servicio $01", jPanel1);
        jScrollPane2.getViewport().add(tablaPIDs21_39, null);
        panelTablaPIDs21_39.add(jScrollPane2, BorderLayout.CENTER);
        jTabbedPane1.addTab("PIDs 21-39 servicio $01", panelTablaPIDs21_39);
        jScrollPane3.getViewport().add(tablaPIDs41_60, null);
        jPanel3.add(jScrollPane3, BorderLayout.CENTER);
        jTabbedPane1.addTab("PIDs 41-60 servicio $01", jPanel3);
        this.add(jTabbedPane1, BorderLayout.CENTER);
        this.add(jToolBar1, BorderLayout.NORTH);
    }

    private void jButton1_actionPerformed(ActionEvent e) {
        getTodosPIDs();
    }


    private void getTodosPIDs() {
        detenerMuestreo(true);
        ControladorVista control = ControladorVista.getInstancia();

        direccionesTabla020.clear(); //= new LinkedHashMap<String, Integer>();
        this.direccionesTabla4060.clear();
        this.direccionesTabla6080.clear();
        cont = 0;
        cont2 = 0;
        cont3 = 0;

        while (modeloTablaPIDs2_19.getRowCount() > 0)
            modeloTablaPIDs2_19.removeRow(0);
        while (modeloTablaPIDs21_39.getRowCount() > 0)
            modeloTablaPIDs21_39.removeRow(0);
        while (modeloTablaPIDs41_59.getRowCount() > 0)
            modeloTablaPIDs41_59.removeRow(0);
        ControladorVista.getInstancia().cancelarEscrituraLectura();

        hilo = new HiloLector();
        hilo.start();

        System.out.println("hilo todo pid:" + hilo.getState().toString());
        /*  if(hilo.getState()==Thread.State.WAITING ||hilo.getState()==Thread.State.TIMED_WAITING || hilo.getState()==Thread.State.BLOCKED)
            hilo.resume();
        else
        if(hilo.getState()==Thread.State.NEW) hilo.start();
        else

         if(hilo.getState()==Thread.State.TERMINATED){

         }  */


    }

    public void detenerMuestreo(boolean detener) {

        if (hilo != null) {
            if (hilo.getState() == Thread.State.WAITING ||
                hilo.getState() == Thread.State.TIMED_WAITING ||
                hilo.getState() == Thread.State.BLOCKED){
                hilo.yield();
                hilo.stop();
            hilo=null;
        }
            else {
                System.out.println("hilo todos pid estado al detener &&&&&&&6 " +
                                   hilo.getState().toString());
                hilo.stop();
                hilo = null;
            }
        }

        detenido = detener;
    }
}
