package application.controller;

import application.core.Repository;
import domain.Loan;

public class LoanDetailController extends ControllerBase {

    public void filterCustomers(String text) {
        Repository.getInstance().getCutomerPMod().getCustomerComboBoxModel().filterContent(text);
    }

    public void saveLoan(Loan loan) {

    }

}
