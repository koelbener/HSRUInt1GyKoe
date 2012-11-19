package application.view.helper;

import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import application.core.Repository;
import application.core.Texts;
import domain.Copy;
import domain.Library;
import domain.Loan;

public class CopiesListCellRenderer extends JLabel implements ListCellRenderer<Copy> {

    private static final long serialVersionUID = 6995598407842395910L;

    public CopiesListCellRenderer() {
        setOpaque(true);
        setHorizontalAlignment(LEFT);
        setVerticalAlignment(CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Copy> list, Copy copy, int index, boolean isSelected, boolean cellHasFocus) {
        Library library = Repository.getInstance().getLibrary();

        StringBuilder sb = new StringBuilder();
        sb.append(copy.getInventoryNumber()).append(" - ");
        sb.append(copy.getCondition()).append(" - ");

        if (library.isCopyLent(copy)) {
            sb.append(Texts.get("BookDetailMainView.copyList.unavailable")).append(" ");
            Loan loan = library.getCurrentLoan(copy);
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            sb.append(format.format(loan.getDueDate().getTime()));
        } else {
            sb.append(Texts.get("BookDetailMainView.copyList.available"));
        }
        setText(sb.toString());
        if (isSelected) {
            setBackground(Color.GRAY);
        } else {
            setBackground(Color.WHITE);
        }
        return this;
    }
}
