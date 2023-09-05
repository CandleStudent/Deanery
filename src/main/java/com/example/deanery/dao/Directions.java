package com.example.deanery.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Directions {
    public static String getDirectionName(int directionId) {
        String query = "SELECT DirectionName FROM directions WHERE DirectionId = " + directionId + ";";
        try (Connection con = GeneralDAO.getConnection("studentsDB.properties")) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            return rs.getString("DirectionName");
        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
        return null;
    }
}
