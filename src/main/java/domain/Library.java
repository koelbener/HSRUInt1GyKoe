package domain;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

public class Library {

    private final List<Copy> copies;
    private final List<Customer> customers;
    private final List<Loan> loans;
    private final List<Book> books;

    public Library() {
        copies = new ArrayList<Copy>();
        customers = new ArrayList<Customer>();
        loans = new ArrayList<Loan>();
        books = new ArrayList<Book>();
    }

    public Loan createAndAddLoan(Customer customer, Copy copy) {
        if (!isCopyLent(copy)) {
            Loan l = new Loan(customer, copy);
            loans.add(l);
            return l;
        } else {
            return null;
        }
    }

    public Customer createAndAddCustomer(String name, String surname) {
        Customer c = new Customer(name, surname);
        customers.add(c);
        return c;
    }

    public Book createAndAddBook(String name) {
        Book b = new Book(name);
        books.add(b);
        return b;
    }

    public Copy createAndAddCopy(Book title) {
        Copy c = new Copy(title);
        copies.add(c);
        return c;
    }

    public Copy addCopy(Copy c) {
        copies.add(c);
        return c;
    }

    public void removeCopy(Copy copy) {
        copies.remove(copy);

    }

    public Book findByBookTitle(String title) {
        for (Book b : books) {
            if (b.getName().equals(title)) {
                return b;
            }
        }
        return null;
    }

    public boolean isCopyLent(Copy copy) {
        for (Loan l : loans) {
            if (l.getCopy().equals(copy) && l.isLent()) {
                return true;
            }
        }
        return false;
    }

    public boolean isOrWasCopyLent(Copy copy) {
        for (Loan l : loans) {
            if (l.getCopy().equals(copy)) {
                return true;
            }
        }
        return false;
    }

    public Customer getLender(Copy copy) {
        Loan currentLoan = getCurrentLoan(copy);
        if (currentLoan != null) {
            return currentLoan.getCustomer();
        }
        return null;
    }

    public Loan getCurrentLoan(Copy copy) {
        for (Loan loan : getLoans()) {
            if (loan.getCopy().equals(copy)) {
                if (loan.isLent()) {
                    return loan;
                }
            }
        }
        return null;
    }

    public List<Copy> getCopiesOfBook(Book book) {
        checkNotNull(book);
        List<Copy> res = new ArrayList<Copy>();
        for (Copy c : copies) {
            if (c.getTitle().equals(book)) {
                res.add(c);
            }
        }

        return res;
    }

    public List<Loan> getLentCopiesOfBook(Book book) {
        List<Loan> lentCopies = new ArrayList<Loan>();
        for (Loan l : loans) {
            if (l.getCopy().getTitle().equals(book) && l.isLent()) {
                lentCopies.add(l);
            }
        }
        return lentCopies;
    }

    public List<Loan> getCustomerLoans(Customer customer) {
        List<Loan> lentCopies = new ArrayList<Loan>();
        for (Loan l : loans) {
            if (l.getCustomer().equals(customer)) {
                lentCopies.add(l);
            }
        }
        return lentCopies;
    }

    public List<Loan> getCustomerOpenLoans(Customer customer) {
        List<Loan> lentCopies = new ArrayList<Loan>();
        for (Loan l : loans) {
            if (l.getCustomer().equals(customer) && l.isLent()) {
                lentCopies.add(l);
            }
        }
        return lentCopies;
    }

    public boolean canCustomerMakeMoreLoans(Customer customer) {
        boolean result = true;
        List<Loan> openLoans = getCustomerOpenLoans(customer);
        if (openLoans.size() >= 3) {
            result = false;
        } else {
            for (Loan loan : openLoans) {
                if (loan.isOverdue()) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    public List<Loan> getOverdueLoans() {
        List<Loan> overdueLoans = new ArrayList<Loan>();
        for (Loan l : getLoans()) {
            if (l.isOverdue())
                overdueLoans.add(l);
        }
        return overdueLoans;
    }

    public List<Copy> getAvailableCopies() {
        return getCopies(false);
    }

    public List<Copy> getLentOutBooks() {
        return getCopies(true);
    }

    private List<Copy> getCopies(boolean isLent) {
        List<Copy> retCopies = new ArrayList<Copy>();
        for (Copy c : copies) {
            if (isLent == isCopyLent(c)) {
                retCopies.add(c);
            }
        }
        return retCopies;
    }

    public List<Copy> getCopies() {
        return new ArrayList<Copy>(copies);
    }

    public Copy getCopyByInventoryNr(long id) {
        Copy result = null;
        for (Copy copy : copies) {
            if (copy.getInventoryNumber() == id) {
                result = copy;
            }
        }
        return result;
    }

    public List<Loan> getLoans() {
        return new ArrayList<Loan>(loans);
    }

    public List<Loan> getOpenLoans() {
        List<Loan> openLoans = new ArrayList<Loan>();
        for (Loan loan : loans) {
            if (loan.isLent()) {
                openLoans.add(loan);
            }
        }
        return openLoans;
    }

    public List<Book> getBooks() {
        return new ArrayList<Book>(books);
    }

    public List<Customer> getCustomers() {
        return new ArrayList<Customer>(customers);
    }

    public List<Book> getAvailableBooks() {
        List<Book> availableBooks = new ArrayList<Book>();
        for (Book book : books) {
            if (getLentCopiesOfBook(book).size() < getCopiesOfBook(book).size()) {
                availableBooks.add(book);
            }
        }
        return availableBooks;
    }

}
