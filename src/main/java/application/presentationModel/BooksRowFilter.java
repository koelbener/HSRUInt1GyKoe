package application.presentationModel;

import javax.swing.RowFilter;

import application.viewModel.BookTableModel;

public class BooksRowFilter extends RowFilter<BookTableModel, Integer> {

    private boolean include = true;

    @Override
    public boolean include(RowFilter.Entry<? extends BookTableModel, ? extends Integer> entry) {
        return include;
    }

    public void setInclude(boolean include) {
        this.include = include;
    }

}
