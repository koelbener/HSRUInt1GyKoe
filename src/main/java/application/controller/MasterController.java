package application.controller;

import java.util.List;

import application.core.Language;
import application.core.Texts;
import domain.Loan;

public class MasterController extends ControllerBase {

    public void switchToLanguage(Language selectedLanguage) {
        Texts.getInstance().switchTo(selectedLanguage.getLocale());
    }

    public List<Loan> getOverdueLoans() {
        return getRepository().getLoansPMod().getOverdueLoans();
    }

}
