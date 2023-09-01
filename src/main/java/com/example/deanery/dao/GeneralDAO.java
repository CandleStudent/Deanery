package com.example.deanery.dao;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public abstract class GeneralDAO {

    // JDBC variables for opening and managing connection
    protected static Connection con;
    protected static Statement stmt;
    protected static ResultSet rs;

    public static Connection getCon() {
        return con;
    }

    public static Statement getStmt() {
        return stmt;
    }

    public static ResultSet getRs() {
        return rs;
    }

    protected static Connection getConnection(String pathToBase) throws SQLException, IOException {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get(pathToBase))) {
            props.load(in);
        }
        String url = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");

        return DriverManager.getConnection(url, username, password);
    }

}
