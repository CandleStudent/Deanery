package com.example.deanery.dao;

import javax.sql.DataSource;

import com.example.deanery.dao.studentdb.AcademicGroupsDAO;
import com.example.deanery.model.Group;
import com.mysql.cj.jdbc.MysqlDataSource;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Properties;

public enum Application {

    INSTANCE;

    private DataSource dataSourceStudents;
    private DataSource dataSourceUsers;

    //  Обновление данных в соответствии с текущим временем
    //  Обновляет данные по номеру курса, по выпускникам
    public static void updateToDate() {
        LocalDate currentDate = LocalDate.now();

        String query = """
                SELECT *
                FROM academicgroups
                WHERE IsGraduated = 0;""";

        //  Обновление для выпускающихся групп
        AcademicGroupsDAO academicGroupsDAO = new AcademicGroupsDAO(INSTANCE.dataSourceStudents());
        ObservableList<Group> groups = academicGroupsDAO.initDataForGroups(query);
        for (Group group : groups) {
            LocalDate gradDate = group.getGraduationYear();
            if (gradDate.isEqual(currentDate) || gradDate.isBefore(currentDate)) {
                academicGroupsDAO.updateGraduation(group);
            } else {
                group.updateDates();
                // in database
                academicGroupsDAO.updateTermAndSession(group);
            }
        }
    }

    public DataSource dataSourceStudents() {
        if (dataSourceStudents == null) {
            MysqlDataSource dataSource = new MysqlDataSource();
            Properties props = new Properties();
            String pathToStudentsDB = "studentsDB.properties";
            try (InputStream in = Files.newInputStream(Paths.get(pathToStudentsDB))) {
                props.load(in);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            dataSource.setUser(props.getProperty("username"));
            dataSource.setPassword(props.getProperty("password"));
            dataSource.setURL(props.getProperty("url"));
            this.dataSourceStudents = dataSource;
        }
        return dataSourceStudents;
    }

    public DataSource dataSourceUsers() {
        if (dataSourceUsers == null) {
            MysqlDataSource dataSource = new MysqlDataSource();
            Properties props = new Properties();
            String pathToStudentsDB = "userDatabase.properties";
            try (InputStream in = Files.newInputStream(Paths.get(pathToStudentsDB))) {
                props.load(in);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            dataSource.setUser(props.getProperty("username"));
            dataSource.setPassword(props.getProperty("password"));
            dataSource.setURL(props.getProperty("url"));
            this.dataSourceUsers = dataSource;
        }
        return dataSourceUsers;
    }

}
