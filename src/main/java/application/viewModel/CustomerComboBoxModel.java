package application.viewModel;

import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import application.core.Repository;
import domain.Customer;

public class CustomerComboBoxModel extends AbstractListModel<Customer> implements ComboBoxModel<Customer> {

    private static final long serialVersionUID = -3087379838481895619L;
    List<Customer> customers;
    int selectedElement;

    public CustomerComboBoxModel() {
        initContents();
        selectedElement = 0;
    }

    private void initContents() {
        customers = Repository.getInstance().getLibrary().getCustomers();
    }

    public void updateTexts() {
        initContents();
        fireContentsChanged(this, 0, 3);
    }

    @Override
    public Customer getElementAt(int arg0) {
        return customers.get(arg0);
    }

    @Override
    public int getSize() {
        return customers.size();
    }

    @Override
    public Object getSelectedItem() {
        return customers.get(selectedElement);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedElement = customers.indexOf(anItem);

    }

}
