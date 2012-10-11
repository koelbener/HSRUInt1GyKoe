package application.viewModel;

import java.util.List;

import javax.swing.AbstractListModel;

import domain.Book;

public class BookListModel extends AbstractListModel<Book> {

    private static final long serialVersionUID = 1L;
    List<Book> bookList;

    public BookListModel(List<Book> list) {
        this.bookList = list;
    }

    public void propagateUpdate(int pos) {
        fireContentsChanged(this, pos, pos);
    }

    @Override
    public Book getElementAt(int index) {
        return bookList.get(index);
    }

    @Override
    public int getSize() {
        return bookList.size();
    }

}
