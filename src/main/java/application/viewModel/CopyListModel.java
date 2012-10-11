package application.viewModel;

import java.util.List;

import javax.swing.AbstractListModel;

import domain.Copy;

public class CopyListModel extends AbstractListModel<Copy> {

    private static final long serialVersionUID = 1L;
    List<Copy> copyList;

    public CopyListModel(List<Copy> list) {
        this.copyList = list;
    }

    public void propagateUpdate(int pos) {
        fireContentsChanged(this, pos, pos);
    }

    @Override
    public Copy getElementAt(int index) {
        return copyList.get(index);
    }

    @Override
    public int getSize() {
        return copyList.size();
    }

}
