package com.example.deanery;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class DeaneryApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DeaneryApplication.class.getResource("Enter.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Вход");
        stage.setScene(scene);
        URL imgUrl = Paths.get("src\\main\\resources\\com\\example\\deanery\\img\\symbol.png").toUri().toURL();
        stage.getIcons().add(new Image(String.valueOf(imgUrl)));
        stage.show();
        com.example.deanery.dao.Application.updateToDate();
    }

    public static void main(String[] args) {
        launch();
    }
}