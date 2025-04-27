package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
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
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class WatchlistController implements Initializable {
    @FXML
    private JFXListView<Movie> watchlistView;

    private final WatchlistRepository watchlistRepository = new WatchlistRepository();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateWatchlistView();

        watchlistView.setCellFactory(listView -> new MovieCell(movie -> {
            watchlistRepository.removeMovie(movie);
            updateWatchlistView();
        }, "Remove"));
    }

    private void updateWatchlistView() {
        List<Movie> movies = watchlistRepository.getMoviesFromWatchlist();

        if (movies.isEmpty()) {
            watchlistView.getItems().clear();
            createWatchlistPlaceholder();
        } else {
            watchlistView.setItems(FXCollections.observableArrayList(movies));
        }
    }

    // Create placeholder if the watchlist is empty
    private void createWatchlistPlaceholder() {
        Label placeholderLabel = new Label("No movies on watchlist yet.");
        placeholderLabel.getStyleClass().add("placeholder-label");

        VBox placeholderContainer = new VBox(placeholderLabel);
        placeholderContainer.setAlignment(Pos.TOP_LEFT);
        placeholderContainer.setPadding(new Insets(10, 0, 0, 10));

        watchlistView.setPlaceholder(placeholderContainer);
    }
}