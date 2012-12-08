package application.presentationModel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;

import application.core.Repository;
import application.core.Texts;
import application.presentationModel.componentModel.BatchReturnLoansTableModel;
import application.presentationModel.componentModel.LoanDetailTableModel;
import application.presentationModel.componentModel.LoanSearchFilterComboBoxModel;
import application.presentationModel.componentModel.LoanSearchFilterComboBoxModel.FilterOption;
import application.presentationModel.componentModel.LoanTableModel;

import com.google.common.base.Preconditions;
import com.jgoodies.validation.ValidationResult;

import domain.Condition;
import domain.Copy;
import domain.Customer;
import domain.Loan;

public class LoansPMod extends AbstractPresentationModel {
    private static BatchReturnLoansTableModel batchReturnLoansTableModel;
    private final LoanTableModel loanTableModel;
    private final TableRowSorter<LoanTableModel> loanTableRowSorter;
    private final LoanDetailTableModel loanDetailTableModel;
    private final LoanSearchFilterComboBoxModel loanSearchFilterComboBoxModel;
    private String searchString = "";
    private FilterOption searchOption = FilterOption.OPEN_LOANS;

    public LoansPMod() {
        super();
        // LoanTable
        loanTableModel = new LoanTableModel(library.getLoans());
        // no need to remove Observer, as loanTableModel always exists
        Repository.getInstance().getBooksPMod().addObserver(loanTableModel);
        this.addObserver(loanTableModel);

        // Filter and Sorter of LoanTable
        loanTableRowSorter = new TableRowSorter<LoanTableModel>(loanTableModel);
        loanSearchFilterComboBoxModel = new LoanSearchFilterComboBoxModel(searchOption);
        updateFilter(searchOption, searchString);

        // LoanDetailTable
        loanDetailTableModel = new LoanDetailTableModel(null);
        // no need to remove Observer, as loanTableModel always exists
        Repository.getInstance().getBooksPMod().addObserver(loanDetailTableModel);

        // BatchReturnLoansTableModel
        batchReturnLoansTableModel = new BatchReturnLoansTableModel();
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
                if (stringToCheck.toLowerCase().indexOf(searchString.toLowerCase()) != -1) {
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

    public void updateFilter() {
        updateFilter(searchOption, searchString);
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

    public Loan createAndAddLoan(Customer customer, Copy copy) {
        Loan loan = library.createAndAddLoan(customer, copy);
        setChanged();
        notifyObservers();
        return loan;
    }

    public boolean hasOpenLoan(Long copyId) {
        for (Loan loan : library.getLoans()) {
            if (loan.isLent() && loan.getCopy().getInventoryNumber() == copyId) {
                return true;
            }
        }
        return false;
    }

    public ValidationResult validateLoan(Copy copy, Customer customer) {
        ValidationResult result = new ValidationResult();

        if (copy == null) {
            result.addError(Texts.get("validation.noCopyFound"));
        } else if (library.isCopyLent(copy)) {
            result.addError(Texts.get("validation.copyLent"));
        } else if (copy.getCondition().equals(Condition.LOST)) {
            result.addError(Texts.get("validation.lostBook"));
        } else if (!library.canCustomerMakeMoreLoans(customer)) {
            result.addError(Texts.get("validation.noMoreLoansAllowed"));
        } else if (customer == null) {
            result.addError(Texts.get("validation.noCustomerSelected"));
        }

        return result;

    }

    public int getOverdueLoans(Customer customer) {
        int result = 0;
        for (Loan loan : library.getCustomerOpenLoans(customer)) {
            if (loan.isOverdue()) {
                result++;
            }
        }
        return result;
    }

    public BatchReturnLoansTableModel getBatchReturnLoansTableModel() {
        return batchReturnLoansTableModel;
    }

    public void renewBatchReturnLoansModel() {
        batchReturnLoansTableModel = new BatchReturnLoansTableModel();

    }

}
