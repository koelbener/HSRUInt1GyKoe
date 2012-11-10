package application.view.helper;

import java.awt.Component;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import application.core.Repository;
import application.util.IconUtil;
import domain.Customer;
import domain.Loan;

public class CustomerComboBoxRenderer extends JLabel implements ListCellRenderer<Object> {

    private static final long serialVersionUID = 6995598407842395910L;

    public CustomerComboBoxRenderer() {
        setOpaque(true);
        setHorizontalAlignment(LEFT);
        setVerticalAlignment(CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        boolean newLoanAllowed = false;
        if (value instanceof Customer) {
            newLoanAllowed = true;
            List<Loan> customerLoans = Repository.getInstance().getLibrary().getCustomerLoans((Customer) value);
            if (customerLoans.size() >= 3) {
                newLoanAllowed = false;
            } else {
                for (Loan loan : customerLoans) {
                    if (loan.isOverdue()) {
                        newLoanAllowed = false;
                        break;
                    }
                }
            }

        }

        if (newLoanAllowed) {
            setIcon(IconUtil.loadIcon("check.png"));
        } else {
            setIcon(IconUtil.loadIcon("warning.png"));
        }
        setText(value.toString());

        return this;
    }
}
