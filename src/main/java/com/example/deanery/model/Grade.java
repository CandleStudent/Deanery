package com.example.deanery.model;

import com.example.deanery.dao.Application;
import com.example.deanery.dao.studentdb.DisciplinesDAO;

import java.time.LocalDate;

public class Grade {

    private Student student;
    private int studentId;
    private int disciplineId;
    private int examSessionNum;
    private LocalDate examDate;
    private int examPoints;
    private String disciplineName;

    public Grade() {
    }

    public Grade(Student student, int disciplineId, int examSessionNum, LocalDate examDate, int examPoints) {
        this.student = student;
        this.disciplineId = disciplineId;
        this.examSessionNum = examSessionNum;
        this.examDate = examDate;
        this.examPoints = examPoints;
        DisciplinesDAO disciplinesDAO = new DisciplinesDAO(Application.INSTANCE.dataSourceStudents());
        this.disciplineName = disciplinesDAO.getNameById(disciplineId);
    }

    public Grade(Student student, int examSessionNum, LocalDate examDate, int examPoints, String disciplineName) {
        this.student = student;
        this.examSessionNum = examSessionNum;
        this.examDate = examDate;
        this.examPoints = examPoints;
        this.disciplineName = disciplineName;
    }

    public Grade(int studentId, int disciplineId, int examSessionNum, LocalDate examDate, int examPoints) {
        this.studentId = studentId;
        this.disciplineId = disciplineId;
        this.examSessionNum = examSessionNum;
        this.examDate = examDate;
        this.examPoints = examPoints;
        DisciplinesDAO disciplinesDAO = new DisciplinesDAO(Application.INSTANCE.dataSourceStudents());
        this.disciplineName = disciplinesDAO.getNameById(disciplineId);
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(int disciplineId) {
        this.disciplineId = disciplineId;
    }

    public int getExamSessionNum() {
        return examSessionNum;
    }

    public void setExamSessionNum(int examSessionNum) {
        this.examSessionNum = examSessionNum;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public int getExamPoints() {
        return examPoints;
    }

    public void setExamPoints(int examPoints) {
        this.examPoints = examPoints;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }
}
