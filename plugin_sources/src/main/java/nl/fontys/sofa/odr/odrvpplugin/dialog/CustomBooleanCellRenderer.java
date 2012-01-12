/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.dialog;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author eigo
 */
public class CustomBooleanCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JCheckBox comp = new JCheckBox();
        comp.setEnabled(table.getModel().isCellEditable(row, column));
        comp.setHorizontalAlignment(JCheckBox.CENTER);
        comp.setVerticalAlignment(JCheckBox.CENTER);
        comp.setBackground(Color.WHITE);
        comp.setSelected((Boolean) value);
        return comp;
    }
}
