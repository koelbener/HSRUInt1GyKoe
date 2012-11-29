package application.view.subView;

import static application.view.subView.LendingMasterSubView.NAME_COMBOBOX_FILTER;
import static application.view.subView.LendingMasterSubView.NAME_TABLE_LOANS;
import static org.fest.swing.data.TableCell.row;

import java.util.GregorianCalendar;

import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import application.LibraryApp;
import application.core.Texts;
import application.presentationModel.componentModel.LoanTableModel;
import application.view.AbstractFestTest;
import application.view.mainView.MasterMainView;
import domain.Book;
import domain.Copy;
import domain.Customer;
import domain.IllegalLoanOperationException;
import domain.Library;
import domain.Loan;
import domain.Shelf;

public class LendingMasterSubViewTestSearchComponent extends AbstractFestTest {
    FrameFixture window;
    private static Library library;
    private static Copy copyIsLent;
    private static Copy copyWasLent;
    private static Copy copyIsDue;

    @BeforeClass
    public static void setUpOnce() throws Exception {
        FailOnThreadViolationRepaintManager.install();
        library = new Library();

        Customer customer = library.createAndAddCustomer("Kent", "Beck");

        Book book = createBook();
        copyIsLent = library.createAndAddCopy(book);
        copyWasLent = library.createAndAddCopy(book);
        copyIsDue = library.createAndAddCopy(book);

        library.createAndAddLoan(customer, copyIsLent);
        library.createAndAddLoan(customer, copyWasLent).returnCopy();
        markAsDue(library.createAndAddLoan(customer, copyIsDue));

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
        window.tabbedPane().selectTab(Texts.get("BookMasterMainView.tab.lending"));

    }

    @After
    public void tearDown() {
        window.cleanUp();
    }

    @Test
    public void searchBoxHidesDefaultTestOnFocus() {
        window.textBox().requireText(Texts.get("LendingMasterMainView.searchDefault"));
        window.textBox().focus();
        window.textBox().requireText("");
        window.focus();
        window.textBox().requireText(Texts.get("LendingMasterMainView.searchDefault"));
    }

    @Test
    public void searchAllOpen() {
        window.comboBox(NAME_COMBOBOX_FILTER).selectItem(Texts.get("LendingMasterMainView.filterComboBox.onlyOpen"));
        window.table(NAME_TABLE_LOANS).requireRowCount(2);
        verifyRow(0, copyIsLent);
        verifyRow(1, copyIsDue);
    }

    @Test
    public void searchAll() {
        window.comboBox(NAME_COMBOBOX_FILTER).selectItem(Texts.get("LendingMasterMainView.filterComboBox.all"));
        window.table(NAME_TABLE_LOANS).requireRowCount(3);
        verifyRow(0, copyIsLent);
        verifyRow(1, copyWasLent);
        verifyRow(2, copyIsDue);
    }

    @Test
    public void searchOnlyDue() {
        window.comboBox(NAME_COMBOBOX_FILTER).selectItem(Texts.get("LendingMasterMainView.filterComboBox.onlyDue"));
        window.table(NAME_TABLE_LOANS).requireRowCount(1);
        verifyRow(0, copyIsDue);
    }

    private void verifyRow(int index, Copy copy) {
        window.table(NAME_TABLE_LOANS).requireCellValue(row(index).column(LoanTableModel.COLUMN_COPY_ID), "" + copy.getInventoryNumber());
    }

    private static Book createBook() {
        Book book = library.createAndAddBook("book 1");
        book.setShelf(Shelf.B1);
        return book;
    }

    private static void markAsDue(Loan dueLoan) throws IllegalLoanOperationException {
        GregorianCalendar pickupDate = new GregorianCalendar();
        pickupDate.add(GregorianCalendar.DAY_OF_YEAR, -(Loan.DAYS_TO_RETURN_BOOK + 1));
        dueLoan.setPickupDate(pickupDate);
    }

}
