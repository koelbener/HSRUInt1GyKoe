package application.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.core.BookChangeEvent;
import domain.Book;

public class BookDetailController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(BookDetailController.class);

    /**
     * @return <code>true</code> if the book was successfully persisted and the
     *         calling dialog can be closed
     */
    public boolean saveBook(Book book) {
        logger.info("Posting book update to the EventBus: {}", book.toString());
        getEventBus().post(new BookChangeEvent(book));

        getRepository().getBooksPMod().updateBook(book);

        return true; // TODO remove return val?
    }

}
