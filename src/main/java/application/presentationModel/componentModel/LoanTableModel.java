package application.presentationModel.componentModel;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import application.core.Repository;
import application.core.Texts;
import application.presentationModel.BooksPMod;
import application.presentationModel.LoansPMod;
import domain.Loan;

public class LoanTableModel extends AbstractTableModel implements Observer {

    public static final int COLUMN_LENT_TO = 5;
    public static final int COLUMN_LENT_UNTIL = 4;
    public static final int COLUMN_LENT_FROM = 3;
    public static final int COLUMN_BOOK_TITLE = 2;
    public static final int COLUMN_COPY_ID = 1;
    public static final int COLUMN_STATUS = 0;
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
                Texts.get("LendingMasterMainView.table.column.lentFrom"), //
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
        return 6;
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
        case COLUMN_LENT_FROM:
            result = Date.class;
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
            result = getStatus(loan);
            break;
        case COLUMN_COPY_ID:
            result = loan.getCopy().getInventoryNumber();
            break;
        case COLUMN_BOOK_TITLE:
            result = loan.getCopy().getTitle().getName();
            break;
        case COLUMN_LENT_UNTIL:
            result = getLentUntil(loan);
            break;
        case COLUMN_LENT_FROM:
            result = getLentFrom(loan);
            break;
        case COLUMN_LENT_TO:
            result = loan.getCustomer().getSurname() + " " + loan.getCustomer().getName();
            break;

        default:
            break;
        }

        return result;
    }

    private Object getLentFrom(Loan loan) {
        Object result = null;
        GregorianCalendar pickupDate = loan.getPickupDate();
        if (pickupDate != null) {
            result = pickupDate.getTime();
        }
        return result;
    }

    private Object getLentUntil(Loan loan) {
        Object result = null;
        GregorianCalendar returnDate = loan.getReturnDate();
        if (returnDate != null) {
            result = returnDate.getTime();
        }
        return result;
    }

    private Object getStatus(Loan loan) {
        Object result;
        if (loan.isOverdue()) {
            result = Texts.get("LendingMasterMainView.table.cell.due");
        } else if (loan.isLent()) {
            result = Texts.get("LendingMasterMainView.table.cell.lent");
        } else {
            result = Texts.get("LendingMasterMainView.table.cell.back");
        }
        return result;
    }

    public Loan getLoan(int index) {
        return loans.get(index);
    }

    public void addLoan(Loan loan) {
        int index = loans.size();
        loans.add(loan);
        fireTableRowsInserted(index, index);
    }

    public void updateLoan(Loan loan) {
        for (int i = 0; i < loans.size(); i++) {
            if (loans.get(i).equals(loan)) {
                loans.set(i, loan);
                fireTableRowsUpdated(i, i);
            }
        }
    }

    public void updateStateValues() {
        if (getRowCount() > 0)
            fireTableRowsUpdated(0, getRowCount() - 1);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o.getClass().equals(BooksPMod.class)) {
            fireTableDataChanged();
        } else if (o.getClass().equals(LoansPMod.class)) {
            Repository.getInstance().getLoansPMod().updateFilter();
        }

    }

}
