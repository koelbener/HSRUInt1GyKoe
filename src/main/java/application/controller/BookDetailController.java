package application.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.core.Repository;
import domain.Book;
import domain.Copy;

public class BookDetailController extends ControllerBase {
    private static final Logger logger = LoggerFactory.getLogger(BookDetailController.class);

    public void saveBook(Book book, List<Copy> newCopies) {

        String title = book.getName();
        Book existingBook = getRepository().getBooksPMod().findByBookTitle(title);
        if (existingBook == null) {
            logger.debug("Creating new book \"{}\"", title);
            existingBook = getRepository().getBooksPMod().createAndAddBook(title);
            existingBook.updateFrom(book);
            getRepository().getBooksPMod().addBook(existingBook);
        } else {
            logger.debug("Updating book \"{}\"", title);
        }

        List<Copy> existingCopies = new ArrayList<Copy>(getRepository().getBooksPMod().getCopiesOfBook(book));
        updateCopies(newCopies, existingBook, existingCopies);

        getRepository().getBooksPMod().updateBook(existingBook);
    }

    private void updateCopies(List<Copy> newCopies, Book existingBook, List<Copy> existingCopies) {
        for (Copy copy : newCopies) {
            if (newlyAdded(copy)) {
                logger.debug("Adding copy \"{}\"", copy);
                copy.setTitle(existingBook);
                getRepository().getCopyPMod().addCopy(copy);
            } else {
                if (existingCopies.remove(copy)) {
                    logger.debug("Updating copy \"{}\"", copy);
                    // the copy has not been removed
                    Copy copyToPersist = getRepository().getCopyPMod().getCopyByInventoryId(copy.getInventoryNumber());
                    copyToPersist.updateFrom(copy);
                    Repository.getInstance().getCopyPMod().update(copy);
                }
            }
        }
        // delete all copies which are not in the list anymore
        for (Copy copy : existingCopies) {
            logger.debug("Removing copy \"{}\"", copy);
            getRepository().getCopyPMod().removeCopy(copy);
        }
    }

    private boolean newlyAdded(Copy copy) {
        return copy.getTitle() == null;
    }

}
