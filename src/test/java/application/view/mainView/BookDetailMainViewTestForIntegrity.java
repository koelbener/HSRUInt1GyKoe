package application.view.mainView;

import static application.view.mainView.dialogView.BookDetailMainViewBase.NAME_BUTTON_DELETE_COPY;
import static application.view.mainView.dialogView.BookDetailMainViewBase.NAME_BUTTON_SAVE;
import static application.view.subView.BookMasterSubView.NAME_BUTTON_OPEN;
import static application.view.subView.BookMasterSubView.NAME_TABLE_BOOKS;
import static org.fest.swing.data.TableCell.row;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Pattern;

import javax.swing.SwingUtilities;

import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import application.LibraryApp;
import application.core.Repository;
import application.presentationModel.BooksPMod;
import application.presentationModel.componentModel.BookTableModel;
import application.view.AbstractFestTest;
import application.view.mainView.dialogView.BookDetailMainViewBase;
import application.view.mainView.dialogView.EditBookDetailMainView;
import domain.Book;
import domain.Copy;
import domain.Customer;
import domain.Library;
import domain.Shelf;

public class BookDetailMainViewTestForIntegrity extends AbstractFestTest {
    private static final String BOOK_PUBLISHER = "HSR Hochschule für Technik Rapperswil";
    private static final String BOOK_AUTHOR = "Michi Gysel";
    private static final String BOOK_TITLE = "Testing with JUnit 4";
    private FrameFixture bookMaster;
    private FrameFixture bookDetail;

    private static Library library;

    private static Copy lentCopy;
    private static Copy returnedCopy;
    private static Copy unlentCopy;

    @BeforeClass
    public static void setUpOnce() throws Exception {
        FailOnThreadViolationRepaintManager.install();
        library = new Library();
        Book b = library.createAndAddBook(BOOK_TITLE);
        b.setAuthor(BOOK_AUTHOR);
        b.setPublisher(BOOK_PUBLISHER);
        b.setShelf(Shelf.D1);

        lentCopy = library.createAndAddCopy(b);
        returnedCopy = library.createAndAddCopy(b);
        unlentCopy = library.createAndAddCopy(b);

        Customer customer = library.createAndAddCustomer("Muster", "Peter");

        library.createAndAddLoan(customer, lentCopy);
        library.createAndAddLoan(customer, returnedCopy).returnCopy();

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
    public void deleteNotPossibleOnLentCopy() throws InterruptedException {
        bookMaster.table(NAME_TABLE_BOOKS).cell(row(0).column(BookTableModel.COLUMN_AMOUNT)).requireValue("2/3");

        // open a book and edit the title
        openFirstBook();
        bookDetail = findFrame(bookMaster, EditBookDetailMainView.class.getSimpleName());

        bookDetail.list().requireItemCount(3);

        selectCopy(lentCopy);
        bookDetail.button(NAME_BUTTON_DELETE_COPY).requireDisabled();

        selectCopy(returnedCopy);
        bookDetail.button(NAME_BUTTON_DELETE_COPY).requireDisabled();

        selectCopy(unlentCopy);
        bookDetail.button(NAME_BUTTON_DELETE_COPY).requireEnabled();
        bookDetail.button(NAME_BUTTON_DELETE_COPY).click();

        // save the changes
        bookDetail.button(NAME_BUTTON_SAVE).click();
        bookDetail.requireNotVisible();
        bookMaster.table(NAME_TABLE_BOOKS).cell(row(0).column(BookTableModel.COLUMN_AMOUNT)).requireValue("1/2");
    }

    @Test
    public void parallelEditOnBook() {

        // open a book
        openFirstBook();
        bookDetail = findFrame(bookMaster, EditBookDetailMainView.class.getSimpleName());

        // edit it
        bookMaster.focus();
        bookMaster.table().cell(row(0).column(BookTableModel.COLUMN_AUTHOR)).enterValue("new title");

        // the book detail view should be disabled...
        bookDetail.focus();
        bookDetail.button(BookDetailMainViewBase.NAME_BUTTON_SAVE).requireDisabled();

    }

    @Test
    public void parallelEditOnDifferentBook() throws InvocationTargetException, InterruptedException {

        // open a book
        openFirstBook();
        bookDetail = findFrame(bookMaster, EditBookDetailMainView.class.getSimpleName());

        // edit it
        bookMaster.focus();
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                BooksPMod booksPMod = Repository.getInstance().getBooksPMod();
                Book newBook = booksPMod.createAndAddBook("new book");
                newBook.setAuthor("my author");
                newBook.setPublisher("my publisher");
                newBook.setShelf(Shelf.C1);
                booksPMod.addBook(newBook);
            }
        });
        bookMaster.table().cell(row(1).column(BookTableModel.COLUMN_AUTHOR)).enterValue("new title");

        // the book detail view should be disabled...
        bookDetail.focus();
        bookDetail.button(BookDetailMainViewBase.NAME_BUTTON_SAVE).requireEnabled();

    }

    void selectCopy(Copy lentCopy2) {
        bookDetail.list().selectItem(Pattern.compile(lentCopy2.getInventoryNumber() + ".*"));
    }

    private void openFirstBook() {
        bookMaster.table(NAME_TABLE_BOOKS).selectRows(0);
        bookMaster.button(NAME_BUTTON_OPEN).click();
    }

}
