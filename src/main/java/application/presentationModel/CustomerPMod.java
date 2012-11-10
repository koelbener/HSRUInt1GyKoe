package application.presentationModel;

import application.viewModel.CustomerComboBoxModel;

public class CustomerPMod extends pModBase {

    private CustomerComboBoxModel customerComboBoxModel = new CustomerComboBoxModel();

    public CustomerComboBoxModel getCustomerComboBoxModel() {
        return customerComboBoxModel;
    }

    public void setCustomerComboBoxModel(CustomerComboBoxModel customerComboBoxModel) {
        this.customerComboBoxModel = customerComboBoxModel;
    }

}
