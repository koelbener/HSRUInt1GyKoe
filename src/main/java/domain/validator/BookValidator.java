package domain.validator;

import application.core.Texts;

import com.google.common.base.Strings;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;

import domain.Book;

public class BookValidator implements Validator<Book> {

    @Override
    public ValidationResult validate(Book book) {
        ValidationResult result = new ValidationResult();

        if (Strings.isNullOrEmpty(book.getName())) {
            result.addError(Texts.get("validation.titleMustNotBeEmpty"));
        }

        if (Strings.isNullOrEmpty(book.getAuthor())) {
            result.addError(Texts.get("validation.authorMustNotBeEmpty"));
        }

        if (Strings.isNullOrEmpty(book.getPublisher())) {
            result.addError(Texts.get("validation.publisherMustNotBeEmpty"));
        }

        if (book.getShelf() == null) {
            result.addError(Texts.get("validation.shelfMustNotBeEmpty"));
        }

        return result;
    }

}
