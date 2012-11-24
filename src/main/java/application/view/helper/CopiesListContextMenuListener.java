package application.view.helper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JList;

import application.controller.BookDetailController;
import application.viewModel.CopyListModel;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;

import domain.Copy;
import domain.Copy.Condition;

public class CopiesListContextMenuListener implements MouseListener, ActionListener {

    private final CopiesContextMenu menu;
    private final CopyListModel copyListModel;
    private final JList<Copy> list;
    private final BookDetailController bookDetailController;

    public CopiesListContextMenuListener(final JList<Copy> list, CopyListModel copyListModel, final BookDetailController bookDetailController) {
        Preconditions.checkNotNull(bookDetailController);
        this.list = list;
        this.copyListModel = copyListModel;
        this.bookDetailController = bookDetailController;
        menu = new CopiesContextMenu(this);

        list.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isMetaDown()) {
                    int r = list.locationToIndex(e.getPoint());
                    // ;
                    if (Ints.contains(list.getSelectedIndices(), r)) {
                        // current row is selected, do nothing.
                        menu.enableCopyRelatedOptions(true);
                    } else if (r >= 0 && r < list.getModel().getSize() && list.getCellBounds(r, r).contains(e.getPoint())) {
                        list.setSelectedIndex(r);
                        menu.enableCopyRelatedOptions(true);
                    } else {
                        menu.enableCopyRelatedOptions(false);
                    }

                    boolean enableDelete = bookDetailController.areCopiesDeletable(list.getSelectedValuesList());
                    menu.enableDelete(enableDelete);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if ("BookDetailMainView.copyList.context.add".equals(command)) {
            copyListModel.addCopy(new Copy());
        } else if ("BookDetailMainView.copyList.context.remove".equals(command)) {
            copyListModel.removeCopy(list.getSelectedValuesList());
        } else {
            for (Condition c : Condition.values()) {
                if (c.getKey().equals(command)) {
                    for (Copy copy : list.getSelectedValuesList()) {
                        copy.setCondition(c);
                        copyListModel.updateCopy(copy);
                    }
                }
            }
        }
    }
}
