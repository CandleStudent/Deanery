package com.example.deanery.dao;

import com.example.deanery.model.Group;
import com.example.deanery.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class Students {

    private DataSource dataSource;

    public Students(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    //  Получить все данные о студентах.
    public static ObservableList<Student> initDataFromStudents(String query) throws SQLException, IOException {
        ObservableList<Student> data = FXCollections.observableArrayList();
        try (Connection con = GeneralDAO.getConnection("studentsDB.properties")) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Integer id = Integer.parseInt(rs.getString("Id"));
                String lastName = rs.getString("LastName");
                String firstName = rs.getString("FirstName");
                String patronymic = rs.getString("Patronim");
                LocalDate birthday = LocalDate.parse(rs.getString("Birthday"));
                String address = rs.getString("Address");
                int groupNum = Integer.parseInt(rs.getString("GroupNum"));
                Group group = StudentsDbDAO.getGroup(groupNum);
                String phone = rs.getString("PhoneNum");
                LocalDate admissionDate = LocalDate.parse(rs.getString("AdmissionDate"));
                String document = rs.getString("Document");
                data.add(new Student(id, lastName, firstName, patronymic, birthday, address, group, phone, admissionDate, document));
            }

        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
        return data;
    }

    public static void updateStudent(Student student) {
        String query = String.format("UPDATE students SET PhoneNum = '%s', LastName = '%s', FirstName = '%s', Patronim = '%s', GroupNum = %d, AdmissionDate = '%s', Document = '%s', Address = '%s' WHERE Id = %d;",
                student.getPhoneNum(),
                student.getLastName(),
                student.getFirstName(),
                student.getPatronim(),
                student.getGroup().getGroupNum(),
                student.getAdmissionDate().toString(),
                student.getDocumentNum(),
                student.getAddress(),
                student.getId());
        try (Connection con = GeneralDAO.getConnection("studentsDB.properties")) {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);

        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public static void addStudent(Student student) {
        String query = String.format("INSERT students(Birthday, LastName, FirstName, Patronim, GroupNum, PhoneNum, AdmissionDate, Document, Address) VALUES ('%s', '%s', '%s', '%s', %d, '%s', '%s', '%s', '%s');",
                student.getBirthday().toString(),
                student.getLastName(),
                student.getFirstName(),
                student.getPatronim(),
                student.getGroup().getGroupNum(),
                student.getPhoneNum(),
                student.getAdmissionDate().toString(),
                student.getDocumentNum(),
                student.getAddress());
        try (Connection con = GeneralDAO.getConnection("studentsDB.properties")) {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            ResultSet rs = stmt.executeQuery("SELECT Id from students ORDER BY Id DESC LIMIT 1");
            rs.next();
            student.setId(rs.getInt("Id"));

        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    // expelling the student
    // set isExpelled = true and groupNum = null
    public static void expelStudent(Student student) {
        //String query = "DELETE FROM students WHERE Id = '" + student.getId() + "';";
        String query = "UPDATE students set isExpelled = 1, GroupNum = NULL WHERE Id = '" + student.getId() + "';";
        try (Connection con = GeneralDAO.getConnection("studentsDB.properties")) {
            // getting Statement object to execute query
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public static Student getStudent(int studentId) {
        String query = String.format("""
                SELECT * FROM students
                WHERE Id = %d;""", studentId);
        try (Connection con = GeneralDAO.getConnection("studentsDB.properties")) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
                Integer id = rs.getInt("Id");
                String lastName = rs.getString("LastName");
                String firstName = rs.getString("FirstName");
                String patromin = rs.getString("Patronim");
                LocalDate birthay = LocalDate.parse(rs.getString("Birthday"));
                String address = rs.getString("Address");
                int groupNum = Integer.parseInt(rs.getString("GroupNum"));
                Group group = StudentsDbDAO.getGroup(groupNum);
                String phone = rs.getString("PhoneNum");
                LocalDate admissionDate = LocalDate.parse(rs.getString("AdmissionDate"));
                String document = rs.getString("Document");
                return new Student(id, lastName, firstName, patromin, birthay, address, group, phone, admissionDate, document);
        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
        return null;
    }

    public static boolean isPassportExist(String passport) {
        String query = String.format("""
                SELECT Document FROM students
                WHERE Document = '%s';""", passport);
        try (Connection con = GeneralDAO.getConnection("studentsDB.properties")) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                if (passport.equals(rs.getString("Document"))) {
                    return true;
                }
            }
        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
        return false;
    }
}
