package application.presentationModel;

import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.core.Repository;
import application.viewModel.BookSearchFilterComboBoxModel;
import application.viewModel.BookTableModel;
import domain.Book;

public class BooksPMod extends pModBase {

    private final Logger logger = LoggerFactory.getLogger(BooksPMod.class);
    private final BookTableModel bookTableModel;
    private final BookSearchFilterComboBoxModel filterComboBoxModel;
    private final TableRowSorter<BookTableModel> bookTableRowSorter;
    private String searchString = "";
    private int filterColumn = -1;

    public BooksPMod() {
        bookTableModel = new BookTableModel(Repository.getInstance().getLibrary().getBooks());
        bookTableRowSorter = new TableRowSorter<BookTableModel>(bookTableModel);
        filterComboBoxModel = new BookSearchFilterComboBoxModel();
    }

    public BookSearchFilterComboBoxModel getFilterComboBoxModel() {
        return filterComboBoxModel;
    }

    public BookTableModel getBookTableModel() {
        return bookTableModel;
    }

    public TableRowSorter<BookTableModel> getBookTableRowSorter() {
        return bookTableRowSorter;
    }

    public void setSearchString(String filter) {
        logger.trace("Filter books table for \"{}\"", filter);
        searchString = filter;
        updateFilter();
    }

    public void setOnlyAvailableBooks(boolean onlyAvailableBooks) {
        if (onlyAvailableBooks) {
            bookTableModel.setData(Repository.getInstance().getLibrary().getAvailableBooks());
        } else {
            bookTableModel.setData(Repository.getInstance().getLibrary().getBooks());
        }
    }

    public void setFilterColumn(int filterColumn) {
        this.filterColumn = filterColumn;
        updateFilter();
    }

    private void updateFilter() {
        if (filterColumn >= 0) {
            bookTableRowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchString, filterColumn));
        } else {
            bookTableRowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchString));
        }
    }

    public void addBook(Book book) {
        bookTableModel.addBook(book);
        setChanged();
        notifyObservers();
    }

    public void updateBook(Book book) {
        bookTableModel.updateBook(book);
        setChanged();
        notifyObservers();
    }

    public int getBooksCount() {
        return bookTableModel.getRowCount();
    }

    public int getCopiesCount() {
        return Repository.getInstance().getLibrary().getCopies().size();
    }

}
