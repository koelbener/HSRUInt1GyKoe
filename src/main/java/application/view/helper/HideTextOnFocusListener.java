package application.view.helper;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextField;

public class HideTextOnFocusListener {

    private String text;
    private final JTextField textField;

    public HideTextOnFocusListener(JTextField textFieldP, String textP) {

        // TODO: P??
        this.textField = textFieldP;
        this.text = textP;

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent arg0) {
                if (textField.getText().equals(text)) {
                    textField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent arg0) {
                if (textField.getText().equals("")) {
                    textField.setText(text);
                }
            }
        });

    }

    public void updateText(String text) {
        this.text = text;
    }

}
