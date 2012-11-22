package application.view.mainView;

import static application.view.mainView.dialogView.BookDetailMainViewBase.NAME_BUTTON_ADD_COPY;
import static application.view.mainView.dialogView.BookDetailMainViewBase.NAME_BUTTON_CANCEL;
import static application.view.mainView.dialogView.BookDetailMainViewBase.NAME_BUTTON_SAVE;
import static application.view.mainView.dialogView.BookDetailMainViewBase.NAME_COMBOBOX_SHELF;
import static application.view.mainView.dialogView.BookDetailMainViewBase.NAME_TEXTBOX_AUTHOR;
import static application.view.mainView.dialogView.BookDetailMainViewBase.NAME_TEXTBOX_PUBLISHER;
import static application.view.mainView.dialogView.BookDetailMainViewBase.NAME_TEXTBOX_TITLE;
import static application.view.mainView.dialogView.BookDetailMainViewBase.NAME_VALIDATION_PANEL;
import static application.view.subView.BookMasterSubView.NAME_BUTTON_NEW;
import static application.view.subView.BookMasterSubView.NAME_BUTTON_OPEN;
import static application.view.subView.BookMasterSubView.NAME_TABLE_BOOKS;
import static org.fest.swing.data.TableCell.row;
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
import application.view.AbstractFestTest;
import application.view.mainView.dialogView.EditBookDetailMainView;
import application.view.mainView.dialogView.NewBookDetailMainView;
import application.viewModel.BookTableModel;
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
        MasterMainView masterMainView = GuiActionRunner.execute(new GuiQuery<MasterMainView>() {
            @Override
            protected MasterMainView executeInEDT() {
                return LibraryApp.createMainWindow(library);
            }
        });
        bookMaster = new FrameFixture(masterMainView.getContainer());
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
    public void validateOpenBook() {
        openFirstBook();
        bookDetail = findFrame(bookMaster, EditBookDetailMainView.class.getSimpleName());
        // verify the text boxes
        bookDetail.textBox(NAME_TEXTBOX_TITLE).requireText(BOOK_TITLE);
        bookDetail.textBox(NAME_TEXTBOX_PUBLISHER).requireText(BOOK_PUBLISHER);
        bookDetail.textBox(NAME_TEXTBOX_AUTHOR).requireText(BOOK_AUTHOR);
    }

    @Test
    public void validateEditBookEmptyTitle() {
        openFirstBook();
        bookDetail = findFrame(bookMaster, EditBookDetailMainView.class.getSimpleName());

        bookDetail.textBox(NAME_TEXTBOX_TITLE).deleteText();
        bookDetail.textBox(NAME_TEXTBOX_AUTHOR).focus();

        // txtTitle should be focused and validation error displayed.
        bookDetail.textBox(NAME_TEXTBOX_TITLE).requireFocused();
        assertEquals(1, bookDetail.panel(NAME_VALIDATION_PANEL).list().component().getModel().getSize());
        bookDetail.button(NAME_BUTTON_CANCEL).click();
        bookDetail.requireNotVisible();
    }

    @Test
    public void validateNewBookEmptyTitle() {
        openNewBook();
        bookDetail = findFrame(bookMaster, NewBookDetailMainView.class.getSimpleName());
        // verify the text boxes
        bookDetail.textBox(NAME_TEXTBOX_TITLE).requireText("");
        bookDetail.textBox(NAME_TEXTBOX_AUTHOR).requireText("");
        bookDetail.textBox(NAME_TEXTBOX_PUBLISHER).requireText("");

        bookDetail.textBox(NAME_TEXTBOX_AUTHOR).setText("foobar");
        bookDetail.textBox(NAME_TEXTBOX_PUBLISHER).setText("foobar");
        bookDetail.comboBox(NAME_COMBOBOX_SHELF).selectItem(3);
        // hit save and check the validations
        bookDetail.button(NAME_BUTTON_SAVE).click();
        bookDetail.requireVisible();
        assertEquals(1, bookDetail.panel(NAME_VALIDATION_PANEL).list().component().getModel().getSize());
        bookDetail.button(NAME_BUTTON_CANCEL).click();
        bookDetail.requireNotVisible();
    }

    @Test
    public void editBook() {
        final String OLD_TITLE = bookMaster.table(NAME_TABLE_BOOKS).cell(row(0).column(BookTableModel.COLUMN_TITLE)).value();

        // open a book and edit the title
        openFirstBook();
        bookDetail = findFrame(bookMaster, EditBookDetailMainView.class.getSimpleName());
        final String NEW_TITLE = "newTitle";
        bookDetail.textBox(NAME_TEXTBOX_TITLE).setText(NEW_TITLE);

        // add two new copies
        bookDetail.list().requireItemCount(0);
        bookDetail.button(NAME_BUTTON_ADD_COPY).click(); // add two new copies
        bookDetail.button(NAME_BUTTON_ADD_COPY).click();
        bookDetail.list().requireItemCount(2);

        // save the changes
        bookDetail.button(NAME_BUTTON_SAVE).click();
        bookDetail.requireNotVisible();

        // verify that the changes were applied
        bookMaster.table(NAME_TABLE_BOOKS).cell(row(0).column(BookTableModel.COLUMN_TITLE)).requireValue(NEW_TITLE);
        bookMaster.table(NAME_TABLE_BOOKS).cell(row(0).column(BookTableModel.COLUMN_AMOUNT)).requireValue("2/2");

        // restore original state
        bookMaster.table(NAME_TABLE_BOOKS).cell(row(0).column(BookTableModel.COLUMN_TITLE)).enterValue(OLD_TITLE);
    }

    @Test
    public void newBook() {
        String title = "a title";
        String publisher = "a publisher";
        String author = "an author";

        // open a book and edit the title
        openNewBook();
        bookDetail = findFrame(bookMaster, NewBookDetailMainView.class.getSimpleName());
        bookDetail.textBox(NAME_TEXTBOX_TITLE).setText(title);
        bookDetail.textBox(NAME_TEXTBOX_PUBLISHER).setText(publisher);
        bookDetail.textBox(NAME_TEXTBOX_AUTHOR).setText(author);
        bookDetail.comboBox(NAME_COMBOBOX_SHELF).selectItem(2);

        // add two new copies
        bookDetail.list().requireItemCount(0);
        bookDetail.button(NAME_BUTTON_ADD_COPY).click(); // add two new copies
        bookDetail.button(NAME_BUTTON_ADD_COPY).click();
        bookDetail.list().requireItemCount(2);

        // save the changes
        bookDetail.button(NAME_BUTTON_SAVE).click();
        bookDetail.requireNotVisible();

        // verify that the changes were applied
        bookMaster.table(NAME_TABLE_BOOKS).cell(row(1).column(BookTableModel.COLUMN_TITLE)).requireValue(title);
        bookMaster.table(NAME_TABLE_BOOKS).cell(row(1).column(BookTableModel.COLUMN_AUTHOR)).requireValue(author);
        bookMaster.table(NAME_TABLE_BOOKS).cell(row(1).column(BookTableModel.COLUMN_PUBLISHER)).requireValue(publisher);
        bookMaster.table(NAME_TABLE_BOOKS).cell(row(1).column(BookTableModel.COLUMN_AMOUNT)).requireValue("2/2");
    }

    private void openNewBook() {
        bookMaster.button(NAME_BUTTON_NEW).click();
    }

    private void openFirstBook() {
        bookMaster.table(NAME_TABLE_BOOKS).selectRows(0);
        bookMaster.button(NAME_BUTTON_OPEN).click();
    }

}
