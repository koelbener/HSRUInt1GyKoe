package application;

import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.core.Repository;
import application.data.DataLoder;
import application.data.XmlDataLoader;
import application.presentationModel.BooksPMod;
import application.presentationModel.CopyPMod;
import application.presentationModel.CustomerPMod;
import application.presentationModel.LoansPMod;
import application.presentationModel.ShelfPMod;
import application.view.mainView.MainViewFactory;
import application.view.mainView.MasterMainView;

import com.google.common.base.Preconditions;

import domain.Library;

public class LibraryApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryApp.class);

    /**
     * Launch the application.
     */
    public static void main(String[] args) throws Exception {

        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                LOGGER.error("Uncaught exception", e);
            }
        });

        DataLoder dataLoader = new XmlDataLoader();
        final Library library = dataLoader.loadLibrary();

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                ToolTipManager.sharedInstance().setInitialDelay(0);
                ToolTipManager.sharedInstance().setDismissDelay(5000);
                createMainWindow(library);
            }
        });

    }

    /**
     * Must run in the <strong>AWT event dispatching thread</strong>!
     */
    public static MasterMainView createMainWindow(Library library) {
        Preconditions.checkState(SwingUtilities.isEventDispatchThread());

        Repository.getInstance().setLibrary(library);

        initPMods();
        Repository.getInstance().setMainViewFactory(new MainViewFactory());

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOGGER.error("Unable to set a native look and feel.", e);
        }

        MasterMainView masterView = Repository.getInstance().getMainViewFactory().getMasterMainView();

        return masterView;
    }

    private static void initPMods() {
        Repository.getInstance().setBooksPMod(new BooksPMod());
        Repository.getInstance().setShelfPMod(new ShelfPMod());
        Repository.getInstance().setLoansPMod(new LoansPMod());
        Repository.getInstance().setCustomerPMod(new CustomerPMod());
        Repository.getInstance().setCopyPMod(new CopyPMod());
    }

}
