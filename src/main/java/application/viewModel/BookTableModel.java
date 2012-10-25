package application.viewModel;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import application.core.Repository;
import domain.Book;

public class BookTableModel extends AbstractTableModel {

    public static final int COLUMN_AMOUNT = 3;
    public static final int COLUMN_PUBLISHER = 2;
    public static final int COLUMN_AUTHOR = 1;
    public static final int COLUMN_TITLE = 0;
    private static final long serialVersionUID = -67214736125029646L;
    private List<Book> books;
    private final String[] columnNames = new String[] { "Name", "Autor", "Verlag", "Kopien" };

    public BookTableModel(List<Book> books) {
        this.books = books;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        boolean result;
        switch (col) {
        case COLUMN_TITLE:
            result = true;
            break;
        case COLUMN_AUTHOR:
            result = true;
            break;
        case COLUMN_PUBLISHER:
            result = true;
            break;
        case COLUMN_AMOUNT:
            result = false;
            break;
        default:
            result = false;
        }
        return result;
    }

    @Override
    public Class<?> getColumnClass(int col) {
        Class<?> result;
        switch (col) {
        case COLUMN_TITLE:
            result = String.class;
        case COLUMN_AUTHOR:
            result = Double.class;
        case COLUMN_PUBLISHER:
            result = String.class;
        case COLUMN_AMOUNT:
            result = Integer.class;
        default:
            result = String.class;
        }

        return result;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        // TODO validation
        Book book = getBook(row);
        switch (col) {
        case COLUMN_TITLE:
            book.setName(value.toString());
            break;
        case COLUMN_AUTHOR:
            book.setAuthor(value.toString());
            break;
        case COLUMN_PUBLISHER:
            book.setPublisher(value.toString());
            break;
        }
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
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int row, int col) {
        Object result = null;
        Book book = books.get(row);
        switch (col) {
        case COLUMN_TITLE:
            result = book.getName();
            break;
        case COLUMN_AUTHOR:
            result = book.getAuthor();
            break;
        case COLUMN_PUBLISHER:
            result = book.getPublisher();
            break;
        case COLUMN_AMOUNT:
            result = Repository.getInstance().getLibrary().getCopiesOfBook(book).size();
            break;
        }
        return result;
    }

    public Book getBook(int index) {
        return books.get(index);
    }

}
