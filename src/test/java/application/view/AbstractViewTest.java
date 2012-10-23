package application.view;

import org.fest.swing.core.Robot;
import org.fest.swing.finder.WindowFinder;
import org.fest.swing.fixture.FrameFixture;

public class AbstractViewTest {

    protected FrameFixture findFrame(FrameFixture parent, String frameName) {
        Robot robot = parent.robot;
        return WindowFinder.findFrame(frameName).using(robot);
    }

}
