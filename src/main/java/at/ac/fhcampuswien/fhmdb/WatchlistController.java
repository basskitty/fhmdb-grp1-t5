package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class WatchlistController implements Initializable {
    @FXML
    private JFXListView<Movie> watchlistView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        watchlistView.setCellFactory(listView -> new MovieCell(movie -> {
            WatchlistRepository.removeMovie(movie);
            watchlistView.setItems(FXCollections.observableArrayList(WatchlistRepository.getWatchlist()));
        }, "Remove"));

        if (WatchlistRepository.getWatchlist().isEmpty()) {
            createWatchlistPlaceholder();
        } else {
            watchlistView.setItems(FXCollections.observableArrayList(WatchlistRepository.getWatchlist()));
        }
    }

    // Create placeholder if there are no movies matching with the chosen filters
    private void createWatchlistPlaceholder() {
        Label placeholderLabel = new Label("No movies on watchlist yet.");
        placeholderLabel.getStyleClass().add("placeholder-label");

        VBox placeholderContainer = new VBox(placeholderLabel);
        placeholderContainer.setAlignment(Pos.TOP_LEFT);
        placeholderContainer.setPadding(new Insets(10, 0, 0, 10));

        watchlistView.setPlaceholder(placeholderContainer);
    }
}
