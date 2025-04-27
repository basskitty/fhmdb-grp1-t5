package at.ac.fhcampuswien.fhmdb;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;
import javafx.scene.control.Button;

import java.io.IOException;

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
    private HBox navigationBox;

    public void initialize() {
        // Connect buttons
        homeBtn.setOnAction(e -> loadView("home-view.fxml"));
        watchlistBtn.setOnAction(e -> loadView("watchlist-view.fxml"));

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
            Pane view = FXMLLoader.load(getClass().getResource(fxmlFile));
            contentPane.getChildren().setAll(view);

            updateNavigationButtons(fxmlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateNavigationButtons(String fxmlFile) {
        homeBtn.getStyleClass().remove("active-nav-button");
        watchlistBtn.getStyleClass().remove("active-nav-button");

        if (fxmlFile.equals("home-view.fxml")) {
            if (!homeBtn.getStyleClass().contains("active-nav-button")) {
                homeBtn.getStyleClass().add("active-nav-button");
            }
        } else if (fxmlFile.equals("watchlist-view.fxml")) {
            if (!watchlistBtn.getStyleClass().contains("active-nav-button")) {
                watchlistBtn.getStyleClass().add("active-nav-button");
            }
        }
    }
}
