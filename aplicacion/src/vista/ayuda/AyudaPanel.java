package vista.ayuda;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import javax.swing.tree.DefaultMutableTreeNode;

import javax.swing.tree.TreeSelectionModel;

import modelo.MonitoreoPID;

import org.jdesktop.swingx.JXEditorPane;
import org.jdesktop.swingx.JXTree;

public class AyudaPanel extends JPanel implements TreeSelectionListener {
    private JSplitPane jSplitPane1 = new JSplitPane();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JXTree jXTree1;
    private JPanel jPanel1 = new JPanel();
    private JXEditorPane jXEditorPane1 = new JXEditorPane();
    private JScrollPane jScrollPane2 = new JScrollPane();
    private BorderLayout borderLayout1 = new BorderLayout();
    private BorderLayout borderLayout2 = new BorderLayout();
    private List contenidos= new ArrayList<Contenido>();

    public AyudaPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        DefaultMutableTreeNode top =
            new DefaultMutableTreeNode("Escaner Automotriz");
        cargarNodos(top);
        jXTree1 = new JXTree(top);

        this.setLayout(borderLayout2);
        this.setSize(new Dimension(565, 303));
        jPanel1.setLayout(borderLayout1);
        jXEditorPane1.setMinimumSize(new Dimension(479, 200));
        jXEditorPane1.setPreferredSize(new Dimension(479, 200));

        jScrollPane1.getViewport().add(jXTree1, null);
        jSplitPane1.add(jScrollPane1, JSplitPane.LEFT);
        jScrollPane2.getViewport().add(jXEditorPane1, null);
        jPanel1.add(jScrollPane2, BorderLayout.CENTER);
        jSplitPane1.add(jPanel1, JSplitPane.RIGHT);
        this.add(jSplitPane1, BorderLayout.WEST);


        jXTree1.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        jXTree1.addTreeSelectionListener(this);

      
        HTMLEditorKit kit = new HTMLEditorKit();
        jXEditorPane1.setEditorKit(kit);

        // add some styles to the html
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("body {color:#000; font-family:times; margin: 4px; }");
        styleSheet.addRule("h1 {color: blue;}");
        styleSheet.addRule("h2 {color: #ff0000;}");
        styleSheet.addRule("pre {font : 10px monaco; color : black; background-color : #fafafa; }");

        Document doc = kit.createDefaultDocument();
        jXEditorPane1.setDocument(doc);
        jXEditorPane1.setEditable(false);
        

    }

    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node =
            (DefaultMutableTreeNode)jXTree1.getLastSelectedPathComponent();

        if (node == null)
            //Nothing is selected.
            return;

        String nodeInfo =(String)node.getUserObject();
        
        if (node.isLeaf()) {
            // create some simple html as a string
        for (int i=0; i<contenidos.size();i++){
                if(((Contenido)contenidos.get(i)).getId().equals(nodeInfo)){
                    this.jXEditorPane1.setText(((Contenido)contenidos.get(i)).getTexto());
                }
            }
            
        } else {

        }
    }

    private void cargarNodos(DefaultMutableTreeNode top) {
        FileReader fr;
        BufferedReader bf;
        String linea;
        DefaultMutableTreeNode categoria = null;
        DefaultMutableTreeNode item = null;
        try {
            fr = new FileReader("arch/ayuda.car");
            bf = new BufferedReader(fr);
            while ((linea = bf.readLine()) != null) {
                if(linea.contains("<categoria>")){
                    linea= linea.replace("<categoria>", "");
                    linea= linea.replace("</categoria>", "");
                    categoria= new DefaultMutableTreeNode(linea);
                    top.add(categoria);
                }else
                    if(linea.contains("<item>")){
                        linea= linea.replace("<item>", "");
                        linea= linea.replace("</item>", "");
                        item=new DefaultMutableTreeNode(linea);
                        if(categoria!=null) categoria.add(item);
                        
                }else
                    if(linea.contains("<contenido>")){
                        String conte= linea.replace("<contenido>", "");
                        linea = bf.readLine();
                        while(linea!=null && !linea.contains("</contenido>")){
                            conte+=linea;
                            linea = bf.readLine();
                        }
                        conte+=linea.replace("</contenido>", "");
                        Contenido c= new Contenido((String) item.getUserObject(),conte);
                        contenidos.add(c);
                    }
                    
                
                    
                
                

            }

        } catch (IOException e) {
            System.out.println("error al leer archivo de ayuda");
            }catch(Exception ee){
                System.out.println("Archivo de ayuda corrupto");
            }
        
        /* DefaultMutableTreeNode category = null;
        //DefaultMutableTreeNode item = null;

        category = new DefaultMutableTreeNode("Introducción");
        top.add(category);

        //original Tutorial

        item = new DefaultMutableTreeNode("Instalación");
        category.add(item);
        DefaultMutableTreeNode item2 = new DefaultMutableTreeNode("Conexión");
        category.add(item); */


        //Tutorial Continued
        /* book = new DefaultMutableTreeNode(new BookInfo
                ("The Java Tutorial Continued: The Rest of the JDK",
                "tutorialcont.html"));
            category.add(book);

            //JFC Swing Tutorial
            book = new DefaultMutableTreeNode(new BookInfo
                ("The JFC Swing Tutorial: A Guide to Constructing GUIs",
                "swingtutorial.html"));
            category.add(book);

            //...add more books for programmers...

            category = new DefaultMutableTreeNode("Books for Java Implementers");
            top.add(category);

            //VM
            book = new DefaultMutableTreeNode(new BookInfo
                ("The Java Virtual Machine Specification",
                 "vm.html"));
            category.add(book);

            //Language Spec
            book = new DefaultMutableTreeNode(new BookInfo
                ("The Java Language Specification",
                 "jls.html"));
            category.add(book); */
    }
}
