package com.example.deanery.model;

import com.example.deanery.dao.Application;
import com.example.deanery.dao.DisciplinesDAO;

public class Discipline {

    private int id;
    private String name;

    public Discipline(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Discipline(int id) {
        this.id = id;
        DisciplinesDAO disciplinesDAO = new DisciplinesDAO(Application.INSTANCE.dataSourceStudents());
        this.name = disciplinesDAO.getNameById(id);
    }

    public Discipline() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        DisciplinesDAO disciplinesDAO = new DisciplinesDAO(Application.INSTANCE.dataSourceStudents());
        this.name = disciplinesDAO.getNameById(id);
    }

    public String getName() {
        return name;
    }
}
