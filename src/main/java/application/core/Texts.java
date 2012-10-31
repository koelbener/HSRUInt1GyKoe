package application.core;

import java.util.Locale;
import java.util.Observable;
import java.util.ResourceBundle;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Texts extends Observable {
    private Locale currentLocale = Language.DEUTSCH.getLocale();
    private ResourceBundle bundle = ResourceBundle.getBundle("messages", currentLocale);
    private static final Logger logger = LoggerFactory.getLogger(Texts.class);

    private static Texts instance;

    private Texts() {
    };

    public static Texts getInstance() {
        if (instance == null) {
            instance = new Texts();
        }
        return instance;
    }

    public static String get(String key) {
        try {
            return getInstance().bundle.getString(key);
        } catch (java.util.MissingResourceException e) {
            logger.error("Text with key " + key + " not available!");
            return "!" + key + "!";
        }
    }

    private void loadBundle() {
        ResourceBundle.clearCache();
        logger.info("load new texts with locale " + currentLocale);
        bundle = ResourceBundle.getBundle("messages", currentLocale);
        if (!bundle.getLocale().equals(currentLocale)) {
            logger.warn("did not load locale " + currentLocale + " correctly!");
        }
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                getInstance().setChanged();
                getInstance().notifyObservers();
            }
        });
    }

    public void switchTo(Locale locale) {
        currentLocale = locale;
        loadBundle();
    }

}
