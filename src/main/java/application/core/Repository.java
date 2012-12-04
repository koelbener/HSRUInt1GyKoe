package application.core;

import application.presentationModel.BooksPMod;
import application.presentationModel.CopyPMod;
import application.presentationModel.CustomerPMod;
import application.presentationModel.LoansPMod;
import application.presentationModel.ShelfPMod;
import application.view.mainView.MainViewFactory;
import domain.Library;

/**
 * Singleton to store core Objects
 */
public class Repository {
    private static Repository instance;

    private Library library;

    private BooksPMod booksPMod;
    private ShelfPMod shelfPMod;
    private LoansPMod loansPMod;
    private CopyPMod copyPMod;
    private MainViewFactory mainViewFactory;

    private CustomerPMod cutomerPMod;

    private Repository() {
    }

    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    public MainViewFactory getMainViewFactory() {
        return mainViewFactory;
    }

    public void setMainViewFactory(MainViewFactory mainViewFactory) {
        this.mainViewFactory = mainViewFactory;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public Library getLibrary() {
        return this.library;
    }

    public void setBooksPMod(BooksPMod booksPMod) {
        this.booksPMod = booksPMod;
    }

    public BooksPMod getBooksPMod() {
        return booksPMod;
    }

    public ShelfPMod getShelfPMod() {
        return shelfPMod;
    }

    public void setShelfPMod(ShelfPMod shelfPMod) {
        this.shelfPMod = shelfPMod;
    }

    public LoansPMod getLoansPMod() {
        return loansPMod;
    }

    public void setLoansPMod(LoansPMod loansPMod) {
        this.loansPMod = loansPMod;
    }

    public void setCustomerPMod(CustomerPMod customerPMod) {
        this.cutomerPMod = customerPMod;
    }

    public CustomerPMod getCustomerPMod() {
        return cutomerPMod;
    }

    public void setCopyPMod(CopyPMod copyPMod) {
        this.copyPMod = copyPMod;
    }

    public CopyPMod getCopyPMod() {
        return copyPMod;
    }

}
