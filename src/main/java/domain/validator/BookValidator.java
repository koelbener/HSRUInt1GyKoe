package domain.validator;

import com.google.common.base.Strings;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;

import domain.Book;

public class BookValidator implements Validator<Book> {

    @Override
    public ValidationResult validate(Book book) {
        ValidationResult result = new ValidationResult();

        if (Strings.isNullOrEmpty(book.getName())) {
            result.addError("Titel darf nicht leer sein.");
        }

        if (Strings.isNullOrEmpty(book.getAuthor())) {
            result.addError("Autor darf nicht leer sein.");
        }

        if (Strings.isNullOrEmpty(book.getPublisher())) {
            result.addError("Verlag darf nicht leer sein.");
        }

        if (book.getShelf() == null) {
            result.addError("Regal darf nicht leer sein.");
        }

        return result;
    }

}
