package com.example.deanery.controller;

import com.example.deanery.dao.Students;
import com.example.deanery.model.Group;
import com.example.deanery.model.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.example.deanery.dao.StudentsDbDAO.getGroup;

public class EditStudentController extends EditController{
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField patronimField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField passportField;
    @FXML
    private TextField groupField;
    @FXML
    private TextField admissionDateField;
    @FXML
    private TextField birthdayField;
    @FXML
    private TextField addressField;

    private Student student;

    public void setStudent(Student student) {
        this.student = student;
        if (student.getId() != null) {
//            idField.setText(String.valueOf(student.getId()));
            lastNameField.setText(student.getLastName());
            firstNameField.setText(student.getFirstName());
            patronimField.setText(student.getPatronim());
            phoneField.setText(student.getPhoneNum());
            passportField.setText(student.getDocumentNum());
            groupField.setText(String.valueOf(student.getGroup().getGroupNum()));
            groupField.setEditable(false);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            admissionDateField.setText(student.getAdmissionDate().format(formatter));
            birthdayField.setText(student.getBirthday().format(formatter));
            addressField.setText(student.getAddress());
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (lastNameField.getText() == null
                || lastNameField.getText().length() == 0
                || firstNameField.getText() == null
                || firstNameField.getText().length() == 0
                || patronimField.getText() == null
                || patronimField.getText().length() == 0) {
            errorMessage += "Некорректно введено ФИО\n";
        }

        String numRegex = "[0-9]+";
        if (!phoneField.getText().matches(numRegex)) {
            errorMessage += "В поле номера телефона содержатся символы, отличные от цифр\n";
        }
        if (phoneField.getText() == null || phoneField.getText().length() == 0) {
            errorMessage += "Поле ввода номера телефона пустое\n";
        }
        if (phoneField.getText().length() != 11) {
            errorMessage += "Номер телефона содержит меньше или больше, чем 11 цифр\n";
        }
        if (!passportField.getText().matches(numRegex)) {
            errorMessage += "В поле паспорта содержатся символы, отличные от цифр\n";
        }
        if (passportField.getText().length() != 10) {
            errorMessage += "В поле паспорта не 10 цифр\n";
        }
        if (Students.isPassportExist(passportField.getText())) {
            errorMessage += "Студент с таким паспортом уже присутствует в базе";
        }
        String dateRegex = "^\\d{2}-\\d{2}-\\d{4}$";
        if (!admissionDateField.getText().matches(dateRegex)) {
            errorMessage += "Дата поступления введена неправильно";
        }
        if (!birthdayField.getText().matches(dateRegex)) {
            errorMessage += "Дата рождения введена неправильно\n";
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

    @FXML
    private void handleOk() {
        if (isInputValid()) {
//            student.setId(Integer.parseInt(idField.getText()));
            student.setLastName(lastNameField.getText());
            student.setFirstName(firstNameField.getText());
            student.setPatronim(patronimField.getText());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            student.setAdmissionDate(LocalDate.parse(admissionDateField.getText(), formatter));
            Group group = getGroup(Integer.parseInt(groupField.getText()));
            student.setGroup(group);
            student.setPhoneNum(phoneField.getText());
            student.setDocumentNum(passportField.getText());
            student.setBirthday(LocalDate.parse(birthdayField.getText(), formatter));
            student.setAddress(addressField.getText());

            okClicked = true;
            dialogStage.close();
        }
    }
}
