package application.presentationModel;

import application.core.Repository;
import application.presentationModel.componentModel.CopyStatusComboBoxModel;
import domain.Copy;
import domain.Customer;
import domain.Library;
import domain.Loan;

public class CopyPMod extends pModBase {

    private final Library library;
    private final CopyStatusComboBoxModel copyStatusModel;

    public CopyPMod() {
        library = Repository.getInstance().getLibrary();
        copyStatusModel = new CopyStatusComboBoxModel();
    }

    public CopyStatusComboBoxModel getCopyStatusModel() {
        return copyStatusModel;
    }

    public Copy searchCopy(Long copyId) {
        return library.getCopyByInventoryNr(copyId);
    }

    public boolean isCopyLent(Copy copy) {
        return library.isCopyLent(copy);
    }

    public Customer getLender(Copy copy) {
        return library.getLender(copy);
    }

    public Loan getCurrentLoan(Copy copy) {
        return library.getCurrentLoan(copy);
    }
}
