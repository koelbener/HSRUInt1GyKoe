package application.viewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractListModel;

import application.core.Repository;
import domain.Customer;

public class CustomerComboBoxModel extends AbstractListModel<Customer> {

    private static final long serialVersionUID = -3087379838481895619L;
    List<Customer> customers;
    int selectedElement;

    public CustomerComboBoxModel() {
        initContents();
        selectedElement = 0;
    }

    private void initContents() {
        setCustomers(Repository.getInstance().getLibrary().getCustomers());
    }

    public void setCustomers(List<Customer> customers) {
        sortCustomers(customers);
        this.customers = customers;
        fireContentsChanged(this, 0, customers.size() - 1);
    }

    private void sortCustomers(List<Customer> customers) {
        Collections.sort(customers, new Comparator<Customer>() {
            @Override
            public int compare(Customer c1, Customer c2) {
                return c1.toString().compareTo(c2.toString());
            };
        });
    }

    public void updateTexts() {
        initContents();
    }

    @Override
    public Customer getElementAt(int arg0) {
        if (customers.size() > arg0) {
            return customers.get(arg0);
        }
        return null;
    }

    @Override
    public int getSize() {
        return customers.size();
    }

    public void filterContent(String filter) {

        List<Customer> allCustomers = Repository.getInstance().getLibrary().getCustomers();
        List<Customer> filtredCustomers = new ArrayList<Customer>();
        for (Customer customer : allCustomers) {
            if (customer.toString().toLowerCase().indexOf(filter.toLowerCase()) != -1) {
                filtredCustomers.add(customer);
            }
        }
        setCustomers(filtredCustomers);
    }

    public void updateCustomer(Customer customer) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).equals(customer)) {
                fireContentsChanged(this, i, i);
            }
        }
    }

}
