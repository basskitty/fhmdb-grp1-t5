package at.ac.fhcampuswien.fhmdb;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;
import javafx.scene.control.Button;

import java.io.IOException;

public class MainController {
    @FXML
    private HBox topHBox;

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

    @FXML
    private Region spacer;

    @FXML
    public void initialize() {
        // --- HBox auf Maximalbreite setzen ---
        topHBox.setPrefWidth(Double.MAX_VALUE);

        // --- Spacer wachsen lassen ---
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Buttons verknüpfen
        homeBtn.setOnAction(e -> loadView("home-view.fxml"));
        watchlistBtn.setOnAction(e -> loadView("watchlist-view.fxml"));

        // --- Sandwich Menü Toggle ---
        menuBtn.setOnAction(event -> {
            boolean isVisible = navigationBox.isVisible();
            navigationBox.setVisible(!isVisible);
        });

        // Standard Start-View
        loadView("home-view.fxml");
    }

    private void loadView(String fxmlFile) {
        try {
            Pane view = FXMLLoader.load(getClass().getResource(fxmlFile));
            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
