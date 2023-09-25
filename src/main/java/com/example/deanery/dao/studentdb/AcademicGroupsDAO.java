package com.example.deanery.dao.studentdb;

import com.example.deanery.dao.Application;
import com.example.deanery.model.Direction;
import com.example.deanery.model.Group;
import com.example.deanery.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;

public class AcademicGroupsDAO {

    private DataSource dataSource;

    public AcademicGroupsDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void updateTermAndSession(Group group) {
        try (Connection con = dataSource.getConnection()) {
            String query = """
                        UPDATE academicgroups
                        SET Term = ?,
                        LastSession = ?
                        WHERE GroupNum = ?""";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, group.getTerm());
            stmt.setInt(1, group.getLastSession());
            stmt.setInt(3, group.getGroupNum());
            stmt.executeUpdate();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public void updateGraduation(Group group) {
        try (Connection con = dataSource.getConnection()) {
            String query = """
                UPDATE academicgroups
                SET IsGraduation = 1, Term = NULL, LastSession = 8
                WHERE GroupNum = ?""";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, group.getGroupNum());
            stmt.executeUpdate();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public ObservableList<Group> initDataForGroups(Student student) {
        ObservableList<Group> data = FXCollections.observableArrayList();
        try (Connection con = dataSource.getConnection()) {
            String query = "SELECT * FROM academicgroups " +
                    "WHERE IsGraduated = 0 " +
                    "AND GroupNum != ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, student.getGroup().getGroupNum());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int groupNum = Integer.parseInt(rs.getString("GroupNum"));
                DirectionsDAO directionsDAO = new DirectionsDAO(Application.INSTANCE.dataSourceStudents());
                String direction = directionsDAO.getNameById(rs.getInt("DirectionId"));
                Direction direction1 = Direction.APPLIED_MATHS_AND_CS;
                if (direction.equals("Прикладная информатика")) {
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

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return data;
    }

    public ObservableList<Group> initDataForGroupsInApplication() {
        ObservableList<Group> data = FXCollections.observableArrayList();
        try (Connection con = dataSource.getConnection()) {
            String query = """
                SELECT *
                FROM academicgroups
                WHERE IsGraduated = 0;""";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int groupNum = Integer.parseInt(rs.getString("GroupNum"));
                DirectionsDAO directionsDAO = new DirectionsDAO(Application.INSTANCE.dataSourceStudents());
                String direction = directionsDAO.getNameById(rs.getInt("DirectionId"));
                Direction direction1 = Direction.APPLIED_MATHS_AND_CS;
                if (direction.equals("Прикладная информатика")) {
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

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return data;
    }

    public Group getByNumber(int num) {
        try (Connection con = dataSource.getConnection()) {
            String query = "select * from academicgroups where GroupNum = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, num);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int groupNum = Integer.parseInt(rs.getString("GroupNum"));
            DirectionsDAO directionsDAO = new DirectionsDAO(Application.INSTANCE.dataSourceStudents());
            String direction = directionsDAO.getNameById(rs.getInt("DirectionId"));
            Direction direction1 = Direction.APPLIED_MATHS_AND_CS;
            if (direction.equals("Прикладная информатика")) {
                direction1 = Direction.APPLIED_CS;
            } else if (direction.equals("Информационная безопасность")) {
                direction1 = Direction.INFORMATION_SECURITY;
            }
            int term = Integer.parseInt(rs.getString("Term"));
            LocalDate graduationDate = LocalDate.parse(rs.getString("GraduationYear"));
            LocalDate startDate = LocalDate.parse(rs.getString("StartDate"));
            int lastSession = rs.getInt("LastSession");
            return new Group(groupNum, direction1, term, graduationDate, startDate, lastSession);

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return null;
    }
}
