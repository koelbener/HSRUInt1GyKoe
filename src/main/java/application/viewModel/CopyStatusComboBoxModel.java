package application.viewModel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import domain.Copy.Condition;

public class CopyStatusComboBoxModel extends AbstractListModel<Condition> implements ComboBoxModel<Condition> {

    private static final long serialVersionUID = 592368359539347695L;

    List<Condition> elements;
    int selectedElement;

    public CopyStatusComboBoxModel() {
        elements = new ArrayList<Condition>();
        initContents();
        setSelectedItem(getElementAt(0));
    }

    private void initContents() {
        for (Condition c : Condition.values()) {
            elements.add(c);
        }
    }

    @Override
    public Condition getElementAt(int arg0) {
        return elements.get(arg0);
    }

    @Override
    public int getSize() {
        return elements.size();
    }

    @Override
    public Object getSelectedItem() {
        return elements.get(selectedElement);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedElement = elements.indexOf(anItem);
    }
}