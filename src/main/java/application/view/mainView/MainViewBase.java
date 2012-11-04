package application.view.mainView;

import java.awt.Frame;
import java.awt.Point;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.controller.ControllerBase;
import application.view.ViewBase;

import com.google.common.io.Resources;

/**
 * A MainView is a view inheriting from JFrame. Every window of the UI is a MainViewBase descendant.
 * 
 */
public abstract class MainViewBase<R, T extends ControllerBase> extends ViewBase<R, T, JFrame> implements Observer {

    private static final Logger logger = LoggerFactory.getLogger(MainViewBase.class);

    public MainViewBase(R referenceObject) {
        super(referenceObject);
        container.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        checkPositionAgainstActiveFrames();
        container.setVisible(true);
    }

    @Override
    protected JFrame initContainer() {
        return new JFrame();
    }

    @Override
    protected void initUIElements() {
        container.getContentPane().removeAll();
    }

    protected void setIcon(String file) {
        ImageIcon myAppImage = loadIcon(file);
        if (myAppImage != null)
            container.setIconImage(myAppImage.getImage());
    }

    protected ImageIcon loadIcon(String strPath) {
        URL imgURL = Resources.getResource("icons/" + strPath);
        if (imgURL != null)
            return new ImageIcon(imgURL);
        else
            return null;
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
