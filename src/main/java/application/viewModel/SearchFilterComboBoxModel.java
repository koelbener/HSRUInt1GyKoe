package application.viewModel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import application.core.Texts;

@SuppressWarnings("rawtypes")
// is not supported by Java6
public class SearchFilterComboBoxModel extends AbstractListModel implements ComboBoxModel {

    private static final long serialVersionUID = 592368359539347695L;
    List<SearchFilterElement> elements;
    int selectedElement;

    public SearchFilterComboBoxModel() {
        initContents();

        selectedElement = 0;
    }

    private void initContents() {
        elements = new ArrayList<SearchFilterElement>();
        elements.add(new SearchFilterElement(Texts.get("BookMasterMainView.filterComboBox.all"), -1));
        elements.add(new SearchFilterElement(Texts.get("BookMasterMainView.filterComboBox.title"), BookTableModel.COLUMN_TITLE));
        elements.add(new SearchFilterElement(Texts.get("BookMasterMainView.filterComboBox.author"), BookTableModel.COLUMN_AUTHOR));
        elements.add(new SearchFilterElement(Texts.get("BookMasterMainView.filterComboBox.publisher"), BookTableModel.COLUMN_PUBLISHER));
    }

    public void updateTexts() {
        initContents();
        fireContentsChanged(this, 0, 3);
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
        return elements.get(selectedElement);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedElement = elements.indexOf(anItem);

    }

}
