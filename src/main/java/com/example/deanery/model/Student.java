package com.example.deanery.model;

import com.example.deanery.dao.GradesDAO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Student {

    private Integer id;
    private String lastName;
    private String firstName;
    private String patronim;
    private LocalDate birthday;
    private String address;
    private Group group;
    private String phoneNum;
    private LocalDate admissionDate;

    private boolean isExpelled = false;
    private boolean isSabbatical = false;
    private boolean isGraduated = false;

    private String documentNum;

    public Student(Integer id, String lastName, String firstName, String patronim, LocalDate birthday, String address, Group group, String phoneNum, LocalDate admissionDate, String documentNum) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronim = patronim;
        this.birthday = birthday;
        this.address = address;
        this.documentNum = documentNum;
        this.group = group;
        this.phoneNum = phoneNum;
        this.admissionDate = admissionDate;
    }

    public Student() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronim() {
        return patronim;
    }

    public void setPatronim(String patronim) {
        this.patronim = patronim;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDocumentNum() {
        return documentNum;
    }

    public void setDocumentNum(String documentNum) {
        this.documentNum = documentNum;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }

    public boolean isExpelled() {
        return isExpelled;
    }

    public void setExpelled(boolean expelled) {
        isExpelled = expelled;
    }

    public boolean isSabbatical() {
        return isSabbatical;
    }

    public void setSabbatical(boolean sabbatical) {
        isSabbatical = sabbatical;
    }

    public boolean isGraduated() {
        return isGraduated;
    }

    public void setGraduated(boolean graduated) {
        isGraduated = graduated;
    }

    /**
     *
     * @return
     * -1 if the student hasn't had a session,
     * else average point for all the exams during the period of study
     */
    public double getAverage() {
        double average = 0;
        List<Grade> grades = GradesDAO.getStudentGrades(this);
        if (grades == null || grades.size() == 0) {
            return -1;
        }
        average = grades.stream().collect(Collectors.averagingDouble(Grade::getExamPoints));

        return average;
    }

    public String getFullName() {
        return lastName + " " + firstName + " " + patronim;
    }
}
