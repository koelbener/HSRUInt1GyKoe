package application.presentationModel.componentModel;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import application.util.IconUtil;
import domain.Condition;

public class ConditionListCellRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = 1L;

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel result = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Condition) {
            Condition condition = (Condition) value;
            result.setIcon(IconUtil.loadIcon(condition.getIcon()));
        }
        return result;
    }

}
