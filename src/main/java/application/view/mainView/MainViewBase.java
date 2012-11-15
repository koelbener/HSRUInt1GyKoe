package application.view.mainView;

import java.awt.Frame;
import java.awt.Point;
import java.awt.Window;
import java.util.Arrays;
import java.util.List;
import java.util.Observer;

import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.controller.ControllerBase;
import application.util.IconUtil;
import application.view.ViewBase;

/**
 * A MainView is a view inheriting from JFrame. Every window of the UI is a MainViewBase descendant.
 * 
 */
public abstract class MainViewBase<R, T extends ControllerBase, S extends Window> extends ViewBase<R, T, S> implements Observer {

    private static final Logger logger = LoggerFactory.getLogger(MainViewBase.class);

    public MainViewBase(R referenceObject) {
        super(referenceObject);
        checkPositionAgainstActiveFrames();
        container.setVisible(true);
    }

    protected void setIcon(String file) {
        ImageIcon myAppImage = IconUtil.loadIcon(file);
        if (myAppImage != null)
            container.setIconImage(myAppImage.getImage());
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
                Point myPosition = container.getLocation();
                logger.trace("check frame position {} against {}", myPosition, framePosition);
                if (!frame.equals(getContainer()) && frame.getLocation().equals(container.getLocation())) {
                    container.setLocation(container.getLocation().x + gap, container.getLocation().y + gap);
                    logger.debug("change location of frame {} from {} to {}", new Object[] { container.getName(), myPosition, container.getLocation() });
                    needToCheck = true;
                    break;
                }
            }
        }

    }

}
