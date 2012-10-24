package application.view;

import static application.view.BookDetailMainView.NAME_BOOK_DETAIL_MAIN_VIEW;
import static application.view.BookDetailMainView.NAME_BUTTON_CANCEL;
import static application.view.BookMasterMainView.NAME_BUTTON_OPEN;
import static application.view.BookMasterMainView.NAME_TABLE_BOOKS;

import java.awt.event.KeyEvent;

import org.fest.swing.annotation.GUITest;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
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
public class BookMasterMainViewTest extends AbstractFestTest {
    FrameFixture window;
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
        window.table(NAME_TABLE_BOOKS).selectRows(1);
        window.button(NAME_BUTTON_OPEN).requireEnabled();
    }

    @Test
    public void openBookButtonBecomesEnabledWhenSelectingAMultipleBooks() {
        window.button(NAME_BUTTON_OPEN).requireDisabled();
        window.table(NAME_TABLE_BOOKS).selectRows(1, 2, 3);
        window.button(NAME_BUTTON_OPEN).requireEnabled();
    }

    @Test
    public void openBookButtonBecomesDisabledWhenDeselectingBooks() {
        window.table(NAME_TABLE_BOOKS).selectRows(1, 2, 3);
        // deselect the rows again
        window.table(NAME_TABLE_BOOKS).pressKey(KeyEvent.VK_CONTROL);
        window.table(NAME_TABLE_BOOKS).selectRows(1, 2, 3);
        window.table(NAME_TABLE_BOOKS).releaseKey(KeyEvent.VK_CONTROL);
        window.button(NAME_BUTTON_OPEN).requireDisabled();
    }

    @Test
    public void openBookButtonOpensNewView() {
        window.table(NAME_TABLE_BOOKS).selectRows(1);
        window.button(NAME_BUTTON_OPEN).click();

        FrameFixture bookDetailDialog = findFrame(window, NAME_BOOK_DETAIL_MAIN_VIEW);
        bookDetailDialog.requireVisible();
        bookDetailDialog.button(NAME_BUTTON_CANCEL).click();
        bookDetailDialog.requireNotVisible();
        window.requireVisible();
    }

}
