package application.view.helper;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTable;

import com.google.common.primitives.Ints;

public class BooksTableContextMenuListener implements MouseListener {

    private final BooksContextMenu menu;
    private final JTable table;

    public BooksTableContextMenuListener(final JTable table, ActionListener newBookActionListener, ActionListener openBookActionListener) {
        this.table = table;
        menu = new BooksContextMenu(newBookActionListener, openBookActionListener);
        this.table.addMouseListener(new MouseListener() {

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

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger())
            doPop(e);

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger())
            doPop(e);

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private void doPop(MouseEvent e) {
        menu.show(e.getComponent(), e.getX(), e.getY());
    }

}
