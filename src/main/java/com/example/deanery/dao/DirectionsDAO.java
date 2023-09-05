package com.example.deanery.dao;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;

public class DirectionsDAO {

    private DataSource dataSource;

    public DirectionsDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getDirectionName(int directionId) {
        try (Connection con = dataSource.getConnection()) {
            String query = "SELECT DirectionName FROM directions WHERE DirectionId = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, directionId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getString("DirectionName");
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return null;
    }
}
