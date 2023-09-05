package com.example.deanery.model;

import com.example.deanery.dao.Disciplines;

public class Discipline {

    private int id;
    private String name;

    public Discipline(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Discipline(int id) {
        this.id = id;
        this.name = Disciplines.getDisciplineName(id);
    }

    public Discipline() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        this.name = Disciplines.getDisciplineName(id);
    }

    public String getName() {
        return name;
    }
}
