package application.view.helper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JPopupMenu;

import application.core.Repository;
import application.presentationModel.componentModel.CopyListModel;

import com.google.common.primitives.Ints;

import domain.Condition;
import domain.Copy;

public class CopiesListContextMenuListener extends AbstractContextMenuListener implements ActionListener {

    private final CopiesContextMenu menu;
    private final CopyListModel copyListModel;
    private final JList<Copy> list;

    public CopiesListContextMenuListener(final JList<Copy> list, CopyListModel copyListModel) {
        this.list = list;
        this.copyListModel = copyListModel;
        menu = new CopiesContextMenu(this);

        list.addMouseListener(new SimpleMouseListener() {

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

                    boolean enableDelete = Repository.getInstance().getCopyPMod().areCopiesDeletable(list.getSelectedValuesList());
                    menu.enableDelete(enableDelete);
                }
            }
        });

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

    public void updateTexts() {
        menu.updateTexts();
    }

    @Override
    protected JPopupMenu getMenu() {
        return menu;
    }
}
