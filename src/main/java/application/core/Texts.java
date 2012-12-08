package application.core;

import java.util.Locale;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Texts extends Observable {
    private Locale currentLocale = Language.DEUTSCH.getLocale();
    private ResourceBundle bundle = ResourceBundle.getBundle("messages", currentLocale);
    private static final Logger LOGGER = LoggerFactory.getLogger(Texts.class);

    private static Texts instance;
    private static final String PARAMETER_PATTERN = Pattern.compile("\\{\\}").toString();

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
            LOGGER.error("Text with key " + key + " not available!");
            return "!" + key + "!";
        }
    }

    public static String get(String key, Object... params) {
        return format(get(key), params);
    }

    static String format(String result, Object... params) {
        for (Object param : params) {
            result = result.replaceFirst(PARAMETER_PATTERN, param.toString());
        }
        return result;
    }

    private void loadBundle() {
        ResourceBundle.clearCache();
        LOGGER.info("load new texts with locale " + currentLocale);
        bundle = ResourceBundle.getBundle("messages", currentLocale);
        if (!bundle.getLocale().equals(currentLocale)) {
            LOGGER.warn("did not load locale " + currentLocale + " correctly!");
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

    /**
     * only to be used for testing!
     */
    static void setInstance(Texts instance) {
        Texts.instance = instance;
    }

    public Locale getCurrentLocale() {
        return currentLocale;
    }

}
