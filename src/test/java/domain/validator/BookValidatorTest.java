package domain.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.jgoodies.validation.ValidationResult;

import domain.Book;
import domain.Shelf;

public class BookValidatorTest {

    private BookValidator validator;
    private Book validBook;

    @Before
    public void setUp() throws Exception {
        validator = new BookValidator();
        validBook = new Book();
        validBook.setName("Testing with JUnit 4");
        validBook.setAuthor("Michi Gysel");
        validBook.setPublisher("HSR Hochschule f�r Technik Rapperswil");
        validBook.setShelf(Shelf.A1);
    }

    @Test
    public void validateBookTitleIsMandatory() {
        // Setup fixtures
        validBook.setName("");
        // Exercise SUT
        ValidationResult validationResult = validator.validate(validBook);
        // Validate result
        assertTrue(validationResult.hasErrors());
        assertEquals(1, validationResult.size());
        assertEquals("Titel darf nicht leer sein.", validationResult.get(0).formattedText());
    }

    @Test
    public void validateBookAuthorIsMandatory() {
        // Setup fixtures
        validBook.setAuthor(null);
        // Exercise SUT
        ValidationResult validationResult = validator.validate(validBook);
        // Validate result
        assertTrue(validationResult.hasErrors());
        assertEquals(1, validationResult.size());
        assertEquals("Autor darf nicht leer sein.", validationResult.get(0).formattedText());
    }

    @Test
    public void validateBookPublisherIsMandatory() {
        // Setup fixtures
        validBook.setPublisher("");
        // Exercise SUT
        ValidationResult validationResult = validator.validate(validBook);
        // Validate result
        assertTrue(validationResult.hasErrors());
        assertEquals(1, validationResult.size());
        assertEquals("Verlag darf nicht leer sein.", validationResult.get(0).formattedText());
    }

    @Test
    public void validateBookShelfIsMandatory() {
        // Setup fixtures
        validBook.setShelf(null);
        // Exercise SUT
        ValidationResult validationResult = validator.validate(validBook);
        // Validate result
        assertTrue(validationResult.hasErrors());
        assertEquals(1, validationResult.size());
        assertEquals("Regal darf nicht leer sein.", validationResult.get(0).formattedText());

    }

}
