package application.presentationModel.componentModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import application.core.Repository;
import application.core.Texts;
import application.presentationModel.BooksPMod;
import domain.Customer;
import domain.Loan;

public class BatchReturnLoansTableModel extends AbstractTableModel implements Observer {

    public static final int COLUMN_COPY_ID = 0;
    public static final int COLUMN_TITLE = 1;
    public static final int COLUMN_AUTHOR = 2;
    public static final int COLUMN_ENDDATE = 3;
    public static final int COLUMN_MESSAGE = 4;
    private static final long serialVersionUID = 1L;
    private String[] columnNames;
    private final List<Loan> loans;

    public BatchReturnLoansTableModel() {
        this.loans = new ArrayList<Loan>();
        Texts.getInstance().addObserver(this);
        setColumns();
    }

    public void setColumns() {
        columnNames = new String[] {
                //
                Texts.get("LoanDetailMainView.table.column.copyId"), //
                Texts.get("LoanDetailMainView.table.column.title"), //
                Texts.get("LoanDetailMainView.table.column.author"), //
                Texts.get("LoanDetailMainView.table.column.endDate"), //
                Texts.get("ReturnLoansMainView.table.column.message") };
        this.fireTableStructureChanged();
    }

    @Override
    public int getRowCount() {
        return loans.size();
    }

    public int getRowIndex(Loan loan) {
        return loans.indexOf(loan);
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public Long getCopyOfRow(int index) {
        if (index >= 0 && index < loans.size()) {
            return (Long) getValueAt(index, COLUMN_COPY_ID);
        }
        return null;

    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Class<?> result = String.class;

        switch (columnIndex) {
        case COLUMN_COPY_ID:
            result = Long.class;
            break;
        case COLUMN_TITLE:
            result = String.class;
            break;
        case COLUMN_AUTHOR:
            result = String.class;
            break;
        case COLUMN_ENDDATE:
            result = Date.class;
            break;
        case COLUMN_MESSAGE:
            result = String.class;
        default:
            break;
        }

        return result;
    }

    public Loan getLoan(int row) {
        if (row >= 0 && row < loans.size()) {
            return loans.get(row);
        }
        return null;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Loan loan = loans.get(rowIndex);
        Object result = null;
        switch (columnIndex) {
        case COLUMN_COPY_ID:
            result = loan.getCopy().getInventoryNumber();
            break;
        case COLUMN_AUTHOR:
            result = loan.getCopy().getTitle().getAuthor();
            break;
        case COLUMN_TITLE:
            result = loan.getCopy().getTitle().getName();
            break;
        case COLUMN_ENDDATE:
            result = loan.getDueDate().getTime();
            break;
        case COLUMN_MESSAGE:
            if (loan.isOverdue()) {
                result = Texts.get("LoanDetailMainViewBase.loansOverview.fine", Loan.FINE_PER_BOOK);
            } else {
                result = "";
            }
            break;
        }

        return result;
    }

    public void removeLoan(int row) {
        loans.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public void addLoan(Loan loan) {
        int index = loans.size();
        loans.add(loan);
        fireTableRowsInserted(index, index);
    }

    public void updateLoans(Customer customer) {
        loans.clear();
        loans.addAll(Repository.getInstance().getLibrary().getCustomerOpenLoans(customer));
        fireTableDataChanged();
    }

    public void updateLoans(Loan loan) {
        for (int i = 0; i < loans.size(); i++) {
            if (loans.get(i).equals(loan)) {
                loans.set(i, loan);
                fireTableRowsUpdated(i, i);
            }
        }

    }

    @Override
    public void update(Observable o, Object arg) {
        if (o.getClass().equals(BooksPMod.class)) {
            fireTableDataChanged();
        } else if (o.getClass().equals(Texts.class)) {
            setColumns();
            fireTableStructureChanged();
        }
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void updateTableData() {
        List<Loan> loansToRemove = new ArrayList<Loan>();
        for (Loan loan : loans) {
            if (!loan.isLent()) {
                loansToRemove.add(loan);
            }
        }
        for (Loan loan : loansToRemove) {
            loans.remove(loan);
        }
        fireTableDataChanged();
    }
}
