package com.example.deanery.dao;

import com.example.deanery.model.Grade;
import com.example.deanery.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GradesDAO {
    public static ObservableList<Grade> initDataFromGrades(Student student) {
        ObservableList<Grade> data = FXCollections.observableArrayList();
        String query = "SELECT  ExamSessionNumber, ExamDate, ExamPoints, DisciplineName FROM grades JOIN disciplines WHERE grades.subjectId = disciplines.Id AND StudentId = \"" + student.getId() + "\" ORDER BY ExamSessionNumber;";
        try (Connection con = GeneralDAO.getConnection("studentsDB.properties")) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int sessionNum = rs.getInt("ExamSessionNumber");
                String discipline = rs.getString("DisciplineName");
                int points = rs.getInt("ExamPoints");
                LocalDate date = LocalDate.parse(rs.getString("ExamDate"));
                Grade grade = new Grade(student, sessionNum, date, points, discipline);
                data.add(grade);
            }
        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
        return data;
    }

    public static void addGrade(Grade grade) {
        String query = String.format("""
                        INSERT grades(StudentId, SubjectId, ExamSessionNumber, ExamDate, ExamPoints)
                        VALUES (%d, %d, %d, '%s', %d);""",
                grade.getStudent().getId(),
                grade.getDisciplineId(),
                grade.getExamSessionNum(),
                grade.getExamDate().toString(),
                grade.getExamPoints());

        try (Connection con = GeneralDAO.getConnection("studentsDB.properties")) {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public static List<Grade> getStudentGrades(Student student) {
        List<Grade> grades = new ArrayList<>();
        String query = String.format("""
                SELECT * FROM grades WHERE StudentId = %d;""",
                student.getId());
        try (Connection con = GeneralDAO.getConnection("studentsDB.properties")) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int subjectId = rs.getInt("SubjectId");
                int examSessionNum = rs.getInt("ExamSessionNumber");
                LocalDate examDate = LocalDate.parse(rs.getString("ExamDate"));
                int points = rs.getInt("ExamPoints");
                Grade grade = new Grade(student, subjectId, examSessionNum, examDate, points);
                grades.add(grade);
            }
            return grades;
        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
        return null;
    }
}
