package application.view.helper;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import application.core.Texts;
import application.util.IconUtil;

public class BooksContextMenu extends JPopupMenu {
    private static final long serialVersionUID = 1L;
    private final JMenuItem newBook;
    private final JMenuItem openBook;

    public BooksContextMenu(ActionListener newBookActionListener, ActionListener openBookActionListener) {
        newBook = new JMenuItem("", IconUtil.loadIcon("add.gif"));
        openBook = new JMenuItem("", IconUtil.loadIcon("edit.gif"));
        updateTexts();
        add(newBook);
        add(openBook);
        newBook.addActionListener(newBookActionListener);
        openBook.addActionListener(openBookActionListener);
    }

    public void updateTexts() {
        newBook.setText(Texts.get("BookMasterMainView.btnNewBook.text"));
        openBook.setText(Texts.get("BookMasterMainView.btnOpenBook.text"));
    }

}
