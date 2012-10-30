package application.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.Book;
import domain.Copy;

public class BookDetailController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(BookDetailController.class);

    /**
     * @return <code>true</code> if the book was successfully persisted and the calling dialog can be closed
     */
    public boolean saveBook(Book book, List<Copy> copies) {

        String title = book.getName();
        Book existingBook = getRepository().getLibrary().findByBookTitle(title);
        if (existingBook == null) {
            logger.debug("Creating new book \"{}\"", title);
            existingBook = getRepository().getLibrary().createAndAddBook(title);
            existingBook.updateFrom(book);
        } else {
            logger.debug("Updating book \"{}\"", title);
        }

        List<Copy> existingCopies = new ArrayList<Copy>(getRepository().getLibrary().getCopiesOfBook(existingBook));
        for (Copy copy : copies) {
            if (newlyAdded(copy)) {
                copy.setTitle(existingBook);
                getRepository().getLibrary().addCopy(copy);
            } else {
                existingCopies.remove(copy);
            }
        }
        // remove all copies that were not in the list anymore
        for (Copy copy : existingCopies) {
            getRepository().getLibrary().removeCopy(copy);
        }

        getRepository().getBooksPMod().updateBook(book);

        return true; // TODO remove return val?
    }

    private boolean newlyAdded(Copy copy) {
        return copy.getTitle() == null;
    }

}
