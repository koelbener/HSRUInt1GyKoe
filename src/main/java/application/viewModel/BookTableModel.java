package application.viewModel;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.core.Repository;
import application.core.Texts;

import com.jgoodies.validation.ValidationResult;

import domain.Book;
import domain.validator.BookValidator;

public class BookTableModel extends AbstractTableModel {

    public static final int COLUMN_AMOUNT = 3;
    public static final int COLUMN_PUBLISHER = 2;
    public static final int COLUMN_AUTHOR = 1;
    public static final int COLUMN_TITLE = 0;
    private static final long serialVersionUID = -67214736125029646L;
    private List<Book> books;
    private String[] columnNames;
    private final Logger logger = LoggerFactory.getLogger(BookTableModel.class);

    public BookTableModel(List<Book> books) {
        this.books = books;
        setColumns();
    }

    public void setColumns() {
        columnNames = new String[] {
                //
                Texts.get("BookMasterMainView.table.column.name"), //
                Texts.get("BookMasterMainView.table.column.author"), //
                Texts.get("BookMasterMainView.table.column.publisher"), //
                Texts.get("BookMasterMainView.table.column.copies") };
        this.fireTableStructureChanged();
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
            break;
        case COLUMN_AUTHOR:
            result = String.class;
            break;
        case COLUMN_PUBLISHER:
            result = String.class;
            break;
        case COLUMN_AMOUNT:
            result = Integer.class;
            break;
        default:
            logger.warn("Unknown column ID: {}", col);
            result = String.class;
        }

        return result;
    }

    private boolean validateRow(String value, int changedRow, int changedColumn) {
        boolean result = true;

        // get origin book
        Book originBook = getBook(changedRow);
        // create a validationBook object and copy fields from originBook
        Book bookToValidate = new Book();
        bookToValidate.updateFrom(originBook);

        switch (changedColumn) {
        case COLUMN_TITLE:
            bookToValidate.setName(value.toString());
            break;
        case COLUMN_AUTHOR:
            bookToValidate.setAuthor(value.toString());
            break;
        case COLUMN_PUBLISHER:
            bookToValidate.setPublisher(value.toString());
            break;
        }
        ValidationResult validation = new BookValidator().validate(bookToValidate);

        if (validation.hasErrors()) {
            JOptionPane.showMessageDialog(null, validation.getMessagesText(), "Buch Validierung fehlgeschlagen", JOptionPane.WARNING_MESSAGE);
            result = false;
        }

        return result;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        Book book = getBook(row);
        if (validateRow(value.toString(), row, col)) {
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
