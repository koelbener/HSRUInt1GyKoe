package application.presentationModel;

import application.core.Repository;
import application.viewModel.BookListModel;

public class BooksPMod extends pModBase {

    private final BookListModel bookListModel;

    public BookListModel getBookListModel() {
        return bookListModel;
    }

    public BooksPMod() {
        bookListModel = new BookListModel(Repository.getInstance().getLibrary().getBooks());
    }

}
