package application.view;

import java.awt.AWTKeyStroke;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShortcutsManager implements KeyEventDispatcher {

    private final Logger logger = LoggerFactory.getLogger(ShortcutsManager.class);
    private static ShortcutsManager instance = new ShortcutsManager();
    private final Map<KeyStroke, ShortcutAction> shortcuts;

    private ShortcutsManager() {
        shortcuts = new HashMap<KeyStroke, ShortcutAction>();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
    }

    public static ShortcutsManager getInstance() {
        return instance;
    }

    public void registerShortcut(int key, ShortcutAction action) {
        KeyStroke keyStroke = KeyStroke.getKeyStroke(key, KeyEvent.CTRL_DOWN_MASK);
        shortcuts.put(keyStroke, action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        AWTKeyStroke keyStroke = KeyStroke.getAWTKeyStrokeForEvent(e);
        final ShortcutAction shortcut = shortcuts.get(keyStroke);
        if (shortcut != null) {
            logger.debug("Shortcut \"{}\" detected.", keyStroke);
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    shortcut.run();
                }
            });
        }
        return false;
    }

    public interface ShortcutAction {
        public void run();
    }
}
