package application.view.subView;

import java.util.Observer;

import javax.swing.JPanel;

import application.controller.ControllerBase;
import application.view.ShortcutsManager;
import application.view.ViewBase;

public abstract class SubViewBase<R, T extends ControllerBase> extends ViewBase<R, T, JPanel> implements Observer {

    public SubViewBase(R referenceObject) {
        super(referenceObject);
        initShortcuts(ShortcutsManager.getInstance());
    }

    protected abstract void initShortcuts(ShortcutsManager shortcutsManager);

    @Override
    protected JPanel initContainer() {
        return new JPanel();
    }

    @Override
    protected void initUIElements() {
        container.removeAll();
    }

}
