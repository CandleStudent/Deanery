package com.example.deanery.dao;

import com.example.deanery.model.Discipline;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class DisciplinesDAO {

    private DataSource dataSource;

    public DisciplinesDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getDisciplineName(int id) {
        try (Connection con = dataSource.getConnection()) {
            String query = "SELECT DisciplineName FROM disciplines WHERE Id = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getString("DisciplineName");
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return null;
    }

    // Список экзаменационных дисциплин для конкретного направления на конкретной сессии
    public ArrayList<Discipline> getDisciplinesAtSession(int sessionNum, int directionId) {
        ArrayList<Discipline> disciplines = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            String query = String.format("""
                select disciplines.Id, disciplines.DisciplineName
                from sessions join disciplines
                where sessions.DirectionId = ? AND
                sessions.ExamSessionNum = ?
                AND disciplines.Id = sessions.DisciplineId;""");
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, directionId);
            stmt.setInt(2, sessionNum);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int disciplineId = rs.getInt("disciplines.Id");
                String disciplineName = rs.getString("disciplines.DisciplineName");
                Discipline discipline = new Discipline(disciplineId, disciplineName);
                disciplines.add(discipline);
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return disciplines;
    }
}
