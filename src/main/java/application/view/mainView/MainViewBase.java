package application.view.mainView;

import java.awt.Frame;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.controller.ControllerBase;
import application.core.Repository;
import application.util.IconUtil;
import application.view.ViewBase;

/**
 * A MainView is a view inheriting from JFrame. Every window of the UI is a MainViewBase descendant.
 * 
 */
public abstract class MainViewBase<R, T extends ControllerBase, S extends Window> extends ViewBase<R, T, S> implements Observer {

    private final Logger logger = LoggerFactory.getLogger(MainViewBase.class);
    protected final List<Observable> observables = new ArrayList<Observable>();

    public MainViewBase(R referenceObject) {
        super(referenceObject);
        checkPositionAgainstActiveFrames();
        Repository.getInstance().getBooksPMod().addObserver(this);
        addObservables();
        installObservers();
        installRemoveObservers();
        container.setVisible(true);
    }

    protected abstract void addObservables();

    protected void setIcon(String file) {
        ImageIcon myAppImage = IconUtil.loadIcon(file);
        if (myAppImage != null) {
            container.setIconImage(myAppImage.getImage());
        }
    }

    /**
     * checks if the JFrame is located on the same location as another frame. The method moves the frame until it finds a free location.
     */
    private void checkPositionAgainstActiveFrames() {
        int gap = 20;
        boolean needToCheck = true;
        List<Window> windows = Arrays.asList(Frame.getWindows());

        while (needToCheck) {
            needToCheck = false;
            for (Window window : windows) {
                if (window.isVisible()) {
                    // change location of this frame and start checking all frames
                    // again
                    Point framePosition = window.getLocation();
                    Point myPosition = container.getLocation();
                    logger.trace("check frame position {} against {}", myPosition, framePosition);
                    if (!window.equals(getContainer()) && window.getLocation().equals(container.getLocation())) {
                        container.setLocation(container.getLocation().x + gap, container.getLocation().y + gap);
                        logger.debug("change location of frame {} from {} to {}",
                                new Object[] { container.getName(), myPosition, container.getLocation() });
                        needToCheck = true;
                        break;
                    }
                }
            }
        }
    }

    private void installObservers() {
        for (Observable o : observables) {
            o.addObserver(this);
        }
    }

    private void installRemoveObservers() {
        final Observer thisObj = this;
        getContainer().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent arg0) {
                for (Observable o : observables) {
                    o.deleteObserver(thisObj);
                }
            }
        });
    }
}
