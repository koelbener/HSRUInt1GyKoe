package application.viewModel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import application.core.Repository;
import application.core.Texts;
import domain.Loan;

public class LoanSearchFilterComboBoxModel extends AbstractListModel<Runnable> implements ComboBoxModel<Runnable> {

    private static final long serialVersionUID = 592368359539347695L;
    List<Runnable> elements;
    int selectedElement;
    private Runnable filterOpenLoansJob;
    private Runnable filterOverdueLoansJob;
    private Runnable filterAllLoansJob;

    public LoanSearchFilterComboBoxModel() {
        initJobs();
        initContents();

        selectedElement = 0;
    }

    private void initContents() {
        elements = new ArrayList<Runnable>();
        elements.add(filterAllLoansJob);
        elements.add(filterOpenLoansJob);
        elements.add(filterOverdueLoansJob);

    }

    abstract class FiltredData<T> {
        public abstract List<T> getData();

    }

    public void updateTexts() {
        initContents();
        fireContentsChanged(this, 0, 3);
    }

    @Override
    public Runnable getElementAt(int arg0) {
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

    private void initJobs() {
        filterOpenLoansJob = new Runnable() {

            @Override
            public void run() {
                List<Loan> data = Repository.getInstance().getLibrary().getOpenLoans();
                Repository.getInstance().getLoansPMod().getLoanTableModel().setData(data);
            }

            @Override
            public String toString() {
                return Texts.get("LendingMasterMainView.filterComboBox.onlyOpen");
            }

        };

        filterOverdueLoansJob = new Runnable() {

            @Override
            public void run() {
                List<Loan> data = Repository.getInstance().getLibrary().getOverdueLoans();
                Repository.getInstance().getLoansPMod().getLoanTableModel().setData(data);
            }

            @Override
            public String toString() {
                return Texts.get("LendingMasterMainView.filterComboBox.onlyDue");
            }
        };

        filterAllLoansJob = new Runnable() {

            @Override
            public void run() {
                List<Loan> data = Repository.getInstance().getLibrary().getLoans();
                Repository.getInstance().getLoansPMod().getLoanTableModel().setData(data);
            }

            @Override
            public String toString() {
                return Texts.get("LendingMasterMainView.filterComboBox.all");
            }
        };
    }

}
