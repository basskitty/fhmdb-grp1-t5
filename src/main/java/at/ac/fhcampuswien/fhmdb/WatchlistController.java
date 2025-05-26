package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import at.ac.fhcampuswien.fhmdb.observer.Observer;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.application.Platform;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

public class WatchlistController implements Initializable, Observer<Movie> {
    @FXML
    private JFXListView<Movie> watchlistView;

    private WatchlistRepository watchlistRepository;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
         // Initialize repository
        try {
            watchlistRepository = WatchlistRepository.getInstance();
            watchlistRepository.addObserver(this);
        } catch (DatabaseException dbEx) {
            showNotification(
                "Datenbank-Fehler",
                dbEx.getUserMessage(),
                3.0,
                Pos.BOTTOM_RIGHT,
                NotificationType.ERROR
            );
            // disable the view since we can’t load data
            watchlistRepository = null;
            watchlistView.setPlaceholder(createWatchlistPlaceholder("Fehler beim Laden der Watchlist."));
            return;
        }
        
        updateWatchlistView();

        watchlistView.setCellFactory(listView -> new MovieCell(movie -> {
            if (watchlistRepository != null) {
                try
                {
                    watchlistRepository.removeMovie(movie);
                }
                catch (DatabaseException e)
                {
                    showNotification(
                        "Datenbank-Fehler",
                        e.getUserMessage(),
                        3.0,
                        Pos.BOTTOM_RIGHT,
                        NotificationType.ERROR
                    );
                }
            }
        }, "Remove"));
    }

    private void updateWatchlistView() {
        if (watchlistRepository == null) {
            watchlistView.setItems(FXCollections.observableArrayList());
            return;
        }


        try {
            List<Movie> movies = watchlistRepository.getMoviesFromWatchlist();
            if (movies.isEmpty()) {
                watchlistView.getItems().clear();
                watchlistView.setPlaceholder(createWatchlistPlaceholder("No movies on watchlist yet."));
            } else {
                watchlistView.setItems(FXCollections.observableArrayList(movies));
            }
        } catch (DatabaseException dbEx) {
            showNotification(
                "Datenbank-Fehler",
                dbEx.getUserMessage(),
                3.0,
                Pos.BOTTOM_RIGHT,
                NotificationType.ERROR
            );
            watchlistView.getItems().clear();
            watchlistView.setPlaceholder(createWatchlistPlaceholder("Fehler beim Laden der Watchlist."));
        }
    }

    @Override
    public void update(Movie movie, boolean success, String message)
    {
        Platform.runLater(() -> {
            updateWatchlistView();
            showNotification(
                    "Watchlist",
                    message,
                    2.5,
                    Pos.BOTTOM_RIGHT,
                    success ? NotificationType.CONFIRM : NotificationType.WARNING
            );
        });
    }

    // Create placeholder if the watchlist is empty
    private VBox createWatchlistPlaceholder(String placeholderText) {
        Label placeholderLabel = new Label(placeholderText);
        placeholderLabel.getStyleClass().add("placeholder-label");

        VBox placeholderContainer = new VBox(placeholderLabel);
        placeholderContainer.setAlignment(Pos.TOP_LEFT);
        placeholderContainer.setPadding(new Insets(10, 0, 0, 10));

        return placeholderContainer;
    }



    public enum NotificationType {
        INFO, WARNING, ERROR, CONFIRM
    }
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