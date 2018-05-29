package Model;
import com.sun.javafx.collections.MappingChange;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Student class
 * contains its gender and student number
 * and an arraylist of semesters
 * <p>
 * contains a subclass of semesters which stores semester code, actions , credit hours and program
 * uses function to place into array with correct program
 */
public class Student {
    private char gender;
    private int studentNumber;
    private ArrayList<Semester> semesters;
    //semester class
    protected class Semester {
        public int getYear() {
            return year;
        }

        private int year;
        private int term;
        private String creditHours;
        private String action;
        private String program;
        private boolean isAction = false;
        private boolean isStudying = false;
        private int semesterCode;
        //constructor which converts semester code into year and term
        public Semester(String semesterCode) {
            this.semesterCode = Integer.parseInt(semesterCode);
            if (semesterCode.length() == 3) {
                this.term = Character.getNumericValue(semesterCode.charAt(2));
                this.year = Integer.parseInt(semesterCode.substring(0, 2)) + 1900;
            } else {
                this.term = Character.getNumericValue(semesterCode.charAt(3));
                this.year = Integer.parseInt(semesterCode.substring(0, 3)) + 1900;
            }
        }
        //setters and getters *******************************************************
        public String getCreditHours() {
            return creditHours;
        }
        public void setCreditHours(String creditHours) {
            this.isStudying = true;
            this.creditHours = creditHours;
        }
        public String getAction() {
            return action;
        }
        public void setAction(String action) {
            this.isAction = true;
            this.action = action;
        }
        public String getProgram() {
            return program;
        }
        public void setProgram(String program) {
            this.program = program;
        }
        public boolean isAction() {
            return isAction;
        }
        public boolean isStudying() {
            return isStudying;
        }
        public int getSemesterCode() {
            return semesterCode;
        }
    }
    //student constructor
    public Student(char gender, int studentNumber) {
        this.gender = gender;
        this.studentNumber = studentNumber;
        semesters = new ArrayList<>();
    }
    public int getStudentNumber() {
        return studentNumber;
    }
    //function to process new smeester
    public void processNewSemester(String semesterCode, String creditHours) {
        int semesterCodeTemp = Integer.parseInt(semesterCode);
        int addmissionSemster=0;
        String cancelterm = null;
        String lastprogram = null;
        Semester thisSemester = new Semester(semesterCode);
        thisSemester.setCreditHours(creditHours);
        for (Semester semester : this.semesters) {
            if(addmissionSemster == semester.getSemesterCode() && !semester.getCreditHours().equals("L8")){
                cancelterm = semester.getCreditHours();
            }
            if (semester.action != null) {
                //search for most recent action with corrresponding program and sets it
                if ("admt".equals(semester.action)) {
                    thisSemester.setProgram(semester.getProgram());
                    lastprogram = semester.getProgram();
                }
                if ("add".equals(semester.action) && semester.getSemesterCode() <= semesterCodeTemp) {
                    if(creditHours.equals("L8") || semester.getSemesterCode() < semesterCodeTemp) {
                        addmissionSemster = semester.getSemesterCode();
                        thisSemester.setProgram(semester.getProgram());
                    }
                }
                if ("rem".equals(semester.action) && semester.getSemesterCode() == semesterCodeTemp) {
                    thisSemester.setProgram(null);
                }
                if ("dropout".equals(semester.action) && semester.getSemesterCode() <= semesterCodeTemp) {
                    thisSemester.setProgram(null);
                }
            }
        }
        if(creditHours.equals(cancelterm) && !creditHours.equals("L8")){
            thisSemester.setProgram(null);
        }
//add to semester list
        semesters.add(thisSemester);
    }
    //method to process new program
    public void processNewProgram(String semesterCode, String action, String program) {
        Semester thisSemester = new Semester(semesterCode);
        thisSemester.setAction(action);
        String tempProgram = null;
        //determines what to do with program with action
        switch (action) {
            case "admt":
                tempProgram = program;
                break;
            case "add":
                tempProgram = program;
                break;
            case "rem":
                tempProgram = program;
                break;
            case "dropout":
                tempProgram = null;
                break;
            case "fin":
                tempProgram = program;
                break;
        }
        //add to semester list
        thisSemester.setProgram(tempProgram);
        semesters.add(thisSemester);
    }
    //return semester array
    public ArrayList<Semester> getSemesters() {
        return semesters;
    }
    public char getGender() {
        return gender;
    }
}