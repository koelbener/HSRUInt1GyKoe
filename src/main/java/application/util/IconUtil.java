package application.util;

import java.net.URL;

import javax.swing.ImageIcon;

import com.google.common.io.Resources;

public class IconUtil {

    public static ImageIcon loadIcon(String iconName) {
        URL imgURL = Resources.getResource("icons/" + iconName);
        if (imgURL != null)
            return new ImageIcon(imgURL);
        else
            return null;
    }
}
