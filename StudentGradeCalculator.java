
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

import java.awt.Font;
import java.awt.Dimension;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

public class StudentGradeCalculator
{
    public static void main(String[] args)
    {

        StudentGradeCalculator gradeCalculator = new StudentGradeCalculator();
        //Getting data from the file into an array
        String studentsData[][] = gradeCalculator.readStudentDataFromFile();

        // headings of the table
        String[] columnNames = new String[] {"Name", "HW1", "HW2", "HW3","Midterm","Project","Final","Grade"};
        gradeCalculator.populateContent(studentsData,columnNames);
    }

    //Method to read data from file
    public String[][] readStudentDataFromFile(){
        String studentGradesFileName = "student_grades_input.txt";
        int numberOfStudents = 0;
        int numberOfAssignments = 0;
        String[] studentGradeContentArray =null ;
        String[][] allStudentGradesDataArray = null;
        BufferedReader studentGradesReader = null;
        String studentGradeContent = "";
        int totalNumberOfStudents = 0;

        try {
            studentGradesReader = new BufferedReader(new FileReader(studentGradesFileName));
            // Looping for each student
            while ( (studentGradeContent = studentGradesReader.readLine()) != null && !(studentGradeContent.trim().equals("")) ) {
                numberOfStudents++;
                // Splitting the line data using the comma delimiter
                studentGradeContentArray = studentGradeContent.split(",");
            }
            numberOfAssignments = studentGradeContentArray.length;
            allStudentGradesDataArray = new String[numberOfStudents][numberOfAssignments+1];
            studentGradesReader.close();
            studentGradesReader = new BufferedReader(new FileReader(studentGradesFileName));

            while ((studentGradeContent = studentGradesReader.readLine()) != null && !(studentGradeContent.trim().equals(""))) {

       // use comma as separator
                if ( totalNumberOfStudents != 0 ) {
                    studentGradeContentArray = studentGradeContent.split(",");
                    for (int columnIndex = 0; columnIndex < studentGradeContentArray.length; columnIndex++) {
                        allStudentGradesDataArray[totalNumberOfStudents-1][columnIndex] = studentGradeContentArray[columnIndex];
                    }
                    allStudentGradesDataArray[totalNumberOfStudents-1][numberOfAssignments] =  this.calculateStudentFinalGrades(studentGradeContentArray);
                }
                totalNumberOfStudents++;
            }

        } catch (FileNotFoundException e) {
          e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            // closing the resources
            if (studentGradesReader != null) {
                try {
                    studentGradesReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return allStudentGradesDataArray;
    }

    // Using the given formulae calculating the final grade
    public String calculateStudentFinalGrades(String[] studentGradeContentArray) {
        int hw1 = Integer.parseInt(studentGradeContentArray[1]);
        int hw2 = Integer.parseInt(studentGradeContentArray[2]);
        int hw3 = Integer.parseInt(studentGradeContentArray[3]);
        int midTermMarks = Integer.parseInt(studentGradeContentArray[4]);
        int projectMarks = Integer.parseInt(studentGradeContentArray[5]);
        int finalMarks = Integer.parseInt(studentGradeContentArray[6]);

 // Applying the formulae to calculate the final grade
       Double finalNumericalGrade = (0.45 * ((hw1+hw2+hw3)/3))+(0.25*projectMarks)+ (0.30*((midTermMarks+finalMarks)/2));

        Integer finalGradeInteger = finalNumericalGrade.intValue();
        String finalGrade = null;


  if( finalGradeInteger >=0 && finalGradeInteger <=59 ) {
            finalGrade = "F";

        } else if ( finalGradeInteger >= 60 && finalGradeInteger <=69 ) {
            finalGrade = "D";
        } else if ( finalGradeInteger >=70 && finalGradeInteger <=79 ) {
            finalGrade = "C";
        } else if ( finalGradeInteger >= 80 && finalGradeInteger <=89 ) {
            finalGrade = "B";
        } else if ( finalGradeInteger >= 90 && finalGradeInteger <=100 ) {
            finalGrade = "A";
        } else {
            finalGrade = "Cannot be graded";
        }

        // returning the final grade

        return finalGrade;
    }

    // Calculate the total number of A’s,B’s,C’s,D’s and F’s
public Map calculateGradeTotals(String[][] studentGradeContentArray){

        int gradeIndex = 7;
        int numberOfAgrades = 0;
        int numberOfBgrades = 0;
        int numberOfCgrades = 0;
        int numberOfDgrades = 0;
        int numberOfFgrades = 0;

        // Iterating the studentGradeContentArray and then calculating the number of A's,B's,C's,D's and F's
        for ( int i = 0 ; i < studentGradeContentArray.length - 1; i++ ) {
            if ( studentGradeContentArray[i][gradeIndex].equals("A") ){
                numberOfAgrades++;
            } else if ( studentGradeContentArray[i][gradeIndex].equals("B") ){
                numberOfBgrades++;
            } else if ( studentGradeContentArray[i][gradeIndex].equals("C") ){
                numberOfCgrades++;
            } else if ( studentGradeContentArray[i][gradeIndex].equals("D") ){
                numberOfDgrades++;
            } else if ( studentGradeContentArray[i][gradeIndex].equals("F") ){
                numberOfFgrades++;
            }
        }

        Map gradeTotalsMap = new HashMap();
        gradeTotalsMap.put("numberOfAgrades",numberOfAgrades);
        gradeTotalsMap.put("numberOfBgrades",numberOfBgrades);
        gradeTotalsMap.put("numberOfCgrades",numberOfCgrades);
        gradeTotalsMap.put("numberOfDgrades",numberOfDgrades);
        gradeTotalsMap.put("numberOfFgrades",numberOfFgrades);

        return gradeTotalsMap;
    }



// Method to populate the content on the JFrame
    public void populateContent(String[][] fileContents, String[] columnNames){
        JTable studentGradeTable = new JTable(fileContents,columnNames);
        JTableHeader header = studentGradeTable.getTableHeader();
        header.setFont(new Font("Dialog", Font.BOLD, 17));

        JScrollPane scrollpane = new JScrollPane(studentGradeTable);
        scrollpane.setPreferredSize(new Dimension(700, 400));

        Map gradeMap = new HashMap();
        gradeMap = calculateGradeTotals(fileContents);
        String gradeTotals = " A Grades : "+gradeMap.get("numberOfAgrades")+" , "+
                " B Grades : "+gradeMap.get("numberOfBgrades")+" , "+
                " C Grades : "+gradeMap.get("numberOfCgrades")+" , "+
                " D Grades : "+gradeMap.get("numberOfDgrades")+" , "+
                " F Grades : "+gradeMap.get("numberOfFgrades");

        JLabel totalGradesLabel = new JLabel(gradeTotals);
        totalGradesLabel.setPreferredSize(new Dimension(110, 20));

        JFrame frame = new JFrame("StudentGradeCalculator");

        frame.add(scrollpane);

        frame.getContentPane().add(totalGradesLabel, java.awt.BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
