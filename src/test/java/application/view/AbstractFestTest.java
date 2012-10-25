package application.view;

import org.fest.swing.core.MouseClickInfo;
import org.fest.swing.core.Robot;
import org.fest.swing.data.TableCell;
import org.fest.swing.finder.WindowFinder;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JTableFixture;

public class AbstractFestTest {

    protected FrameFixture findFrame(FrameFixture parent, String frameName) {
        Robot robot = parent.robot;
        return WindowFinder.findFrame(frameName).using(robot);
    }

    protected void doubleClickOnCell(JTableFixture table, String cellValue) {
        TableCell anyCellInCopiesColumn = table.cell(cellValue);
        table.requireNotEditable(anyCellInCopiesColumn);
        table.click(anyCellInCopiesColumn, MouseClickInfo.leftButton().times(2));
    }

}
