package util;

import java.util.Enumeration;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

public class JRadioButtonGroup extends ButtonGroup {
    public JRadioButtonGroup() {
        super();
    }
    public JRadioButton getSelectionRadioButton(){
        for (Enumeration e=this.getElements(); e.hasMoreElements(); ) {
               JRadioButton b = (JRadioButton)e.nextElement();
            if (b.getModel() == super.getSelection()) 
                   return b;
               
        }
    return null;
    }
    public void removeAll(){
        
        for (Enumeration e=this.getElements(); e.hasMoreElements(); ) {
               JRadioButton b = (JRadioButton)e.nextElement();
                 this.remove(b);
               
        }
    }
}
