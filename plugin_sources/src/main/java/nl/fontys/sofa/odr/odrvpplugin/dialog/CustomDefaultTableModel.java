/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.dialog;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author eigo
 */
public class CustomDefaultTableModel extends DefaultTableModel {

    private Class[] types;
    private List<Integer>[] editableCells;

    public CustomDefaultTableModel() {
        super(new Object[][]{}, new String[]{"Selection", "Name", "State"});

        types = new Class[]{
            java.lang.Boolean.class, java.lang.String.class, java.lang.String.class
        };
        editableCells = new List[3];
        for (int i = 0; i < editableCells.length; i++) {
            editableCells[i] = new ArrayList<Integer>();
        }
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        return types[columnIndex];
    }

    public void setCellEditable(int row, int coll, boolean editable) {
        if (editable) {
            editableCells[coll].add(row);
        } else {
            editableCells[coll].remove(Integer.valueOf(row));
        }
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        List<Integer> list = editableCells[i1];
        int myInt = Integer.valueOf(i);
        return list.contains(myInt);
    }
}
