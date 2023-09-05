package com.example.deanery.dao;

import com.example.deanery.model.Direction;
import com.example.deanery.model.Group;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class AcademicGroupsDAO {
    public static ObservableList<Group> initDataForGroups(String query) {
        ObservableList<Group> data = FXCollections.observableArrayList();
        try (Connection con = GeneralDAO.getConnection("studentsDB.properties")) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

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

        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
        return data;
    }

    public static Group getGroup(int num) {
        String query = "select * from academicgroups where GroupNum = '" + num + "';";
        try (Connection con = GeneralDAO.getConnection("studentsDB.properties")) {

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(query);

            rs.next();

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
            return new Group(groupNum, direction1, term, graduationDate, startDate, lastSession);

        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
        return null;
    }
}
