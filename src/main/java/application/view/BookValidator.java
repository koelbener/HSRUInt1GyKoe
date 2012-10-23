package application.view;

import com.google.common.base.Strings;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;

import domain.Book;

public class BookValidator implements Validator<Book> {

    @Override
    public ValidationResult validate(Book book) {
        ValidationResult result = new ValidationResult();

        if (Strings.isNullOrEmpty(book.getName())) {
            result.addError("The Name is required");
        }

        return result;
    }

}
