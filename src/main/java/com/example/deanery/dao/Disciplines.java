package com.example.deanery.dao;

import com.example.deanery.model.Discipline;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Disciplines {
    public static String getDisciplineName(int id) {
        String query = "SELECT DisciplineName FROM disciplines WHERE Id = \"" + id + "\";";
        try (Connection con = GeneralDAO.getConnection("studentsDB.properties")) {
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

        try (Connection con = GeneralDAO.getConnection("studentsDB.properties")) {
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
}
