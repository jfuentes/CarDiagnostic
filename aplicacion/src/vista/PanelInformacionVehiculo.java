package vista;

import conexion.BufferEvent;
import conexion.BufferEventListener;

import conexion.ConexionException;
import conexion.OperacionNoSoportadaException;

import java.awt.BorderLayout;
import java.awt.Rectangle;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import modelo.ErrorDTC;
import modelo.InformacionVehiculo;
import modelo.RespuestaOBD;

import org.jdesktop.swingx.JXTable;

import util.ComandosELM;
import util.ModelTable;

public   class PanelInformacionVehiculo extends JPanel implements Manipulable {
    private JTabbedPane jTabbedPane1 = new JTabbedPane();
    private JXTable tablaInformacionVehiculo = new JXTable();

    private static final PanelInformacionVehiculo instancia =
        new PanelInformacionVehiculo();

    //---------------------------------------------------
    int cont = 0;
    ModelTable modeloTabla = new ModelTable();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private BorderLayout borderLayout1 = new BorderLayout();


    public static PanelInformacionVehiculo getInstancia() {
        ControladorVista control = ControladorVista.getInstancia();
        instancia.cont = 0;
           if (control.estaConectado()) {
                     control.setInterfaz(instancia);
               
                instancia.getInformacionVehiculo();
            }
     
        return instancia;
    }

    private PanelInformacionVehiculo() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        modeloTabla.addColumn("Parámetro");
        modeloTabla.addColumn("Descripción");
        tablaInformacionVehiculo.setModel(modeloTabla);

        this.setLayout(borderLayout1);
        jScrollPane1.getViewport().add(tablaInformacionVehiculo, null);
        jTabbedPane1.addTab("Información del Vehículo (servicio $09)",
                            jScrollPane1);
        this.add(jTabbedPane1, BorderLayout.CENTER);
    }

    private void mostrarExcepcionConexion(ConexionException e) {
        JOptionPane.showMessageDialog(instancia, e.getMessage(),
                                      "Error de conexión",
                                      JOptionPane.ERROR_MESSAGE);
    }

    private void getInformacionVehiculo() {
         cont = 0;

        while (modeloTabla.getRowCount() > 0)
            modeloTabla.removeRow(0);
        this.detenerMuestreo(true);
        Thread t=new Thread(){
            public void run(){
                ControladorVista control = ControladorVista.getInstancia();

                if (control.estaConectado()) {
                    try {
                        control.escribirPuertoConexion(ComandosELM.INFORMACION_VEHICULO_09,
                                                       900);
                        control.escribirPuertoConexion(ComandosELM.INFORMACION_VEHICULO_09,
                                                       900);
                        control.escribirPuertoConexion(ComandosELM.INFORMACION_VEHICULO_09,
                                                       900);
                    } catch (Exception e) {
                    }
                } else {
                    JOptionPane.showMessageDialog(PanelInformacionVehiculo.this,
                                                  "Operacion Fallida, No está conectado");
                }
            }
        };
        t.start();
        
    }

    public void detenerMuestreo(boolean detener) {
        ControladorVista.getInstancia().cancelarEscrituraLectura();
    }

    public void tratarRespuesta(RespuestaOBD r) {
               
                 if(r instanceof InformacionVehiculo){
                     InformacionVehiculo iv=(InformacionVehiculo)r;
                     modeloTabla.addRow(new String[]{"info",iv.getInfo()});
                }else{
                    if(r instanceof ErrorDTC){
                        if(modeloTabla.getRowCount()>0){
                            modeloTabla.setValueAt("SIN DATOS", 0, 0);
                        }else
                        modeloTabla.addRow(new String[]{"SIN DATOS"});
                    }
                }
                 
            
        }

     
}
