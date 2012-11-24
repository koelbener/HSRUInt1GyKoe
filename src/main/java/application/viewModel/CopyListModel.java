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

    @Override
    public Copy getElementAt(int index) {
        return copyList.get(index);
    }

    @Override
    public int getSize() {
        return copyList.size();
    }

    public void addCopy(Copy copy) {
        copyList.add(copy);
        int index = copyList.size() - 1;
        fireIntervalAdded(this, index, index);
    }

    public boolean removeCopy(List<Copy> copies) {
        for (Copy copy : copies) {
            int index = copies.indexOf(copy);
            copyList.remove(copy);
            fireIntervalRemoved(this, index, index);
        }
        return copyList.isEmpty();
    }

    public List<Copy> getAll() {
        return copyList;
    }

    public void updateCopy(Copy copy) {
        int index = copyList.indexOf(copy);
        fireContentsChanged(this, index, index);
    }

}
