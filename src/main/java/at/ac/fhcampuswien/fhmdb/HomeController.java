package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;
import at.ac.fhcampuswien.fhmdb.models.Decade;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.Sorting;
import at.ac.fhcampuswien.fhmdb.models.sorting.SortContext;
import at.ac.fhcampuswien.fhmdb.observer.Observer;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.util.*;


public class HomeController implements Initializable, Observer<Movie>
{

    @FXML
    public JFXButton searchBtn;
    @FXML
    public TextField searchField;
    @FXML
    public JFXListView<Movie> movieListView;
    @FXML
    public JFXComboBox<Genre> genreComboBox; // ComboBox holding Genre objects.
    @FXML
    public JFXButton sortBtn;
    @FXML
    public CheckComboBox<Decade> releaseYearComboBox;
    @FXML
    public JFXComboBox<Integer> ratingComboBox;


    SortContext sortContext = new SortContext();

    // This observable list backs the ListView.
    final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    // Public List<Movie> allMovies = Movie.initializeMovies();
    public List<Movie> allMovies = new ArrayList<>();

    private WatchlistRepository watchlistRepository;

    public HomeController()
    {
        System.out.println("Init Homecontroller");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try
        {
            allMovies = MovieAPI.getAllMovies();
        }
        catch(MovieApiException apiException)
        {
            observableMovies.clear();                                                                              // ← ADDED: clear any partial data
            movieListView.setItems(observableMovies);                                                              // ← ADDED: ensure ListView is set
            movieListView.setPlaceholder(createPlaceholder());                                                     // ← ADDED: show placeholder

            showNotification(                                                                                       // ← ADDED: notify user
                    "API Fehler",
                    "Fehler beim Laden der Filme: " + apiException.getMessage(),
                    3.0,
                    Pos.BOTTOM_RIGHT,
                    NotificationType.ERROR
            );
        }

        // LOAD MOVIES
        // Load all Movies & Set List View to observable List
        observableMovies.addAll(allMovies);


        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(listView -> new MovieCell(this::onDetailsClicked, "Show Details"));

        // LOAD GENRES
        // Populate the ComboBox with all Genre enum values.
        genreComboBox.getItems().addAll(Genre.values());
        genreComboBox.setPromptText("Filter by genre");

        releaseYearComboBox.getItems().addAll(Decade.values());
        setupReleaseYearPromptSimulation();

        // Fill rating combo box (from 0 to 10)
        ratingComboBox.getItems().addAll(
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        );
        ratingComboBox.setPromptText("Select minimum rating");

        // SET EVENT LISTENERS
        searchBtn.setOnAction(e -> filterMovies());

        sortBtn.setText(Sorting.NAME_ASC.buttonText);
        sortBtn.setOnAction(e -> sortMovies());

        // initialize watchlistRepo & register observer
        try {
            watchlistRepository = WatchlistRepository.getInstance();
            watchlistRepository.addObserver(this);
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

        // now cell factory for “Add” button
        movieListView.setCellFactory(listView ->
                new MovieCell(movie -> {
                    try {
                        watchlistRepository.addMovie(movie);
                    } catch (DatabaseException dbEx) {
                        showNotification(
                                "Datenbank-Fehler",
                                dbEx.getUserMessage(),
                                3.0,
                                Pos.BOTTOM_RIGHT,
                                NotificationType.ERROR
                        );
                    }
                }, "Add")
        );

        sortBtn.setText(sortContext.getButtonLabel());
        sortBtn.setOnAction(e -> onSortButtonClicked());
    }

    @Override
    public void update(Movie movie, boolean success, String message) {
        Platform.runLater(() -> {
            showNotification(
                    "Watchlist",
                    message,
                    2.5,
                    Pos.BOTTOM_RIGHT,
                    success ? NotificationType.CONFIRM : NotificationType.WARNING
            );
        });
    }

    @FXML
    private void onSortButtonClicked() {
        sortContext.nextState();
        List<Movie> sorted = sortContext.applySort(observableMovies);
        observableMovies.setAll(sorted);
        sortBtn.setText(sortContext.getButtonLabel());
    }

    private void onDetailsClicked(Movie movie) {
        // TODO: Implement Detail Overview
    }

    void filterMovies()
    {
        try
        {
            // Retrieve selected filters from user inputs
            String searchText = getSearchText();
            List<Decade> selectedDecades = getSelectedDecades();
            String minRating = getSelectedRating();
            Genre selectedGenre = getSelectedGenre();

            List<Movie> searchedMovies = getMoviesfromAPI(searchText, selectedGenre, selectedDecades, minRating);

            // Fallback: If search text doesn't match with any title,
            // filter descriptions locally
            if (searchText != null && searchedMovies.isEmpty())
            {
                List<Movie> moviesWithoutQueryFilter = getMoviesfromAPI(null, selectedGenre, selectedDecades, minRating);
                searchedMovies = filterDescriptionLocally(searchText, moviesWithoutQueryFilter);
            }

            // Testing results from MovieUtils methods
            System.out.println("Most popular actor: " + MovieUtils.getMostPopularActor(searchedMovies));
            System.out.println("Longest movie title: " + MovieUtils.getLongestMovieTitle(searchedMovies));
            System.out.println("Movies between 1980 and 1998: " + MovieUtils.getMoviesBetweenYears(searchedMovies, 1980, 1998));
            System.out.println("Movies directed by Hayao Miyazaki: " + MovieUtils.countMoviesFrom(searchedMovies, "Hayao Miyazaki"));

            // Update the observable list and refresh the ListView.
            observableMovies.setAll(searchedMovies);
            movieListView.setPlaceholder(searchedMovies.isEmpty() ? createPlaceholder() : null);
            movieListView.refresh();
        }
        catch(MovieApiException apiException)
        {
            // clear previous results
            observableMovies.clear();
            movieListView.setPlaceholder(createPlaceholder());               // show "no results" placeholder

            // show error notification
            showNotification(
                    "API Fehler",
                    "Fehler beim Laden der Filme: " + apiException.getMessage(),
                    3.0,
                    Pos.BOTTOM_RIGHT,
                    NotificationType.ERROR
            );
        }
    }


    List<Movie> getMoviesfromAPI(String query, Genre genre, List<Decade> selectedDecades, String rating) throws MovieApiException
    {
        List<String> selectedYears = getSelectedYears(selectedDecades);

        // If no decades selected → only 1 API call with null for releaseYear
        if (selectedYears.isEmpty()) {
            return MovieAPI.getFilteredMovies(query, genre, null, rating);
        }

        List<Movie> filteredMovies = new ArrayList<>();

        for (String year : selectedYears) {
            List<Movie> response = MovieAPI.getFilteredMovies(query, genre, year, rating);
            filteredMovies.addAll(response);
        }

        return filteredMovies;
    }

    // GETTER methods for user input
    private String getSearchText() {
        String input = searchField.getText();
        return (input != null && !input.trim().isEmpty()) ? input.trim().toLowerCase() : null;
    }

    private Genre getSelectedGenre() {
        Genre selected = genreComboBox.getValue();
        return (selected != null && !selected.equals(Genre.ALL_GENRE)) ? selected : null;
    }

    private String getSelectedRating() {
        Integer rating = ratingComboBox.getValue();
        return (rating != null && rating >= 0 && rating <= 10) ? rating.toString() : null;
    }

    private List<Decade> getSelectedDecades() {
        return releaseYearComboBox.getCheckModel().getCheckedItems();
    }

    public List<String> getSelectedYears(List<Decade> selectedDecades) {
        List<String> years = new ArrayList<>();

        if (selectedDecades == null || selectedDecades.isEmpty()) {
            return years;
        }

        for (Decade decade : selectedDecades)
        {
            for (int year = decade.getStartYear(); year <= decade.getEndYear(); year++)
            {
                years.add(String.valueOf(year));
            }
        }
        return years;
    }

    // FALLBACK method: If search text input matches no titles, filter locally in description
    public List<Movie> filterDescriptionLocally(String inputSearch, List<Movie> moviesFromAPI) {
            return moviesFromAPI.stream()
                    .filter(movie -> movie.getDescription() != null &&
                            movie.getDescription().toLowerCase().contains(inputSearch))
                    .toList();
    }


    /**
     * Toggles the sorting order of movies by title (case‑insensitive).
     */
    void sortMovies()
    {
        Comparator<Movie> comparator = Comparator.comparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER);
        if (sortBtn.getText().equals(Sorting.NAME_ASC.buttonText))
        {
            FXCollections.sort(observableMovies, comparator);
            sortBtn.setText(Sorting.NAME_DESC.buttonText);
        } else
        {
            FXCollections.sort(observableMovies, comparator.reversed());
            sortBtn.setText(Sorting.NAME_ASC.buttonText);
        }
    }

    // Create placeholder if there are no movies matching with the chosen filters
    private VBox createPlaceholder() {
        Label placeholderLabel = new Label("No movies matching with the chosen filters.");
        placeholderLabel.getStyleClass().add("placeholder-label");

        VBox placeholderContainer = new VBox(placeholderLabel);
        placeholderContainer.setAlignment(Pos.TOP_LEFT);
        placeholderContainer.setPadding(new Insets(10, 0, 0, 10));

        return placeholderContainer;
    }

    // Prompt text simulation for CheckComboBox
    public void setupReleaseYearPromptSimulation() {
        if (releaseYearComboBox.getCheckModel().getCheckedItems().isEmpty()) {
            releaseYearComboBox.setTitle("Filter by decade");
        }
        releaseYearComboBox.getCheckModel().getCheckedItems().addListener((ListChangeListener<Decade>) change -> {
            if (releaseYearComboBox.getCheckModel().getCheckedItems().isEmpty()) {
                releaseYearComboBox.setTitle("Filter by decade");
            } else {
                releaseYearComboBox.setTitle(null); // empty when selection is made
            }
        });
    }

    public enum NotificationType { INFO, WARNING, ERROR, CONFIRM }

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
            case ERROR   -> notif.showError();
            case WARNING -> notif.showWarning();
            case CONFIRM -> notif.showConfirm();
            default      -> notif.showInformation();
        }
    }
}
