package application.controller;

import java.util.ArrayList;
import java.util.List;

import application.core.Repository;
import application.core.Texts;

import com.jgoodies.validation.ValidationResult;

import domain.Copy;
import domain.Customer;
import domain.Library;
import domain.Loan;

public class LoanDetailController extends ControllerBase {

    public void filterCustomers(String text) {
        getRepository().getCustomerPMod().getCustomerListModel().filterContent(text);
    }

    public ValidationResult validateLoan(Copy copy, Customer customer) {
        ValidationResult result = new ValidationResult();
        Library library = getRepository().getLibrary();

        if (copy == null) {
            result.addError(Texts.get("validation.noCopyFound"));
        } else if (library.isCopyLent(copy)) {
            result.addError(Texts.get("validation.copyLent"));
        } else if (!library.canCustomerMakeMoreLoans(customer)) {
            result.addError(Texts.get("validation.noMoreLoansAllowed"));
        } else if (customer == null) {
            result.addError(Texts.get("validation.noCustomerSelected"));
        }

        return result;

    }

    public Loan saveLoan(Copy copy, Customer customer) {
        Loan loan = getRepository().getLibrary().createAndAddLoan(customer, copy);
        getRepository().getLoansPMod().addLoan(loan);
        return loan;
    }

    public Copy searchCopy(Long copyId) {
        return getRepository().getLibrary().getCopyByInventoryNr(copyId);
    }

    public List<Long> returnCopies(int[] selectedRows) {
        List<Long> result = new ArrayList<Long>();
        for (int row : selectedRows) {
            Loan loan = Repository.getInstance().getLoansPMod().getLoanDetailTableModel().getLoan(row);
            if (loan.returnCopy()) {
                result.add(loan.getCopy().getInventoryNumber());
                getRepository().getLoansPMod().updateLoan(loan);
                getRepository().getCustomerPMod().updateCustomer(loan.getCustomer());
            }
        }
        return result;
    }
}
