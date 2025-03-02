package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.scene.control.Label;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MovieCellTest extends ApplicationTest {

    private MovieCell cell;

    @Override
    public void start(Stage stage) {
        cell = new MovieCell();
    }

    @Test
    void movieCell_showsGenresCorrectly() {
        MovieCell cell = new MovieCell();
        Movie testMovie = new Movie("Test Movie", "Some description",
                Arrays.asList(Genre.ACTION, Genre.COMEDY));

        cell.updateItem(testMovie, false);

        VBox layout = (VBox) cell.getGraphic();
        Label genresLabel = (Label) layout.getChildren().get(2);

        assertNotNull(genresLabel, "Genres-Label should exist");
        assertEquals("ACTION, COMEDY", genresLabel.getText());
    }

    @Test
    public void movieCell_showsNoGenresIfEmpty() {
        MovieCell cell = new MovieCell();
        Movie testMovie = new Movie("Genreless Movie", "Some description");

        cell.updateItem(testMovie, false);

        VBox layout = (VBox) cell.getGraphic();
        Label genresLabel = (Label) layout.getChildren().get(2);

        assertNotNull(genresLabel, "Genres-Label should exist");
        assertEquals("No genres available", genresLabel.getText());
    }
}