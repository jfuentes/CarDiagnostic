package pruebasArquitectura;


/* --------------
 * DialDemo1.java
 * --------------
 * (C) Copyright 2006, by Object Refinery Limited.
 */


import conexion.BufferEvent;
import conexion.BufferEventListener;

import conexion.Conexion;

import conexion.ConexionException;
import conexion.ConexionSerial;
import conexion.MuchosListenerException;
import conexion.OperacionNoSoportadaException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.Point;

import java.awt.event.MouseAdapter;

import java.awt.event.MouseEvent;

import java.io.IOException;

import javax.bluetooth.DiscoveryListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.experimental.chart.plot.dial.DialPlot;
import org.jfree.experimental.chart.plot.dial.SimpleDialFrame;
import org.jfree.experimental.chart.plot.dial.DialTextAnnotation;
import org.jfree.experimental.chart.plot.dial.StandardDialRange;
import org.jfree.experimental.chart.plot.dial.StandardDialScale;
import org.jfree.experimental.chart.plot.dial.DialBackground;
import org.jfree.experimental.chart.plot.dial.DialCap;
import org.jfree.experimental.chart.plot.dial.DialPointer;
import org.jfree.experimental.chart.plot.dial.DialValueIndicator;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;

/**
 * A sample application showing the use of a {@link DialPlot}.
 */
public class DialDemo1 extends JFrame implements ChangeListener, BufferEventListener {
    boolean ban=true;
    /** A slider to update the dataset value. */
    JSlider slider;
    
    /** The dataset. */
    DefaultValueDataset dataset;
    
    /** 
     * Creates a new instance.
     *
     * @param title  the frame title.
     */
    
    
       Conexion co;
    private JButton jButton1 = new JButton();
    private JMenuBar jMenuBar1 = new JMenuBar();
    private JMenu jMenu1 = new JMenu();


    public DialDemo1(String title) {
        super(title);
        
        this.dataset = new DefaultValueDataset(0.0);
        
        // get data for diagrams
        DialPlot plot = new DialPlot();
        plot.setView(0.0, 0.0,1, 1);
        
        plot.setDataset(this.dataset);
        SimpleDialFrame dialFrame = new SimpleDialFrame();
        dialFrame.setBackgroundPaint(Color.black);
        dialFrame.setForegroundPaint(Color.darkGray);
        plot.setDialFrame(dialFrame);
        
        GradientPaint gp = new GradientPaint(new Point(), 
                 new Color( 200, 150,  200), new Point(), 
                new Color(30, 150, 30));
        DialBackground db = new DialBackground(gp);
         
        db.setGradientPaintTransformer(new StandardGradientPaintTransformer(
                GradientPaintTransformType.VERTICAL));
        plot.setBackground(db);
        
        DialTextAnnotation annotation1 = new DialTextAnnotation("RPM");
        
        annotation1.setFont(new Font("Dialog", Font.BOLD, 15));
        annotation1.setRadius(0.7);
        
        plot.addLayer(annotation1);

        DialValueIndicator dvi = new DialValueIndicator(0, "c");
        //dvi.setFont(new Font("",Font.BOLD,11));
        plot.addLayer(dvi);
        
        StandardDialScale scale = new StandardDialScale(0, 300, -120, -300);
        scale.setTickRadius(0.88);
        scale.setTickLabelOffset(0.15);
        scale.setMajorTickIncrement(20);
        scale.setTickLabelPaint(Color.red);
        scale.setMajorTickPaint(Color.white);
        Font f=new Font("Dialog", Font.PLAIN, 11);
        
        scale.setTickLabelFont(new Font("Dialog", Font.PLAIN, 11));
        
        plot.addScale(0, scale);
        
        
        StandardDialRange range = new StandardDialRange(3000.0, 3600.0, Color.red);
        range.setInnerRadius(0.52);
        range.setOuterRadius(0.55);
        plot.addLayer(range);
        
        StandardDialRange range2 = new StandardDialRange(2000.0, 2990.0, 
                Color.orange);
        range2.setInnerRadius(0.52);
        range2.setOuterRadius(0.55);
        plot.addLayer(range2);

        StandardDialRange range3 = new StandardDialRange(0.0, 1990.0, 
                Color.green);
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
        chart1.setTitle("Demo Dial 1");
        ChartPanel cp1 = new ChartPanel(chart1);
        cp1.setPreferredSize(new Dimension(400, 400));
        this.slider = new JSlider(0, 300);
        this.slider.setMajorTickSpacing(50);
        this.slider.setPaintLabels(true);
        this.slider.addChangeListener(this);
        JPanel content = new JPanel(new BorderLayout());
        content.add(cp1);
        content.add(this.slider, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(content);
        pack();
        setVisible(true);
       // conectar();
    }

    public DialDemo1() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle a change in the slider by updating the dataset value.  This
     * automatically triggers a chart repaint.
     *
     * @param e  the event.
     */
    public void stateChanged(ChangeEvent e) {
        this.dataset.setValue(new Integer(this.slider.getValue()));
    }

    /**
     * Starting point for the demo application.
     * 
     * @param args  ignored.
     */
    public static void main(String[] args) {
        DialDemo1 app = new DialDemo1("JFreeChart - Demo Dial 1");
        
        //String puerto = (String)puertos.getItemAt(puertos.getSelectedIndex());
       
    }
    public void conectar(){
        co = new ConexionSerial("COM18", 9600);
        try {
            co.addBufferEventListener(this);
        } catch (MuchosListenerException f) {
            log(f.toString() );
        } catch (OperacionNoSoportadaException f) {
            log(f.toString());
        }
        try {
            co.abrirConexion();
            log("conexion abierta a:COM18");
        }  catch (ConexionException f) {
            log(f.toString());
        }
      /*   try {
            co.escribirBuffer("ate0\r");
             Thread.sleep(1000);
            co.escribirBuffer("0100\r");
            Thread.sleep(1000); 
        } catch (IOException e) {
            log(e.toString());
        } catch (InterruptedException e) {
        } */


       /* while(true){
            if(ban){
            ban=false;
            try {
                co.escribirPuerto("010c\r");
                
            } catch (IOException e) {
                log(e.toString());
            } 
            }
        }*/
    }
    public void log(String a){
        System.err.println(a);
        
    }

    public void bufferSalidaEscrito(BufferEvent e) {
    }

    public void bufferEntradaEscrito(BufferEvent e) {
          
           int i=0;  try{
        /* String s=co.getBufferConexion().leerBufferEntrada();
        //log("buffer de entrada escrito con: "+s);
        s=s.charAt(6)+""+s.charAt(7)+""+s.charAt(9)+""+s.charAt(10);
        //log("trim: "+s); */
        String s=co.getBufferConexion().leerBufferEntrada();
        log("entrada: "+s); 
        s=s.substring(6,8);
                         
        
        StringBuffer sb=new StringBuffer(s);
        
       
        
        i=Integer.parseInt(s,16);
        
        this.dataset.setValue(i);
        }catch(Exception ee){log(ee.toString());}
        if (co != null) {
          //  log(i + "\n");
        }
        ban=true;
       /*  try {
           // co.escribirPuerto("010d\r");
            
        } catch (IOException ed) {
            log(ed.toString());
        }  */
        
    }

    private void jbInit() throws Exception {
        this.setSize(new Dimension(563, 300));
        this.setJMenuBar(jMenuBar1);
        jMenu1.setText("click");
        jMenu1.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    jMenu1_mousePressed(e);
                }
            });
        jMenuBar1.add(jMenu1);
    }

    private void jMenu1_mousePressed(MouseEvent e) {
        
    }
}
