package at.ac.fhcampuswien.fhmdb;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;

import java.io.IOException;

import static javafx.application.ConditionalFeature.FXML;

public class MainController {
    @FXML
    private StackPane contentPane;

    @FXML
    private Button homeBtn;

    @FXML
    private Button watchlistBtn;

    @FXML
    public void initialize() {
        // Buttons verknüpfen
        homeBtn.setOnAction(e -> loadView("home-view.fxml"));
        watchlistBtn.setOnAction(e -> loadView("watchlist-view.fxml"));

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
