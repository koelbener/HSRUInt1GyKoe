package application.viewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import application.core.Repository;
import application.core.Texts;
import domain.Customer;
import domain.Loan;

public class LoanDetailTableModel extends AbstractTableModel {

    public static final int COLUMN_COPY_ID = 0;
    public static final int COLUMN_TITLE = 1;
    public static final int COLUMN_AUTHOR = 2;
    public static final int COLUMN_ENDDATE = 3;
    private static final long serialVersionUID = 1L;
    private String[] columnNames;
    private final List<Loan> loans;

    public LoanDetailTableModel(Customer customer) {
        this.loans = new ArrayList<Loan>();
        setColumns();
    }

    public void setColumns() {
        columnNames = new String[] {
                //
                Texts.get("LoanDetailMainView.table.column.copyId"), //
                Texts.get("LoanDetailMainView.table.column.title"), //
                Texts.get("LoanDetailMainView.table.column.author"), Texts.get("LoanDetailMainView.table.column.endDate") };
        this.fireTableStructureChanged();
    }

    @Override
    public int getRowCount() {
        return loans.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Class<?> result = String.class;

        switch (columnIndex) {
        case COLUMN_COPY_ID:
            result = Integer.class;
            break;
        case COLUMN_TITLE:
            result = String.class;
            break;
        case COLUMN_AUTHOR:
            result = String.class;
            break;
        case COLUMN_ENDDATE:
            result = String.class;
            break;
        default:
            break;
        }

        return result;
    }

    public Loan getLoan(int row) {
        return loans.get(row);
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
            GregorianCalendar dueDate = loan.getDueDate();
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            result = format.format(dueDate.getTime());
            break;
        }

        return result;
    }

    public void addLoan(Loan loan) {
        loans.add(loan);
        fireTableDataChanged();
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
}
