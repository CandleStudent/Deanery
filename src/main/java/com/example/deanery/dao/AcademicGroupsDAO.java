package com.example.deanery.dao;

import com.example.deanery.model.Direction;
import com.example.deanery.model.Group;
import com.example.deanery.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class AcademicGroupsDAO {

    private DataSource dataSource;

    public AcademicGroupsDAO(DataSource dataSource) {
        this.dataSource = dataSource;
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
                String direction = DirectionsDAO.getDirectionName(rs.getInt("DirectionId"));
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

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return data;
    }

    public Group getGroup(int num) {
        try (Connection con = dataSource.getConnection()) {
            String query = "select * from academicgroups where GroupNum = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, num);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int groupNum = Integer.parseInt(rs.getString("GroupNum"));
            String direction = DirectionsDAO.getDirectionName(rs.getInt("DirectionId"));
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
