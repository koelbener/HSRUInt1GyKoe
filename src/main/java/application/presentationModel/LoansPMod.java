package application.presentationModel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;

import application.core.Repository;
import application.viewModel.LoanDetailTableModel;
import application.viewModel.LoanSearchFilterComboBoxModel;
import application.viewModel.LoanSearchFilterComboBoxModel.FilterOption;
import application.viewModel.LoanTableModel;

import com.google.common.base.Preconditions;

import domain.Library;
import domain.Loan;

public class LoansPMod extends pModBase {
    private final LoanTableModel loanTableModel;
    private final TableRowSorter<LoanTableModel> loanTableRowSorter;
    private final LoanDetailTableModel loanDetailTableModel;
    private final Library library;
    private final LoanSearchFilterComboBoxModel loanSearchFilterComboBoxModel;
    private String searchString = "";
    private FilterOption searchOption = FilterOption.OPEN_LOANS;

    public LoansPMod() {
        library = Repository.getInstance().getLibrary();
        loanTableModel = new LoanTableModel(library.getLoans());
        loanTableRowSorter = new TableRowSorter<LoanTableModel>(loanTableModel);
        loanDetailTableModel = new LoanDetailTableModel(null);
        loanSearchFilterComboBoxModel = new LoanSearchFilterComboBoxModel(searchOption);
        updateFilter(searchOption, searchString);
    }

    public LoanDetailTableModel getLoanDetailTableModel() {
        return loanDetailTableModel;
    }

    public LoanTableModel getLoanTableModel() {
        return loanTableModel;
    }

    public TableRowSorter<LoanTableModel> getLoanTableRowSorter() {
        return loanTableRowSorter;
    }

    public void addLoan(Loan loan) {
        loanTableModel.addLoan(loan);
        loanDetailTableModel.addLoan(loan);
        setChanged();
        notifyObservers();
    }

    public void updateLoan(Loan loan) {
        loanTableModel.updateLoan(loan);
        loanDetailTableModel.updateLoans(loan);
        setChanged();
        notifyObservers();
    }

    public int getLoansCount() {
        return loanTableModel.getRowCount();
    }

    public int getCurrentLoansCount() {
        return library.getOpenLoans().size();
    }

    public int getOverdueLoansCount() {
        return library.getOverdueLoans().size();
    }

    private class LoanRowFilter extends RowFilter<Object, Object> {

        private final FilterOption option;

        public LoanRowFilter(FilterOption option) {
            Preconditions.checkNotNull(option);
            this.option = option;
        }

        @Override
        public boolean include(RowFilter.Entry<? extends Object, ? extends Object> entry) {
            boolean result = false;
            LoanTableModel model = (LoanTableModel) entry.getModel();
            Loan loan = model.getLoan((Integer) entry.getIdentifier());
            switch (option) {
            case ALL_LOANS:
                result = true;
                break;
            case OPEN_LOANS:
                result = loan.isLent();
                break;
            case DUE_LOANS:
                result = loan.isOverdue();
                break;
            }
            return result;
        }

    }

    public ComboBoxModel<FilterOption> getSearchFilterModel() {
        return loanSearchFilterComboBoxModel;
    }

    public void updateFilter(String searchString) {
        this.searchString = searchString;
        updateFilter(searchOption, searchString);
    }

    public void updateFilter(FilterOption searchOption) {
        this.searchOption = searchOption;
        updateFilter(searchOption, searchString);
    }

    private void updateFilter(FilterOption option, String searchString) {
        List<RowFilter<Object, Object>> filters = new ArrayList<RowFilter<Object, Object>>();

        filters.add(new LoanRowFilter(option));

        if (!searchString.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + searchString));
        }

        loanTableRowSorter.setRowFilter(RowFilter.andFilter(filters));
    }

}
