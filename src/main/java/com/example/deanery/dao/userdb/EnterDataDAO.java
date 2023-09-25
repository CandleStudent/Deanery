package com.example.deanery.dao.userdb;

import com.example.deanery.model.User;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;

public class EnterDataDAO {

    private DataSource dataSource;

    public EnterDataDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Принимает логин и пароль.
     * <p>
     *
     * @return -1 — no login in database,
     * 0 — password isn't correct,
     * 1 — all input data correct.
     */
    public int isEnterDataCorrect(User user, String inputLogin, String inputPassword) {
        try (Connection con = dataSource.getConnection()) {
            String query = "SELECT EXISTS(select * from enterdata where login = ?) as isExists";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, inputLogin);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            boolean isLoginExists = rs.getBoolean("isExists");
            if (isLoginExists) {
                query = "select * from enterdata where login = ?";
                stmt = con.prepareStatement(query);
                stmt.setString(1, inputLogin);
                rs = stmt.executeQuery();
                rs.next();
                String password = rs.getString("pass");
                if (password.equals(inputPassword)) {
                    int userId = rs.getInt("id");
                    user.setId(userId);
                    user.setRoleId(getRole(userId));
                    return 1;
                } else {
                    return 0;
                }
            } else {
                return -1;
            }

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return -1;
    }

    /**
     * @return 1 - administrator,
     * 2 - student,
     * -1 - something wrong
     */
    public int getRole(int userId) {
        try (Connection con = dataSource.getConnection()) {
            String query = "SELECT * FROM users WHERE id = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt("roleId");
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return -1;
    }

    public int getStudentId(int userId) {
        try (Connection con = dataSource.getConnection()) {
            String query = "SELECT * FROM studentsusers WHERE userId = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt("studentId");
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return -1;
    }
}
