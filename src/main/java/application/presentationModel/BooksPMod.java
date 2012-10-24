package application.presentationModel;

import javax.swing.table.TableRowSorter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.core.Repository;
import application.viewModel.BookTableModel;
import domain.Book;

public class BooksPMod extends pModBase {

    private final Logger logger = LoggerFactory.getLogger(BooksPMod.class);
    private final BookTableModel bookTableModel;
    private final TableRowSorter<BookTableModel> bookTableRowSorter;
    private final BooksRowFilter rowFilter;

    public BooksPMod() {
        bookTableModel = new BookTableModel(Repository.getInstance().getLibrary().getBooks());
        bookTableRowSorter = new TableRowSorter<BookTableModel>(bookTableModel);
        rowFilter = new BooksRowFilter();
        bookTableRowSorter.setRowFilter(rowFilter);
    }

    public BookTableModel getBookTableModel() {
        return bookTableModel;
    }

    public TableRowSorter<BookTableModel> getBookTableRowSorter() {
        return bookTableRowSorter;
    }

    public void updateBook(Book book) {
        bookTableModel.fireTableDataChanged();
    }

    public void setSearchString(String filter) {
        logger.debug("Filter books table for \"{}\"", filter);
        rowFilter.setInclude(false);
        bookTableModel.fireTableDataChanged();
    }
}
