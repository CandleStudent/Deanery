package com.example.deanery.controller;

import com.example.deanery.dao.DisciplinesDAO;
import com.example.deanery.model.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class EditGradeController extends EditController {

    //  хранит ноды gridPane для получения данных с TextField позже
    ObservableList<Node> childrens;
    @FXML
    private Label titleLabel;
    @FXML
    private GridPane gridPane;
    @FXML
    private BorderPane borderPane;

    private Student student;
    private ArrayList<Grade> grades;
    private ArrayList<Discipline> disciplines;
    private int examSessionNum;

    public void setExamSessionNum(int examSessionNum) {
        this.examSessionNum = examSessionNum;
    }

    public void setStudent(Student student) {
        this.student = student;

        gridPane = new GridPane();
        int directionId = student.getGroup().getDirection().getDirectionId();
        titleLabel.setText("Данные по сессии №" + examSessionNum);

        disciplines = DisciplinesDAO.getDisciplinesAtSession(examSessionNum, directionId);
        gridPane.addColumn(0);
        gridPane.addColumn(1);
        gridPane.addColumn(2);
        for (int i = 0; i < disciplines.size(); i++) {
            Label disciplineName = new Label();
            disciplineName.setText(disciplines.get(i).getName());
            TextField disciplinePoints = new TextField();
            TextField examDate = new TextField();
            gridPane.addRow(i, disciplineName, disciplinePoints, examDate);
//            gridPane.add(disciplineName, 0, i);
//            gridPane.add(disciplinePoints, 1, i);
//            gridPane.add(examDate, 2, i);
        }
        borderPane.setCenter(gridPane);
        childrens = gridPane.getChildren();
    }

    public void setGrades(ArrayList<Grade> grades) {
        this.grades = grades;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            for (int i = 0; i < gridPane.getRowCount(); i++) {
                Label disciplineNameLabel = (Label) getNodeByRowColumnIndex(i, 0);
                TextField disicplinePointsField = (TextField) getNodeByRowColumnIndex(i, 1);
                TextField examDateField = (TextField) getNodeByRowColumnIndex(i, 2);
                int disciplineId = getDisciplineIdByName(disciplineNameLabel.getText());
                int disciplinePoints = Integer.parseInt(disicplinePointsField.getText());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate examDate = LocalDate.parse(examDateField.getText(), formatter);
                Grade grade = new Grade(student, disciplineId, examSessionNum, examDate, disciplinePoints);
                grades.add(grade);
            }

            okClicked = true;
            dialogStage.close();
        }
    }

    private int getDisciplineIdByName(String name) {
        for (Discipline discipline : disciplines) {
            if (discipline.getName().equals(name)) {
                return discipline.getId();
            }
        }
        return -1;
    }

    private boolean isInputValid() {
        String errorMessage = "";

        String numRegex = "[0-9]+";
        for (int i = 0; i < gridPane.getRowCount(); i++) {

            Label nameLabel = (Label) getNodeByRowColumnIndex(i, 0);
            String name = nameLabel.getText();
            TextField pointsField = (TextField) getNodeByRowColumnIndex(i, 1);
            TextField dateField = (TextField) getNodeByRowColumnIndex(i, 2);

            if (pointsField.getText() == null
                    || !pointsField.getText().matches(numRegex)
                    || pointsField.getText().length() == 0
                    || Integer.parseInt(pointsField.getText()) > 100) {
                errorMessage = name + ": Количество баллов введено некорректно. Проверьте, все ли символы являются цифрами и выполняется ли условие, что введенное число меньше ста";
            }

            String dateRegex = "^\\d{2}-\\d{2}-\\d{4}$";
            if (!dateField.getText().matches(dateRegex)) {
                errorMessage += "Дата экзамена " + name + " введена неправильно";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Некорректные поля");
            alert.setHeaderText("Внесите корректную информацию");
            alert.setContentText(errorMessage);
            alert.setResizable(true);
            alert.showAndWait();

            return false;
        }
    }

    public Node getNodeByRowColumnIndex(final int row, final int column) {
        Node result = null;
        for (Node node : childrens) {
            if (gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }
}
