package com.example.deanery.controller;

import com.example.deanery.dao.EnterDataDAO;
import com.example.deanery.dao.StudentsDAO;
import com.example.deanery.model.Student;
import com.example.deanery.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class EnterController {

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passField;


    public void enterButtonAction() {
        User user = new User();
        int enter = EnterDataDAO.isEnterDataCorrect(user, loginField.getText(), passField.getText());
        switch(enter) {
            case(-1) -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка!");
                alert.setHeaderText("Сообщение об ошибке");
                alert.setContentText("Неверный логин. Такого аккаунта не существует");
                alert.showAndWait();
            }
            case(0) -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка!");
                alert.setHeaderText("Сообщение об ошибке");
                alert.setContentText("Неверный пароль!");
                alert.showAndWait();
            }
            case(1) -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Успех");
                alert.setHeaderText("Сообщение об успешном входе");
                alert.setContentText("Вы успешно вошли в систему");
                alert.showAndWait();
                if (user.getRoleId() == 1) {
                    showMainMenu();
                } else if (user.getRoleId() == 2) {
                    showStudentMenu(EnterDataDAO.getStudentId(user.getId()));
                }
            }
        }
    }

    private void showMainMenu() {
        try {
            Stage menuStage = new Stage();
            menuStage.setTitle("Деканат");
            URL url = Paths.get("src\\main\\resources\\com\\example\\deanery\\MainMenu.fxml").toUri().toURL();
            Parent root = FXMLLoader.load(url);
            Scene regScene = new Scene(root, 800, 650);
            URL imgUrl = Paths.get("src\\main\\resources\\com\\example\\deanery\\img\\symbol.png").toUri().toURL();
            menuStage.getIcons().add(new Image(String.valueOf(imgUrl)));
            menuStage.setScene(regScene);
            //  closing previous scene
            Stage stage = (Stage) loginField.getScene().getWindow();
            stage.close();
            // \closing
            menuStage.show();
        } catch( IOException e) {
            e.printStackTrace();
        }
    }

    private void showStudentMenu(int studentId) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = Paths.get("src\\main\\resources\\com\\example\\deanery\\StudentMenu.fxml").toUri().toURL();
            loader.setLocation(url);
            BorderPane page = (BorderPane) loader.load();

            // создаем окно Stage
            Stage newStage = new Stage();
            URL imgUrl = Paths.get("src\\main\\resources\\com\\example\\deanery\\img\\symbol.png").toUri().toURL();
            newStage.getIcons().add(new Image(String.valueOf(imgUrl)));
            newStage.setTitle("Студент");
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.initOwner(null);
            Scene scene = new Scene(page);
            newStage.setScene(scene);

            // передаем адресата в контроллер
            StudentMenuController controller = loader.getController();
            controller.setStage(newStage);
            Student student = StudentsDAO.getStudent(studentId);
            controller.setStudent(student);

            //  closing previous scene
            Stage stage = (Stage) loginField.getScene().getWindow();
            stage.close();
            // \closing
            newStage.show();
        } catch( IOException e) {
            e.printStackTrace();
        }
    }


}
