package application;

import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.core.Repository;
import application.data.DataLoder;
import application.data.XmlDataLoader;
import application.presentationModel.BooksPMod;
import application.presentationModel.LoansPMod;
import application.presentationModel.ShelfPMod;
import application.view.mainView.MasterMainView;
import domain.Library;

public class LibraryApp {
    private static final Logger logger = LoggerFactory.getLogger(LibraryApp.class);

    /**
     * Launch the application.
     */
    public static void main(String[] args) throws Exception {

        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                logger.error("Uncaught exception", e);
            }
        });

        DataLoder dataLoader = new XmlDataLoader();
        final Library library = dataLoader.loadLibrary();

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                createMainWindow(library);
            }
        });

    }

    /**
     * Must run in the <strong>AWT event dispatching thread</strong>!
     */
    public static MasterMainView createMainWindow(Library library) {
        Repository.getInstance().setLibrary(library);

        initPMods();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.error("Unable to set a native look and feel.", e);
        }

        MasterMainView bookMasterView = new MasterMainView();

        return bookMasterView;
    }

    private static void initPMods() {
        Repository.getInstance().setBooksPMod(new BooksPMod());
        Repository.getInstance().setShelfPMod(new ShelfPMod());
        Repository.getInstance().setLoansPMod(new LoansPMod());
    }

}
