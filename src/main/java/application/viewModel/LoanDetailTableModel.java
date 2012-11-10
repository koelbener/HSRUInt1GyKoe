package application.viewModel;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import application.core.Repository;
import application.core.Texts;
import domain.Customer;
import domain.Loan;

public class LoanDetailTableModel extends AbstractTableModel {

    private static final int COLUMN_COPY_ID = 0;
    private static final int COLUMN_TITLE = 1;
    private static final int COLUMN_AUTHOR = 2;
    private static final long serialVersionUID = 1L;
    private String[] columnNames;
    private final List<Loan> loans;

    public LoanDetailTableModel(Customer customer) {
        this.loans = Repository.getInstance().getLibrary().getCustomerLoans(customer);
        setColumns();
    }

    public void setColumns() {
        columnNames = new String[] {
                //
                Texts.get("LoanDetailMainView.table.column.copyId"), //
                Texts.get("LoanDetailMainView.table.column.title"), //
                Texts.get("LoanDetailMainView.table.column.author") };
        this.fireTableStructureChanged();
    }

    @Override
    public int getRowCount() {
        return loans.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
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
        case COLUMN_COPY_ID:
            result = loan.getCopy().getInventoryNumber();
            break;
        case COLUMN_AUTHOR:
            result = loan.getCopy().getTitle().getAuthor();
            break;
        case COLUMN_TITLE:
            result = loan.getCopy().getTitle().getName();
            break;
        }

        return result;
    }

}
