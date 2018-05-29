package UI;

import javax.swing.*;
import java.awt.*;

/**
 * Diagram panel which generates and paints ui
 */
public class Diagram extends JPanel {
    private static final int WIDTH = 1600;
    private static final int HEIGHT = 650;

    private final static int NUM_MILESTONES = 5;
    private final static int START_X = 50;
    private final static int START_Y = 220;
    private final static int SPACING = 300;
    private int[][][] results;
    private String selectedMajor;
    private class DiagramIcon implements Icon{
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g;
            //for each milestone
            String[] milestone_titles = {"Admitted", "Year1", "Year2", "Year3", "Year4"};
            for (int i = 0; i < NUM_MILESTONES; i++) {
                int currentX = START_X + SPACING * i;
                SemesterDraw.draw(g2d, currentX, START_Y, milestone_titles[i], results, i, selectedMajor);
            }

        }

        @Override
        public int getIconHeight() {
            return HEIGHT;
        }

        @Override
        public int getIconWidth() {
            return WIDTH;
        }
    }

    //constructor
    public Diagram(int[][][] results, String selectedMajor){
        this.selectedMajor = selectedMajor;
        this.results = results;
        Icon icon = new DiagramIcon();
        JLabel label = new JLabel(icon);
        add(label);
    }

}
