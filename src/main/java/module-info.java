module com.example.deanery {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires mysql.connector.java;
    requires java.naming;  // Java Naming and Directory Interface (JNDI) API.

    opens com.example.deanery to javafx.fxml;
    exports com.example.deanery;
    exports com.example.deanery.controller;
    opens com.example.deanery.controller to javafx.fxml;
}