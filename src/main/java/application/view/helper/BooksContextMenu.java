package application.view.helper;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import application.util.IconUtil;

public class BooksContextMenu extends JPopupMenu {
    private static final long serialVersionUID = 1L;
    private final JMenuItem newBook;
    private final JMenuItem openBook;

    public BooksContextMenu(ActionListener newBookActionListener, ActionListener openBookActionListener) {
        newBook = new JMenuItem("New", IconUtil.loadIcon("add.gif"));
        openBook = new JMenuItem("Open", IconUtil.loadIcon("edit.gif"));
        add(newBook);
        add(openBook);
        newBook.addActionListener(newBookActionListener);
        openBook.addActionListener(openBookActionListener);
    }

}
