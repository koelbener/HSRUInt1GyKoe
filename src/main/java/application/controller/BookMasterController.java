package application.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.view.BookDetailMainView;
import application.viewModel.BookTableModel;
import domain.Book;

public class BookMasterController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(BookMasterController.class);

    public void openBooks(int[] selectedIndices) {
        BookTableModel bookTableModel = getRepository().getBooksPMod().getBookTableModel();

        for (int index : selectedIndices) {
            Book book = bookTableModel.getBook(index);
            logger.debug("opening book {}", book.getName());
            new BookDetailMainView(book);
        }

    }

    public void searchBooks(String filter) {
        getRepository().getBooksPMod().setSearchString(filter);
    }

    public void openNewBook() {
        new BookDetailMainView(null);
    }

}
