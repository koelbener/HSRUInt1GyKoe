package application.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.io.Resources;

import domain.Book;
import domain.Copy;
import domain.Customer;
import domain.IllegalLoanOperationException;
import domain.Library;
import domain.Loan;
import domain.Shelf;

public class XmlDataLoader implements DataLoder {
    private static final Logger logger = LoggerFactory.getLogger(XmlDataLoader.class);

    @Override
    public Library loadLibrary() throws Exception {
        Library result = new Library();
        initLibrary(result);
        return result;
    }

    private void initLibrary(Library library) throws ParserConfigurationException, SAXException, IOException, IllegalLoanOperationException {

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        loadCustomersFromXml(library, builder, Resources.getResource("books/xml/customers.xml").openStream());
        loadBooksFromXml(library, builder, Resources.getResource("books/xml/books.xml").openStream());

        // create pseudo random books and loans
        createBooksAndLoans(library);

        logger.info("Initialisation of the library was successful!");
        logger.debug("Books in library: " + library.getBooks().size());
        logger.debug("Customers: " + library.getCustomers().size());
        logger.debug("Copies in library: " + library.getCopies().size());
        logger.debug("Copies currently on loan: " + library.getLentOutBooks().size());
        int lentBooksPercentage = (int) (((double) library.getLentOutBooks().size()) / library.getCopies().size() * 100);
        logger.debug("Percent copies on loan: " + lentBooksPercentage + "%");
        logger.debug("Copies currently overdue: " + library.getOverdueLoans().size());

        for (Loan l : library.getOverdueLoans())
            logger.debug("Loan by {} is overdue by {} days.", l.getCustomer(), l.getDaysOverdue());
    }

    private void createBooksAndLoans(Library library) throws IllegalLoanOperationException {
        for (int i = 0; i < library.getBooks().size(); i++) {
            switch (i % 4) {
            case 0:
                Copy c1 = library.createAndAddCopy(library.getBooks().get(i));
                c1.setCondition(Copy.Condition.GOOD);
                createLoansForCopy(library, c1, i, 5);
                Copy c2 = library.createAndAddCopy(library.getBooks().get(i));
                c2.setCondition(Copy.Condition.DAMAGED);
                createLoansForCopy(library, c2, i, 2);
                Copy c3 = library.createAndAddCopy(library.getBooks().get(i));
                c3.setCondition(Copy.Condition.WASTE);
                break;
            case 1:
                Copy c4 = library.createAndAddCopy(library.getBooks().get(i));
                createLoansForCopy(library, c4, i, 4);
                library.createAndAddCopy(library.getBooks().get(i));
                break;
            case 2:
                Copy c5 = library.createAndAddCopy(library.getBooks().get(i));
                createLoansForCopy(library, c5, i, 2);
                break;
            case 3:
                Copy c6 = library.createAndAddCopy(library.getBooks().get(i));
                createOverdueLoanForCopy(library, c6, i);
                break;
            }
        }
    }

    private void loadBooksFromXml(Library library, DocumentBuilder builder, InputStream file) throws SAXException, IOException {
        Document doc2 = builder.parse(file);
        NodeList titles = doc2.getElementsByTagName("title");
        for (int i = 0; i < titles.getLength(); i++) {
            Node title = titles.item(i);
            Book b = library.createAndAddBook(getTextContentOf(title, "name"));
            b.setAuthor(getTextContentOf(title, "author"));
            b.setPublisher(getTextContentOf(title, "publisher"));
            b.setShelf(Shelf.A1);
        }
    }

    private void loadCustomersFromXml(Library library, DocumentBuilder builder, InputStream file) throws SAXException, IOException {
        Document doc = builder.parse(file);
        NodeList customers = doc.getElementsByTagName("customer");
        for (int i = 0; i < customers.getLength(); i++) {
            Node customer = customers.item(i);

            Customer c = library.createAndAddCustomer(getTextContentOf(customer, "name"), getTextContentOf(customer, "surname"));
            c.setAdress(getTextContentOf(customer, "street"), Integer.parseInt(getTextContentOf(customer, "zip")), getTextContentOf(customer, "city"));
        }
    }

    private void createLoansForCopy(Library library, Copy copy, int position, int count) throws IllegalLoanOperationException {
        // Create Loans in the past
        for (int i = count; i > 1; i--) {
            Loan l = library.createAndAddLoan(getCustomer(library, position + i), copy);
            GregorianCalendar pickup = l.getPickupDate();
            pickup.add(GregorianCalendar.MONTH, -i);
            pickup.add(GregorianCalendar.DAY_OF_MONTH, position % 10);
            l.setPickupDate(pickup);
            GregorianCalendar ret = (GregorianCalendar) pickup.clone();
            ret.add(GregorianCalendar.DAY_OF_YEAR, position % 10 + i * 2);
            l.returnCopy(ret);
        }
        // Create actual open loans
        if (position % 2 == 0) {
            Loan l = library.createAndAddLoan(getCustomer(library, position), copy);
            GregorianCalendar pickup = l.getPickupDate();
            pickup.add(GregorianCalendar.DAY_OF_MONTH, -position % 10);
            l.setPickupDate(pickup);
        }
    }

    private void createOverdueLoanForCopy(Library library, Copy copy, int position) throws IllegalLoanOperationException {
        Loan l = library.createAndAddLoan(getCustomer(library, position), copy);
        GregorianCalendar pickup = l.getPickupDate();
        pickup.add(GregorianCalendar.MONTH, -1);
        pickup.add(GregorianCalendar.DAY_OF_MONTH, -position % 15);
        l.setPickupDate(pickup);
    }

    private Customer getCustomer(Library library, int position) {
        return library.getCustomers().get(position % library.getCustomers().size());
    }

    private String getTextContentOf(Node element, String name) {
        NodeList attributes = element.getChildNodes();
        for (int r = 0; r < attributes.getLength(); r++) {
            if (attributes.item(r).getNodeName().equals(name)) {
                return attributes.item(r).getTextContent();
            }
        }
        return "";
    }

}
