package com.example.deanery.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Group {

    private final int groupNum;
    private ArrayList<Student> students = new ArrayList<>();
    private Direction direction;
    private int term;
    private LocalDate graduationYear;
    private LocalDate startDate;
    private int lastSession;

    public Group(int groupNum) {
        this.groupNum = groupNum;
    }

    public Group(int groupNum, Direction direction, int term, LocalDate graduationYear) {
        this.groupNum = groupNum;
        this.direction = direction;
        this.term = term;
        this.graduationYear = graduationYear;
    }

    public Group(int groupNum, Direction direction, int term, LocalDate graduationYear, LocalDate startDate) {
        this.groupNum = groupNum;
        this.direction = direction;
        this.term = term;
        this.graduationYear = graduationYear;
        this.startDate = startDate;
    }

    public Group(int groupNum, Direction direction, int term, LocalDate graduationYear, LocalDate startDate, int lastSession) {
        this.groupNum = groupNum;
        this.direction = direction;
        this.term = term;
        this.graduationYear = graduationYear;
        this.startDate = startDate;
        this.lastSession = lastSession;
    }

    public int getLastSession() {
        return lastSession;
    }

    public void setLastSession(int lastSession) {
        this.lastSession = lastSession;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getGroupNum() {
        return groupNum;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public LocalDate getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(LocalDate graduationYear) {
        this.graduationYear = graduationYear;
    }

    /**
     * По текущей дате определяется, сколько сессий было у данной группы
     * @param currentDate
     * дата, относительно которой рассчитывается количество сессий данной группы
     */
    private void setNewLastSession(LocalDate currentDate) {
        int currLastSession = (term - 1) * 2;
        if (currentDate.getMonthValue() < 7) {
            currLastSession++;
        }
        setLastSession(currLastSession);
    }

    /**
     * по текущей дате определяет, на каком курсе сейчас находится группа
     * @param currentDate
     * дата, относительно которой рассчитывается количество сессий данной группы
     */
    private void setCurrentTerm(LocalDate currentDate) {
        int currTerm = currentDate.getYear() - startDate.getYear();
        if (currentDate.getMonthValue() > 6) {
            currTerm++;
        }
        setTerm(currTerm);
    }

    /**
     *  обновляет данные по курсу и по последней сессии в зависимости от текущего времени
     */
    public void updateDates() {
        LocalDate currentDate = LocalDate.now();
        setCurrentTerm(currentDate);
        setNewLastSession(currentDate);
    }
}
