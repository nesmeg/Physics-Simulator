package simulator.view;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class ColorTheme extends JDialog {

    private int _theme;

    public ColorTheme() {

        String[] options = {"Light", "Dark"};

        int x = JOptionPane.showOptionDialog(null, "Chose the theme you want to use", "Theme chooser", 
                                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        _theme = x;
    }

    public int getTheme() {
        return _theme;
    }
}
