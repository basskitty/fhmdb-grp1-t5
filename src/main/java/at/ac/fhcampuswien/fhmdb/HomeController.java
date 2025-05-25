package at.ac.fhcampuswien.fhmdb;
import at.ac.fhcampuswien.fhmdb.database.MovieEntity;
import at.ac.fhcampuswien.fhmdb.database.MovieRepository;
import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;
import at.ac.fhcampuswien.fhmdb.models.Decade;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.Sorting;
import at.ac.fhcampuswien.fhmdb.models.sorting.SortContext;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
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


public class HomeController implements Initializable
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


    // This observable list backs the ListView.
    final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    private WatchlistRepository watchlistRepository;

    MovieAPI movieAPI = new MovieAPI();
    private SortContext sortContext = new SortContext();
    List<Movie> allMovies;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeData();
        initializeUi();
    }

    public void initializeData () {

        try 
        {
            allMovies = movieAPI.getAllMovies();
            saveMoviesToDatabase(allMovies);
        } 
        catch (MovieApiException e) 
        {
             // API failed → inform user, then fallback to DB
             showNotification(
                "API-Fehler",
                e.getUserMessage(),
                3.0,
                Pos.BOTTOM_RIGHT,
                NotificationType.ERROR
            );
            try {
                allMovies = getMoviesFromDatabase();
            } catch (DatabaseException dbEx) {
                // DB also failed → inform user and leave list empty
                showNotification(
                    "Datenbank-Fehler",
                    dbEx.getUserMessage(),
                    3.0,
                    Pos.BOTTOM_RIGHT,
                    NotificationType.ERROR
                );
                allMovies = Collections.emptyList();
            }
        }
        catch(DatabaseException e)
        {
            // Saving to DB failed on first run → inform user, but still proceed with API data
            showNotification(
                "Datenbank-Fehler",
                e.getUserMessage(),
                3.0,
                Pos.BOTTOM_RIGHT,
                NotificationType.WARNING
            );
        }
    }

    public void initializeUi () {
        // LOAD MOVIES
        // Load all Movies & Set List View to observable List   
         // Prepare watchlist repository
         try {
            watchlistRepository = WatchlistRepository.getInstance();
        } catch (DatabaseException dbEx) {
            showNotification("Datenbank-Fehler", dbEx.getUserMessage(),
                             3.0, Pos.BOTTOM_RIGHT, NotificationType.ERROR);
            // disable watchlist adds:
            watchlistRepository = null;
        }
        
        
        observableMovies.addAll(allMovies);
        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(listView -> new MovieCell(movie -> {
            if (watchlistRepository != null) {
                try {
                    watchlistRepository.addMovie(movie);
                    showNotification("Watchlist", "„" + movie.getTitle() + "“ hinzugefügt.",
                                     2.5, Pos.BOTTOM_RIGHT, NotificationType.CONFIRM);
                } catch (DatabaseException e) {
                    showNotification("Datenbank-Fehler", e.getUserMessage(),
                                     3.0, Pos.BOTTOM_RIGHT, NotificationType.ERROR);
                }
            }
        }, "Add to Watchlist"));

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
        searchBtn.setOnAction(e -> {
            try {
                filterMovies();
            } catch (MovieApiException apiEx) {
                showNotification(
                    "API-Fehler",
                    apiEx.getUserMessage(),
                    3.0,
                    Pos.BOTTOM_RIGHT,
                    NotificationType.ERROR
                );
            }
        });

        sortBtn.setText(Sorting.NAME_ASC.buttonText);
        sortBtn.setOnAction(e -> onSortButtonClicked());
    }


    void filterMovies() throws MovieApiException
    {
        // Retrieve selected filters from user inputs
        String searchText = getSearchText();
        List<Decade> selectedDecades = getSelectedDecades();
        String minRating = getSelectedRating();
        Genre selectedGenre = getSelectedGenre();

        List<Movie> searchedMovies = getMoviesFromAPI(searchText, selectedGenre, selectedDecades, minRating);

        // Fallback: If search text doesn't match with any title,
        // filter descriptions locally
        if (searchText != null && searchedMovies.isEmpty()) {
            List<Movie> moviesWithoutQueryFilter = getMoviesFromAPI(null, selectedGenre, selectedDecades, minRating);
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


    List<Movie> getMoviesFromAPI(String query, Genre genre, List<Decade> selectedDecades, String rating) throws MovieApiException
    {
        List<String> selectedYears = getSelectedYears(selectedDecades);

        // If no decades selected → only 1 API call with null for releaseYear
        if (selectedYears.isEmpty()) {
            return movieAPI.getFilteredMovies(query, genre, null, rating);
        }

        List<Movie> filteredMovies = new ArrayList<>();

        for (String year : selectedYears) {
            List<Movie> response = movieAPI.getFilteredMovies(query, genre, year, rating);
            filteredMovies.addAll(response);
        }

        return filteredMovies;
    }

    private void saveMoviesToDatabase(List<Movie> movies) throws DatabaseException
    {
        MovieRepository movieRepository = MovieRepository.getInstance();
        movieRepository.removeAll();
        movieRepository.addAllMovies(movies);
    }

    private List<Movie> getMoviesFromDatabase() throws DatabaseException  
    {
        MovieRepository movieRepository = MovieRepository.getInstance();
        return MovieEntity.toMovies(movieRepository.getAllMovies());
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

    /*
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
     */


    @FXML
    private void onSortButtonClicked() {
        sortContext.nextState();
        List<Movie> sorted = sortContext.applySort(observableMovies);
        observableMovies.setAll(sorted);
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

    //------------------ NOTIFICATIONS ----------------------
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
