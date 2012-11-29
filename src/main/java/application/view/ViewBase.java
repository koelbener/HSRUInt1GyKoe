package application.view;

import java.awt.Component;
import java.util.Observable;
import java.util.Observer;

import application.controller.ControllerBase;
import application.core.Repository;
import application.core.Texts;
import domain.Library;

public abstract class ViewBase<R, T extends ControllerBase, S extends Component> implements Observer {

    protected T controller;
    protected S container;
    protected Library library;
    private R referenceObject;

    public ViewBase(R referenceObject) {
        container = initContainer();
        container.setName(this.getClass().getSimpleName());
        this.referenceObject = referenceObject;
        initModel();
        initUIElements();
        setTexts();
        controller = initController();
        initListeners();

        Texts.getInstance().addObserver(this);
    }

    protected abstract S initContainer();

    @Override
    public void update(Observable arg0, Object arg1) {
        setTexts();
    }

    protected abstract void setTexts();

    protected void initModel() {
        library = Repository.getInstance().getLibrary();
    }

    protected abstract void initUIElements();

    protected abstract void initListeners();

    protected abstract T initController();

    protected T getController() {
        return controller;
    }

    protected R getReferenceObject() {
        return referenceObject;
    }

    protected void setReferenceObject(R referenceObject) {
        this.referenceObject = referenceObject;
    }

    protected boolean isNewEntity() {
        return referenceObject == null;
    }

    public S getContainer() {
        return container;
    }

}
