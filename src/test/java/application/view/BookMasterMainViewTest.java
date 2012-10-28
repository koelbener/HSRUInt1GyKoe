package application.view;

import static application.view.BookDetailMainView.NAME_BOOK_DETAIL_MAIN_VIEW;
import static application.view.BookDetailMainView.NAME_BUTTON_CANCEL;
import static application.view.BookDetailMainView.NAME_TEXTBOX_TITLE;
import static application.view.BookMasterMainView.NAME_BUTTON_OPEN;
import static application.view.BookMasterMainView.NAME_SEARCH_FIELD;
import static application.view.BookMasterMainView.NAME_TABLE_BOOKS;
import static application.view.BookMasterMainView.SEARCH_DEFAULT_VALUE;
import static application.viewModel.BookTableModel.COLUMN_AMOUNT;
import static application.viewModel.BookTableModel.COLUMN_TITLE;
import static org.fest.swing.data.TableCell.row;

import java.awt.event.KeyEvent;

import org.fest.swing.annotation.GUITest;
import org.fest.swing.core.MouseClickInfo;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JTableFixture;
import org.fest.swing.fixture.JTextComponentFixture;
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
        assertThatDetailViewIsVisible();
    }

    @Test
    public void testDoubleClickOpenBook() {
        JTableFixture booksTable = window.table(NAME_TABLE_BOOKS);
        // make sure that the doubleclick is fired on a cell that is not editable.
        booksTable.selectCell(row(3).column(COLUMN_AMOUNT));
        booksTable.doubleClick();
        assertThatDetailViewIsVisible();
    }

    @Test
    public void testFocusOnSearchField() {
        JTextComponentFixture searchField = window.textBox(NAME_SEARCH_FIELD);
        searchField.requireText(SEARCH_DEFAULT_VALUE);
        window.textBox(NAME_SEARCH_FIELD).focus();
        searchField.requireText("");
        window.table(NAME_TABLE_BOOKS).focus();
        searchField.requireText(SEARCH_DEFAULT_VALUE);
    }

    @Test
    public void testFilter() {
        int numberOfBooks = window.table(NAME_TABLE_BOOKS).target.getRowCount();
        window.textBox(NAME_SEARCH_FIELD).setText("Beginning Groovy and Grails");
        window.table(NAME_TABLE_BOOKS).focus();
        window.table(NAME_TABLE_BOOKS).requireRowCount(1);
        window.textBox(NAME_SEARCH_FIELD).setText("");
        window.table(NAME_TABLE_BOOKS).requireRowCount(numberOfBooks);
    }

    @Test
    public void testEditableTable() {
        final String NEW_TITLE_VALUE = "C for dummies";
        window.table(NAME_TABLE_BOOKS).enterValue(row(3).column(COLUMN_TITLE), NEW_TITLE_VALUE);
        window.table(NAME_TABLE_BOOKS).click(row(3).column(COLUMN_AMOUNT), MouseClickInfo.leftButton().times(2));

        FrameFixture bookDetailDialog = findFrame(window, NAME_BOOK_DETAIL_MAIN_VIEW);
        bookDetailDialog.textBox(NAME_TEXTBOX_TITLE).requireText(NEW_TITLE_VALUE);
    }

    private void assertThatDetailViewIsVisible() {
        FrameFixture bookDetailDialog = findFrame(window, NAME_BOOK_DETAIL_MAIN_VIEW);
        bookDetailDialog.requireVisible();
        bookDetailDialog.button(NAME_BUTTON_CANCEL).click();
        bookDetailDialog.requireNotVisible();
        window.requireVisible();
    }

}
