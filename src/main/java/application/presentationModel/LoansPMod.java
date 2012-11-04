package application.presentationModel;

import application.core.Repository;
import application.viewModel.LoanTableModel;

public class LoansPMod extends pModBase {
    private final LoanTableModel loanTableModel;

    public LoansPMod() {
        loanTableModel = new LoanTableModel(Repository.getInstance().getLibrary().getLoans());
    }

    public LoanTableModel getLoanTableModel() {
        return loanTableModel;
    }

}
