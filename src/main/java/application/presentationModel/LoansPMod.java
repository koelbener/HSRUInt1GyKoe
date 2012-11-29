package application.presentationModel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;

import application.core.Repository;
import application.viewModel.CopyStatusComboBoxModel;
import application.viewModel.LoanDetailTableModel;
import application.viewModel.LoanSearchFilterComboBoxModel;
import application.viewModel.LoanSearchFilterComboBoxModel.FilterOption;
import application.viewModel.LoanTableModel;

import com.google.common.base.Preconditions;

import domain.Copy;
import domain.Customer;
import domain.Library;
import domain.Loan;

public class LoansPMod extends pModBase {
    private final LoanTableModel loanTableModel;
    private final TableRowSorter<LoanTableModel> loanTableRowSorter;
    private final LoanDetailTableModel loanDetailTableModel;
    private final CopyStatusComboBoxModel copyStatusModel;
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
        copyStatusModel = new CopyStatusComboBoxModel();
        updateFilter(searchOption, searchString);
    }

    public CopyStatusComboBoxModel getCopyStatusModel() {
        return copyStatusModel;
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

    public Copy searchCopy(Long copyId) {
        return library.getCopyByInventoryNr(copyId);
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

    public boolean isCopyLent(Copy copy) {
        return library.isCopyLent(copy);
    }

    public Customer getLender(Copy copy) {
        return library.getLender(copy);
    }

    public Loan getCurrentLoan(Copy copy) {
        return library.getCurrentLoan(copy);
    }

    public Loan getFirstLoanOfCustomer(Customer customer) {
        Loan result;
        List<Loan> loans = library.getCustomerOpenLoans(customer);
        if (!loans.isEmpty()) {
            result = loans.get(0);
        } else {
            result = null;
        }
        return result;
    }

    private class LoanRowStatusFilter extends RowFilter<Object, Object> {

        private final FilterOption option;

        public LoanRowStatusFilter(FilterOption option) {
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

    private class LoanRowSearchFilter extends RowFilter<Object, Object> {

        private final String searchString;

        public LoanRowSearchFilter(String searchString) {
            Preconditions.checkNotNull(searchString);
            this.searchString = searchString;
        }

        @Override
        public boolean include(RowFilter.Entry<? extends Object, ? extends Object> entry) {
            boolean result = false;
            LoanTableModel model = (LoanTableModel) entry.getModel();
            Loan loan = model.getLoan((Integer) entry.getIdentifier());
            for (String stringToCheck : getRelevantStrings(loan)) {
                if (stringToCheck.indexOf(searchString) != -1) {
                    result = true;
                    break;
                }
            }
            return result;
        }

        private List<String> getRelevantStrings(Loan loan) {
            Preconditions.checkNotNull(loan);
            List<String> result = new ArrayList<String>();
            result.add(loan.getCustomer().getFullName());
            result.add(Long.toString(loan.getCopy().getInventoryNumber()));
            result.add(loan.getCopy().getTitle().getName());
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

        filters.add(new LoanRowStatusFilter(option));

        if (!searchString.isEmpty()) {
            filters.add(new LoanRowSearchFilter(searchString));
        }

        loanTableRowSorter.setRowFilter(RowFilter.andFilter(filters));
    }

}
