package com.example.deanery.dao;

import com.example.deanery.model.*;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

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
