package application.viewModel;

import java.util.List;

import javax.swing.AbstractListModel;

public class BookListModel extends AbstractListModel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    List<domain.Book> bookList;

    public BookListModel(List<domain.Book> list) {
        this.bookList = list;
    }

    public void propagateUpdate(int pos) {
        fireContentsChanged(this, pos, pos);
    }

    @Override
    public Object getElementAt(int index) {
        return bookList.get(index);
    }

    @Override
    public int getSize() {
        return bookList.size();
    }

}
