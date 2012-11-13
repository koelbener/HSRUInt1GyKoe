package application.view.subView;

import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.DialogFixture;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import application.LibraryApp;
import application.core.Texts;
import application.data.DataLoder;
import application.data.XmlDataLoader;
import application.view.AbstractFestTest;
import application.view.mainView.MasterMainView;
import domain.Library;

public class LendingMasterSubViewTest extends AbstractFestTest {
    DialogFixture window;
    private static Library library;

    @BeforeClass
    public static void setUpOnce() throws Exception {
        FailOnThreadViolationRepaintManager.install();
        DataLoder dataLoader = new XmlDataLoader();
        library = dataLoader.loadLibrary();
    }

    @Before
    public void setUp() throws Exception {
        MasterMainView mainWindow = GuiActionRunner.execute(new GuiQuery<MasterMainView>() {
            @Override
            protected MasterMainView executeInEDT() {
                return LibraryApp.createMainWindow(library);
            }
        });
        window = new DialogFixture(mainWindow.getContainer());
        window.show(); // shows the frame to test
        window.tabbedPane().selectTab(Texts.get("BookMasterMainView.tab.lending"));

    }

    @Test
    public void searchBoxHidesDefaultTestOnFocus() {
        window.textBox().requireText(Texts.get("LendingMasterMainView.searchDefault"));
        window.textBox().focus();
        window.textBox().requireText("");
        window.comboBox().focus();
        window.textBox().requireText(Texts.get("LendingMasterMainView.searchDefault"));
    }

}
