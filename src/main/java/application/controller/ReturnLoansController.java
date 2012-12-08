package application.controller;

import application.core.Repository;
import domain.Copy;
import domain.Loan;

public class ReturnLoansController extends ControllerBase {

    public void addLoan(Long copyId) {
        Copy copy = Repository.getInstance().getCopyPMod().searchCopy(copyId);
        Loan currentLoan = Repository.getInstance().getCopyPMod().getCurrentLoan(copy);
        Repository.getInstance().getLoansPMod().getBatchReturnLoansTableModel().addLoan(currentLoan);
    }

    public void removeLoan(int selectedRow) {
        Repository.getInstance().getLoansPMod().getBatchReturnLoansTableModel().removeLoan(selectedRow);
    }
}
