package com.example.deanery.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;


public class MainMenuController {

    @FXML
    private MenuItem exitItem;
    @FXML
    private MenuItem closeItem;
    @FXML
    private MenuItem showHelpItem;
    @FXML
    private MenuBar menuBar;

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
        } catch( IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleShowHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Помощь");
        alert.setHeaderText("Справка по приложению");
        alert.setContentText("""
                Данное приложение создано для работы с данными студентов.
                - В главном окне вам представлен список всех обучающихся на данный момент студентов.
                - Вы можете осуществлять поиск по ФИО или номеру группы, задавая их в поисковой строке.
                - Вы можете получить дополнительную информацию о студенте, выбрав его в таблице. Справа отобразятся дополнительные сведения.
                - Используя соответствующие кнопки, вы можете зачислить нового студента, изменить данные выбранного студента, перевести выбранного студента в другую группу и отчислить выбранного студента.""");
        alert.setResizable(true);
        alert.showAndWait();
    }
}
