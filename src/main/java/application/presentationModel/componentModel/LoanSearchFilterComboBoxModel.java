package application.presentationModel.componentModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import application.core.Texts;
import application.presentationModel.componentModel.LoanSearchFilterComboBoxModel.FilterOption;

public class LoanSearchFilterComboBoxModel extends AbstractListModel<FilterOption> implements ComboBoxModel<FilterOption>, Observer {

    private static final long serialVersionUID = 592368359539347695L;
    public static final int INDEX_ALL_LOANS = 0;
    public static final int INDEX_OPEN_LOANS = 1;
    public static final int INDEX_DUE_LOANS = 2;

    public enum FilterOption {
        ALL_LOANS("LendingMasterMainView.filterComboBox.all"), //
        OPEN_LOANS("LendingMasterMainView.filterComboBox.onlyOpen"), //
        DUE_LOANS("LendingMasterMainView.filterComboBox.onlyDue");

        private final String text;
        private String textLocalised;

        private FilterOption(String text) {
            this.text = text;
            updateText();
        }

        public void updateText() {
            this.textLocalised = Texts.get(text);
        }

        @Override
        public String toString() {
            return textLocalised;
        }
    }

    List<FilterOption> elements;
    int selectedElement;

    public LoanSearchFilterComboBoxModel(FilterOption defaultValue) {
        initContents();
        setSelectedItem(defaultValue);
        Texts.getInstance().addObserver(this);
    }

    private void initContents() {
        elements = new ArrayList<FilterOption>();
        elements.add(INDEX_ALL_LOANS, FilterOption.ALL_LOANS);
        elements.add(INDEX_OPEN_LOANS, FilterOption.OPEN_LOANS);
        elements.add(INDEX_DUE_LOANS, FilterOption.DUE_LOANS);
    }

    @Override
    public FilterOption getElementAt(int arg0) {
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

    @Override
    public void update(Observable o, Object arg) {
        for (FilterOption e : FilterOption.values()) {
            e.updateText();
        }
        fireContentsChanged(this, 0, 3);
    }

}
