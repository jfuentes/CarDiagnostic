package util;

import javax.swing.table.DefaultTableModel;

public class ModelTable extends DefaultTableModel{
    public boolean isCellEditable(int row, int column){
        return false;
    }
}
