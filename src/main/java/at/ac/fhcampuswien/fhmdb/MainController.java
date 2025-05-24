package at.ac.fhcampuswien.fhmdb;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.util.Duration;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.Objects;

import org.controlsfx.control.Notifications;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;

public class MainController {
    @FXML
    private StackPane contentPane;

    @FXML
    private Button menuBtn;

    @FXML
    private Button homeBtn;

    @FXML
    private Button watchlistBtn;

    @FXML
    private Button aboutBtn;

    @FXML
    private HBox navigationBox;

    public void initialize() {
        // Connect buttons
        homeBtn.setOnAction(e -> loadView("home-view.fxml"));
        watchlistBtn.setOnAction(e -> loadView("watchlist-view.fxml"));
        aboutBtn.setOnAction(e -> loadView("about-view.fxml"));

        // Toggle sandwich menu
        menuBtn.setOnAction(event -> {
            if (navigationBox.isVisible()) {
                menuBtn.setText("☰");
                navigationBox.setVisible(false);
            } else {
                menuBtn.setText("×");
                navigationBox.setVisible(true);
            }
        });

        // Standard Start-View
        loadView("home-view.fxml");
    }

    
    private void loadView(String fxmlFile) {
        try {
            Pane view = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource(fxmlFile))
            );
            contentPane.getChildren().setAll(view);
            updateNavigationButtons(fxmlFile);
        }
        catch (Exception ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof MovieApiException apiEx) {

            }
            else if (cause instanceof DatabaseException dbEx) {

            }
            else {

            }
        }
    }
    /* showNotification(
                    "API-Fehler",
                    apiEx.getUserMessage(),
                    3.0,
                    Pos.BOTTOM_RIGHT,
                    NotificationType.ERROR
                );
       showNotification(
                    "Datenbank-Fehler",
                    dbEx.getUserMessage(),
                    3.0,
                    Pos.BOTTOM_RIGHT,
                    NotificationType.ERROR
                );
       showNotification(
                    "Ansichtsfehler",
                    "Die Ansicht „" + fxmlFile + "“ konnte nicht geladen werden.",
                    3.0,
                    Pos.BOTTOM_RIGHT,
                    NotificationType.ERROR
                );
     */
    private void updateNavigationButtons(String fxmlFile) {
        homeBtn.getStyleClass().remove("active-nav-button");
        watchlistBtn.getStyleClass().remove("active-nav-button");
        aboutBtn.getStyleClass().remove("active-nav-button");


        switch (fxmlFile) {
            case "home-view.fxml" -> {
                if (!homeBtn.getStyleClass().contains("active-nav-button")) {
                    homeBtn.getStyleClass().add("active-nav-button");
                }
            }
            case "watchlist-view.fxml" -> {
                if (!watchlistBtn.getStyleClass().contains("active-nav-button")) {
                    watchlistBtn.getStyleClass().add("active-nav-button");
                }
            }
            case "about-view.fxml" -> {
                if (!aboutBtn.getStyleClass().contains("active-nav-button")) {
                    aboutBtn.getStyleClass().add("active-nav-button");
                }
            }
        }
    }

    public enum NotificationType {
        INFO, WARNING, ERROR, CONFIRM
    }

    /**
     * Displays a toast‐style notification in the corner.
     *
     * @param title    the bold heading on the popup
     * @param message  the body text
     * @param seconds  how many seconds to stay visible
     * @param position where on screen (e.g. BOTTOM_RIGHT)
     * @param type     style (INFO/WARNING/ERROR/CONFIRM)
     */

    private void showNotification(String title,
                                  String message,
                                  double seconds,
                                  Pos position,
                                  NotificationType type) {
        Notifications notif = Notifications.create()
                                 .title(title)
                                 .text(message)
                                 .hideAfter(Duration.seconds(seconds))
                                 .position(position);

        switch (type) {
            case ERROR    -> notif.showError();
            case WARNING  -> notif.showWarning();
            case CONFIRM  -> notif.showConfirm();
            default       -> notif.showInformation();
        }
    }
}
