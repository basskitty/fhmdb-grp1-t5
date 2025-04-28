package at.ac.fhcampuswien.fhmdb;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.Objects;

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
            Pane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFile)));
            contentPane.getChildren().setAll(view);

            updateNavigationButtons(fxmlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
}
