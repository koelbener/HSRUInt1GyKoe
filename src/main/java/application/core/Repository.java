package application.core;

import application.presentationModel.BooksPMod;
import application.presentationModel.ShelfPMod;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import domain.Book;
import domain.Library;

/**
 * Singleton to store core Objects
 */
public class Repository {
    private static Repository instance;
    private final EventBus eventBus;

    private Library library;

    private BooksPMod booksPMod;
    private ShelfPMod shelfPMod;

    private Repository() {
        eventBus = new EventBus();
        eventBus.register(this);
    }

    @Subscribe
    public void updatedBook(BookChangeEvent e) {
        Book updatedBook = e.getBook();
        // what if the book title changes?
        Book existingBook = library.findByBookTitle(updatedBook.getName());
        if (existingBook == null) {
            existingBook = library.createAndAddBook(updatedBook.getName());
        }
        existingBook.updateFrom(updatedBook);

        // TODO notify the booksPMod
    }

    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public Library getLibrary() {
        return this.library;
    }

    public void setBooksPMod(BooksPMod booksPMod) {
        this.booksPMod = booksPMod;
    }

    public BooksPMod getBooksPMod() {
        return booksPMod;
    }

    public ShelfPMod getShelfPMod() {
        return shelfPMod;
    }

    public void setShelfPMod(ShelfPMod shelfPMod) {
        this.shelfPMod = shelfPMod;
    }

}
