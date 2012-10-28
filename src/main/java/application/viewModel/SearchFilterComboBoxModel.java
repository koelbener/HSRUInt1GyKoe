package application.viewModel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

public class SearchFilterComboBoxModel extends AbstractListModel<SearchFilterElement> implements ComboBoxModel<SearchFilterElement> {

    private static final long serialVersionUID = 592368359539347695L;
    List<SearchFilterElement> elements;
    SearchFilterElement selectedElement;

    public SearchFilterComboBoxModel() {
        elements = new ArrayList<SearchFilterElement>();
        elements.add(new SearchFilterElement("Suche in allen Feldern", -1));
        elements.add(new SearchFilterElement("Suche in Titel", BookTableModel.COLUMN_TITLE));
        elements.add(new SearchFilterElement("Suche in Author", BookTableModel.COLUMN_AUTHOR));
        elements.add(new SearchFilterElement("Suche in Verlag", BookTableModel.COLUMN_PUBLISHER));

        selectedElement = elements.get(0);
    }

    @Override
    public SearchFilterElement getElementAt(int arg0) {
        return elements.get(arg0);
    }

    @Override
    public int getSize() {
        return elements.size();
    }

    @Override
    public Object getSelectedItem() {
        return selectedElement;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedElement = (SearchFilterElement) anItem;

    }

}
