package application.controller;

import javax.swing.RowSorter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.view.EditBookDetailMainView;
import application.view.NewBookDetailMainView;
import application.viewModel.BookTableModel;
import domain.Book;

public class BookMasterController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(BookMasterController.class);

    public void openBooks(int[] selectedIndices) {
        BookTableModel bookTableModel = getRepository().getBooksPMod().getBookTableModel();
        RowSorter<BookTableModel> sorter = getRepository().getBooksPMod().getBookTableRowSorter();

        for (int index : selectedIndices) {
            Book book = bookTableModel.getBook(sorter.convertRowIndexToModel(index));
            logger.debug("opening book {}", book.getName());
            new EditBookDetailMainView(book);
        }

    }

    public void searchBooks(String filter, int columnIndex) {
        getRepository().getBooksPMod().setSearchString(filter, columnIndex);
    }

    public void openNewBook() {
        new NewBookDetailMainView();
    }

}
