package application.view.helper;

import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import application.core.Repository;
import application.core.Texts;
import application.util.IconUtil;
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
        sb.append(Texts.get(copy.getCondition().getKey())).append(" - ");

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
            setBackground(Color.LIGHT_GRAY);
        } else {
            setBackground(Color.WHITE);
        }

        setIcon(IconUtil.loadIcon(copy.getCondition().getIcon()));

        return this;
    }
}
