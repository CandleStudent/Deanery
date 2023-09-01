package com.example.deanery.dao;

import com.example.deanery.model.User;

import java.io.IOException;
import java.sql.*;

public class EnterDataDAO extends GeneralDAO {

    public static Connection getCon() {
        return con;
    }

    public static Statement getStmt() {
        return stmt;
    }

    public static ResultSet getRs() {
        return rs;
    }

    /**
     * Принимает логин и пароль.
     * <p>
     * @return -1 — no login in database,
     * 0 — password isn't correct,
     * 1 — all input data correct.
     */
    public static int isEnterDataCorrect(User user, String inputLogin, String inputPassword) {
        String query = "select * from enterData;";
        try (Connection con = getConnection("userDatabase.properties")) {
            ;
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String login = rs.getString("login");
                if (login.equals(inputLogin)) {
                    String password = rs.getString("pass");
                    if (password.equals(inputPassword)) {
                        int userId = rs.getInt("id");
                        user.setId(userId);
                        user.setRoleId(getRole(userId));
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
        return -1;
    }

    /**
    * @return 1 - administrator,
    * 2 - student,
     * -1 - something wrong
     */
    public static int getRole(int userId) {
        String query = String.format("""
                        SELECT * FROM users WHERE id = %d;""",
                userId);
        try (Connection con = getConnection("userDatabase.properties")) {
            ;
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            rs.next();
            return rs.getInt("roleId");
        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
        return -1;
    }

    public static int getStudentId(int userId) {
        String query = String.format("""
                        SELECT * FROM studentsusers WHERE userId = %d;""",
                userId);
        try (Connection con = getConnection("userDatabase.properties")) {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            rs.next();
            return rs.getInt("studentId");
        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
        }
        return -1;
    }
}
