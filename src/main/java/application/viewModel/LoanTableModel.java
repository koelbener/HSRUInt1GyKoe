package application.viewModel;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import application.core.Texts;
import domain.Loan;

public class LoanTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private final List<Loan> loans;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat();
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
    public Object getValueAt(int rowIndex, int columnIndex) {

        Loan loan = loans.get(rowIndex);

        Object result = null;

        switch (columnIndex) {
        case 0:
            result = loan.isOverdue() ? "fällig" : "ok";
            break;
        case 1:
            result = loan.getCopy().getInventoryNumber();
            break;
        case 2:
            result = loan.getCopy().getTitle();
            break;
        case 3:
            GregorianCalendar returnDate = loan.getReturnDate();
            if (returnDate != null) {
                result = dateFormatter.format(returnDate.getGregorianChange()); // TODO replace with cellrenderer
            }
            break;
        case 4:
            result = loan.getCustomer().getSurname() + " " + loan.getCustomer().getName();
            break;

        default:
            break;
        }

        return result;
    }

}
