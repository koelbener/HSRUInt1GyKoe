package application.viewModel;

import java.util.List;

import javax.swing.AbstractListModel;

import domain.Copy;

@SuppressWarnings("rawtypes")
// is not supported by Java6
public class CopyListModel extends AbstractListModel {

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

    public void addCopy(Copy copy) {
        copyList.add(copy);
        propagateUpdate(copyList.size() - 1);

    }

    public void removeCopy(Copy copy) {
        copyList.remove(copy);
        fireContentsChanged(this, 0, copyList.size());
    }

    public List<Copy> getAll() {
        return copyList;
    }

}
