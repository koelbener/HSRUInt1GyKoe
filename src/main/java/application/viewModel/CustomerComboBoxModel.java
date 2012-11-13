package application.viewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.core.Repository;
import domain.Customer;

@SuppressWarnings("rawtypes")
// is not supported by Java6
public class CustomerComboBoxModel extends AbstractListModel implements MutableComboBoxModel {

    private static final Logger logger = LoggerFactory.getLogger(CustomerComboBoxModel.class);
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
        logger.debug("load new customers to customerComboBox: " + customers);
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
        return customers.get(arg0);
    }

    @Override
    public int getSize() {
        return customers.size();
    }

    @Override
    public Object getSelectedItem() {
        if (customers.size() >= selectedElement + 1) {
            return customers.get(selectedElement);
        }
        return "Kein Eintrag gefunden";
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedElement = customers.indexOf(anItem);
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

    @Override
    public void addElement(Object arg0) {
        customers.add((Customer) arg0);
    }

    @Override
    public void insertElementAt(Object arg0, int arg1) {
        customers.add(arg1, (Customer) arg0);
    }

    @Override
    public void removeElement(Object arg0) {
        customers.remove(arg0);
    }

    @Override
    public void removeElementAt(int arg0) {
        customers.remove(arg0);
    }

}
