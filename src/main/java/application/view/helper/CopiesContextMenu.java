package application.view.helper;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import application.core.Texts;
import application.util.IconUtil;
import domain.Condition;

public class CopiesContextMenu extends JPopupMenu {
    private static final long serialVersionUID = 1L;
    private final List<JMenuItem> copyAwareMenuItems = new ArrayList<JMenuItem>();
    private final JMenuItem deleteMenuItem;

    public CopiesContextMenu(ActionListener listener) {
        createMenuItem(null, "BookDetailMainView.copyList.context.add", listener, "add.gif");
        deleteMenuItem = createMenuItem(null, "BookDetailMainView.copyList.context.remove", listener, "delete.gif");
        copyAwareMenuItems.add(deleteMenuItem);
        JMenu submenu = new JMenu("Set condition");
        add(submenu);
        for (Condition c : Condition.values()) {
            copyAwareMenuItems.add(createMenuItem(submenu, c.getKey(), listener, c.getIcon()));
        }
    }

    private JMenuItem createMenuItem(JMenu menu, String text, ActionListener listener, String iconName) {
        JMenuItem result = new JMenuItem(Texts.get(text));
        result.setActionCommand(text);
        if (menu != null) {
            menu.add(result);
        } else {
            add(result);
        }
        result.addActionListener(listener);
        if (iconName != null)
            result.setIcon(IconUtil.loadIcon(iconName));
        return result;
    }

    public void enableCopyRelatedOptions(boolean enable) {
        for (JMenuItem item : copyAwareMenuItems) {
            item.setEnabled(enable);
        }
    }

    public void enableDelete(boolean enable) {
        deleteMenuItem.setEnabled(enable);
    }
}
