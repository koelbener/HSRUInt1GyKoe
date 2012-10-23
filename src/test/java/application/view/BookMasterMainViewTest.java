package application.view;

import static application.view.BookDetailMainView.NAME_BOOK_DETAIL_MAIN_VIEW;
import static application.view.BookDetailMainView.NAME_BUTTON_CANCEL;
import static application.view.BookMasterMainView.NAME_BUTTON_OPEN;
import static application.view.BookMasterMainView.NAME_LIST_BOOKS;

import org.fest.swing.annotation.GUITest;
import org.fest.swing.core.Robot;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.finder.WindowFinder;
import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import application.LibraryApp;
import application.data.DataLoder;
import application.data.XmlDataLoader;
import domain.Library;

@GUITest
public class BookMasterMainViewTest {
    private FrameFixture window;
    private static Library library;

    @BeforeClass
    public static void setUpOnce() throws Exception {
        FailOnThreadViolationRepaintManager.install();
        DataLoder dataLoader = new XmlDataLoader();
        library = dataLoader.loadLibrary();
    }

    @Before
    public void setUp() throws Exception {
        BookMasterMainView mainWindow = GuiActionRunner.execute(new GuiQuery<BookMasterMainView>() {
            @Override
            protected BookMasterMainView executeInEDT() {
                return LibraryApp.createMainWindow(library);
            }
        });
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

    @Test
    public void openBookButtonOpensNewView() {
        window.list(NAME_LIST_BOOKS).selectItem(1);
        window.button(NAME_BUTTON_OPEN).click();

        FrameFixture bookDetailDialog = findFrame(window, NAME_BOOK_DETAIL_MAIN_VIEW);
        bookDetailDialog.requireVisible();
        bookDetailDialog.button(NAME_BUTTON_CANCEL).click();
        bookDetailDialog.requireNotVisible();
        window.requireVisible();
    }

    private FrameFixture findFrame(FrameFixture parent, String frameName) {
        Robot robot = window.robot;
        return WindowFinder.findFrame(frameName).using(robot);
    }

}
