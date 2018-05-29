package Model;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
/**
 * This class scans the csv file of semesters, programs and students
 * also counts how many students are in each scenario
 */
public class CSVScan {
    //hash map of students
    private static Map<Integer, Student> studentMap;
    //constructor
    public CSVScan() {
        studentMap = new TreeMap<>();
        readStudentFile();
        readProgramFile();
        readSemesterFile();
    }
    //read the program file
    private static void readProgramFile() {
        String csvFile = "src/data/test_programs.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();
            //while file has next line that is not null
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] student = line.split(cvsSplitBy);
                int studentNum = Integer.parseInt(student[0]);
                String semesterCode = student[1];
                String action = student[2];
                //find the student and reference it
                Student s = studentMap.get(studentNum);
                //process new semester
                s.processNewProgram(semesterCode, action, (student.length == 4) ? student[3] : null);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //read semester file
    private static void readSemesterFile() {
        String csvFile = "src/data/test_semesters.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();
            //while file has next line
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] student = line.split(cvsSplitBy);
                int studentNum = Integer.parseInt(student[0]);
                String semesterCode = student[1];
                String creditHours = student[2];
                //find student and add semester
                studentMap.get(studentNum).processNewSemester(semesterCode, creditHours);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //read student file
    private static void readStudentFile() {
        String csvFile = "src/data/test_students.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ", ";
        try {
            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] student = line.split(cvsSplitBy);
                int studentNum = Integer.parseInt(student[0]);
                char gender = student[1].charAt(0);
                //create new student and put it in the hashmap
                studentMap.put(studentNum, new Student(gender, studentNum));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //combine all the arrays to pass into UI
    public int[][][] combinedStudentArrays(String program, int gradYear, int studyingYear){
        int[][] studentsInCS = studentsInProgram(program, gradYear, studyingYear);
        int[][][] studentsLeavingCS = studentsLeavingProgram(program, gradYear, studyingYear);
        int[][][] studentsJoiningCS = studentsEnteringProgram(program, gradYear, studyingYear);
        int[][][] result = new int[9][6][4];

        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 4; j++){
                result[0][i][j] = studentsInCS[i][j];
            }
        }

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                for(int k = 0; k < 4; k++) {
                    result[i+1][j][k] = studentsJoiningCS[i][j][k];
                }
            }
        }
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 5; j++){
                for(int k = 0; k < 4; k++) {
                    result[i+5][j][k] = studentsLeavingCS[i][j][k];
                }
            }
        }

        return result;
    }
    //count students in CS
    public int[][] studentsInProgram(String program, int gradYear, int studyingYear) {
        //result for storing data
        int[][] result = new int[6][4];
        Iterator iterator = studentMap.entrySet().iterator();
        //for each student
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            Student thisStudent = studentMap.get(pair.getKey());
            int gender = 0;
            //determine gender to place in 2d array
            switch (thisStudent.getGender()) {
                case 'M':
                    gender = 1;
                    break;
                case 'F':
                    gender = 2;
                    break;
                case 'U':
                    gender = 3;
                    break;
            }
            //boolean to check students studied in year with the program
            boolean studiedinYear = didStudentStudyInYear(thisStudent,studyingYear,program);

            //booleans for preventing double count
            boolean isYear1 = false, isYear2 = false, isYear3 = false, isYear4 = false;
            assert (gender != 0);
            //extract semester list from student
            ArrayList<Student.Semester> semesters = thisStudent.getSemesters();
            if (semesters != null && studiedinYear) {
                for (Student.Semester semester : semesters) {
                    //if semester equals specified program and action
                    if (program.equals(semester.getProgram()) && semester.isStudying()
                            && isInGradYear(semesters,gradYear,program)) {
                        //match credithours with year and add to result
                        if (("L2".equals(semester.getCreditHours()) || "L3".equals(semester.getCreditHours())) && !isYear1) {
                            result[1][gender] += 1;
                            isYear1 = true;
                        }
                        if (("L5".equals(semester.getCreditHours()) || "L4".equals(semester.getCreditHours())) && !isYear2) {
                            result[2][gender] += 1;
                            isYear2 = true;
                        }
                        if (("L7".equals(semester.getCreditHours()) || "L6".equals(semester.getCreditHours())) && !isYear3) {
                            result[3][gender] += 1;
                            isYear3 = true;
                        }
                        if ("L8".equals(semester.getCreditHours()) && !isYear4) {
                            result[4][gender] += 1;
                            isYear4 = true;
                        }
                        //action only
                    } else if (program.equals(semester.getProgram()) && semester.isAction()
                            && isInGradYear(semesters,gradYear,program)) {
                        if ("admt".equals(semester.getAction())) {
                            result[0][gender] += 1;

                            if(gradYear == 2010 && studyingYear == 2006 && gender==2){
                                System.out.println("HEY DUMB SHIT ITS" +thisStudent.getStudentNumber());
                            }
                        }
                        if ("fin".equals(semester.getAction())) {
                            result[5][gender] += 1;
                        }
                    }
                }
            }
        }
        //add up totals
        for (int i = 0; i < 6; i++) {
            result[i][0] = (result[i][1] + result[i][2] + result[i][3]);
        }
        return result;
    }
    //count students leaving CS
    public int[][][] studentsLeavingProgram(String program, int gradYear, int studyingYear) {
        //for storing result
        int[][][] result = new int[4][5][4];
        Iterator iterator = studentMap.entrySet().iterator();
        //for each student
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            Student thisStudent = studentMap.get(pair.getKey());
            int gender = 0;
            //determine gender
            switch (thisStudent.getGender()) {
                case 'M':
                    gender = 1;
                    break;
                case 'F':
                    gender = 2;
                    break;
                case 'U':
                    gender = 3;
                    break;
            }
            assert (gender != 0);

            boolean studiedinYear = didStudentStudyInYear(thisStudent,studyingYear,program);

            ArrayList<Student.Semester> semesters = thisStudent.getSemesters();
            String creditHours = null;
            if (semesters != null && studiedinYear) {
                for (Student.Semester semester : semesters) {
                    //for each semester, find the semester with the "add" action
                    if ("admt".equals(semesters.get(0).getAction()) && program.equals(semesters.get(0).getProgram())
                            && isInGradYear(semesters,gradYear,program)) {
                        if ("add".equals(semester.getAction()) && !program.equals(semester.getProgram())) {
                            int semCode = semester.getSemesterCode();
                            int minSemCode = 0;
                            //find the previous semesters credit hours
                            for (Student.Semester semesterPrevious : semesters) {
                                if (semesterPrevious.getSemesterCode() > minSemCode && semesterPrevious.getSemesterCode() < semCode) {
                                    minSemCode = semesterPrevious.getSemesterCode();
                                    creditHours = semesterPrevious.getCreditHours();
                                }
                            }
                            //add to count
                            int programNum = getProgramNum(program, semester.getProgram());
                            int yearNum = getYear(creditHours);
                            if (programNum != 4) {
                                result[yearNum][programNum][gender] += 1;
                            }
                            break;
                            //if dropout, find previous semester and add to total
                        } else if ("dropout".equals(semester.getAction())) {
                            int semCode = semester.getSemesterCode();
                            int minSemCode = 0;
                            for (Student.Semester semesterPrevious : semesters) {
                                if (semesterPrevious.getSemesterCode() > minSemCode && semesterPrevious.getSemesterCode() < semCode) {
                                    minSemCode = semesterPrevious.getSemesterCode();
                                    creditHours = semesterPrevious.getCreditHours();
                                }
                            }
                            int yearNum = getYear(creditHours);
                            result[yearNum][4][gender] += 1;
                            break;
                        }
                    }
                }
            }
        }
        //add totals
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                result[i][j][0] = (result[i][j][1] + result[i][j][2] + result[i][j][3]);
            }
        }
        return result;
    }
    //count students joining CS
    public int[][][] studentsEnteringProgram(String program, int gradYear, int studyingYear) {
        int[][][] result = new int[4][4][4];
        Iterator iterator = studentMap.entrySet().iterator();
        //for each student
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            Student thisStudent = studentMap.get(pair.getKey());
            int gender = 0;
            //determine gender
            switch (thisStudent.getGender()) {
                case 'M':
                    gender = 1;
                    break;
                case 'F':
                    gender = 2;
                    break;
                case 'U':
                    gender = 3;
                    break;
            }
            assert (gender != 0);


            boolean studiedinYear = didStudentStudyInYear(thisStudent,studyingYear,program);
            //extract semesters from student
            ArrayList<Student.Semester> semesters = thisStudent.getSemesters();
            int yearNum = 0;
            int progCode = 0;
            if (semesters != null &&studiedinYear) {
                for (Student.Semester semester : semesters) {
                    //for semester, if action and major match -> do action
                    if ("add".equals(semester.getAction()) && program.equals(semester.getProgram())
                            && isInGradYear(semesters,gradYear,program)) {
                        for (Student.Semester prevSemester : semesters) {
                            //find the matching semester and take its level
                            if (semester.getSemesterCode() == prevSemester.getSemesterCode()) {
                                if ("rem".equals(prevSemester.getAction())) {
                                    progCode = getProgramNum(program, prevSemester.getProgram());
                                }
                                if (prevSemester.getCreditHours() != null) {
                                    yearNum = getYear("L" + (Integer.parseInt(prevSemester.getCreditHours().substring(1)) - 1));
                                }
                            }
                        }
                        if (progCode != 4) {
                            //addd to result
                            result[yearNum][progCode][gender] += 1;
                        }
                    }
                }
            }
        }
        //add total
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j][0] = (result[i][j][1] + result[i][j][2] + result[i][j][3]);
            }
        }
        return result;
    }

    //check if student graduated in said year
    private boolean isInGradYear(ArrayList<Student.Semester> semesters, int year, String program){
        if(year == -1){
            return true;
        }
        for(Student.Semester semester: semesters){
            if(semester.isAction() && semester.getAction().equals("fin") && semester.getProgram().equals(program) && semester.getYear() == year){
                return true;
            }
        }
        return false;
    }


    //determine program number to place in array
    private int getProgramNum(String programSelect, String program) {
        int sosy=0;
        int csMaj=0;
        int csMnr=0;
        int csJnt=0;
        int other=0;
        if(programSelect.equals("CSMAJ")){
            sosy=0;
            csMnr=1;
            csJnt=2;
            other=3;
            csMaj=4;
        }else if(programSelect.equals("SOSY")){
            csMaj=0;
            csMnr=1;
            csJnt=2;
            other=3;
            sosy=4;
        }else if(programSelect.equals("CSMNR")){
            csMaj=0;
            sosy=1;
            csJnt=2;
            other=3;
            csMnr=4;
        }else if(programSelect.equals("CSJNT")){
            csMaj=0;
            sosy=1;
            csMnr=2;
            other=3;
            csJnt=4;
        }else if(programSelect.equals("OTHER")){
            csMaj=0;
            sosy=1;
            csMnr=2;
            csJnt=3;
            other=4;
        }
        if (program.equals("SOSY")) {
            return sosy;
        } else if (program.equals("CSMNR")) {
            return csMnr;
        } else if (program.equals("OTHER") || program.equals("HIST") || program.equals("ENSC")
                || program.equals("MSE") || program.equals("SIAT") || program.equals("Other")) {
            return other;
        } else if ("CSJNT".equals(program)) {
            return csJnt;
        }else if("CSMAJ".equals(program)){
            return csMaj;
        }
        return 4;
    }

    //determine if a student has studied in that year

    private boolean didStudentStudyInYear(Student student, int year, String program){
        for(Student.Semester semester:student.getSemesters()){
            if(semester.getYear()==year && !("admt".equals(semester.getAction()) || "fin".equals(semester.getAction())) && program.equals(semester.getProgram())){
                return true;
            }
        }
        return (year == -1);
    }

    //determine year number to place in array
    private int getYear(String creditHours) {
        if ("L1".equals(creditHours)) {
            return 0;
        }

        if (("L2".equals(creditHours) || "L3".equals(creditHours))) {
            return 1;
        }
        if (("L4".equals(creditHours) || "L5".equals(creditHours))) {
            return 2;
        }
        if (("L6".equals(creditHours) || "L7".equals(creditHours))) {
            return 3;
        }
        return 0;
    }

    //get number of students joined each year in array
    public int[] getJoiningStudents(String program, int gradYear, int studyingYear, int milestoneYear){
        int[][][] joiningResult = studentsEnteringProgram(program, gradYear, studyingYear);
        milestoneYear--;
        int[] finalResult = new int[4];
        for(int i = 0; i < 4; i++){
            finalResult[0] += joiningResult[milestoneYear][i][0];
            finalResult[1] += joiningResult[milestoneYear][i][1];
            finalResult[2] += joiningResult[milestoneYear][i][2];
            finalResult[3] += joiningResult[milestoneYear][i][3];
        }
        return finalResult;
    }


//get total admittance per milestone
    public int[] getAdmittanceTotal(String program, int gradYear, int studyingYear){
        int[][] result = studentsInProgram(program, gradYear, studyingYear);
        return result[0];
    }
}