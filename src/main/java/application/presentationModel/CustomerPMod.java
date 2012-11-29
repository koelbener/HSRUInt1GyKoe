package application.presentationModel;

import application.presentationModel.componentModel.CustomerListModel;
import domain.Customer;

public class CustomerPMod extends pModBase {

    private CustomerListModel customerListModel = new CustomerListModel();

    public CustomerListModel getCustomerListModel() {
        return customerListModel;
    }

    public void setCustomerListModel(CustomerListModel customerListModel) {
        this.customerListModel = customerListModel;
    }

    public void updateCustomer(Customer customer) {
        customerListModel.updateCustomer(customer);
    }

}
