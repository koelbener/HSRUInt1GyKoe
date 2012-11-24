package application.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.Book;
import domain.Copy;

public class BookDetailController extends ControllerBase {
    private static final Logger logger = LoggerFactory.getLogger(BookDetailController.class);

    public void saveBook(Book book, List<Copy> newCopies) {

        String title = book.getName();
        Book existingBook = getRepository().getLibrary().findByBookTitle(title);
        if (existingBook == null) {
            logger.debug("Creating new book \"{}\"", title);
            existingBook = getRepository().getLibrary().createAndAddBook(title);
            existingBook.updateFrom(book);
            getRepository().getBooksPMod().addBook(existingBook);
        } else {
            logger.debug("Updating book \"{}\"", title);
        }

        List<Copy> existingCopies = new ArrayList<Copy>(getRepository().getLibrary().getCopiesOfBook(existingBook));
        for (Copy copy : newCopies) {
            if (newlyAdded(copy)) {
                copy.setTitle(existingBook);
                getRepository().getLibrary().addCopy(copy);
            } else {
                existingCopies.remove(copy);
            }
        }
        // delete all copies which are not in the list anymore
        for (Copy copy : existingCopies) {
            getRepository().getLibrary().removeCopy(copy);
        }

        getRepository().getBooksPMod().updateBook(existingBook);
    }

    public boolean areCopiesDeletable(List<Copy> copies) {
        boolean result = true;
        for (Copy copy : copies) {
            if (getRepository().getLibrary().isOrWasCopyLent(copy)) {
                result = false;
                break;
            }
        }
        return result;
    }

    private boolean newlyAdded(Copy copy) {
        return copy.getTitle() == null;
    }

}
