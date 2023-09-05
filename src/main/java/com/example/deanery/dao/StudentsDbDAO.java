package com.example.deanery.dao;

import com.example.deanery.model.*;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class StudentsDbDAO extends GeneralDAO {

    public static Connection getCon() {
        return con;
    }

    public static Statement getStmt() {
        return stmt;
    }

    public static ResultSet getRs() {
        return rs;
    }

    public static String getDisciplineName(int id) {
        String query = "SELECT DisciplineName FROM disciplines WHERE Id = \"" + id + "\";";
        try (Connection con = getConnection("studentsDB.properties")) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            rs.getString("DisciplineName");
        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
        return null;
    }

    // Список экзаменационных дисциплин для конкретного направления на конкретной сессии
    public static ArrayList<Discipline> getDisciplinesAtSession(int sessionNum, int directionId) {
        ArrayList<Discipline> disciplines = new ArrayList<>();
        String query = String.format("""
                select disciplines.Id, disciplines.DisciplineName
                from sessions join disciplines
                where sessions.DirectionId = %d AND
                sessions.ExamSessionNum = %d
                AND disciplines.Id = sessions.DisciplineId;""", directionId, sessionNum);

        try (Connection con = getConnection("studentsDB.properties")) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int disciplineId = rs.getInt("disciplines.Id");
                String disciplineName = rs.getString("disciplines.DisciplineName");
                Discipline discipline = new Discipline(disciplineId, disciplineName);
                disciplines.add(discipline);
            }
        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
        return disciplines;
    }

    //  Обновление данных в соответствии с текущим временем
    //  Обновляет данные по номеру курса, по выпускникам
    public static void updateToDate() {
        LocalDate currentDate = LocalDate.now();

        String query = """
                SELECT *
                FROM academicgroups
                WHERE IsGraduated = 0;""";

        //  Обновление для выпускающихся групп
        ObservableList<Group> groups = AcademicGroups.initDataForGroups(query);
        for (Group group : groups) {
            LocalDate gradDate = group.getGraduationYear();
            if (gradDate.isEqual(currentDate) || gradDate.isBefore(currentDate)) {
                updateGraduation(group);
            } else {
                group.updateDates();
                // in database
                updateTermAndSession(group);
            }
        }
    }

    private static void updateTermAndSession(Group group) {
        String query = String.format("""
                        UPDATE academicgroups
                        SET Term = %d,
                        LastSession = %d
                        WHERE GroupNum = %d;""",
                group.getTerm(),
                group.getLastSession(),
                group.getGroupNum());
        try (Connection con = getConnection("studentsDB.properties")) {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    private static void updateGraduation(Group group) {
        String query = String.format("""
                UPDATE academicgroups
                SET IsGraduation = 1, Term = NULL, LastSession = 8
                WHERE GroupNum = %d;""", group.getGroupNum());
        try (Connection con = getConnection("studentsDB.properties")) {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

}
