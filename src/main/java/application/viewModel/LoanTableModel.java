package application.viewModel;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import application.core.Texts;
import domain.Loan;

public class LoanTableModel extends AbstractTableModel {

    private static final int COLUMN_LENT_TO = 4;
    private static final int COLUMN_LENT_UNTIL = 3;
    private static final int COLUMN_BOOK_TITLE = 2;
    private static final int COLUMN_COPY_ID = 1;
    private static final int COLUMN_STATUS = 0;
    private static final long serialVersionUID = 1L;
    private final List<Loan> loans;
    private String[] columnNames;

    public LoanTableModel(List<Loan> loans) {
        this.loans = loans;
        setColumns();
    }

    public void setColumns() {
        columnNames = new String[] {
                //
                Texts.get("LendingMasterMainView.table.column.status"), //
                Texts.get("LendingMasterMainView.table.column.copy"), //
                Texts.get("LendingMasterMainView.table.column.title"), //
                Texts.get("LendingMasterMainView.table.column.lentUntil"), //
                Texts.get("LendingMasterMainView.table.column.lentTo") };
        this.fireTableStructureChanged();
    }

    @Override
    public int getRowCount() {
        return loans.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Class<?> result = String.class;

        switch (columnIndex) {
        case COLUMN_STATUS:
            result = String.class;
            break;
        case COLUMN_COPY_ID:
            result = Long.class;
            break;
        case COLUMN_BOOK_TITLE:
            result = String.class;
            break;
        case COLUMN_LENT_UNTIL:
            result = Date.class;
            break;
        case COLUMN_LENT_TO:
            result = String.class;
            break;
        default:
            break;
        }

        return result;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Loan loan = loans.get(rowIndex);

        Object result = null;

        switch (columnIndex) {
        case COLUMN_STATUS:
            result = loan.isOverdue() ? "fällig" : "ok";
            break;
        case COLUMN_COPY_ID:
            result = loan.getCopy().getInventoryNumber();
            break;
        case COLUMN_BOOK_TITLE:
            result = loan.getCopy().getTitle().getName();
            break;
        case COLUMN_LENT_UNTIL:
            GregorianCalendar returnDate = loan.getReturnDate();
            if (returnDate != null) {
                result = returnDate.getTime();
            }
            break;
        case COLUMN_LENT_TO:
            result = loan.getCustomer().getSurname() + " " + loan.getCustomer().getName();
            break;

        default:
            break;
        }

        return result;
    }

    public Loan getLoan(int index) {
        return loans.get(index);
    }

}
