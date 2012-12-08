package application.controller;

import javax.swing.RowSorter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.core.Repository;
import application.presentationModel.componentModel.LoanSearchFilterComboBoxModel.FilterOption;
import application.presentationModel.componentModel.LoanTableModel;
import domain.Loan;

public class LendingMasterController extends ControllerBase {

    private static final Logger logger = LoggerFactory.getLogger(LendingMasterController.class);

    public void openLoans(int[] selectedRows) {
        LoanTableModel loanTableModel = getRepository().getLoansPMod().getLoanTableModel();
        RowSorter<LoanTableModel> sorter = getRepository().getLoansPMod().getLoanTableRowSorter();

        for (int index : selectedRows) {
            Loan loan = loanTableModel.getLoan(sorter.convertRowIndexToModel(index));
            logger.debug("opening loan {}", loan.getCopy().getInventoryNumber());
            openLoanView(loan);
        }
    }

    private void openLoanView(Loan loan) {
        Repository.getInstance().getMainViewFactory().getLoanDetailMainView(loan);
    }

    public void newLoan() {
        openLoanView(null);

    }

    public void filterLoans() {
        getRepository().getLoansPMod().updateFilter();
    }

    public void filterLoans(String searchString) {
        getRepository().getLoansPMod().updateFilter(searchString);
    }

    public void filterLoans(FilterOption option) {
        getRepository().getLoansPMod().updateFilter(option);
    }

    public void openBackReturn() {
        getRepository().getMainViewFactory().getReturnLoansView();

    }

}
