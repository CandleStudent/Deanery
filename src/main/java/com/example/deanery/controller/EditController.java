package com.example.deanery.controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public abstract class EditController {
    protected boolean okClicked = false;
    protected Stage dialogStage;

    // устанавливает сцену для этого окна
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    protected void handleCancel() {
        dialogStage.close();
    }
}
