package application.core;

import application.presentationModel.BooksPMod;
import domain.Library;

/**
 * Singelton to store core Objects
 * 
 */
public class Repository {
    private static Repository instance;

    private Library library;

    private BooksPMod booksPMod;

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
}