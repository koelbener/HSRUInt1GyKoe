package application.view.mainView;

import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.controller.ControllerBase;
import application.util.IconUtil;
import application.view.ViewBase;

/**
 * A MainView is a view inheriting from JFrame. Every window of the UI is a MainViewBase descendant.
 * 
 */
public abstract class MainViewBase<R, T extends ControllerBase> extends ViewBase<R, T, JDialog> implements Observer {

    private static final Logger logger = LoggerFactory.getLogger(MainViewBase.class);

    public MainViewBase(R referenceObject) {
        super(referenceObject);
        container.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        checkPositionAgainstActiveFrames();
        container.setVisible(true);
        installEscapeCloseOperation();
    }

    @Override
    protected JDialog initContainer() {
        return new JDialog();
    }

    @Override
    protected void initUIElements() {
        container.getContentPane().removeAll();
    }

    protected void setIcon(String file) {
        ImageIcon myAppImage = IconUtil.loadIcon(file);
        if (myAppImage != null)
            container.setIconImage(myAppImage.getImage());
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
