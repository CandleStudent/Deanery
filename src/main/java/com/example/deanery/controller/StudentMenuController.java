package com.example.deanery.controller;

import com.example.deanery.model.Student;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.text.DecimalFormat;

public class StudentMenuController {


    @FXML
    private Label studentIdLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label patronimLabel;
    @FXML
    private Label birthdayLabel;
    @FXML
    private Label groupLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label admissionDateLabel;
    @FXML
    private Label passportLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label directionLabel;
    @FXML
    private Hyperlink gradesHyperlink;
    @FXML
    private Label averageLabel;
    @FXML
    private Label termLabel;

    private Student student = new Student();
    private Stage stage;

    @FXML
    private MenuBar menuBar;
    @FXML
    private Label titleLabel;

    @FXML
    private void handleCloseApp() {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleExit() {
        try {
            Stage enterStage = new Stage();
            enterStage.setTitle("Вход");
            URL url = Paths.get("src\\main\\resources\\com\\example\\deanery\\Enter.fxml").toUri().toURL();
            Parent root = FXMLLoader.load(url);
            Scene regScene = new Scene(root, 600, 400);
            enterStage.setScene(regScene);
            //  closing previous scene
            Stage stage = (Stage) menuBar.getScene().getWindow();
            stage.close();
            // \closing
            enterStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleShowHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Помощь");
        alert.setHeaderText("Справка по приложению");
        alert.setContentText("""
                Данное приложение создано для демонстрации ваших данных.
                - В главном окне вам представлена вся информация о вас.
                - Вы можете получить сведения о своей успеваемости, кликнув на соответствующую гиперссылку""");
        alert.setResizable(true);
        alert.showAndWait();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setStudent(Student student) {
        this.student = student;
        titleLabel.setText(student.getFullName());

        studentIdLabel.setText(student.getId().toString());
        lastNameLabel.setText(student.getLastName());
        firstNameLabel.setText(student.getFirstName());
        patronimLabel.setText(student.getPatronim());
        birthdayLabel.setText(student.getBirthday().toString());
        groupLabel.setText(String.valueOf(student.getGroup().getGroupNum()));
        phoneLabel.setText(student.getPhoneNum());
        admissionDateLabel.setText(student.getAdmissionDate().toString());
        passportLabel.setText(student.getDocumentNum());
        addressLabel.setText(student.getAddress());
        directionLabel.setText(student.getGroup().getDirection().toString());
        gradesHyperlink.setVisited(false);
        gradesHyperlink.setText("просмотреть");
        termLabel.setText(String.valueOf(student.getGroup().getTerm()));
        double average = student.getAverage();
        if (average > 0) {
            String formattedAverage = new DecimalFormat("#0.00").format(average);
            averageLabel.setText(formattedAverage);
        } else {
            averageLabel.setText("нет данных");
        }
    }

    public void gradesHandle() {
        try {
            showGradeForm(student);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showGradeForm(Student student) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL url = Paths.get("src\\main\\resources\\com\\example\\deanery\\Grades.fxml").toUri().toURL();
        loader.setLocation(url);

        AnchorPane page = (AnchorPane) loader.load();

        // создаем диалоговое окно Stage
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Успеваемость студента");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(null);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // передаем адресата в контроллер
        GradesController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setStudent(student);

        controller.removeInputNewResultsButton();

        dialogStage.showAndWait();
    }

}
