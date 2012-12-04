package application.view.mainView.dialogView;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import application.core.Repository;

import com.jgoodies.validation.view.ValidationResultViewFactory;

import domain.Book;

public class EditBookDetailMainView extends BookDetailMainViewBase {

    public EditBookDetailMainView(Book book) {
        super(book);
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        txtFieldTitle.addFocusListener(new BookDetailValidateFocusListener());
        txtFieldAuthor.addFocusListener(new BookDetailValidateFocusListener());
        txtFieldPublisher.addFocusListener(new BookDetailValidateFocusListener());

        final EditBookDetailMainView thisObject = this;
        getContainer().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent arg0) {
                Repository.getInstance().getMainViewFactory().closeMainView(thisObject);
            }
        });

    }

    /**
     * Responsible for correct foucsHandling for validating components. On focusLost, it validates the current input. This Listener tries to always get the focus back to the
     * invalid component
     * 
     */
    private class BookDetailValidateFocusListener implements FocusListener {
        Component invalidComponent;

        @Override
        public void focusLost(FocusEvent e) {
            boolean hasErrorsBefore = validationModel.getResult().hasErrors();

            validateBook();
            /*
             * Only request focus if a new error occured or if the originComponent is the same as invalidComponent
             */
            boolean componentIsInvalidComponent = invalidComponent != null && invalidComponent.equals(invalidComponent);
            if (validationModel.getResult().hasErrors() && (!hasErrorsBefore || componentIsInvalidComponent)) {
                invalidComponent = e.getComponent();
                e.getComponent().requestFocus();
                e.getComponent().setBackground(Color.RED);
            } else {
                /*
                 * Otherwise just update the validationPanel
                 */
                pnValidation.removeAll();
                pnValidation.add(ValidationResultViewFactory.createReportList(validationModel), "cell 0 0,alignx right,growy");
                pnValidation.revalidate();

                e.getComponent().setBackground(Color.WHITE);
            }

        }

        @Override
        public void focusGained(FocusEvent arg0) {
        }

    }

}
