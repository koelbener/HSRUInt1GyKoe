package application.controller;

import java.util.ArrayList;
import java.util.List;

import com.jgoodies.validation.ValidationResult;

import domain.Condition;
import domain.Copy;
import domain.Customer;
import domain.Loan;

public class LoanDetailController extends ControllerBase {

    public void filterCustomers(String text) {
        getRepository().getCustomerPMod().getCustomerListModel().filterContent(text);
    }

    public ValidationResult validateLoan(Copy copy, Customer customer) {
        return getRepository().getLoansPMod().validateLoan(copy, customer);
    }

    public Loan saveLoan(Copy copy, Customer customer) {
        Loan loan = getRepository().getLoansPMod().createAndAddLoan(customer, copy);
        getRepository().getLoansPMod().addLoan(loan);
        return loan;
    }

    public void changeCondition(int rowIndex, Condition condition) {
        Loan loan = getRepository().getLoansPMod().getLoanDetailTableModel().getLoan(rowIndex);
        getRepository().getCopyPMod().changeCondition(loan.getCopy(), condition);
    }

    public List<Long> returnCopies(int[] selectedRows) {
        List<Long> result = new ArrayList<Long>();
        for (int row : selectedRows) {
            Loan loan = getRepository().getLoansPMod().getLoanDetailTableModel().getLoan(row);
            if (loan.returnCopy()) {
                result.add(loan.getCopy().getInventoryNumber());
                getRepository().getLoansPMod().updateLoan(loan);
                getRepository().getCustomerPMod().updateCustomer(loan.getCustomer());
            }
        }
        return result;
    }

    public int getOverdueLoans(Customer currentCustomer) {
        return getRepository().getLoansPMod().getOverdueLoans(currentCustomer);
    }
}
