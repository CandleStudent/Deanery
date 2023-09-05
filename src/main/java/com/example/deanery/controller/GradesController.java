package com.example.deanery.controller;

import com.example.deanery.dao.Grades;
import com.example.deanery.model.Grade;
import com.example.deanery.model.Student;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GradesController {

    @FXML
    private Button inputNewResultsButton;
    private ObservableList<Grade> sessionData = FXCollections.observableArrayList();
    @FXML
    private Label nameLabel;
    @FXML
    private TableView<Grade> sessionTable;
    @FXML
    private TableColumn<Grade, Integer> sessionColumn;
    @FXML
    private TableColumn<Grade, String> disciplineColumn;
    @FXML
    private TableColumn<Grade, Integer> pointsColumn;
    @FXML
    private TableColumn<Grade, String> dateColumn;

    private Stage dialogStage;
    private Student student;

    // устанавливает сцену для этого окна
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setStudent(Student student) {
        this.student = student;
        nameLabel.setText(student.getFullName());
        initData();
    }

    private void initData() {
        sessionTable.getItems().clear();
        sessionData = Grades.initDataFromGrades(this.student);
        sessionTable.setItems(sessionData);

        sessionColumn.setCellValueFactory(column -> new SimpleObjectProperty<>(column.getValue().getExamSessionNum()));
        disciplineColumn.setCellValueFactory(column -> new SimpleObjectProperty<>(column.getValue().getDisciplineName()));
        pointsColumn.setCellValueFactory(column -> new SimpleObjectProperty<>(column.getValue().getExamPoints()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        dateColumn.setCellValueFactory(column -> new ReadOnlyStringWrapper(formatter.format(column.getValue().getExamDate())));
    }

    @FXML
    private void editInputGradesHandle() {
        ObservableList<Grade> previousGrades = Grades.initDataFromGrades(student);
        int lastSession;
        if (previousGrades.size() != 0) {
           lastSession = previousGrades.get(previousGrades.size() - 1).getExamSessionNum();
        } else {
            lastSession = 0;
        }
        int examSessionNum = lastSession + 1;

        // все возможные на данный момент сессии уже заполнены
        if (!(lastSession < student.getGroup().getLastSession())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(null);
            alert.setTitle("Все заполнено");
            alert.setHeaderText("Все данные по успеваемости уже заполнены");
            alert.setContentText("Подождите до следующей сессии");
            alert.showAndWait();
        } else {
            ArrayList<Grade> grades = new ArrayList<>();
            boolean okClicked = showInputGradesForm(grades, examSessionNum);
            if (okClicked) {
                // database update
                for (Grade grade : grades) {
                    Grades.addGrade(grade);
                }
                //tableView update
                initData();
            }
        }
    }

    private boolean showInputGradesForm(ArrayList<Grade> grades, int examSessionNum) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = Paths.get("src\\main\\resources\\com\\example\\deanery\\EditGrade.fxml").toUri().toURL();
            loader.setLocation(url);
            AnchorPane page = (AnchorPane) loader.load();

            // создаем диалоговое окно Stage
            Stage dialogStage = new Stage();
            URL imgUrl = Paths.get("src\\main\\resources\\com\\example\\deanery\\img\\symbol.png").toUri().toURL();
            dialogStage.getIcons().add(new Image(String.valueOf(imgUrl)));
            dialogStage.setTitle(student.getFullName());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(null);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // передаем адресата в контроллер
            EditGradeController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setExamSessionNum(examSessionNum);
            controller.setGrades(grades);
            controller.setStudent(student);

            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void removeInputNewResultsButton() {
        inputNewResultsButton.setDisable(true);
        inputNewResultsButton.setVisible(false);
    }
}
