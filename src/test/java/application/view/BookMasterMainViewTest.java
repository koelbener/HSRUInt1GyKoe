package application.view;

import static application.view.BookMasterMainView.NAME_BUTTON_OPEN;
import static application.view.BookMasterMainView.NAME_LIST_BOOKS;

import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import application.LibraryApp;

public class BookMasterMainViewTest {
    private FrameFixture window;

    @Before
    public void setUp() throws Exception {
        BookMasterMainView mainWindow = LibraryApp.createMainWindow();
        window = new FrameFixture(mainWindow);
        window.show(); // shows the frame to test
    }

    @After
    public void tearDown() {
        window.cleanUp();
    }

    @Test
    public void openBookButtonBecomesEnabledWhenSelectingASingleBook() {
        window.button(NAME_BUTTON_OPEN).requireDisabled();
        window.list(NAME_LIST_BOOKS).selectItem(1);
        window.button(NAME_BUTTON_OPEN).requireEnabled();
    }

    @Test
    public void openBookButtonBecomesEnabledWhenSelectingAMultipleBooks() {
        window.button(NAME_BUTTON_OPEN).requireDisabled();
        window.list(NAME_LIST_BOOKS).selectItems(1, 2, 3);
        window.button(NAME_BUTTON_OPEN).requireEnabled();
    }

    @Test
    public void openBookButtonBecomesDisabledWhenDeselectingBooks() {
        window.list(NAME_LIST_BOOKS).selectItems(1, 2, 3);
        window.list(NAME_LIST_BOOKS).clearSelection();
        window.button(NAME_BUTTON_OPEN).requireDisabled();
    }

}
