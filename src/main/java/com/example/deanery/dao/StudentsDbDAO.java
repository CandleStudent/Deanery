package com.example.deanery.dao;

import com.example.deanery.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentsDbDAO extends GeneralDAO {

    public static Connection getCon() {
        return con;
    }

    public static Statement getStmt() {
        return stmt;
    }

    public static ResultSet getRs() {
        return rs;
    }

    //  Получить все данные о студентах.
    public static ObservableList<Student> initDataFromStudents(String query) throws SQLException, IOException {
        ObservableList<Student> data = FXCollections.observableArrayList();
        try (Connection con = getConnection("studentsDB.properties")) {
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
                Group group = getGroup(groupNum);
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

    public static ObservableList<Grade> initDataFromGrades(Student student) {
        ObservableList<Grade> data = FXCollections.observableArrayList();
        String query = "SELECT  ExamSessionNumber, ExamDate, ExamPoints, DisciplineName FROM grades JOIN disciplines WHERE grades.subjectId = disciplines.Id AND StudentId = \"" + student.getId() + "\" ORDER BY ExamSessionNumber;";
        try (Connection con = getConnection("studentsDB.properties")) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int sessionNum = rs.getInt("ExamSessionNumber");
                String discipline = rs.getString("DisciplineName");
                int points = rs.getInt("ExamPoints");
                LocalDate date = LocalDate.parse(rs.getString("ExamDate"));
                Grade grade = new Grade(student, sessionNum, date, points, discipline);
                data.add(grade);
            }
        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
        return data;
    }

    public static ObservableList<Group> initDataForGroups(String query) {
        ObservableList<Group> data = FXCollections.observableArrayList();
        try (Connection con = getConnection("studentsDB.properties")) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int groupNum = Integer.parseInt(rs.getString("GroupNum"));
                String direction = getDirectionName(rs.getInt("DirectionId"));
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
        try (Connection con = getConnection("studentsDB.properties")) {

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(query);

            rs.next();

            int groupNum = Integer.parseInt(rs.getString("GroupNum"));
            String direction = getDirectionName(rs.getInt("DirectionId"));
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

    public static String getDirectionName(int directionId) {
        String query = "SELECT DirectionName FROM directions WHERE DirectionId = " + directionId + ";";
        try (Connection con = getConnection("studentsDB.properties")) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            return rs.getString("DirectionName");
        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
        return null;
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
        try (Connection con = getConnection("studentsDB.properties")) {
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
        try (Connection con = getConnection("studentsDB.properties")) {
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
        try (Connection con = getConnection("studentsDB.properties")) {
            // getting Statement object to execute query
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public static String getDisciplineName(int id) {
        String query = "SELECT DisciplineName FROM disciplines WHERE Id = \"" + id + "\";";
        try (Connection con = getConnection("studentsDB.properties")) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            rs.getString("DisciplineName");
        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
        return null;
    }

    // Список экзаменационных дисциплин для конкретного направления на конкретной сессии
    public static ArrayList<Discipline> getDisciplinesAtSession(int sessionNum, int directionId) {
        ArrayList<Discipline> disciplines = new ArrayList<>();
        String query = String.format("""
                select disciplines.Id, disciplines.DisciplineName
                from sessions join disciplines
                where sessions.DirectionId = %d AND
                sessions.ExamSessionNum = %d
                AND disciplines.Id = sessions.DisciplineId;""", directionId, sessionNum);

        try (Connection con = getConnection("studentsDB.properties")) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int disciplineId = rs.getInt("disciplines.Id");
                String disciplineName = rs.getString("disciplines.DisciplineName");
                Discipline discipline = new Discipline(disciplineId, disciplineName);
                disciplines.add(discipline);
            }
        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
        return disciplines;
    }

    public static void addGrade(Grade grade) {
        String query = String.format("""
                        INSERT grades(StudentId, SubjectId, ExamSessionNumber, ExamDate, ExamPoints)
                        VALUES (%d, %d, %d, '%s', %d);""",
                grade.getStudent().getId(),
                grade.getDisciplineId(),
                grade.getExamSessionNum(),
                grade.getExamDate().toString(),
                grade.getExamPoints());

        try (Connection con = getConnection("studentsDB.properties")) {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public static List<Grade> getStudentGrades(Student student) {
        List<Grade> grades = new ArrayList<>();
        String query = String.format("""
                SELECT * FROM grades WHERE StudentId = %d;""",
                student.getId());
        try (Connection con = getConnection("studentsDB.properties")) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int subjectId = rs.getInt("SubjectId");
                int examSessionNum = rs.getInt("ExamSessionNumber");
                LocalDate examDate = LocalDate.parse(rs.getString("ExamDate"));
                int points = rs.getInt("ExamPoints");
                Grade grade = new Grade(student, subjectId, examSessionNum, examDate, points);
                grades.add(grade);
            }
            return grades;
        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
        return null;
    }

    //  Обновление данных в соответствии с текущим временем
    //  Обновляет данные по номеру курса, по выпускникам
    public static void updateToDate() {
        LocalDate currentDate = LocalDate.now();

        String query = """
                SELECT *
                FROM academicgroups
                WHERE IsGraduated = 0;""";

        //  Обновление для выпускающихся групп
        ObservableList<Group> groups = initDataForGroups(query);
        for (Group group : groups) {
            LocalDate gradDate = group.getGraduationYear();
            if (gradDate.isEqual(currentDate) || gradDate.isBefore(currentDate)) {
                updateGraduation(group);
            } else {
                group.updateDates();
                // in database
                updateTermAndSession(group);
            }
        }
    }

    private static void updateTermAndSession(Group group) {
        String query = String.format("""
                        UPDATE academicgroups
                        SET Term = %d,
                        LastSession = %d
                        WHERE GroupNum = %d;""",
                group.getTerm(),
                group.getLastSession(),
                group.getGroupNum());
        try (Connection con = getConnection("studentsDB.properties")) {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    private static void updateGraduation(Group group) {
        String query = String.format("""
                UPDATE academicgroups
                SET IsGraduation = 1, Term = NULL, LastSession = 8
                WHERE GroupNum = %d;""", group.getGroupNum());
        try (Connection con = getConnection("studentsDB.properties")) {
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
        try (Connection con = getConnection("studentsDB.properties")) {
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
                Group group = getGroup(groupNum);
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
        try (Connection con = getConnection("studentsDB.properties")) {
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
