package application.view.helper;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import application.core.Repository;
import application.core.Texts;
import domain.Copy;

public class CopiesListCellRenderer extends JLabel implements ListCellRenderer<Copy> {

    private static final long serialVersionUID = 6995598407842395910L;

    public CopiesListCellRenderer() {
        setOpaque(true);
        setHorizontalAlignment(LEFT);
        setVerticalAlignment(CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Copy> list, Copy value, int index, boolean isSelected, boolean cellHasFocus) {
        String isAvailable = null;
        if (Repository.getInstance().getLibrary().isCopyLent(value)) {
            isAvailable = Texts.get("BookDetailMainView.copyList.unavailable");
        } else {
            isAvailable = Texts.get("BookDetailMainView.copyList.available");
        }
        setText("#" + value.getInventoryNumber() + " (" + value.getCondition() + "/" + isAvailable + ")");
        if (isSelected) {
            setBackground(Color.GRAY);
        } else {
            setBackground(Color.WHITE);
        }
        return this;
    }
}
