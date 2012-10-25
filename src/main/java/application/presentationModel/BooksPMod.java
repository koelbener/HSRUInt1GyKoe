package application.presentationModel;

import javax.swing.RowFilter;
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

    public BooksPMod() {
        bookTableModel = new BookTableModel(Repository.getInstance().getLibrary().getBooks());
        bookTableRowSorter = new TableRowSorter<BookTableModel>(bookTableModel);
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
        bookTableRowSorter.setRowFilter(RowFilter.regexFilter(filter));
    }
}
