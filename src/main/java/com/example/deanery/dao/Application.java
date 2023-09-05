package com.example.deanery.dao;

import javax.sql.DataSource;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public enum Application {

    INSTANCE;

    private DataSource dataSourceStudents;
    private DataSource dataSourceUsers;

    public DataSource dataSourceStudents() throws IOException {
        if (dataSourceStudents == null) {
            MysqlDataSource dataSource = new MysqlDataSource();
            Properties props = new Properties();
            String pathToStudentsDB = "studentsDB.properties";
            try (InputStream in = Files.newInputStream(Paths.get(pathToStudentsDB))) {
                props.load(in);
            }
            dataSource.setUser(props.getProperty("username"));
            dataSource.setPassword(props.getProperty("password"));
            dataSource.setURL(props.getProperty("url"));
            this.dataSourceStudents = dataSource;
        }
        return dataSourceStudents;
    }

    public DataSource dataSourceUsers() throws IOException {
        if (dataSourceUsers == null) {
            MysqlDataSource dataSource = new MysqlDataSource();
            Properties props = new Properties();
            String pathToStudentsDB = "userDatabase.properties";
            try (InputStream in = Files.newInputStream(Paths.get(pathToStudentsDB))) {
                props.load(in);
            }
            dataSource.setUser(props.getProperty("username"));
            dataSource.setPassword(props.getProperty("password"));
            dataSource.setURL(props.getProperty("url"));
            this.dataSourceUsers = dataSource;
        }
        return dataSourceStudents;
    }

}
