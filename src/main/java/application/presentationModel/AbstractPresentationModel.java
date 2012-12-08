package application.presentationModel;

import java.util.Observable;

import application.core.Repository;
import domain.Library;

public abstract class AbstractPresentationModel extends Observable {
    protected final Library library;

    public AbstractPresentationModel() {
        library = Repository.getInstance().getLibrary();
    }
}
