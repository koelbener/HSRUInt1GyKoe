package application.view;

import java.awt.Frame;
import java.awt.Point;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.controller.ControllerBase;
import application.core.Repository;

import com.google.common.io.Resources;

import domain.Library;

/**
 * A MainView is a view inheriting from JFrame. Every window of the UI is a MainViewBase descendant.
 * 
 */
public abstract class MainViewBase<R, T extends ControllerBase> extends JFrame {

    private static final Logger logger = LoggerFactory.getLogger(MainViewBase.class);

    private static final long serialVersionUID = 1L;
    protected T controller;
    protected Library library;
    private final R referenceObject;

    public MainViewBase(R referenceObject, String windowName) {
        setName(windowName);
        this.referenceObject = referenceObject;
        initModel();
        initUIElements();
        initListeners();
        controller = initController();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        checkPositionAgainstActiveFrames();
        setVisible(true);
    }

    /**
     * checks if the JFrame is located on the same location as another frame. The method moves the frame until it finds a free location.
     */
    private void checkPositionAgainstActiveFrames() {
        int gap = 20;
        boolean needToCheck = true;
        List<Frame> frames = Arrays.asList(Frame.getFrames());
        while (needToCheck) {
            needToCheck = false;
            for (Frame frame : frames) {
                // change location of this frame and start checking all frames
                // again
                Point framePosition = frame.getLocation();
                Point myPosition = this.getLocation();
                logger.trace("check frame position {} against {}", myPosition, framePosition);
                if (!frame.equals(this) && frame.getLocation().equals(this.getLocation())) {
                    setLocation(getLocation().x + gap, getLocation().y + gap);
                    logger.debug("change location of frame {} from {} to {}", new Object[] { getName(), myPosition, this.getLocation() });
                    needToCheck = true;
                    break;
                }
            }
        }

    }

    protected void initModel() {
        library = Repository.getInstance().getLibrary();
    }

    protected void initUIElements() {
        getContentPane().removeAll();
    }

    protected abstract void initListeners();

    protected abstract T initController();

    protected T getController() {
        return controller;
    }

    protected R getReferenceObject() {
        return referenceObject;
    }

    protected boolean isNewEntity() {
        return referenceObject == null;
    }

    protected void setIcon(String file) {
        ImageIcon myAppImage = loadIcon("icons/" + file);
        if (myAppImage != null)
            setIconImage(myAppImage.getImage());
    }

    protected ImageIcon loadIcon(String strPath) {
        URL imgURL = Resources.getResource(strPath);
        if (imgURL != null)
            return new ImageIcon(imgURL);
        else
            return null;
    }
}
