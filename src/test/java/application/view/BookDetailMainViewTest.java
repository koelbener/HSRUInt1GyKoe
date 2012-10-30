package application.view;

import static application.view.BookDetailMainView.NAME_BOOK_DETAIL_MAIN_VIEW;
import static application.view.BookDetailMainView.NAME_BUTTON_CANCEL;
import static application.view.BookDetailMainView.NAME_BUTTON_SAVE;
import static application.view.BookDetailMainView.NAME_TEXTBOX_TITLE;
import static application.view.BookDetailMainView.NAME_VALIDATION_PANEL;
import static application.view.BookMasterMainView.NAME_BUTTON_OPEN;
import static application.view.BookMasterMainView.NAME_TABLE_BOOKS;
import static org.junit.Assert.assertEquals;

import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import application.LibraryApp;
import domain.Book;
import domain.Library;
import domain.Shelf;

public class BookDetailMainViewTest extends AbstractFestTest {
    private static final String BOOK_PUBLISHER = "HSR Hochschule für Technik Rapperswil";
    private static final String BOOK_AUTHOR = "Michi Gysel";
    private static final String BOOK_TITLE = "Testing with JUnit 4";
    private FrameFixture bookMaster;
    private FrameFixture bookDetail;
    private static Library library;

    @BeforeClass
    public static void setUpOnce() throws Exception {
        FailOnThreadViolationRepaintManager.install();
        library = new Library();
        Book b = library.createAndAddBook(BOOK_TITLE);
        b.setAuthor(BOOK_AUTHOR);
        b.setPublisher(BOOK_PUBLISHER);
        b.setShelf(Shelf.D1);

    }

    @Before
    public void setUp() throws Exception {
        BookMasterMainView bookMasterMainView = GuiActionRunner.execute(new GuiQuery<BookMasterMainView>() {
            @Override
            protected BookMasterMainView executeInEDT() {
                return LibraryApp.createMainWindow(library);
            }
        });
        bookMaster = new FrameFixture(bookMasterMainView);
        bookMaster.show();
    }

    @After
    public void tearDown() {
        bookMaster.cleanUp();
        if (bookDetail != null) {
            bookDetail.cleanUp();
        }
    }

    @Test
    public void validateEmptyBookTitle() {
        openFirstBook();
        bookDetail = findFrame(bookMaster, NAME_BOOK_DETAIL_MAIN_VIEW);
        // verify the text boxes
        bookDetail.textBox(NAME_TEXTBOX_TITLE).requireText(BOOK_TITLE);
        // TODO also test the remaining book fields
        bookDetail.textBox(NAME_TEXTBOX_TITLE).deleteText();
        // hit save and check the validations
        bookDetail.button(NAME_BUTTON_SAVE).click();
        bookDetail.requireVisible();
        assertEquals(1, bookDetail.panel(NAME_VALIDATION_PANEL).list().component().getModel().getSize());
        bookDetail.button(NAME_BUTTON_CANCEL).click();
        bookDetail.requireNotVisible();
    }

    private void openFirstBook() {
        bookMaster.table(NAME_TABLE_BOOKS).selectRows(0);
        bookMaster.button(NAME_BUTTON_OPEN).click();
    }

}
