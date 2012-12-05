package application.presentationModel.componentModel;

import javax.swing.DefaultComboBoxModel;

import domain.Condition;

public class ConditionComboBoxModel extends DefaultComboBoxModel<Condition> {

    private static final long serialVersionUID = 1L;

    public ConditionComboBoxModel() {
        super(addEmptyItem(Condition.values()));
    }

    private static Condition[] addEmptyItem(Condition[] values) {
        Condition[] result = new Condition[values.length + 1];
        result[0] = null;
        for (int i = 0; i < values.length; i++) {
            result[i + 1] = values[i];
        }
        return result;
    }

}
