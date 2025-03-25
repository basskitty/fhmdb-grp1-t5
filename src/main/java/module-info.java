module at.ac.fhcampuswien.fhmdb {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.jfoenix;
    requires okhttp3;
    requires com.google.gson;

    opens at.ac.fhcampuswien.fhmdb;
    opens at.ac.fhcampuswien.fhmdb.models to com.google.gson;

    exports at.ac.fhcampuswien.fhmdb;
    exports at.ac.fhcampuswien.fhmdb.models;
}