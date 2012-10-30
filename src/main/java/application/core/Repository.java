package application.core;

import application.presentationModel.BooksPMod;
import application.presentationModel.ShelfPMod;
import domain.Library;

/**
 * Singleton to store core Objects
 */
public class Repository {
    private static Repository instance;

    private Library library;

    private BooksPMod booksPMod;
    private ShelfPMod shelfPMod;

    private Repository() {
    }

    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
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
