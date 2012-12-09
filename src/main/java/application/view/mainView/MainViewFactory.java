package application.view.mainView;

import java.util.ArrayList;
import java.util.List;

import application.core.Repository;
import application.view.mainView.dialogView.BookDetailMainViewBase;
import application.view.mainView.dialogView.EditBookDetailMainView;
import application.view.mainView.dialogView.LoanDetailMainView;
import application.view.mainView.dialogView.NewBookDetailMainView;
import application.view.mainView.dialogView.ReturnLoansMainView;

import com.google.common.base.Preconditions;

import domain.Book;
import domain.Loan;

public class MainViewFactory {

    private MasterMainView masterMainView;
    private LoanDetailMainView loanDetailMainView;
    private ReturnLoansMainView returnLoansMainView;
    private final List<EditBookDetailMainView> bookDetailMainViews = new ArrayList<EditBookDetailMainView>();

    public MasterMainView getMasterMainView() {
        if (masterMainView == null) {
            masterMainView = new MasterMainView();
        }
        return masterMainView;
    }

    public LoanDetailMainView getLoanDetailMainView(Loan loan) {
        if (loanDetailMainView == null) {
            loanDetailMainView = new LoanDetailMainView(loan);
        } else {
            loanDetailMainView.switchToLoan(loan, true, loan == null);
            loanDetailMainView.getContainer().setVisible(true);
        }
        return loanDetailMainView;
    }

    public NewBookDetailMainView getNewBookDetailMainView() {
        return new NewBookDetailMainView();
    }

    public BookDetailMainViewBase getBookDetailMainView(Book book) {
        Preconditions.checkNotNull(book);
        for (BookDetailMainViewBase view : bookDetailMainViews) {
            if (view.getReferenceObject().equals(book)) {
                view.getContainer().setVisible(true);
                return view;
            }
        }
        EditBookDetailMainView newBookDetailView = new EditBookDetailMainView(book);
        bookDetailMainViews.add(newBookDetailView);
        return newBookDetailView;
    }

    public void closeMainView(EditBookDetailMainView view) {
        if (view instanceof EditBookDetailMainView) {
            bookDetailMainViews.remove(view);
        }
    }

    public ReturnLoansMainView getReturnLoansView() {
        if (returnLoansMainView == null) {
            returnLoansMainView = new ReturnLoansMainView(null, "batchReturn.gif");
        } else {
            returnLoansMainView.getContainer().setVisible(true);
        }
        return returnLoansMainView;
    }

    public void closeReturnLoansView() {
        if (returnLoansMainView != null) {
            Repository.getInstance().getLoansPMod().renewBatchReturnLoansModel();
            returnLoansMainView.getContainer().dispose();
            returnLoansMainView = null;
        }
    }
}
