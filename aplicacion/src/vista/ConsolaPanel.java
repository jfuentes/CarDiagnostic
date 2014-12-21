package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;

import java.awt.event.KeyAdapter;

import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class ConsolaPanel extends JPanel {
    private BorderLayout borderLayout1 = new BorderLayout();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JTextArea consolaTextArea = new JTextArea();
    private static final ConsolaPanel instancia=new ConsolaPanel();
    private JPanel jPanel1 = new JPanel();
    private BorderLayout borderLayout2 = new BorderLayout();

    private ConsolaPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static ConsolaPanel getInstancia(){
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
        jScrollPane1.getViewport().add(consolaTextArea, null);
        this.add(jScrollPane1, BorderLayout.CENTER);
        this.add(jPanel1, BorderLayout.NORTH);
    }
    public void log(String quien,String dato){
       // consolaTextArea.append(quien+" : "+dato + "\n");
       consolaTextArea.append( dato + "\n");
    }

     
}
