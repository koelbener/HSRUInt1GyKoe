package application.view.helper;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

import org.junit.Before;
import org.junit.Test;

public class EnableCompontentOnTableSelectionListenerTest {

    private JTable table;
    private JComponent component;
    private ListSelectionModel listSelectionModel;

    @Before
    public void setUp() throws Exception {
        table = mock(JTable.class);
        component = mock(JComponent.class);
        listSelectionModel = mock(ListSelectionModel.class);

        // prepare common mock calls
        when(table.getSelectionModel()).thenReturn(listSelectionModel);
        listSelectionModel.addListSelectionListener((ListSelectionListener) anyObject());
    }

    @Test
    public void noRowSelected() {
        int[] selectedRows = new int[] {};

        // Setup fixtures
        when(table.getSelectedRows()).thenReturn(selectedRows);

        // Exercise SUT
        EnableCompontentOnTableSelectionListener sut = new EnableCompontentOnTableSelectionListener(table, component);
        sut.valueChanged(null);

        // Verify output
        verify(component).setEnabled(false);

    }

    @Test
    public void oneRowSelected() {
        int[] selectedRows = new int[] { 0 };

        // Setup fixtures
        when(table.getSelectedRows()).thenReturn(selectedRows);

        // Exercise SUT
        EnableCompontentOnTableSelectionListener sut = new EnableCompontentOnTableSelectionListener(table, component);
        sut.valueChanged(null);

        // Verify output
        verify(component).setEnabled(true);

    }

    @Test
    public void multipleRowsSelected() {
        int[] selectedRows = new int[] { 0, 1 };

        // Setup fixtures
        when(table.getSelectedRows()).thenReturn(selectedRows);

        // Exercise SUT
        EnableCompontentOnTableSelectionListener sut = new EnableCompontentOnTableSelectionListener(table, component);
        sut.valueChanged(null);

        // Verify output
        verify(component).setEnabled(true);

    }
}
