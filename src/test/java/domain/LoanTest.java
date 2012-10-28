package domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

public class LoanTest {
    private static final int TOLERANCE_IN_MILLIS = 10;

    @Test
    public void testLoanCreation() {
        Loan l = createSampleLoan();
        assertEqualsWithTolerance(TOLERANCE_IN_MILLIS, new GregorianCalendar(), l.getPickupDate());
        assertEquals("Keller", l.getCustomer().getName());
        assertEquals("Design Pattern", l.getCopy().getTitle().getName());
    }

    @Test
    public void testReturn() {
        Loan l = createSampleLoan();
        assertTrue(l.isLent());
        l.returnCopy();
        assertFalse(l.isLent());
    }

    @Test
    public void testDurationCalculation() throws IllegalLoanOperationException {
        Loan l = createSampleLoan();
        assertEquals(-1, l.getDaysOfLoanDuration());

        GregorianCalendar returnDate = (GregorianCalendar) l.getPickupDate().clone();
        returnDate.add(GregorianCalendar.DAY_OF_YEAR, 12);

        l.returnCopy(returnDate);

        assertEquals(12, l.getDaysOfLoanDuration());
        assertEquals(l.getReturnDate(), returnDate);

    }

    @Test
    public void testDateConsistency() throws IllegalLoanOperationException {
        Loan l = createSampleLoan();
        l.setPickupDate(new GregorianCalendar(2009, 01, 01));
        assertTrue(l.isLent());
        try {
            l.returnCopy(new GregorianCalendar(2008, 12, 31));
            fail("Book cannot retuned before the pickup date");
        } catch (IllegalLoanOperationException e) {

        }
        assertTrue(l.isLent());
        l.returnCopy(new GregorianCalendar(2009, 12, 31));
        assertFalse(l.isLent());
        try {
            l.setPickupDate(new GregorianCalendar(2008, 10, 31));
            fail("pickup date cannot be set after the book was returned");
        } catch (IllegalLoanOperationException e) {
        }
    }

    private Loan createSampleLoan() {
        Customer customer = new Customer("Keller", "Peter");
        Book title = new Book("Design Pattern");
        Copy copy = new Copy(title);
        Loan loan = new Loan(customer, copy);
        return loan;
    }

    private void assertEqualsWithTolerance(long toleranceInMillis, Calendar expected, Calendar actual) {
        assertEqualsWithTolerance(toleranceInMillis, expected.getTimeInMillis(), actual.getTimeInMillis());
    }

    private void assertEqualsWithTolerance(long tolerance, long expected, long actual) {
        assertTrue(Math.abs(expected - actual) < tolerance);
    }
}
