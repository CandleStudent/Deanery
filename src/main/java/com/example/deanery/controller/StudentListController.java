package com.example.deanery.controller;

import com.example.deanery.dao.Application;
import com.example.deanery.dao.studentdb.StudentsDAO;
import com.example.deanery.model.Student;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;

import static java.sql.DriverManager.getConnection;

public class StudentListController {

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

    private ObservableList<Student> studentsData = FXCollections.observableArrayList();
    @FXML
    private TableView<Student> studentsTable;
    @FXML
    private TableColumn<Student, Integer> idColumn;
    @FXML
    private TableColumn<Student, String> lastNameColumn;
    @FXML
    private TableColumn<Student, String> firstNameColumn;
    @FXML
    private TableColumn<Student, String> patronimColumn;
    @FXML
    private TableColumn<Student, String> birthdayColumn;
    @FXML
    private TableColumn<Student, Integer> groupColumn;
    @FXML
    private TableColumn<Student, String> phoneColumn;

    @FXML
    private TextField searchBox;

    @FXML
    private void initialize() {
//        initData();
        idColumn.setCellValueFactory(column -> new SimpleObjectProperty<Integer>(column.getValue().getId()));
        lastNameColumn.setCellValueFactory(column -> new SimpleObjectProperty<String>(column.getValue().getLastName()));
        firstNameColumn.setCellValueFactory(column -> new SimpleObjectProperty<String>(column.getValue().getFirstName()));
        patronimColumn.setCellValueFactory(column -> new SimpleObjectProperty<String>(column.getValue().getPatronim()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        phoneColumn.setCellValueFactory(column -> new SimpleObjectProperty<String>(column.getValue().getPhoneNum()));
        birthdayColumn.setCellValueFactory(column -> new ReadOnlyStringWrapper(formatter.format(column.getValue().getBirthday())));
        groupColumn.setCellValueFactory(column -> new SimpleObjectProperty<Integer>(column.getValue().getGroup().getGroupNum()));

        try {
            StudentsDAO studentsDAO = new StudentsDAO(Application.INSTANCE.dataSourceStudents());
            studentsData = studentsDAO.initDataFromStudents();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        FilteredList<Student> filteredData = new FilteredList<>(studentsData);
        studentsTable.setItems(filteredData);

        showStudentDetails(null);
        studentsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showStudentDetails(newValue)
        );

        searchBox.textProperty().addListener((observable, oldValue, newValue) ->
                filteredData.setPredicate(createPredicate(newValue))
        );
    }

    private void showStudentDetails(Student student) {
        if (student != null) {
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
        } else {
            studentIdLabel.setText("");
            lastNameLabel.setText("");
            firstNameLabel.setText("");
            patronimLabel.setText("");
            birthdayLabel.setText("");
            groupLabel.setText("");
            phoneLabel.setText("");
            admissionDateLabel.setText("");
            passportLabel.setText("");
            addressLabel.setText("");
            directionLabel.setText("");
            gradesHyperlink.setText("");
            termLabel.setText("");
            averageLabel.setText("");
        }
    }

    private void initData() {
        studentsTable.getItems().clear();
        try {
            StudentsDAO studentsDAO = new StudentsDAO(Application.INSTANCE.dataSourceStudents());
            studentsData = studentsDAO.initDataFromStudents();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        studentsTable.setItems(studentsData);
    }

    public void addStudent() {
        Student tempStudent = new Student();
        boolean okClicked = this.showStudentEditDialog(tempStudent);
        if (okClicked) {
            // добавление в бд
            StudentsDAO studentsDAO = new StudentsDAO(Application.INSTANCE.dataSourceStudents());
            studentsDAO.addStudent(tempStudent);
            studentsData.add(tempStudent);
            FilteredList<Student> filteredData = new FilteredList<>(studentsData);
            studentsTable.setItems(filteredData);
        }
    }

    public void expelStudent() {
        int selectedIndex = studentsTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Student currStudent = studentsTable.getItems().get(selectedIndex);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Отчислисть " + currStudent.getLastName() + " " + currStudent.getFirstName() + " " + currStudent.getPatronim() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.setTitle("Подтверждение");
            alert.setHeaderText("Подтверждение отчисления");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                studentsData.remove(selectedIndex);
                studentsTable.setItems(studentsData);
                // удаление из бд
                StudentsDAO studentsDAO = new StudentsDAO(Application.INSTANCE.dataSourceStudents());
                studentsDAO.expelStudent(currStudent);
            }
        } else {
            // nothing chosen
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(null);
            alert.setTitle("Ничего не выбрано");
            alert.setHeaderText("Нет выбранного студента");
            alert.setContentText("Выберите студента в таблице");
            alert.showAndWait();
        }
    }

    public void changeStudent() {
        Student selectedStudent = studentsTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            boolean okClicked = showStudentEditDialog(selectedStudent);
            if (okClicked) {
                showStudentDetails(selectedStudent);
                int selectedIndex = studentsTable.getSelectionModel().getSelectedIndex();
                studentsData.set(selectedIndex, selectedStudent);
                //  тут изменение в бд
                StudentsDAO studentsDAO = new StudentsDAO(Application.INSTANCE.dataSourceStudents());
                studentsDAO.updateStudent(selectedStudent);
            }
        } else {
            // nothing chosen
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(null);
            alert.setTitle("Ничего не выбрано");
            alert.setHeaderText("Нет выбранного студента");
            alert.setContentText("Выберите студента в таблице");
            alert.showAndWait();
        }
    }

    private boolean showStudentEditDialog(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = Paths.get("src\\main\\resources\\com\\example\\deanery\\Edit.fxml").toUri().toURL();
            loader.setLocation(url);
            AnchorPane page = (AnchorPane) loader.load();

            // создаем диалоговое окно Stage
            Stage dialogStage = new Stage();
            URL imgUrl = Paths.get("src\\main\\resources\\com\\example\\deanery\\img\\symbol.png").toUri().toURL();
            dialogStage.getIcons().add(new Image(String.valueOf(imgUrl)));
            dialogStage.setTitle("Edit student");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(null);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // передаем адресата в контроллер
            EditStudentController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setStudent(student);

            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    public void transferStudent() {
        Student selectedStudent = studentsTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            boolean okClicked = showStudentTransferDialog(selectedStudent);
            if (okClicked) {
                showStudentDetails(selectedStudent);
                int selectedIndex = studentsTable.getSelectionModel().getSelectedIndex();
                studentsData.set(selectedIndex, selectedStudent);
                //  тут изменение в бд
                StudentsDAO studentsDAO = new StudentsDAO(Application.INSTANCE.dataSourceStudents());
                studentsDAO.updateStudent(selectedStudent);
            }
        } else {
            // nothing chosen
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(null);
            alert.setTitle("Ничего не выбрано");
            alert.setHeaderText("Нет выбранного студента");
            alert.setContentText("Выберите студента в таблице");
            alert.showAndWait();
        }
    }

    private boolean showStudentTransferDialog(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = Paths.get("src\\main\\resources\\com\\example\\deanery\\Transfer.fxml").toUri().toURL();
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
            TransferController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setStudent(student);

            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void gradesHandle() {
        Student selectedStudent = studentsTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            try {
                showGradeForm(selectedStudent);
                showStudentDetails(selectedStudent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // nothing chosen
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(null);
            alert.setTitle("Ничего не выбрано");
            alert.setHeaderText("Нет выбранного студента");
            alert.setContentText("Выберите студента в таблице");
            alert.showAndWait();
        }
    }

    public void showGradeForm(Student student) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL url = Paths.get("src\\main\\resources\\com\\example\\deanery\\Grades.fxml").toUri().toURL();
        loader.setLocation(url);

        AnchorPane page = (AnchorPane) loader.load();

        // создаем диалоговое окно Stage
        Stage dialogStage = new Stage();
        URL imgUrl = Paths.get("src\\main\\resources\\com\\example\\deanery\\img\\symbol.png").toUri().toURL();
        dialogStage.getIcons().add(new Image(String.valueOf(imgUrl)));
        dialogStage.setTitle("Успеваемость студента");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(null);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // передаем адресата в контроллер
        GradesController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setStudent(student);

        dialogStage.showAndWait();
    }

    ////////////////////
    // Для фильтрации и поиска //
    /////////////////////

    // поиск по ФИО, группе, направлению
    private boolean searchFindsStudent(Student student, String searchText) {
        String fullName = student.getLastName() + " " + student.getFirstName() + " " + student.getPatronim();
        return (fullName.toLowerCase().contains(searchText.toLowerCase()) ||
                String.valueOf(student.getGroup().getGroupNum()).contains(searchText.toLowerCase()) ||
                student.getGroup().getDirection().getName().toLowerCase().contains(searchText.toLowerCase()));
    }

    private Predicate<Student> createPredicate(String searchText) {
        return student -> {
            if (searchText == null || searchText.isEmpty()) return true;
            return searchFindsStudent(student, searchText);
        };
    }


}
