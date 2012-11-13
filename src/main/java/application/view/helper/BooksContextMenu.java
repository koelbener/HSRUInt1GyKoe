package application.view.helper;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class BooksContextMenu extends JPopupMenu {
    private static final long serialVersionUID = 1L;
    private final JMenuItem newBook;
    private final JMenuItem openBook;

    public BooksContextMenu(ActionListener newBookActionListener, ActionListener openBookActionListener) {
        newBook = new JMenuItem("New");
        openBook = new JMenuItem("Open");
        add(newBook);
        add(openBook);
        newBook.addActionListener(newBookActionListener);
        openBook.addActionListener(openBookActionListener);
    }

}
