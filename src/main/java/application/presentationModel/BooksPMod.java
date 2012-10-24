package application.presentationModel;

import application.core.Repository;
import application.viewModel.BookTableModel;
import domain.Book;

public class BooksPMod extends pModBase {

    private final BookTableModel bookTableModel;

    public BooksPMod() {
        bookTableModel = new BookTableModel(Repository.getInstance().getLibrary().getBooks());
    }

    public BookTableModel getBookTableModel() {
        return bookTableModel;
    }

    public void updateBook(Book book) {
        bookTableModel.fireTableDataChanged();
    }
}
