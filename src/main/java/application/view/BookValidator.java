package application.view;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.util.ValidationUtils;

import domain.Book;

public class BookValidator implements Validator<Book> {

    @Override
    public ValidationResult validate(Book book) {
        ValidationResult result = new ValidationResult();

        if (ValidationUtils.isEmpty(book.getName())) {
            result.addError("The Name is required");
        }

        return result;
    }

}
