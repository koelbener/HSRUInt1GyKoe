package application.core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class LibraryActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent arg0) {
        beforeExecute();
        execute();
        afterExecute();
    }

    protected void afterExecute() {
        // TODO Auto-generated method stub

    }

    protected abstract void execute();

    protected void beforeExecute() {
        // TODO Auto-generated method stub

    }

}
