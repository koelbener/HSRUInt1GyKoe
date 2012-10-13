package application.view;

import javax.swing.JFrame;

import application.controller.ControllerBase;
import application.core.Repository;
import domain.Library;

/**
 * A MainView is a view inheriting from JFrame. Every window of the UI is a
 * MainViewBase descendant.
 * 
 */
public abstract class MainViewBase extends JFrame {

    private static final long serialVersionUID = 1L;
    protected ControllerBase controller;
    protected Library library;

    public MainViewBase() {
        initModel();
        initUIElements();
        initListeners();
        initController();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    protected void initModel() {
        library = Repository.getInstance().getLibrary();
    }

    protected abstract void initController();

    protected abstract void initListeners();

    protected abstract ControllerBase getController();

    protected void initUIElements() {
        getContentPane().removeAll();
    }

}
