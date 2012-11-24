package application.view.helper;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextField;

public class HideTextOnFocusListener {

    private String text;
    private final JTextField textField;

    public HideTextOnFocusListener(JTextField textField, String text) {

        this.textField = textField;
        this.text = text;

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent arg0) {
                if (HideTextOnFocusListener.this.textField.getText().equals(HideTextOnFocusListener.this.text)) {
                    HideTextOnFocusListener.this.textField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent arg0) {
                if (HideTextOnFocusListener.this.textField.getText().equals("")) {
                    HideTextOnFocusListener.this.textField.setText(HideTextOnFocusListener.this.text);
                }
            }
        });

    }

    public void updateText(String text) {
        this.text = text;
    }

}
