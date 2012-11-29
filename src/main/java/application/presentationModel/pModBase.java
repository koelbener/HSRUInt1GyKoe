package application.presentationModel;

import java.util.Observable;

import application.core.Repository;
import domain.Library;

public class pModBase extends Observable {
    protected final Library library;

    public pModBase() {
        library = Repository.getInstance().getLibrary();
    }
}
