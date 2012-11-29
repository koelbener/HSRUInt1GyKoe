package application.view.subView;

import static application.presentationModel.componentModel.BookTableModel.COLUMN_AMOUNT;
import static application.presentationModel.componentModel.BookTableModel.COLUMN_TITLE;
import static application.view.mainView.dialogView.BookDetailMainViewBase.NAME_BUTTON_CANCEL;
import static application.view.mainView.dialogView.BookDetailMainViewBase.NAME_TEXTBOX_TITLE;
import static application.view.subView.BookMasterSubView.NAME_BUTTON_OPEN;
import static application.view.subView.BookMasterSubView.NAME_COMBOBOX_FILTER;
import static application.view.subView.BookMasterSubView.NAME_SEARCH_FIELD;
import static application.view.subView.BookMasterSubView.NAME_TABLE_BOOKS;
import static application.view.subView.BookMasterSubView.searchDefaultText;
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
import application.view.AbstractFestTest;
import application.view.mainView.MasterMainView;
import application.view.mainView.dialogView.EditBookDetailMainView;
import domain.Library;

@GUITest
public class BookMasterSubViewTest extends AbstractFestTest {
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
        MasterMainView mainWindow = GuiActionRunner.execute(new GuiQuery<MasterMainView>() {
            @Override
            protected MasterMainView executeInEDT() {
                return LibraryApp.createMainWindow(library);
            }
        });
        window = new FrameFixture(mainWindow.getContainer());
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
        booksTable.click(row(3).column(COLUMN_AMOUNT), MouseClickInfo.leftButton().times(2));
        assertThatDetailViewIsVisible();
    }

    @Test
    public void testFocusOnSearchField() {
        JTextComponentFixture searchField = window.textBox(NAME_SEARCH_FIELD);
        searchField.requireText(searchDefaultText);
        window.textBox(NAME_SEARCH_FIELD).focus();
        searchField.requireText("");
        window.table(NAME_TABLE_BOOKS).focus();
        searchField.requireText(searchDefaultText);
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

        FrameFixture bookDetailDialog = findFrame(window, EditBookDetailMainView.class.getSimpleName());
        bookDetailDialog.textBox(NAME_TEXTBOX_TITLE).requireText(NEW_TITLE_VALUE);
    }

    @Test
    public void testSearchFilterComboBox() {
        window.textBox(NAME_SEARCH_FIELD).setText("wiley");
        window.table(NAME_TABLE_BOOKS).requireRowCount(5);
        // select "Suche Titel" in comboBox
        window.comboBox(NAME_COMBOBOX_FILTER).selectItem(1);
        // no titles with "wiley" should be found
        window.table(NAME_TABLE_BOOKS).requireRowCount(0);
        // select "Suche Verlag" in comboBox
        window.comboBox(NAME_COMBOBOX_FILTER).selectItem(3);
        // 5 books from wiley verlag should be found
        window.table(NAME_TABLE_BOOKS).requireRowCount(5);
    }

    private void assertThatDetailViewIsVisible() {
        FrameFixture bookDetailDialog = findFrame(window, EditBookDetailMainView.class.getSimpleName());
        bookDetailDialog.requireVisible();
        bookDetailDialog.button(NAME_BUTTON_CANCEL).click();
        bookDetailDialog.requireNotVisible();
        window.requireVisible();
    }

}
