package application.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JOptionPane;

import application.core.Repository;
import application.core.Texts;
import domain.Copy;
import domain.IllegalLoanOperationException;
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

    public void returnBooks(Calendar calendar) {
        List<Loan> loans = Repository.getInstance().getLoansPMod().getBatchReturnLoansTableModel().getLoans();
        List<Loan> updatedLoans = new ArrayList<Loan>();
        for (Loan loan : loans) {
            GregorianCalendar gregCalendar = new GregorianCalendar();
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            try {
                loan.returnCopy(gregCalendar);
                updatedLoans.add(loan);
            } catch (IllegalLoanOperationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        for (Loan loan : updatedLoans) {
            Repository.getInstance().getLoansPMod().updateLoan(loan);
        }

        JOptionPane.showMessageDialog(Repository.getInstance().getMainViewFactory().getReturnLoansView().getContainer(), Texts.get("ReturnLoansMainView.feedbackDialog"));
        Repository.getInstance().getMainViewFactory().closeReturnLoansView();

    }
}
