package application.presentationModel.componentModel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import application.core.Texts;

public class BookSearchFilterComboBoxModel extends AbstractListModel<SearchFilterElement> implements ComboBoxModel<SearchFilterElement> {

    private static final long serialVersionUID = 592368359539347695L;
    List<SearchFilterElement> elements;
    int selectedElement;

    public BookSearchFilterComboBoxModel() {
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
