package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.Sorting;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {
    private static HomeController testController;
    @BeforeAll
    static void init() {
        testController = new HomeController();
    }

    @Test
    public void sort_movies_ascending() {
        testController.initializeData();
        testController.sortMovies(Sorting.ASCENDING);

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
        testController.initializeData();
        testController.sortMovies(Sorting.DESCENDING);

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