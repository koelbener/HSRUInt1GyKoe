package application.view.mainView.dialogView;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import application.controller.ControllerBase;
import application.view.mainView.MainViewBase;

public abstract class DialogViewBase<R, T extends ControllerBase> extends MainViewBase<R, T, JFrame> {

    public DialogViewBase(R referenceObject, String icon) {
        super(referenceObject);
        setIcon(icon);
        getContainer().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        installEscapeCloseOperation();
    }

    @Override
    protected JFrame initContainer() {
        return new JFrame();
    }

    @Override
    protected void initUIElements() {
        container.getContentPane().removeAll();
    }

    // taken from http://www.jroller.com/tackline/entry/closing_dialogs_on_escape
    private void installEscapeCloseOperation() {
        final KeyStroke escapeStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        final String dispatchWindowClosingActionMapKey = "com.spodding.tackline.dispatch:WINDOW_CLOSING";

        Action dispatchClosing = new AbstractAction() {
            private static final long serialVersionUID = 1188563163606900665L;

            @Override
            public void actionPerformed(ActionEvent event) {
                container.dispatchEvent(new WindowEvent(container, WindowEvent.WINDOW_CLOSING));
            }
        };
        JRootPane root = container.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, dispatchWindowClosingActionMapKey);
        root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);
    }
}
