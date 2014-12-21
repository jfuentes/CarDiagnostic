package vista.estadisticas;

import conexion.BufferEvent;
import conexion.BufferEventListener;

import conexion.ConexionException;
import conexion.MuchosListenerException;
import conexion.OperacionNoSoportadaException;

import conexion.TimeOutException;

import java.awt.BorderLayout;
import java.awt.Rectangle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import modelo.MonitoreoPID;
import modelo.RespuestaOBD;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import util.ComandosELM;

import vista.ControladorVista;
import vista.Manipulable;

public class PanelEstadisticas extends JPanel implements Manipulable {
    private Icon iconPause = new ImageIcon("imagenes/pause.png");
    private Icon iconPlay = new ImageIcon("imagenes/play.png");
    private JToolBar jToolBar1 = new JToolBar();
    private JPanel jPanel1 = new JPanel();
    
    
    /**The time revoluciones data.
     */
    private TimeSeries revoluciones;
    private TimeSeries velocidad;

    /** The most recent value added. */
    private double lastValue = 100.0;
    private BorderLayout borderLayout1 = new BorderLayout();
    static final PanelEstadisticas instancia=new PanelEstadisticas();
    private boolean detener=false;
    private boolean esPausa=true;
    private JToggleButton pausaBoton = new JToggleButton();


    class HiloLector extends Thread {
        boolean parar = false;

        public void run() {
            System.out.println("comenzando el hilo todos pid");
            try {
                while (!parar) {
                   /*  ControladorVista.getInstancia().escribirPuertoConexion(ComandosELM.VELOCIDAD_010D,
                                                                           13);
                    ControladorVista.getInstancia().escribirPuertoConexion(ComandosELM.TEMPERATURA_0105,
                                                                           5); */
                    /*   ControladorVista.getInstancia().escribirPuertoConexion("0110\r",
                                                                           0x10); */

                    
                        ControladorVista.getInstancia().escribirPuertoConexion(ComandosELM.RMP_010C,
                                                                           0x0c);
                    
                }

            } catch (IOException e) {
            mostrarException(e);   
            } catch (ConexionException e) {
                mostrarException(e);
            } catch (TimeOutException e) {
                mostrarException(e);
            }


        }
        public void parar(boolean para){
            parar=para;
        }

    }
    HiloLector hilo;
    private void mostrarException(Exception ee){
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
        if(ee instanceof TimeOutException){
            JOptionPane.showMessageDialog(this,
                                          "La Interfaz no reponde, verifique la conexión",
                                          "Excepción Tiempo de espera agota",
                                          JOptionPane.ERROR_MESSAGE);
        }

    }
    public static PanelEstadisticas getInstancia(){

            instancia.detener=instancia.pausaBoton.isSelected();
            ControladorVista.getInstancia().setInterfaz(instancia);
      instancia.inciarMuestreo();
        return instancia;
        
    }
    private PanelEstadisticas() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        pausaBoton.setText("Pausa");
        pausaBoton.setIcon(iconPause);
        pausaBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    pausaBoton_actionPerformed(e);
                }
            });
        jToolBar1.add(pausaBoton, null);
        this.add(iniciarGrafica(), BorderLayout.CENTER);
        this.add(jToolBar1, BorderLayout.NORTH);
    }
    
    public  ChartPanel iniciarGrafica(){
       
        this.revoluciones = new TimeSeries("RPM", Millisecond.class);
        velocidad= new TimeSeries("KPH", Millisecond.class);
        final TimeSeriesCollection dataset = new TimeSeriesCollection(this.revoluciones);
        final TimeSeriesCollection dataset2 = new TimeSeriesCollection(this.velocidad);
        
        final JFreeChart chart = createChart(dataset);

        final ChartPanel chartPanel = new ChartPanel(chart);
           
        return chartPanel;
        
    }
   
    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
            "Estadísticas", 
            "Tiempo", 
            "RPM",
            dataset, 
            true, 
            true, 
            false
        );
        final XYPlot plot = result.getXYPlot();
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(60000.0);  // 60 seconds
        axis = plot.getRangeAxis();
        axis.setRange(0, 7000); 
        return result;
    }
    
    public void load(double l){
       // final double factor = 0.90 + 0.2 * Math.random();
        this.lastValue = l;
        final Millisecond now = new Millisecond();
        //System.out.println("Now = " + now.toString());
        //this.velocidad.add(new Millisecond(), this.lastValue-100);
        this.revoluciones.add(new Millisecond(), this.lastValue);
    }

     
    private   void inciarMuestreo() {
       ControladorVista.getInstancia().cancelarEscrituraLectura();
        if(!pausaBoton.isSelected()){
       hilo=new HiloLector();
       hilo.start();
       }
    }

    public void detenerMuestreo(boolean detener) {
        if(hilo!=null)
        hilo.parar(true);
        ControladorVista.getInstancia().cancelarEscrituraLectura();
    }

    private void pausaBoton_actionPerformed(ActionEvent e) {
        if(pausaBoton.isSelected()){
            if (hilo != null)
                hilo.parar(true);
            ControladorVista.getInstancia().cancelarEscrituraLectura();
            this.esPausa=false;
        }else{
            inciarMuestreo();
            this.esPausa=true;
            
        }
        if(this.esPausa){
            pausaBoton.setText("Pause");
            pausaBoton.setIcon(this.iconPause);
        }else{
            pausaBoton.setText("Play");
            pausaBoton.setIcon(this.iconPlay);
        }
     
    }

    public void tratarRespuesta(RespuestaOBD r) {
        RespuestaOBD respuesta=r;
        if(respuesta !=null && respuesta instanceof MonitoreoPID){
            MonitoreoPID m=(MonitoreoPID)respuesta;
            load(m.getValor());
           }          
      }
}
