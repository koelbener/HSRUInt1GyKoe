package application.view;

import javax.swing.JFrame;

import application.controller.AbstractController;
import application.core.Repository;
import domain.Library;

/**
 * A MainView is a view inheriting from JFrame. Every window of the UI is a
 * MainViewBase descendant.
 * 
 */
public abstract class MainViewBase<T extends AbstractController> extends JFrame {

    private static final long serialVersionUID = 1L;
    protected T controller;
    protected Library library;

    public MainViewBase() {
        initModel();
        initUIElements();
        initListeners();
        controller = initController();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    protected void initModel() {
        library = Repository.getInstance().getLibrary();
    }

    protected void initUIElements() {
        getContentPane().removeAll();
    }

    protected abstract void initListeners();

    protected abstract T initController();

    protected T getController() {
        return controller;
    }

}
