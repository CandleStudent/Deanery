package com.example.deanery.dao;

import java.util.List;

public interface IDAO<T> {
    T getById(int id);
    void addNewRecord(T t);
    void updateRecord(T t);
    void deleteRecord(T t);
    List<T> getAllRecords();
}
