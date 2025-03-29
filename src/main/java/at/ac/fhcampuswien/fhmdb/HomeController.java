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
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    public JFXComboBox<Decade> releaseYearComboBox;
    @FXML
    public JFXComboBox<Double> ratingComboBox;


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
        genreComboBox.setPromptText("Select genre");

        releaseYearComboBox.getItems().addAll(Decade.values());
        releaseYearComboBox.setPromptText("Select release decade");

        // Fill rating combo box (from 1.0 to 10.0)
        ratingComboBox.getItems().addAll(
                1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0
        );
        ratingComboBox.setPromptText("Select minimum rating");


        // SET EVENT LISTENERS
        genreComboBox.setOnAction(e -> filterMovies());

        searchBtn.setOnAction(e -> filterMovies());

        sortBtn.setText(Sorting.NAME_ASC.buttonText);
        sortBtn.setOnAction(e -> sortMovies());
    }



    void filterMovies()
    {
        // Retrieve and normalize search text.
        String searchText = (searchField.getText() != null)
                ? searchField.getText().trim().toLowerCase()
                : "";

        // Retrieve selected decades, rating and genres.
        Decade selectedReleaseDecade = releaseYearComboBox.getValue();
        Double minRating = ratingComboBox.getValue();
        Genre selectedGenre = genreComboBox.getValue();

        List<Movie> filtered = allMovies;

        // If search text is provided, filter by title first, then description if no title matches.
        if (!searchText.isEmpty())
        {
            List<Movie> titleMatches = allMovies.stream()
                    .filter(movie -> movie.getTitle().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
            filtered = !titleMatches.isEmpty()
                    ? titleMatches
                    : allMovies.stream()
                      .filter(movie -> movie.getDescription().toLowerCase().contains(searchText))
                      .collect(Collectors.toList());
        }

        // If a genre is selected, and it is not the ALL_GENRE default, filter further.
        if (selectedGenre != null && !selectedGenre.equals(Genre.ALL_GENRE))
        {
            filtered = filtered.stream()
                    .filter(movie -> movie.getGenres().contains(selectedGenre))
                    .collect(Collectors.toList());
        }

        // Filter by release decade
        if (selectedReleaseDecade != null) {
            int start = selectedReleaseDecade.getStartYear();
            int end = selectedReleaseDecade.getEndYear();
            filtered = filtered.stream()
                    .filter(movie -> movie.getReleaseYear() >= start && movie.getReleaseYear() <= end)
                    .collect(Collectors.toList());
        }

        // Filter by rating
        if(minRating != null) {
            filtered = filtered.stream()
                    .filter(movie -> movie.getRating() >= minRating)
                    .collect(Collectors.toList());
        }

        if (selectedGenre != null && selectedGenre.equals(Genre.ALL_GENRE))
        {
            javafx.application.Platform.runLater(() -> genreComboBox.getSelectionModel().clearSelection());
        }

        // Update the observable list and refresh the ListView.
        observableMovies.setAll(filtered);
        movieListView.setPlaceholder(filtered.isEmpty() ? createPlaceholder() : null);
        movieListView.refresh();
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
}
