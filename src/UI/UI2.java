package UI;

import Model.CSVScan;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * UI class which contains Jframe and all panels
 */
public class UI2 {
    private static Diagram diagram;
    private static UserSelectProgram userSelectProgram;
    private static String selectedMajor;
    private static int[][][] results;
    private static CSVScan scanFile;
    private static JFrame frame;
    private static StudentFilter filter;
    private static JLabel statTotal;
    private static JLabel yearDisplay;
    private static int gradYear = -1;
    private static int studyYear = -1;
    private static JPanel status;


    public static void main(String[] args){
        //initialize
        scanFile = new CSVScan();
        results = scanFile.combinedStudentArrays("CSMAJ",-1,-1);
        selectedMajor = ("CSMAJ");
        frame = new JFrame();
        updateStat();
        updateYearDisplay(2022,2000);
        diagram = new Diagram(results, selectedMajor);
        userSelectProgram = new UserSelectProgram();
        filter = new StudentFilter();
        status = new JPanel(new BorderLayout());

        //add to panels
        status.add(statTotal,BorderLayout.WEST);
        status.add(yearDisplay, BorderLayout.EAST);
        JPanel topPanel = new JPanel(new GridLayout(0,2));
        topPanel.add(userSelectProgram);
        topPanel.add(filter);

        //add to frame
        frame.setLayout(new BorderLayout());
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(diagram, BorderLayout.CENTER);
        frame.add(status, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600,900);
        frame.setVisible(true);
        registerAsObserver();
    }

    //update stats for status bar
    private static void updateStat(){
        int[] totalStudents = new int[4];
        int[] admittance = scanFile.getAdmittanceTotal(selectedMajor,gradYear,studyYear);
        int[] joiningStudents1 = scanFile.getJoiningStudents(selectedMajor,gradYear,studyYear,1);
        int[] joiningStudents2 = scanFile.getJoiningStudents(selectedMajor,gradYear,studyYear,2);
        int[] joiningStudents3 = scanFile.getJoiningStudents(selectedMajor,gradYear,studyYear,3);
        int[] joiningStudents4 = scanFile.getJoiningStudents(selectedMajor,gradYear,studyYear,4);
        totalStudents[0] = admittance[0] + joiningStudents1[0] + joiningStudents2[0] + joiningStudents3[0] + joiningStudents4[0];
        totalStudents[1] = admittance[1] + joiningStudents1[1] + joiningStudents2[1] + joiningStudents3[1] + joiningStudents4[1];
        totalStudents[2] = admittance[2] + joiningStudents1[2] + joiningStudents2[2] + joiningStudents3[2] + joiningStudents4[2];
        totalStudents[3] = admittance[3] + joiningStudents1[3] + joiningStudents2[3] + joiningStudents3[3] + joiningStudents4[3];
        statTotal = new JLabel(totalStudents[0] + " students were part of CS Major [M:" + totalStudents[1] + ", F:" + totalStudents[2] + ", U:" + totalStudents[3] + "]");
        System.out.println(totalStudents[0] + " students were part of CS Major [M:" + totalStudents[1] + ", F:" + totalStudents[2] + ", U:" + totalStudents[3] + "]");
    }

    //update year display for selected year
    private static void updateYearDisplay(int gradYear, int studyYear){
        if(studyYear == -1 && gradYear == -1) {
            yearDisplay = new JLabel("Showing " + selectedMajor + " students who took classes between 2000 and 2022.");
        }else if(gradYear == -1){
            yearDisplay = new JLabel("Showing " + selectedMajor + " students who took classes between " + studyYear + " and 2022.");
        }else if(studyYear == -1){
            yearDisplay = new JLabel("Showing " + selectedMajor + " students who took classes between 2000 and " + gradYear + ".");
        }else{
            yearDisplay = new JLabel("Showing " + selectedMajor + " students who took classes between " + studyYear + " and " + gradYear + ".");
        }
    }
    //register observers and implement functions
    private static void registerAsObserver() {
        UIBox.addObserver(new UIObserver() {
            @Override
            public void stateChanged() {
                frame.remove(diagram);
                //convert selected strings
                selectedMajor = userSelectProgram.getDropdownString();
                if(selectedMajor.equals("CS SoSy")){
                    selectedMajor = "SOSY";
                }else if(selectedMajor.equals("CS Minor")){
                    selectedMajor = "CSMNR";
                }else if(selectedMajor.equals("CS Joint Major")){
                    selectedMajor = "CSJNT";
                }else if(selectedMajor.equals("Unknown")) {
                    selectedMajor = "OTHER";
                }else if(selectedMajor.equals("CS Major")){
                    selectedMajor = "CSMAJ";
                }

                //if invalid prompt dialog box
                if(filter.getGradYear() == -2 || filter.getInYear() == -2) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid 4 digit number.");
                }else{
                    gradYear = filter.getGradYear();
                    studyYear = filter.getInYear();
                    results = scanFile.combinedStudentArrays(selectedMajor, filter.getGradYear(), filter.getInYear());
                    updateYearDisplay(gradYear, studyYear);
                    updateStat();
                    //repaint frame
                    frame.remove(status);
                    status = new JPanel(new BorderLayout());
                    status.add(statTotal,BorderLayout.WEST);
                    status.add(yearDisplay, BorderLayout.EAST);
                    frame.add(status, BorderLayout.SOUTH);
                    diagram = new Diagram(results, selectedMajor);
                    frame.add(diagram, BorderLayout.CENTER);
                    frame.revalidate();
                    frame.repaint();
                }
            }
        });
    }
}
