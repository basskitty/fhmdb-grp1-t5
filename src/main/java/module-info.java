module at.ac.fhcampuswien.fhmdb {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.jfoenix;
    requires okhttp3;
    requires com.google.gson;
    requires ormlite.jdbc;
    requires java.sql;
    requires java.desktop;
    requires com.h2database;

    opens at.ac.fhcampuswien.fhmdb;
    opens at.ac.fhcampuswien.fhmdb.models to com.google.gson;

    exports at.ac.fhcampuswien.fhmdb;
    exports at.ac.fhcampuswien.fhmdb.models;
    exports at.ac.fhcampuswien.fhmdb.database;
    exports at.ac.fhcampuswien.fhmdb.exceptions;
    exports at.ac.fhcampuswien.fhmdb.observer;
    opens at.ac.fhcampuswien.fhmdb.database;

}