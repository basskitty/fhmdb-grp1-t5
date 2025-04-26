package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

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

        watchlistView.setItems(FXCollections.observableArrayList(WatchlistRepository.getWatchlist()));
    }
}
