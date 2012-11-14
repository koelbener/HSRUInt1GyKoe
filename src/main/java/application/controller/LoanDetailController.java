package application.controller;

import application.core.Texts;

import com.jgoodies.validation.ValidationResult;

import domain.Copy;
import domain.Customer;
import domain.Library;
import domain.Loan;

public class LoanDetailController extends ControllerBase {

    public void filterCustomers(String text) {
        getRepository().getCutomerPMod().getCustomerComboBoxModel().filterContent(text);
    }

    public ValidationResult validateLoan(Long copyNr, Customer customer) {
        ValidationResult result = new ValidationResult();
        Library library = getRepository().getLibrary();

        Copy copy = library.getCopyByInventoryNr(copyNr);

        if (copy == null) {
            result.addError(Texts.get("validation.noCopyFound"));
        } else if (library.isCopyLent(copy)) {
            result.addError(Texts.get("validation.copyLent"));
        }

        return result;

    }

    public Loan saveLoan(Long copyId, Customer customer) {
        Copy copy = getRepository().getLibrary().getCopyByInventoryNr(copyId);
        Loan loan = getRepository().getLibrary().createAndAddLoan(customer, copy);
        getRepository().getLoansPMod().addLoan(loan);
        return loan;
    }

    public Copy searchCopy(Long copyId) {
        return getRepository().getLibrary().getCopyByInventoryNr(copyId);
    }
}
