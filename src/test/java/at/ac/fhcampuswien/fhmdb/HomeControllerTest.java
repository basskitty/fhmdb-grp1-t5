package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Decade;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.Sorting;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
    void getSelectedYears_returns_all_years_in_decade_range() {
        // Given
        Decade nineties = Decade.NINETIES;
        Decade eighties = Decade.EIGHTIES;

        // When
        List<String> years = testController.getSelectedYears(List.of(nineties, eighties));

        // Then
        assertTrue(years.contains("1990"));
        assertTrue(years.contains("1985"));
        assertTrue(years.contains("1999"));
        assertTrue(years.contains("1980"));
        assertEquals(20, years.size());
    }

    @Test
    void getSelectedYears_returns_empty_list_if_input_null_or_empty() {
        assertTrue(testController.getSelectedYears(null).isEmpty());
        assertTrue(testController.getSelectedYears(List.of()).isEmpty());
    }


    @Test
    void filterDescriptionLocally_returns_movies_with_description_matching_searchText() {
        // Given
        Movie movie1 = new Movie("Vagabond",
                "The story of a young drifter found frozen to death in a ditch, told through flashbacks.",
                List.of(), 1985, List.of("Agnès Varda"), List.of());
        Movie movie2 = new Movie("The Revenant",
                "Survival drama", List.of(),
                2015, List.of(), List.of("Leonardo DiCaprio", "Tom Hardy", "Domhnall Gleeson"));

        List<Movie> testMovies = List.of(movie1, movie2);

        // When
        List<Movie> result = testController.filterDescriptionLocally("drifter", testMovies);

        // Then
        assertEquals(List.of(movie1), result);
    }


}