package application.presentationModel;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

import domain.Shelf;

public class ShelfPMod extends pModBase {

    private final ComboBoxModel<Shelf> shelfComboBoxModel;

    public ShelfPMod() {
        super();
        shelfComboBoxModel = new DefaultComboBoxModel<Shelf>(Shelf.values());
    }

    public ComboBoxModel<Shelf> getShelfComboBoxModel() {
        return shelfComboBoxModel;
    }

}
