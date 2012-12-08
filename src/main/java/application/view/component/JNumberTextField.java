package application.view.component;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

/**
 * A {@link JTextField} that skips all non-digit keys. The user is only able to enter numbers.
 * 
 * @author Michi Gysel <michi@scythe.ch>
 * 
 */
public class JNumberTextField extends JTextField {
    private static final long serialVersionUID = 1L;
    private String defaultText = "";

    public JNumberTextField(String defaultText) {
        this();
        this.defaultText = defaultText;
        setText(defaultText);
    }

    public JNumberTextField() {
        super();
        addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent arg0) {
                if (getNumber() == null) {
                    setText(defaultText);
                }
            }

            @Override
            public void focusGained(FocusEvent arg0) {
                if (getText().equals(defaultText)) {
                    selectAll();
                }
            }
        });
    }

    @Override
    public void processKeyEvent(KeyEvent ev) {
        if (getText().equals(defaultText)) {
            setText("");
        }

        String oldText = getText();

        try {
            super.processKeyEvent(ev);
            if (!getText().equals("")) {
                Integer.parseInt(getText());
            }
        } catch (NumberFormatException nfe) {
            setText(oldText);
        }
    }

    /**
     * As the user is not even able to enter a dot ("."), only integers (whole numbers) may be entered.
     */
    public Long getNumber() {
        Long result = null;
        String text = getText();
        if (text != null && !"".equals(text) && !text.equals(defaultText)) {
            result = Long.valueOf(text);
        }
        return result;
    }

    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        String oldDefaultText = this.defaultText;
        this.defaultText = defaultText;
        if (getText().equals(oldDefaultText)) {
            setText(defaultText);
        }
    }
}
