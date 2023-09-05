package com.example.deanery.controller;

import com.example.deanery.dao.AcademicGroupsDAO;
import com.example.deanery.dao.Application;
import com.example.deanery.model.Group;
import com.example.deanery.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.HashMap;

public class TransferController {

    @FXML
    private Label currentGroupLabel;
    @FXML
    private ComboBox<Integer> targetGroupComboBox;

    private HashMap<Integer, Group> map = new HashMap<>();

    private Stage dialogStage;
    private Student student;
    private boolean okClicked = false;

    // устанавливает сцену для этого окна
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void setStudent(Student student) {
        this.student = student;
        currentGroupLabel.setText(String.valueOf(student.getGroup().getGroupNum()));
        ObservableList<Group> groupsData = FXCollections.observableArrayList();
        //  получаем список групп, исключая выпускников и ту, в которой студент уже состоит
        AcademicGroupsDAO academicGroupsDAO = new AcademicGroupsDAO(Application.INSTANCE.dataSourceStudents());
        groupsData = academicGroupsDAO.initDataForGroups(student);
        for (Group groupsDatum : groupsData) {
            map.put(groupsDatum.getGroupNum(), groupsDatum);
        }
        ObservableList<Integer> groupsNums = FXCollections.observableArrayList(map.keySet());
        targetGroupComboBox.setItems(groupsNums);
        targetGroupComboBox.getSelectionModel().select(0);
    }

    @FXML
    private void handleOk() {
        Group group = map.get(targetGroupComboBox.getSelectionModel().getSelectedItem());
        student.setGroup(group);
        okClicked = true;
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
