package application.view.helper;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DueDateTableCellRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 1L;
    private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Date date = (Date) value;
        if (new Date().compareTo(date) > 0) {
            cell.setBackground(new Color(255, 64, 58));
        } else {
            Color background = isSelected ? SystemColor.textHighlight : SystemColor.window;
            cell.setBackground(background);
        }

        setText((value == null) ? "" : format.format(value));

        return cell;
    }
}
