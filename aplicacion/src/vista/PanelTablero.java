package vista;

import conexion.BufferEvent;
import conexion.BufferEventListener;

import conexion.ConexionException;
import conexion.MuchosListenerException;
import conexion.OperacionNoSoportadaException;

import conexion.TimeOutException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

import java.io.IOException;

import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javax.swing.JSlider;

import javax.swing.JTabbedPane;

import modelo.MonitoreoPID;
import modelo.RespuestaOBD;

import oracle.jdeveloper.layout.BoxLayout2;

import oracle.jdeveloper.layout.PaneConstraints;
import oracle.jdeveloper.layout.PaneLayout;

import oracle.jdeveloper.layout.VerticalFlowLayout;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.JThermometer;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.experimental.chart.plot.dial.DialBackground;
import org.jfree.experimental.chart.plot.dial.DialCap;
import org.jfree.experimental.chart.plot.dial.DialPlot;
import org.jfree.experimental.chart.plot.dial.DialPointer;
import org.jfree.experimental.chart.plot.dial.DialTextAnnotation;
import org.jfree.experimental.chart.plot.dial.DialValueIndicator;
import org.jfree.experimental.chart.plot.dial.SimpleDialFrame;
import org.jfree.experimental.chart.plot.dial.StandardDialRange;
import org.jfree.experimental.chart.plot.dial.StandardDialScale;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;

import util.ComandosELM;

public class PanelTablero extends JPanel implements 
                                                    Manipulable {
    /*calculo del MPG ao KPL
     *  MPG = (14,7 * 6,17 * 4,54 * * VSS 0.621371) / (3600 * MAF / 100)
    = 710.7 * VSS / MAF = 710,7 VSS * / MAF

    MPG - miles per gallon MPG - millas por galón
    14.7 grams of air to 1 gram of gasoline - ideal air/fuel ratio 14,7 gramos de aire a 1 gramo de gasolina - aire ideal / combustible
    6.17 pounds per gallon - density of gasoline £ 6.17 por galón - la densidad de la gasolina
    4.54 grams per pound - conversion 4,54 gramos por libra - conversión
    VSS - vehicle speed in kilometers per hour VSS - la velocidad del vehículo en kilómetros por hora
    0.621371 miles per hour/kilometers per hour - conversion 0.621371 millas por hora / kilómetros por hora - la conversión
    3600 seconds per hour - conversion 3600 segundos por hora - la conversión
    MAF - mass air flow rate in 100 grams per second MAF - tasa de flujo de masa de aire en 100 gramos por segundo
    100 - to correct MAF to give grams per second 100 - para corregir MAF para dar gramos por segundo

     */
    //de mpg a kilometros por litro
    /*
     * Example:25 mpg = 0.4251 * 25 = 10.627 kpl

     */
    private JPanel rpmPanel = new JPanel();
    private JPanel temperaturaPanel = new JPanel();
    private JPanel kmhPanel = new JPanel();
    private JPanel jPanel4 = new JPanel();
    private JPanel jPanel5 = new JPanel();
    

    // para los relojes
    DefaultValueDataset valorRPM;
    DefaultValueDataset valorKPM;
    private boolean detener = false;
    private double ultimoValor;

    private double distancia=0;
    private double kplIntante=0;
    private double kplPromedio=0;
    private double maf = -1;
    private double velocidad=-1;
    private double n=0; // contador de n muestras
    
    private BorderLayout borderLayout2 = new BorderLayout();

    private static final PanelTablero instancia = new PanelTablero();
    private JThermometer thermo1 = new JThermometer();
    private BorderLayout borderLayout3 = new BorderLayout();
    private BorderLayout borderLayout4 = new BorderLayout();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JTabbedPane jTabbedPane3 = new JTabbedPane();
    private JPanel jPanel2 = new JPanel();
    private JTabbedPane jTabbedPane5 = new JTabbedPane();
    private JPanel jPanel6 = new JPanel();
    private JTabbedPane jTabbedPane7 = new JTabbedPane();
    private JPanel jPanel8 = new JPanel();
    private JFormattedTextField totalKPL = new JFormattedTextField();
    private VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
    private JFormattedTextField intantaneoKPL = new JFormattedTextField();
    private JFormattedTextField distanciaRecorrida = new JFormattedTextField();
    private VerticalFlowLayout verticalFlowLayout3 = new VerticalFlowLayout();
    private VerticalFlowLayout verticalFlowLayout5 = new VerticalFlowLayout();
    private GridLayout gridLayout1 = new GridLayout();
    private BorderLayout borderLayout5 = new BorderLayout();
    
    
    
    
    /*
     * hilo lector
     *  
     */

    class HiloLector extends Thread {
        boolean parar = false;
      public void run() {
            System.out.println("comenzando el hilo todos pid");
            try {
                while (true) {
                   
                        ControladorVista.getInstancia().escribirPuertoConexion(ComandosELM.VELOCIDAD_010D,
                                                                       13);
                  
                    ControladorVista.getInstancia().escribirPuertoConexion(ComandosELM.TEMPERATURA_0105,
                                                                       5);
                ControladorVista.getInstancia().escribirPuertoConexion(ComandosELM.RMP_010C,
                                                                       0x0c);
                    ControladorVista.getInstancia().escribirPuertoConexion("0110\r",
                                                                           0x10);

                }
                    
            } catch (IOException e) {
                mostrarException(e);
            } catch (ConexionException e) {
                mostrarException(e);
            } catch (TimeOutException e) {
                mostrarException(e);
            }

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
    public static PanelTablero getInstancia() {
        instancia.detener = false;
        instancia.cambiarListener();

        return instancia;
    }

    public void cambiarListener() {
       ControladorVista.getInstancia().setInterfaz(this);
        ControladorVista.getInstancia().cancelarEscrituraLectura();
        hilo=new HiloLector();
       hilo.start();
        
    }

    private PanelTablero() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ChartPanel iniciarRelojVelocidad() {
        this.valorKPM = new DefaultValueDataset(0);
        DialPlot plot = new DialPlot();
        plot.setView(0.0, 0.0, 1, 1);
        plot.setDataset(this.valorKPM);

        SimpleDialFrame dialFrame = new SimpleDialFrame();
        dialFrame.setBackgroundPaint(Color.black);
        dialFrame.setForegroundPaint(Color.darkGray);
        plot.setDialFrame(dialFrame);

        GradientPaint gp =
            new GradientPaint(new Point(), new Color(100, 150, 100),
                              new Point(), new Color(0, 0, 0));
        DialBackground db = new DialBackground(gp);

        db.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.VERTICAL));
        plot.setBackground(db);

        DialTextAnnotation annotation1 = new DialTextAnnotation("K/H");

        annotation1.setFont(new Font("Dialog", Font.BOLD, 15));
        annotation1.setRadius(0.7);

        plot.addLayer(annotation1);

        DialValueIndicator dvi = new DialValueIndicator(0, "K/h");
        //dvi.setFont(new Font("",Font.BOLD,11));
        plot.addLayer(dvi);

        StandardDialScale scale = new StandardDialScale(0, 300, -120, -300);
        scale.setTickRadius(0.88);
        scale.setTickLabelOffset(0.15);
        scale.setMajorTickIncrement(20);
        scale.setTickLabelPaint(Color.red);
        scale.setMajorTickPaint(Color.white);
        Font f = new Font("Dialog", Font.PLAIN, 11);

        scale.setTickLabelFont(new Font("Dialog", Font.PLAIN, 11));

        plot.addScale(0, scale);


        StandardDialRange range =
            new StandardDialRange(200.0, 300.0, Color.red);
        range.setInnerRadius(0.52);
        range.setOuterRadius(0.55);
        plot.addLayer(range);

        StandardDialRange range2 =
            new StandardDialRange(2000.0, 2990.0, Color.orange);
        range2.setInnerRadius(0.52);
        range2.setOuterRadius(0.55);
        //plot.addLayer(range2);

        StandardDialRange range3 =
            new StandardDialRange(0.0, 199.0, Color.green);
        range3.setInnerRadius(0.52);
        range3.setOuterRadius(0.55);
        plot.addLayer(range3);

        DialPointer needle = new DialPointer.Pointer();
        plot.addLayer(needle);


        DialCap cap = new DialCap();
        cap.setRadius(0.10);
        cap.setFillPaint(Color.gray);
        cap.setOutlinePaint(Color.blue);
        plot.setCap(cap);
        JFreeChart chart1 = new JFreeChart(plot);
        //chart1.setTitle("Demo Dial 1");
        ChartPanel cp1 = new ChartPanel(chart1);
        //cp1.setPreferredSize(new Dimension(400, 400));
        return cp1;

    }

    public ChartPanel iniciarRelojRPM() {
        this.valorRPM = new DefaultValueDataset(0);
        DialPlot plot = new DialPlot();
        plot.setView(0.0, 0.0, 1, 1);
        plot.setDataset(this.valorRPM);

        SimpleDialFrame dialFrame = new SimpleDialFrame();
        dialFrame.setBackgroundPaint(Color.black);
        dialFrame.setForegroundPaint(Color.darkGray);
        plot.setDialFrame(dialFrame);

        GradientPaint gp =
            new GradientPaint(new Point(), new Color(100, 150, 100),
                              new Point(), new Color(0, 0, 0));
        DialBackground db = new DialBackground(gp);

        db.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.VERTICAL));
        plot.setBackground(db);

        DialTextAnnotation annotation1 = new DialTextAnnotation("RPM");

        annotation1.setFont(new Font("Dialog", Font.BOLD, 15));
        annotation1.setRadius(0.7);

        plot.addLayer(annotation1);

        DialValueIndicator dvi = new DialValueIndicator(0, "RPM");
        dvi.setTemplateValue(11111);
        //dvi.setFont(new Font("",Font.BOLD,11));
        plot.addLayer(dvi);

        StandardDialScale scale = new StandardDialScale(0, 7000, -120, -300);
        scale.setTickRadius(0.88);
        scale.setTickLabelOffset(0.15);
        scale.setMajorTickIncrement(500);
        scale.setTickLabelPaint(Color.red);
        scale.setMajorTickPaint(Color.white);
        Font f = new Font("Dialog", Font.PLAIN, 11);

        scale.setTickLabelFont(new Font("Dialog", Font.PLAIN, 11));

        plot.addScale(0, scale);


        StandardDialRange range =
            new StandardDialRange(6000.0, 7000.0, Color.red);
        range.setInnerRadius(0.52);
        range.setOuterRadius(0.55);
        plot.addLayer(range);

        StandardDialRange range2 =
            new StandardDialRange(4000.0, 5990.0, Color.orange);
        range2.setInnerRadius(0.52);
        range2.setOuterRadius(0.55);
        plot.addLayer(range2);

        StandardDialRange range3 =
            new StandardDialRange(0.0, 3990.0, Color.green);
        range3.setInnerRadius(0.52);
        range3.setOuterRadius(0.55);
        plot.addLayer(range3);

        DialPointer needle = new DialPointer.Pointer();
        plot.addLayer(needle);


        DialCap cap = new DialCap();
        cap.setRadius(0.10);
        cap.setFillPaint(Color.gray);
        cap.setOutlinePaint(Color.blue);
        plot.setCap(cap);
        JFreeChart chart1 = new JFreeChart(plot);
        //chart1.setTitle("Demo Dial 1");
        ChartPanel cp1 = new ChartPanel(chart1);
        // cp1.setPreferredSize(new Dimension(400, 400));
        return cp1;

    }

    public JPanel iniciarGraficoTemperatura() {
        // thermo1.setBackground(Color.white);
        //thermo1.setOutlinePaint(null);
        thermo1.setUnits(2);
        thermo1.setShowValueLines(true);
        // thermo1.setFollowDataInSubranges(true);
        thermo1.setForeground(Color.blue);

        thermo1.setRange(0, 130);
        /*    thermo1.setSubrangeInfo(0, 0, 60, 1, 60);
            thermo1.setSubrangeInfo(1, 61, 80, 61, 80);
            thermo1.setSubrangeInfo(2, 81, 100, 81, 100); */

        //  thermo1.addSubtitle("Sea Water Temp");
        thermo1.setValue(10);
        thermo1.setValueLocation(1);


        return thermo1;
    }

    private void jbInit() throws Exception {
        this.setLayout(borderLayout5);
        this.setSize(new Dimension(577, 368));
        this.setBorder(BorderFactory.createTitledBorder(""));
        rpmPanel.setBorder(BorderFactory.createTitledBorder("Revoluciones por minuto (RPM)"));
        rpmPanel.setLayout(borderLayout2);
        rpmPanel.add(this.iniciarRelojRPM(), BorderLayout.CENTER);

        temperaturaPanel.setBorder(BorderFactory.createTitledBorder("Temperatura"));
        temperaturaPanel.setLayout(borderLayout3);
        temperaturaPanel.add(this.iniciarGraficoTemperatura(),
                             BorderLayout.CENTER);
        kmhPanel.setBorder(BorderFactory.createTitledBorder("Kilómetros por hora (Km/h)"));
        kmhPanel.setLayout(borderLayout4);
        kmhPanel.add(this.iniciarRelojVelocidad(), BorderLayout.CENTER);
        jPanel4.setBorder(BorderFactory.createTitledBorder(""));
        jPanel4.setLayout(gridLayout1);
        jPanel5.setBorder(BorderFactory.createTitledBorder(""));
        jPanel5.setLayout(gridBagLayout1);
        jPanel2.setLayout(verticalFlowLayout5);
        jPanel6.setLayout(verticalFlowLayout3);
        jPanel8.setLayout(verticalFlowLayout1);
        totalKPL.setEditable(false);
        totalKPL.setPreferredSize(new Dimension(4, 30));
        totalKPL.setForeground(Color.blue);
        intantaneoKPL.setEditable(false);
        intantaneoKPL.setPreferredSize(new Dimension(4, 30));
        intantaneoKPL.setForeground(Color.blue);
        distanciaRecorrida.setEditable(false);
        distanciaRecorrida.setPreferredSize(new Dimension(4, 30));
        distanciaRecorrida.setForeground(Color.blue);
        jPanel5.add(rpmPanel,
                    new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                                           GridBagConstraints.BOTH,
                                           new Insets(0, 0, 0, 0), 181, 208));
        jPanel5.add(temperaturaPanel,
                    new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                                           GridBagConstraints.CENTER,
                                           GridBagConstraints.BOTH,
                                           new Insets(0, 0, 0, 0), 7, 265));
        jPanel5.add(kmhPanel,
                    new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                                           GridBagConstraints.BOTH,
                                           new Insets(0, 0, 0, 0), 182, 232));
        jPanel8.add(totalKPL, null);
        jTabbedPane7.addTab("Promedio Rendimiento Km/Lt", jPanel8);
        jPanel4.add(jTabbedPane7, null);
        jPanel6.add(intantaneoKPL, null);
        jTabbedPane5.addTab("Kilómetros por litro", jPanel6);
        jPanel4.add(jTabbedPane5, null);
        jPanel2.add(distanciaRecorrida, null);
        jTabbedPane3.addTab("Distancia recorrida", jPanel2);
        jPanel4.add(jTabbedPane3, null);
        this.add(jPanel4, BorderLayout.NORTH);
        this.add(jPanel5, BorderLayout.CENTER);
    }
  
    private void mostrarExcepcionConexion(ConexionException e) {
        JOptionPane.showMessageDialog(instancia, e.getMessage(),
                                      "Error de conexión",
                                      JOptionPane.ERROR_MESSAGE);
    }

    private void log(RespuestaOBD dato) {
        if (dato instanceof MonitoreoPID) {
            MonitoreoPID m = (MonitoreoPID)dato;
            if (m.getPid() == 0x0c) { //revoluciones
                valorRPM.setValue(m.getValor());

            } else if (m.getPid() == 0x10) { //revoluciones
                maf = m.getValor();

            } else if (m.getPid() == 0x0d) { //velocidad
                // calcula el kpl instantaneo y el acumulado
                valorKPM.setValue(m.getValor());
                // calcular kpl
                velocidad = m.getValor();
                if(maf >1 && velocidad >1){
                    kplIntante =
                        ((14.7 * 6.17 * 45.4 *  velocidad* 0.621371) / (3600 * maf / 100))* 0.4251;
                    
                 kplPromedio *= n;
                kplPromedio += kplIntante;
                    kplPromedio /= (++n);

                totalKPL.setText(String.format("%.2f", kplPromedio) +" Kilómetros / litro"); 
                intantaneoKPL.setText(String.format("%.2f", kplIntante)+" kilómetros / litro");
                // distancia
                Date d=new Date();
                if(ultimoValor>0){
                 double tiempoTranscurrido=d.getTime()-ultimoValor;
                 distancia=distancia+(tiempoTranscurrido/(60*60*1000))*velocidad;
                 distanciaRecorrida.setText(String.format("%.2f", distancia)+"  Kilómetros");
                }
                ultimoValor=d.getTime();   
                }

            } else {
                if (m.getPid() == 0x05) {
                      thermo1.setValue(m.getValor());
                }
            }

            ConsolaPanel consolaPanel = ConsolaPanel.getInstancia();
            consolaPanel.log("PanelTablero",
                             (dato != null) ? ("" + dato.getPid() +
                                               dato.getServicio()) : "null");
       
        }
    }


    public void detenerMuestreo(boolean detener) {
     //   System.out.println("deteniendo muestreo++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
       if(hilo!=null)
       hilo.stop();
       ControladorVista.getInstancia().cancelarEscrituraLectura();
        ControladorVista.getInstancia().cancelarEscrituraLectura();
    }

    public void tratarRespuesta(RespuestaOBD r) {
             RespuestaOBD s = r;
              log(s);
            // calcular la KPL


        }

}
