package application.view.helper;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import application.core.Repository;
import application.util.IconUtil;
import domain.Customer;

public class CustomerListRenderer extends JLabel implements ListCellRenderer<Object> {

    private static final long serialVersionUID = 6995598407842395910L;

    public CustomerListRenderer() {
        setOpaque(true);
        setHorizontalAlignment(LEFT);
        setVerticalAlignment(CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        boolean newLoanAllowed = false;
        if (value instanceof Customer) {
            Customer customer = (Customer) value;
            newLoanAllowed = Repository.getInstance().getCustomerPMod().canCustomerMakeMoreLoans(customer);

            setText(customer.getFullNameAndAddress());
        }

        if (isSelected) {
            setBackground(Color.LIGHT_GRAY);
        } else {
            setBackground(Color.WHITE);
        }

        if (newLoanAllowed) {
            setIcon(IconUtil.loadIcon("check.png"));
        } else {
            setIcon(IconUtil.loadIcon("warning.png"));
        }

        return this;
    }
}
