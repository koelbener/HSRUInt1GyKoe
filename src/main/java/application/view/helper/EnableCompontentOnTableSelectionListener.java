package application.view.helper;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class EnableCompontentOnTableSelectionListener implements ListSelectionListener {

    private final JTable table;
    private final JComponent component;

    public EnableCompontentOnTableSelectionListener(JTable table, JComponent component) {
        this.table = table;
        this.component = component;
        table.getSelectionModel().addListSelectionListener(this);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int[] rows = table.getSelectedRows();
        component.setEnabled(rows != null && rows.length > 0);
    }

}
