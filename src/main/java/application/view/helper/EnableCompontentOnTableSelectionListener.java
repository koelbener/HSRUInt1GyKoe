package application.view.helper;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class EnableCompontentOnTableSelectionListener implements ListSelectionListener {

    private final JTable table;
    private final JComponent component;
    private boolean onlySingleSelection = false;

    public EnableCompontentOnTableSelectionListener(JTable table, JComponent component) {
        this.table = table;
        this.component = component;
        table.getSelectionModel().addListSelectionListener(this);
    }

    public EnableCompontentOnTableSelectionListener(JTable table, JComponent component, boolean onlySingleSelection) {
        this(table, component);
        this.onlySingleSelection = onlySingleSelection;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int[] selectedRows = table.getSelectedRows();
        boolean enabled = (selectedRows != null && selectedRows.length > 0);
        if (onlySingleSelection) {
            enabled = (enabled && selectedRows.length == 1);
        }
        component.setEnabled(enabled);
    }

}
