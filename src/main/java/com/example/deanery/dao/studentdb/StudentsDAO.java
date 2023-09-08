package com.example.deanery.dao.studentdb;

import com.example.deanery.dao.Application;
import com.example.deanery.model.Group;
import com.example.deanery.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class StudentsDAO {

    private DataSource dataSource;

    public StudentsDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    //  Получить все данные о студентах.
    public ObservableList<Student> initDataFromStudents() throws SQLException, IOException {
        ObservableList<Student> data = FXCollections.observableArrayList();
        try (Connection con = dataSource.getConnection()) {
            String query = """
                    SELECT * FROM students JOIN academicgroups 
                    WHERE students.isExpelled = 0 
                    AND academicgroups.IsGraduated = 0
                    AND students.GroupNum = academicgroups.GroupNum ORDER BY students.Id;""";
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
                AcademicGroupsDAO academicGroupsDAO = new AcademicGroupsDAO(Application.INSTANCE.dataSourceStudents());
                Group group = academicGroupsDAO.getByNumber(groupNum);
                String phone = rs.getString("PhoneNum");
                LocalDate admissionDate = LocalDate.parse(rs.getString("AdmissionDate"));
                String document = rs.getString("Document");
                data.add(new Student(id, lastName, firstName, patronymic, birthday, address, group, phone, admissionDate, document));
            }

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return data;
    }

    public void updateStudent(Student student) {
        try (Connection con = dataSource.getConnection()) {
            String query = "UPDATE students " +
                            "SET PhoneNum = ?, LastName = ?, FirstName = ?, Patronim = ?, GroupNum = ?, AdmissionDate = ?, Document = ?, Address = ? " +
                            "WHERE Id = %d";

            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, student.getPhoneNum());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getFirstName());
            stmt.setString(4, student.getPatronim());
            stmt.setInt(5, student.getGroup().getGroupNum());
            stmt.setString(6, student.getAdmissionDate().toString());
            stmt.setString(7, student.getDocumentNum());
            stmt.setString(8, student.getAddress());
            stmt.setInt(9, student.getId());
            stmt.executeUpdate();

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public void addStudent(Student student) {
        try (Connection con = dataSource.getConnection()) {
            String query = "INSERT students(Birthday, LastName, FirstName, Patronim, GroupNum, PhoneNum, AdmissionDate, Document, Address) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement stmt = con.prepareStatement(query);

            stmt.setString(1, student.getBirthday().toString());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getFirstName());
            stmt.setString(4, student.getPatronim());
            stmt.setInt(5, student.getGroup().getGroupNum());
            stmt.setString(6, student.getPhoneNum());
            stmt.setString(7, student.getAdmissionDate().toString());
            stmt.setString(8, student.getDocumentNum());
            stmt.setString(9, student.getAddress());

            stmt.executeUpdate();
            ResultSet rs = stmt.executeQuery("SELECT Id from students ORDER BY Id DESC LIMIT 1");
            rs.next();
            student.setId(rs.getInt("Id"));
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    // expelling the student
    // set isExpelled = true and groupNum = null
    public void expelStudent(Student student) {
        try (Connection con = dataSource.getConnection()) {
            String query = "UPDATE students set isExpelled = 1, GroupNum = NULL WHERE Id = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, student.getId());
            stmt.executeUpdate();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public Student getById(int studentId) {
        try (Connection con = dataSource.getConnection()) {
            String query = """
                    SELECT * FROM students
                    WHERE Id = ?""";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            Integer id = rs.getInt("Id");
            String lastName = rs.getString("LastName");
            String firstName = rs.getString("FirstName");
            String patromin = rs.getString("Patronim");
            LocalDate birthay = LocalDate.parse(rs.getString("Birthday"));
            String address = rs.getString("Address");
            int groupNum = Integer.parseInt(rs.getString("GroupNum"));
            AcademicGroupsDAO academicGroupsDAO = new AcademicGroupsDAO(Application.INSTANCE.dataSourceStudents());
            Group group = academicGroupsDAO.getByNumber(groupNum);
            String phone = rs.getString("PhoneNum");
            LocalDate admissionDate = LocalDate.parse(rs.getString("AdmissionDate"));
            String document = rs.getString("Document");
            return new Student(id, lastName, firstName, patromin, birthay, address, group, phone, admissionDate, document);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return null;
    }

    public boolean isPassportExist(String passport) {
        try (Connection con = dataSource.getConnection()) {
            String query = """
                    SELECT EXISTS(select Document FROM students
                    WHERE Document = ?) as isExists""";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, passport);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getBoolean("isExists");
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return false;
    }
}
