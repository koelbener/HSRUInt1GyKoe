package application.presentationModel;

import java.util.List;

import application.presentationModel.componentModel.ConditionComboBoxModel;
import domain.Condition;
import domain.Copy;
import domain.Customer;
import domain.Loan;

public class CopyPMod extends pModBase {

    private final ConditionComboBoxModel copyStatusModel;

    public CopyPMod() {
        super();
        copyStatusModel = new ConditionComboBoxModel();
    }

    public ConditionComboBoxModel getCopyStatusModel() {
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

    public void changeCondition(Copy copy, Condition condition) {
        if (copy != null && condition != null) {
            copy.setCondition(condition);
        }
        setChanged();
        notifyObservers(copy.getTitle());
    }

    public boolean areCopiesDeletable(List<Copy> copies) {
        boolean result = true;
        for (Copy copy : copies) {
            if (library.isOrWasCopyLent(copy)) {
                result = false;
                break;
            }
        }
        return result;
    }

    public void addCopy(Copy copy) {
        library.addCopy(copy);
    }

    public Copy getCopyByInventoryId(long inventoryNumber) {
        return library.getCopyByInventoryNr(inventoryNumber);
    }

    public void removeCopy(Copy copy) {
        library.removeCopy(copy);
        setChanged();
        notifyObservers(copy.getTitle());
    }

    public void update(Copy copy) {
        setChanged();
        notifyObservers(copy.getTitle());
    }
}
