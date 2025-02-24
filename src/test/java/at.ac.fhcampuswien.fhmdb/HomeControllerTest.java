package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class HomeControllerTest extends ApplicationTest {

    private HomeController controller;

    @Override
    public void start(Stage stage) throws Exception {
        // Adjust the path to your FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @BeforeEach
    public void setUp() {
        // Reset the controller to its initial state before each test if needed.
        // For instance, you can ensure the observableMovies list contains all movies.
        controller.observableMovies.setAll(controller.allMovies);
    }

    @Test
    public void testFilterBySearchText_TitleMatches() {
        // Set search text that matches a known movie title
        interact(() -> controller.searchField.setText("matrix")); // for example, "The Matrix"
        interact(() -> controller.filterMovies());

        // Check that the filtered observableMovies contains only movies with "matrix" in the title.
        List<Movie> filtered = controller.observableMovies;
        assertFalse(filtered.isEmpty(), "Filtered list should not be empty.");
        for (Movie movie : filtered) {
            assertTrue(movie.getTitle().toLowerCase().contains("matrix"));
        }
    }

    @Test
    public void testFilterBySearchText_DescriptionMatches() {
        // Set search text that does not match any title, but matches a description.
        interact(() -> controller.searchField.setText("avada")); // for example, from "Harry Potter"
        interact(() -> controller.filterMovies());

        List<Movie> filtered = controller.observableMovies;
        assertFalse(filtered.isEmpty(), "Filtered list should not be empty.");
        // Since this search falls back to description matching, ensure at least one movie's description contains it.
        boolean found = filtered.stream().anyMatch(movie -> movie.getDescription().toLowerCase().contains("avada"));
        assertTrue(found, "At least one movie should match the description search.");
    }

    @Test
    public void testFilterByGenre_AllGenre() {
        // Assume Genre.ALL_GENRE exists and should display all movies.
        interact(() -> controller.genreComboBox.getSelectionModel().select(Genre.ALL_GENRE));
        interact(() -> controller.filterMovies());

        List<Movie> filtered = controller.observableMovies;
        assertEquals(controller.allMovies.size(), filtered.size(), "All movies should be shown when ALL_GENRE is selected.");
    }

    @Test
    public void testFilterBySearchAndGenre() {
        // Set a search text and a specific genre (other than ALL_GENRE)
        interact(() -> controller.searchField.setText("american"));
        interact(() -> controller.genreComboBox.getSelectionModel().select(Genre.COMEDY));
        interact(() -> controller.filterMovies());

        List<Movie> filtered = controller.observableMovies;
        // Each movie should contain the text in title or description AND have the COMEDY genre.
        assertFalse(filtered.isEmpty(), "Filtered list should not be empty.");
        for (Movie movie : filtered) {
            boolean textMatch = movie.getTitle().toLowerCase().contains("american") ||
                    movie.getDescription().toLowerCase().contains("american");
            assertTrue(textMatch, "Movie should match search text.");
            assertTrue(movie.getGenres().contains(Genre.COMEDY), "Movie should contain COMEDY genre.");
        }
    }
}