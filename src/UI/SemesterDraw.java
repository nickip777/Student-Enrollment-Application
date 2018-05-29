package UI;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

/**
 * draws out semester GUI
 * has bars containing ratios
 * has several boxes per milestone for joing studying and leaving
 */
public class SemesterDraw {
    private static final int MILESTONE_WIDTH = 150;
    private static final int MILESTONE_HEIGHT = 200;


    private static final int MILESTONE_BAR_HEIGHT = 40;
    private static final int MILESTONE_BAR_Y = MILESTONE_HEIGHT / 2 - MILESTONE_BAR_HEIGHT / 2;


    private static final int OFFSET = 20;

    private static final int LEAVING_WIDTH = 225;
    private static final int LEAVING_HEIGHT = 170;
    private static final int JOINING_WIDTH = LEAVING_WIDTH;
    private static final int JOINING_HEIGHT = LEAVING_HEIGHT;

    private static final int ADMISSION_BAR_HEIGHT = 25;
    private static final int ADMISSION_BAR_WIDTH = 110;

    private static final int ADMISSION_LETTER_OFFSET = 10;

    private static final String[] PROGRAMS = {"CS Major", "CS Minor", "CS Joint Major", "Unknown", "Dropped Out"};
    private static String[] programOrder;
    private static String selectedMajor;
    private static final String[] ADMISSION_BOX_TITLES = {"Students Joining", "Students Leaving"};

    private static int[][] studentsInProgram;
    private static int[][][] studentsJoiningProgram;
    private static int[][][] studentsLeavingProgram;

    public static void draw(Graphics2D g2d, int currentX, int startY, String milestone_title, int[][][] results, int milestoneNum, String major) {
        //initialize
        studentsInProgram = new int[6][4];
        studentsJoiningProgram = new int[4][6][4];
        studentsLeavingProgram = new int[4][6][4];
        selectedMajor = major;
        programOrder = new String[5];

        //order programs and split results
        programOrdering();
        splitResults(results);

        //Milestone Box drawing
        g2d.setColor(Color.BLACK);
        g2d.draw(new Rectangle2D.Double(currentX, startY, MILESTONE_WIDTH, MILESTONE_HEIGHT));
        drawArrowLine(g2d, currentX, startY + MILESTONE_HEIGHT, currentX + 50, startY + MILESTONE_HEIGHT+30, 5, 10);
        drawArrowLine(g2d, currentX + MILESTONE_WIDTH, startY+MILESTONE_HEIGHT/2, currentX + MILESTONE_WIDTH *2, startY + MILESTONE_HEIGHT/2, 5,10);
        int[] stayInMajor = getAdmittanceTotal();
        g2d.drawString(new Integer(stayInMajor[milestoneNum]).toString(), currentX + MILESTONE_WIDTH + 10, startY+MILESTONE_HEIGHT/2);

        //Milestone-Add titles
        Font font = g2d.getFont();
        FontRenderContext context = g2d.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(milestone_title, context);
        int titleX = currentX + (MILESTONE_WIDTH / 2) - (int) (bounds.getWidth() / 2);
        int titleY = startY + (int) bounds.getHeight();
        g2d.drawString(milestone_title, titleX, titleY);
        g2d.drawString(selectedMajor, titleX, titleY + MILESTONE_HEIGHT- 20);

        //Admission/leaving Box
        int row2Y = startY - MILESTONE_HEIGHT;
        int row2X = currentX + OFFSET;
        int row3Y = startY + LEAVING_HEIGHT + 70;
        //Admission-titles
        Rectangle2D joiningTitleBounds = font.getStringBounds(ADMISSION_BOX_TITLES[0], context);
        int joiningTitleX = row2X + LEAVING_WIDTH / 2 - (int) joiningTitleBounds.getWidth() / 2-100;
        int joiningTitleY = row2Y + (int) joiningTitleBounds.getHeight();
        Rectangle2D leavingTitleBounds = font.getStringBounds(ADMISSION_BOX_TITLES[1], context);
        int leavingTitleX = row2X + LEAVING_WIDTH / 2 - (int) leavingTitleBounds.getWidth() / 2;
        int leavingTitleY = startY + (int) leavingTitleBounds.getHeight();

        //Milestone-Add Bar
        //Change this to reflect results, when results param is outputted

        Rectangle2D.Double milestoneBar = new Rectangle2D.Double(currentX,
                startY + MILESTONE_BAR_Y,
                MILESTONE_WIDTH,
                MILESTONE_BAR_HEIGHT);
        g2d.setStroke(new BasicStroke(0));
        g2d.draw(milestoneBar);

        //set ratios
        double milestoneMale = (double) studentsInProgram[milestoneNum][1] / (double) studentsInProgram[milestoneNum][0] * MILESTONE_WIDTH;
        double milestoneUn = (double) studentsInProgram[milestoneNum][3] / (double) studentsInProgram[milestoneNum][0] * MILESTONE_WIDTH;
        double milestoneFemale = (double) studentsInProgram[milestoneNum][2] / (double) studentsInProgram[milestoneNum][0] * MILESTONE_WIDTH;

        //draw boxes for bars
        Rectangle2D milestoneMaleRect = new Rectangle2D.Double(currentX + 1, startY + MILESTONE_BAR_Y + 1, milestoneMale, MILESTONE_BAR_HEIGHT - 2);
        Rectangle2D milestoneUnRect = new Rectangle2D.Double(currentX + 1 + milestoneMale, startY + MILESTONE_BAR_Y + 1, milestoneUn - 1, MILESTONE_BAR_HEIGHT - 2);
        Rectangle2D milestoneFemaleRect = new Rectangle2D.Double(currentX + 1 + milestoneMale + milestoneUn, startY + MILESTONE_BAR_Y + 1, milestoneFemale - 2, MILESTONE_BAR_HEIGHT - 2);

        //fill bars
        fillBar(g2d, milestoneMaleRect, Color.CYAN);
        fillBar(g2d, milestoneUnRect, Color.MAGENTA);
        fillBar(g2d, milestoneFemaleRect, Color.RED);
        // Render Numbers
        g2d.setColor(Color.BLACK);

        // Male Number
        if (results[0][milestoneNum][1] != 0) {
            Rectangle2D maleMilestoneNumberBounds = font.getStringBounds(new Integer(studentsInProgram[milestoneNum][1]).toString(), context);
            double maleMilestoneNumberBounds_startX = currentX + milestoneMale / 2 - maleMilestoneNumberBounds.getWidth() / 2;
            double maleMilestoneNumberBounds_startY = startY + MILESTONE_BAR_Y + MILESTONE_BAR_HEIGHT - maleMilestoneNumberBounds.getHeight();
            g2d.drawString(new Integer(studentsInProgram[milestoneNum][1]).toString(), (int) maleMilestoneNumberBounds_startX, (int) maleMilestoneNumberBounds_startY);
        }
        // Female Number
        if (results[0][milestoneNum][2] != 0) {
            Rectangle2D femaleMilestoneNumberBounds = font.getStringBounds(new Integer(studentsInProgram[milestoneNum][2]).toString(), context);
            double femaleMilestoneNumberBounds_startX = currentX + milestoneMale + milestoneUn + milestoneFemale / 2 - femaleMilestoneNumberBounds.getWidth() / 2;
            double femaleMilestoneNumberBounds_startY = startY + MILESTONE_BAR_Y + MILESTONE_BAR_HEIGHT - femaleMilestoneNumberBounds.getHeight();
            g2d.drawString(new Integer(studentsInProgram[milestoneNum][2]).toString(), (int) femaleMilestoneNumberBounds_startX, (int) femaleMilestoneNumberBounds_startY);
        }

        // No Gender Number
        if (results[0][milestoneNum][3] != 0) {
            Rectangle2D unMilestoneNumberBounds = font.getStringBounds(new Integer(studentsInProgram[milestoneNum][3]).toString(), context);
            double unMilestoneNumberBounds_startX = currentX + milestoneMale + milestoneUn / 2 - unMilestoneNumberBounds.getWidth() / 2;
            double unMilestoneNumberBounds_startY = startY + MILESTONE_BAR_Y + MILESTONE_BAR_HEIGHT;
            g2d.drawString(new Integer(studentsInProgram[milestoneNum][3]).toString(), (int) unMilestoneNumberBounds_startX, (int) unMilestoneNumberBounds_startY);
        }

        //Full Number
        Rectangle2D milestoneNumberBounds = font.getStringBounds(new Integer(studentsInProgram[milestoneNum][0]).toString(), context);
        double unMilestoneNumberBounds_startX = currentX + MILESTONE_WIDTH/2;
        double femaleMilestoneNumberBounds_startY = startY + MILESTONE_BAR_Y + MILESTONE_BAR_HEIGHT + 15;
        g2d.drawString(new Integer(studentsInProgram[milestoneNum][0]).toString(), (int) unMilestoneNumberBounds_startX, (int) femaleMilestoneNumberBounds_startY);


        //Admissions Bar / Titles for them
        // Joining
        if (milestoneNum != 0) {

            g2d.draw(new Rectangle2D.Double(row2X - 100, row2Y, LEAVING_WIDTH, LEAVING_HEIGHT));
            drawArrowLine(g2d, row2X - 100, row2Y + LEAVING_HEIGHT, row2X - 40, row2Y + LEAVING_HEIGHT+50, 5, 10);
            int[] joiningStudents = getJoiningStudents(milestoneNum);
            g2d.drawString(new Integer(joiningStudents[milestoneNum-1]).toString(),row2X - 50, row2Y +LEAVING_HEIGHT + 30);
            g2d.drawString(ADMISSION_BOX_TITLES[0], joiningTitleX, joiningTitleY);
            g2d.setColor(Color.BLACK);
            for (int i = 0; i < programOrder.length - 1; i++) { // change this when programs differentiate
                //if String Program == Admission, skip that iteration
                Rectangle2D.Double admissionBar = new Rectangle2D.Double(
                        row2X + (LEAVING_WIDTH - ADMISSION_BAR_WIDTH)-100,
                        joiningTitleY + i * ADMISSION_BAR_HEIGHT + 10,
                        ADMISSION_BAR_WIDTH,
                        ADMISSION_BAR_HEIGHT
                );
                //poopulate bars and set numbers
                g2d.draw(admissionBar);
                populateBars(g2d, studentsJoiningProgram, milestoneNum-1, admissionBar, i, font, context, currentX - 100);
                g2d.setColor(Color.BLACK);
                g2d.drawString(programOrder[i], row2X + ADMISSION_LETTER_OFFSET-100, joiningTitleY + i * ADMISSION_BAR_HEIGHT + ADMISSION_BAR_HEIGHT + 10);

            }
        }

        //Leaving
        if (milestoneNum != 4) {

            //draw box, arrow and set numbers
            g2d.draw(new Rectangle2D.Double(row2X, row3Y, JOINING_WIDTH, JOINING_HEIGHT));
            g2d.drawString(ADMISSION_BOX_TITLES[1], leavingTitleX, leavingTitleY + LEAVING_HEIGHT + 70);
            int[] leavingStudents = getLeavingStudents(milestoneNum);
            g2d.drawString(new Integer(leavingStudents[milestoneNum]).toString(),leavingTitleX - 40, leavingTitleY + LEAVING_HEIGHT + 30);
            for (int i = 0; i < programOrder.length; i++) {
                int pointX = row2X + (LEAVING_WIDTH - ADMISSION_BAR_WIDTH);
                int pointY = leavingTitleY + LEAVING_HEIGHT + i * ADMISSION_BAR_HEIGHT + 85;
                //if String Program == Admission, skip that iteration
                Rectangle2D.Double admissionBar = new Rectangle2D.Double(
                        pointX,
                        pointY,
                        ADMISSION_BAR_WIDTH,
                        ADMISSION_BAR_HEIGHT
                );
                //drawBar(g2d, pointX, pointY, results[0][i][j]);
                g2d.draw(admissionBar);
                g2d.drawString(PROGRAMS[i], row2X + ADMISSION_LETTER_OFFSET, leavingTitleY + LEAVING_HEIGHT + i * ADMISSION_BAR_HEIGHT + ADMISSION_BAR_HEIGHT + 75);
                populateBars(g2d, studentsLeavingProgram, milestoneNum, admissionBar, i, font, context, currentX);
            }
        }
    }


    //fiill bar with color
    private static void fillBar(Graphics2D g2d, Rectangle2D rect, Color color) {
        g2d.setColor(color);
        g2d.fill(rect);
        g2d.draw(rect);
    }

    //split the single array into multiple arrays for easier use
    private static void splitResults(int[][][] results) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                studentsInProgram[i][j] = results[0][i][j];
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    studentsJoiningProgram[i][j][k] = results[i + 1][j][k];
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 4; k++) {
                    studentsLeavingProgram[i][j][k] = results[i + 5][j][k];
                }
            }
        }

    }


    /**
     * Draw an arrow line betwwen two point
     * http://stackoverflow.com/a/27461352
     * @param g  the graphic component
     * @param x1 x-position of first point
     * @param y1 y-position of first point
     * @param x2 x-position of second point
     * @param y2 y-position of second point
     * @param d  the width of the arrow
     * @param h  the height of the arrow
     */
    private static void drawArrowLine(Graphics2D g, int x1, int y1, int x2, int y2, int d, int h) {
        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx * dx + dy * dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;

        x = xm * cos - ym * sin + x1;
        ym = xm * sin + ym * cos + y1;
        xm = x;

        x = xn * cos - yn * sin + x1;
        yn = xn * sin + yn * cos + y1;
        xn = x;

        int[] xpoints = {x2, (int) xm, (int) xn};
        int[] ypoints = {y2, (int) ym, (int) yn};

        g.drawLine(x1, y1, x2, y2);
        g.fillPolygon(xpoints, ypoints, 3);
    }

    //order programs with respect to what program is picekd
    private static void programOrdering() {
        if (selectedMajor.equals("CSMAJ")) {
            programOrder[0] = "CS SoSy";
            programOrder[1] = "CS Minor";
            programOrder[2] = "CS Joint Major";
            programOrder[3] = "Unknown";
            programOrder[4] = "Dropout";
        } else if (selectedMajor.equals("SOSY")) {
            programOrder[0] = "CS Major";
            programOrder[1] = "CS Minor";
            programOrder[2] = "CS Joint Major";
            programOrder[3] = "Unknown";
            programOrder[4] = "Dropout";
        } else if (selectedMajor.equals("CSMNR")) {
            programOrder[0] = "CS Major";
            programOrder[1] = "CS SoSy";
            programOrder[2] = "CS Joint Major";
            programOrder[3] = "Unknown";
            programOrder[4] = "Dropout";
        } else if (selectedMajor.equals("CSJNT")) {
            programOrder[0] = "CS Major";
            programOrder[1] = "CS SoSy";
            programOrder[2] = "CS Minor";
            programOrder[3] = "Unknown";
            programOrder[4] = "Dropout";
        } else if (selectedMajor.equals("OTHER")) {
            programOrder[0] = "CS Major";
            programOrder[1] = "CS SoSy";
            programOrder[2] = "CS Minor";
            programOrder[3] = "CS Joint Major";
            programOrder[4] = "Dropout";
        }
    }

    //populate bars
    private static void populateBars(Graphics2D g2d, int[][][] studentArr, int milestoneNum, Rectangle2D admissionBar, int i, Font font, FontRenderContext context, int currentX) {
        if (studentArr[milestoneNum][i][0] != 0) {
            //setup rectangle male
            double malePercent = (double) ADMISSION_BAR_WIDTH * (double) studentArr[milestoneNum][i][1] / (double) studentArr[milestoneNum][i][0];
            Rectangle2D.Double maleAdmissionBar = new Rectangle2D.Double(
                    admissionBar.getX() + 1,
                    admissionBar.getY() + 1,
                    malePercent - 2,
                    ADMISSION_BAR_HEIGHT - 2
            );
            Rectangle2D maleJoiningNumberBounds = font.getStringBounds(new Integer(studentArr[milestoneNum][i][1]).toString(), context);
            double maleJoiningNumberBounds_startX = admissionBar.getX() + malePercent / 2 - maleJoiningNumberBounds.getWidth() / 2;
            double maleJoiningNumberBounds_startY = admissionBar.getY()+ADMISSION_BAR_HEIGHT-3;

            //setup rectangle no gender
            double noGenderPercent = (double) ADMISSION_BAR_WIDTH * (double) studentArr[milestoneNum][i][3] / (double) studentArr[milestoneNum][i][0];
            Rectangle2D.Double noGenderAdmissionBar = new Rectangle2D.Double(
                    admissionBar.getX() + maleAdmissionBar.getWidth() + 2,
                    admissionBar.getY() + 1,
                    noGenderPercent - 1,
                    ADMISSION_BAR_HEIGHT - 2
            );

            Rectangle2D noGenderJoiningNumberBounds = font.getStringBounds(new Integer(studentArr[milestoneNum][i][3]).toString(), context);
            double noGenderJoiningNumberBounds_startX = noGenderAdmissionBar.getX() + noGenderPercent / 2 - noGenderJoiningNumberBounds.getWidth() / 2;
            double noGenderJoiningNumberBounds_startY = noGenderAdmissionBar.getY()+ADMISSION_BAR_HEIGHT-3;

            //setup rectangle female
            double femalePercent = (double) ADMISSION_BAR_WIDTH * (double) studentArr[milestoneNum][i][2] / (double) studentArr[milestoneNum][i][0];
            Rectangle2D.Double femaleAdmissionBar = new Rectangle2D.Double(
                    admissionBar.getX() + maleAdmissionBar.getWidth() + noGenderAdmissionBar.getWidth() + 3,
                    admissionBar.getY() + 1,
                    femalePercent - 1,
                    ADMISSION_BAR_HEIGHT - 2
            );
            Rectangle2D femaleJoiningNumberBounds = font.getStringBounds(new Integer(studentArr[milestoneNum][i][2]).toString(), context);
            double femaleJoiningNumberBounds_startX = femaleAdmissionBar.getX() + femalePercent / 2 - femaleJoiningNumberBounds.getWidth() / 2;
            double femaleJoiningNumberBounds_startY = femaleAdmissionBar.getY()+ADMISSION_BAR_HEIGHT-3;
            fillBar(g2d, maleAdmissionBar, Color.CYAN);
            fillBar(g2d, noGenderAdmissionBar, Color.GRAY);
            fillBar(g2d, femaleAdmissionBar, Color.RED);
            //Full Number
            g2d.setColor(Color.BLACK);
            double noGenderNumberBounds_startX = currentX + JOINING_WIDTH + 30;
            double femaleNumberBounds_startY = maleAdmissionBar.getY()+ADMISSION_BAR_HEIGHT-3;

            //set total nums
            g2d.drawString(new Integer(studentArr[milestoneNum][i][2]).toString(), (int) femaleJoiningNumberBounds_startX, (int) femaleJoiningNumberBounds_startY);
            g2d.drawString(new Integer(studentArr[milestoneNum][i][1]).toString(), (int) maleJoiningNumberBounds_startX, (int) maleJoiningNumberBounds_startY);
            g2d.drawString(new Integer(studentArr[milestoneNum][i][3]).toString(), (int) noGenderJoiningNumberBounds_startX, (int) noGenderJoiningNumberBounds_startY);
            g2d.drawString(new Integer(studentArr[milestoneNum][i][0]).toString(), (int) noGenderNumberBounds_startX, (int) femaleNumberBounds_startY);
        }
    }

    //get array of students joining per milestone
    private static int[] getJoiningStudents(int milestoneNum){
        int[] finalResult = new int[4];
        for(int i = 0; i < 4; i++){
            finalResult[0] += studentsJoiningProgram[milestoneNum-1][i][0];
            finalResult[1] += studentsJoiningProgram[milestoneNum-1][i][0];
            finalResult[2] += studentsJoiningProgram[milestoneNum-1][i][0];
            finalResult[3] += studentsJoiningProgram[milestoneNum-1][i][0];
        }
        return finalResult;
    }

    //get array of leaving students per milestone
    public static int[] getLeavingStudents(int milestoneNum){
        int[] finalResult = new int[5];
        for(int i = 0; i < 5; i++){
            finalResult[0] += studentsLeavingProgram[milestoneNum][i][0];
            finalResult[1] += studentsLeavingProgram[milestoneNum][i][0];
            finalResult[2] += studentsLeavingProgram[milestoneNum][i][0];
            finalResult[3] += studentsLeavingProgram[milestoneNum][i][0];
        }
        return finalResult;
    }

    //get total admittance to status bar
    public static int[] getAdmittanceTotal(){
        int[] result = new int[5];
        for(int i = 0; i < 4; i++) {
                result[i] = studentsInProgram[i][0] - studentsLeavingProgram[i][0][0] - studentsLeavingProgram[i][1][0]
                -studentsLeavingProgram[i][2][0]-studentsLeavingProgram[i][3][0];
        }
        return result;
    }

}
