package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Decade;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.Sorting;
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
import org.controlsfx.control.CheckComboBox;

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

    // Public List<Movie> allMovies = Movie.initializeMovies();
    public List<Movie> allMovies = MovieAPI.getAllMovies();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // LOAD MOVIES
        // Load all Movies & Set List View to observable List
        observableMovies.addAll(allMovies);

        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(listView -> new MovieCell());

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
    }


    void filterMovies()
    {
        // Retrieve selected filters from user inputs
        String searchText = getSearchText();
        List<Decade> selectedDecades = getSelectedDecades();
        String minRating = getSelectedRating();
        Genre selectedGenre = getSelectedGenre();

        List<Movie> searchedMovies = getMoviesfromAPI(searchText, selectedGenre, selectedDecades, minRating);

        // Fallback: If search text doesn't match with any title,
        // filter descriptions locally
        if (searchText != null && searchedMovies.isEmpty()) {
            searchedMovies = filterDescriptionLocally(searchText, selectedGenre, selectedDecades, minRating);
        }

        // Update the observable list and refresh the ListView.
        observableMovies.setAll(searchedMovies);
        movieListView.setPlaceholder(searchedMovies.isEmpty() ? createPlaceholder() : null);
        movieListView.refresh();
    }


    List<Movie> getMoviesfromAPI(String query, Genre genre, List<Decade> selectedDecades, String rating)
    {
        List<String> selectedYears = getSelectedYears(selectedDecades);

        // If no decades selected → only 1 API call with null for releaseYear
        if (selectedYears.isEmpty()) {
            return MovieAPI.getfilteredMovies(query, genre, null, rating);
        }

        List<Movie> filteredMovies = new ArrayList<>();

        for (String year : selectedYears) {
            List<Movie> response = MovieAPI.getfilteredMovies(query, genre, year, rating);
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

    private List<String> getSelectedYears(List<Decade> selectedDecades) {
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
    private List<Movie> filterDescriptionLocally(String inputSearch, Genre selectedGenre, List<Decade> selectedDecades, String minRating) {
            List<Movie> allMoviesNoSearch = getMoviesfromAPI(null, selectedGenre, selectedDecades, minRating);
            return allMoviesNoSearch.stream()
                    .filter(movie -> movie.getDescription().toLowerCase().contains(inputSearch))
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

}
