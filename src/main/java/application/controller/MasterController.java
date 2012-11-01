package application.controller;

import application.core.Language;
import application.core.Texts;

public class MasterController extends ControllerBase {

    public void switchToLanguage(Language selectedLanguage) {
        Texts.getInstance().switchTo(selectedLanguage.getLocale());
    }

}
