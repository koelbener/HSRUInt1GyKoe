package application.presentationModel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.core.Repository;
import application.presentationModel.componentModel.BookSearchFilterComboBoxModel;
import application.presentationModel.componentModel.BookTableModel;
import domain.Book;
import domain.Copy;

public class BooksPMod extends pModBase {

    private final Logger logger = LoggerFactory.getLogger(BooksPMod.class);
    private final BookTableModel bookTableModel;
    private final BookSearchFilterComboBoxModel filterComboBoxModel;
    private final TableRowSorter<BookTableModel> bookTableRowSorter;
    private String searchString = "";
    private int filterColumn = -1;
    private boolean onlyAvailableBooks = false;

    public BooksPMod() {
        super();
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
        this.onlyAvailableBooks = onlyAvailableBooks;
        updateFilter();
    }

    public void setFilterColumn(int filterColumn) {
        this.filterColumn = filterColumn;
        updateFilter();
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

    private void updateFilter() {
        List<RowFilter<Object, Object>> filters = new ArrayList<RowFilter<Object, Object>>();

        if (filterColumn >= 0) {
            filters.add(RowFilter.regexFilter("(?i)" + searchString, filterColumn));
        } else {
            filters.add(RowFilter.regexFilter("(?i)" + searchString));
        }

        if (onlyAvailableBooks) {
            filters.add(new OnyAvailableFilter());
        }

        bookTableRowSorter.setRowFilter(RowFilter.andFilter(filters));
    }

    private class OnyAvailableFilter extends RowFilter<Object, Object> {

        @Override
        public boolean include(RowFilter.Entry<? extends Object, ? extends Object> entry) {
            String availability = entry.getStringValue(BookTableModel.COLUMN_AMOUNT);
            return availability.charAt(0) != '0';
        }
    }

    public List<Copy> getCopiesOfBook(Book book) {
        return library.getCopiesOfBook(book);
    }

    public Book findByBookTitle(String title) {
        return library.findByBookTitle(title);
    }

    public Book createAndAddBook(String title) {
        Book book = library.createAndAddBook(title);
        setChanged();
        notifyObservers();
        return book;
    }

}
