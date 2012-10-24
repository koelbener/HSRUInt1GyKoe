package application.viewModel;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import application.core.Repository;
import domain.Book;

public class BookTableModel extends AbstractTableModel {

    private static final long serialVersionUID = -67214736125029646L;
    private List<Book> books;

    public BookTableModel(List<Book> books) {
        this.books = books;
    }

    public void setData(List<Book> books) {
        this.books = books;
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public int getRowCount() {
        return books.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        Object result = null;
        Book book = books.get(row);
        switch (col) {
        case 0:
            result = Repository.getInstance().getLibrary().getCopiesOfBook(book).size();
            break;
        case 1:
            result = book.getName();
            break;
        case 2:
            result = book.getAuthor();
            break;
        case 3:
            result = book.getPublisher();
            break;
        }
        return result;
    }

}
