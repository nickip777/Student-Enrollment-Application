package UI;
import Model.CSVScan;
import java.util.Arrays;
/**
 * UI class which displays student count from csvScan
 */
public class UI {
    /*
    private static CSVScan csvScan;
    private static final String[] atMileStone = {"ADMITTED", "YEAR1", "YEAR2", "YEAR3", "YEAR4", "GRADUATED"};
    private static final String[] programs = {"CS Major", "CS Software Systems", "CS Minor", "CS Joint Major", "Unknown", "Dropped Out"};
    private static final String added = "added";
    //main function
    public static void main(String[] args){
        csvScan = new CSVScan();
        printStudentsInProgram();
        printStudentsJoiningProgram();
        printStudentsLeavingProgram();
    }
    //print students in program
    private static void printStudentsInProgram(){
        int[][] results = csvScan.studentsInProgram("CSMAJ");
        System.out.println("************************************\n" +
                "* Students in program at milestone *\n" +
                "************************************");
        int i = 0;
        for(String s: atMileStone){
            System.out.printf("%11s%5d: M=%5d  F=%5d  U=%5d (CS_MAJOR students at "+s+")\n", s,results[i][0],results[i][1],results[i][2],results[i][3]);
            i++;
        }
        System.out.println();
    }/
    //print students joining program
    private static void printStudentsJoiningProgram(){
        int[][][] result = csvScan.studentsEnteringProgram("CSMAJ");
        System.out.println("*************************************************\n" +
                "* Students joining program going into milestone *\n" +
                "*************************************************");
        for(int i = 0; i < 4; i ++) {
            System.out.printf("Milestone: YEAR%d\n",i+1);
            int j = 0;
            int total = 0;
            for (String s : programs) {
                if(s.equals("Unknown")) {
                    break;
                }
                System.out.printf("%11s%5d: M=%5d  F=%5d  U=%5d (CS_MAJOR students at " + s + ")\n",
                        added, result[i][j][0], result[i][j][1],result[i][j][2], result[i][j][3]);
                total += result[i][j][0];
                j++;
            }
            //count total
            System.out.printf("%11s%5d\n","Total",total);
        }
        System.out.println();
    }
    //print students leaving program
    private static void printStudentsLeavingProgram(){
        int[][][] result = csvScan.studentsLeavingProgram("CSMAJ");
        System.out.println("*************************************************\n" +
                "* Students leaving program before milestone  *\n" +
                "*************************************************");
        for(int i = 0; i < 4; i ++) {
            System.out.printf("Milestone: YEAR%d\n",i+1);
            int j = 0;
            int total = 0;
            for (String s : programs) {
                System.out.printf("%11s%5d: M=%5d  F=%5d  U=%5d (CS_MAJOR students at " + s + ")\n",
                        added, result[i][j][0], result[i][j][1],result[i][j][2], result[i][j][3]);
                total += result[i][j][0];
                j++;
            }
            //count total
            System.out.printf("%11s%5d\n","Total",total);
        }
        System.out.println();
    }*/
}