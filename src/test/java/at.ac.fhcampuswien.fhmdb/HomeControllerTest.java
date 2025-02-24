package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.Sorting;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class HomeControllerTest extends ApplicationTest {

    private HomeController testController;

    @Override
    public void start(Stage stage) throws Exception {
        // Adjust the path to your FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
        Parent root = loader.load();
        testController = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @BeforeEach
    public void setUp() {
        // Reset the controller to its initial state before each test if needed.
        // For instance, you can ensure the observableMovies list contains all movies.
        testController.observableMovies.setAll(testController.allMovies);
    }

    @Test
    public void testFilterBySearchText_TitleMatches() {
        // Set search text that matches a known movie title
        interact(() -> testController.searchField.setText("matrix")); // for example, "The Matrix"
        interact(() -> testController.filterMovies());

        // Check that the filtered observableMovies contains only movies with "matrix" in the title.
        List<Movie> filtered = testController.observableMovies;
        assertFalse(filtered.isEmpty(), "Filtered list should not be empty.");
        for (Movie movie : filtered) {
            assertTrue(movie.getTitle().toLowerCase().contains("matrix"));
        }
    }

    @Test
    public void testFilterBySearchText_DescriptionMatches() {
        // Set search text that does not match any title, but matches a description.
        interact(() -> testController.searchField.setText("avada")); // for example, from "Harry Potter"
        interact(() -> testController.filterMovies());

        List<Movie> filtered = testController.observableMovies;
        assertFalse(filtered.isEmpty(), "Filtered list should not be empty.");
        // Since this search falls back to description matching, ensure at least one movie's description contains it.
        boolean found = filtered.stream().anyMatch(movie -> movie.getDescription().toLowerCase().contains("avada"));
        assertTrue(found, "At least one movie should match the description search.");
    }

    @Test
    public void testFilterByGenre_AllGenre() {
        // Assume Genre.ALL_GENRE exists and should display all movies.
        interact(() -> testController.genreComboBox.getSelectionModel().select(Genre.ALL_GENRE));
        interact(() -> testController.filterMovies());

        List<Movie> filtered = testController.observableMovies;
        assertEquals(testController.allMovies.size(), filtered.size(), "All movies should be shown when ALL_GENRE is selected.");
    }

    @Test
    public void testFilterBySearchAndGenre() {
        // Set a search text and a specific genre (other than ALL_GENRE)
        interact(() -> testController.searchField.setText("american"));
        interact(() -> testController.genreComboBox.getSelectionModel().select(Genre.COMEDY));
        interact(() -> testController.filterMovies());

        List<Movie> filtered = testController.observableMovies;
        // Each movie should contain the text in title or description AND have the COMEDY genre.
        assertFalse(filtered.isEmpty(), "Filtered list should not be empty.");
        for (Movie movie : filtered) {
            boolean textMatch = movie.getTitle().toLowerCase().contains("american") ||
                    movie.getDescription().toLowerCase().contains("american");
            assertTrue(textMatch, "Movie should match search text.");
            assertTrue(movie.getGenres().contains(Genre.COMEDY), "Movie should contain COMEDY genre.");
        }
    }



    @Test
    public void sort_movies_ascending() {
        interact(() -> {
            testController.sortBtn.setText(Sorting.NAME_ASC.buttonText);
            testController.sortMovies();
        });
        List<Movie> expected = Arrays.asList(
                new Movie ("American Pie", "Warm as an apple pie.", Arrays.asList(Genre.COMEDY)),
                new Movie("Barbie", "I'm a Barbie Girl, in a Barbie World..", Arrays.asList(Genre.ADVENTURE)),
                new Movie("Better Man", "Robbie Williams, starred by an ape.", Arrays.asList(Genre.DRAMA, Genre.BIOGRAPHY)),
                new Movie("Harry Potter", "Avada Kedavra!", Arrays.asList(Genre.FANTASY, Genre.DRAMA)),
                new Movie("The Matrix", "MISTER ANDERSON!", Arrays.asList(Genre.SCIENCE_FICTION, Genre.ACTION))
        );

        assertEquals(expected, testController.observableMovies);
    }

    @Test
    public void sort_movies_descending() {
        interact(() -> {
            testController.sortBtn.setText(Sorting.NAME_DESC.buttonText);
            testController.sortMovies();
        });

        List<Movie> expected = Arrays.asList(
                new Movie("The Matrix", "MISTER ANDERSON!", Arrays.asList(Genre.SCIENCE_FICTION, Genre.ACTION)),
                new Movie("Harry Potter", "Avada Kedavra!", Arrays.asList(Genre.FANTASY, Genre.DRAMA)),
                new Movie("Better Man", "Robbie Williams, starred by an ape.", Arrays.asList(Genre.DRAMA, Genre.BIOGRAPHY)),
                new Movie("Barbie", "I'm a Barbie Girl, in a Barbie World..", Arrays.asList(Genre.ADVENTURE)),
                new Movie("American Pie", "Warm as an apple pie.", Arrays.asList(Genre.COMEDY))
        );

        assertEquals(expected, testController.observableMovies);
    }
}