package com.example.deanery.dao;

import com.example.deanery.model.*;
import javafx.collections.FXCollections;
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

    public static ObservableList<Group> initDataForGroups(String query) {
        ObservableList<Group> data = FXCollections.observableArrayList();
        try (Connection con = getConnection("studentsDB.properties")) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int groupNum = Integer.parseInt(rs.getString("GroupNum"));
                String direction = getDirectionName(rs.getInt("DirectionId"));
                Direction direction1 = Direction.APPLIED_MATHS_AND_CS;
                if (direction.equals("Прикладная математика и информатика")) {
                    direction1 = Direction.APPLIED_MATHS_AND_CS;
                } else if (direction.equals("Прикладная информатика")) {
                    direction1 = Direction.APPLIED_CS;
                } else if (direction.equals("Информационная безопасность")) {
                    direction1 = Direction.INFORMATION_SECURITY;
                }
                int term = Integer.parseInt(rs.getString("Term"));
                LocalDate graduationDate = LocalDate.parse(rs.getString("GraduationYear"));
                LocalDate startDate = LocalDate.parse(rs.getString("StartDate"));
                int lastSession = rs.getInt("LastSession");
                data.add(new Group(groupNum, direction1, term, graduationDate, startDate, lastSession));
            }

        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
        return data;
    }

    public static Group getGroup(int num) {
        String query = "select * from academicgroups where GroupNum = '" + num + "';";
        try (Connection con = getConnection("studentsDB.properties")) {

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(query);

            rs.next();

            int groupNum = Integer.parseInt(rs.getString("GroupNum"));
            String direction = getDirectionName(rs.getInt("DirectionId"));
            Direction direction1 = Direction.APPLIED_MATHS_AND_CS;
            if (direction.equals("Прикладная математика и информатика")) {
                direction1 = Direction.APPLIED_MATHS_AND_CS;
            } else if (direction.equals("Прикладная информатика")) {
                direction1 = Direction.APPLIED_CS;
            } else if (direction.equals("Информационная безопасность")) {
                direction1 = Direction.INFORMATION_SECURITY;
            }
            int term = Integer.parseInt(rs.getString("Term"));
            LocalDate graduationDate = LocalDate.parse(rs.getString("GraduationYear"));
            LocalDate startDate = LocalDate.parse(rs.getString("StartDate"));
            int lastSession = rs.getInt("LastSession");
            return new Group(groupNum, direction1, term, graduationDate, startDate, lastSession);

        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
        return null;
    }

    public static String getDirectionName(int directionId) {
        String query = "SELECT DirectionName FROM directions WHERE DirectionId = " + directionId + ";";
        try (Connection con = getConnection("studentsDB.properties")) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            return rs.getString("DirectionName");
        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
        return null;
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
        ObservableList<Group> groups = initDataForGroups(query);
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
