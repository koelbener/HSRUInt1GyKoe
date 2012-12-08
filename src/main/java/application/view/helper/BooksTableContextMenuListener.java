package application.view.helper;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.JTable;

import com.google.common.primitives.Ints;

public class BooksTableContextMenuListener extends AbstractContextMenuListener {

    private final BooksContextMenu menu;
    private final JTable table;

    public BooksTableContextMenuListener(final JTable table, ActionListener newBookActionListener, ActionListener openBookActionListener) {
        super();
        this.table = table;
        this.table.addMouseListener(new SimpleMouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isMetaDown()) {
                    int r = table.rowAtPoint(e.getPoint());
                    if (!Ints.contains(table.getSelectedRows(), r)) {

                        if (r >= 0 && r < table.getRowCount()) {
                            table.setRowSelectionInterval(r, r);
                        } else {
                            table.clearSelection();
                        }
                    } // else: current row is selected, do nothing
                }
            }
        });
        menu = new BooksContextMenu(newBookActionListener, openBookActionListener);
    }

    @Override
    protected JPopupMenu getMenu() {
        return menu;
    }

    public void updateTexts() {
        menu.updateTexts();
    }

}
