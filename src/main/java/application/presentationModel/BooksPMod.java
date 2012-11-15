package application.presentationModel;

import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.core.Repository;
import application.viewModel.BookTableModel;
import application.viewModel.SearchFilterComboBoxModel;
import domain.Book;

public class BooksPMod extends pModBase {

    private final Logger logger = LoggerFactory.getLogger(BooksPMod.class);
    private final BookTableModel bookTableModel;
    private final SearchFilterComboBoxModel filterComboBoxModel;
    private final TableRowSorter<BookTableModel> bookTableRowSorter;
    private String searchString = "";
    private int filterColumn = 0;

    public BooksPMod() {
        bookTableModel = new BookTableModel(Repository.getInstance().getLibrary().getBooks());
        bookTableRowSorter = new TableRowSorter<BookTableModel>(bookTableModel);
        filterComboBoxModel = new SearchFilterComboBoxModel();
    }

    public SearchFilterComboBoxModel getFilterComboBoxModel() {
        return filterComboBoxModel;
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
    }

    private void updateFilter() {
        bookTableRowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchString, filterColumn));
    }

}
