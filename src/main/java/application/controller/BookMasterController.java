package application.controller;

import application.core.Repository;
import application.view.BookDetailMainView;
import application.viewModel.BookListModel;
import domain.Book;

public class BookMasterController extends AbstractController {

    public void openBooks(int[] selectedIndices) {
        BookListModel booksListModel = Repository.getInstance().getBooksPMod().getBookListModel();

        for (int index : selectedIndices) {
            Book book = booksListModel.getElementAt(index);
            System.out.println("opening book view " + book.getName());
            new BookDetailMainView(book);
        }

    }

}
