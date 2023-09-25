package com.example.deanery.dao.studentdb;

import com.example.deanery.model.Grade;
import com.example.deanery.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GradesDAO {

    private DataSource dataSource;

    public GradesDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ObservableList<Grade> initDataFromGrades(Student student) {
        ObservableList<Grade> data = FXCollections.observableArrayList();
        try (Connection con = dataSource.getConnection()) {
            String query = "SELECT  ExamSessionNumber, ExamDate, ExamPoints, DisciplineName " +
                    "FROM grades " +
                    "JOIN disciplines " +
                    "WHERE grades.subjectId = disciplines.Id " +
                    "AND StudentId = ? ORDER BY ExamSessionNumber;";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, student.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int sessionNum = rs.getInt("ExamSessionNumber");
                String discipline = rs.getString("DisciplineName");
                int points = rs.getInt("ExamPoints");
                LocalDate date = LocalDate.parse(rs.getString("ExamDate"));
                Grade grade = new Grade(student, sessionNum, date, points, discipline);
                data.add(grade);
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return data;
    }

    public void addGrade(Grade grade) {
        try (Connection con = dataSource.getConnection()) {
            String query = """
                        INSERT grades(StudentId, SubjectId, ExamSessionNumber, ExamDate, ExamPoints)
                        VALUES (?, ?, ?, ?, ?)""";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, grade.getStudent().getId());
            stmt.setInt(2, grade.getDisciplineId());
            stmt.setInt(3, grade.getExamSessionNum());
            stmt.setString(4, grade.getExamDate().toString());
            stmt.setInt(5, grade.getExamPoints());
            stmt.executeUpdate();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public List<Grade> getStudentGrades(Student student) {
        List<Grade> grades = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            String query = """
                SELECT * FROM grades WHERE StudentId = ?""";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, student.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int subjectId = rs.getInt("SubjectId");
                int examSessionNum = rs.getInt("ExamSessionNumber");
                LocalDate examDate = LocalDate.parse(rs.getString("ExamDate"));
                int points = rs.getInt("ExamPoints");
                Grade grade = new Grade(student, subjectId, examSessionNum, examDate, points);
                grades.add(grade);
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return grades;
    }
}
