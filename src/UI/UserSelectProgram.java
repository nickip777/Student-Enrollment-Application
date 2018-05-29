package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * panel for program selection
 */
public class UserSelectProgram extends UIBox {
    private static JComboBox<String> dropDown;
    private final String title = "Select a program: ";
    public UserSelectProgram(){
        super();
        add(makeTitle(), BorderLayout.NORTH);
        add(makeDropdown(), BorderLayout.CENTER);
    }

    //make title
    private Component makeTitle() {
        JLabel label = new JLabel(title);
        label.setBorder(new EmptyBorder(5,5,0,0));

        return label;
    }
    //make dropdown
    private Component makeDropdown(){
        String[] majors = {"CS Major", "CS SoSy", "CS Minor", "CS Joint Major", "Unknown"};
        dropDown = new JComboBox<String>(majors);
        dropDown.setBorder(new EmptyBorder(5,5,30,5));
        dropDown.setMaximumSize(new Dimension(100, 50));
        dropDown.setVisible(true);
        return dropDown;
    }

    //return selected string
    public static String getDropdownString() {
        //String varName = (String) dropDown.getSelectedItem();
        String value = dropDown.getSelectedItem().toString();
        return value;
    }

}
