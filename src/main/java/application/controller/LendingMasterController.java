package application.controller;

import javax.swing.RowSorter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.view.mainView.LoanDetailMainViewBase;
import application.viewModel.LoanTableModel;
import domain.Loan;

public class LendingMasterController extends ControllerBase {

    private static final Logger logger = LoggerFactory.getLogger(LendingMasterController.class);

    public void searchBooks(String searchText) {
        getRepository().getLoansPMod().setSearchString(searchText);

    }

    public void openLoans(int[] selectedRows) {
        LoanTableModel loanTableModel = getRepository().getLoansPMod().getLoanTableModel();
        RowSorter<LoanTableModel> sorter = getRepository().getLoansPMod().getLoanTableRowSorter();

        for (int index : selectedRows) {
            Loan loan = loanTableModel.getLoan(sorter.convertRowIndexToModel(index));
            logger.debug("opening loan {}", loan.getCopy().getInventoryNumber());
            new LoanDetailMainViewBase(loan);
        }

    }

}
