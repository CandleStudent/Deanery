package com.example.deanery.model;

public enum Direction {
    APPLIED_MATHS_AND_CS(1, "Прикладная математика и информатика"),
    APPLIED_CS(2, "Прикладная информатика"),
    INFORMATION_SECURITY(3, "Информационная безопасность");

    private final String name;
    private final int directionId;

    Direction(int directionId, String name) {
        this.directionId = directionId;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public int getDirectionId() {
        return directionId;
    }
}
