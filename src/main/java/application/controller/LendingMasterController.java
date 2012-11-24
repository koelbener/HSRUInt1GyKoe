package application.controller;

import java.awt.Frame;

import javax.swing.RowSorter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.view.mainView.dialogView.LoanDetailMainView;
import application.viewModel.LoanSearchFilterComboBoxModel.FilterOption;
import application.viewModel.LoanTableModel;
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
        boolean loanViewAlreadyOpen = false;
        for (Frame frame : Frame.getFrames()) {
            if (frame.getName().equals(LoanDetailMainView.class.getSimpleName())) {
                frame.dispose();
            }
        }
        if (!loanViewAlreadyOpen) {
            new LoanDetailMainView(loan);
        }
    }

    public void newLoan() {
        openLoanView(null);

    }

    public void filterLoans(String searchString) {
        getRepository().getLoansPMod().updateFilter(searchString);
    }

    public void filterLoans(FilterOption option) {
        getRepository().getLoansPMod().updateFilter(option);
    }

}
