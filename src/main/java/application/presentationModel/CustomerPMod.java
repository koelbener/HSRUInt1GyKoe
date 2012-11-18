package application.presentationModel;

import application.viewModel.CustomerComboBoxModel;
import domain.Customer;

public class CustomerPMod extends pModBase {

    private CustomerComboBoxModel customerComboBoxModel = new CustomerComboBoxModel();

    public CustomerComboBoxModel getCustomerComboBoxModel() {
        return customerComboBoxModel;
    }

    public void setCustomerComboBoxModel(CustomerComboBoxModel customerComboBoxModel) {
        this.customerComboBoxModel = customerComboBoxModel;
    }

    public void updateCustomer(Customer customer) {
        customerComboBoxModel.updateCustomer(customer);
    }

}
