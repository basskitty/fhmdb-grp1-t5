package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MovieUtilsTest {

    private Movie movie1;
    private Movie movie2;
    private Movie movie3;
    private Movie movie4;
    private Movie movie5;
    private Movie movie6;
    private Movie movie7;

    @BeforeEach
    void setup() {
        movie1 = new Movie("Inception", "Mind-bending thriller", List.of(), 2010, List.of());
        movie1.getMainCast().addAll(List.of("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Tom Hardy"));

        movie2 = new Movie("The Revenant", "Survival drama", List.of(), 2015, List.of());
        movie2.getMainCast().addAll(List.of("Leonardo DiCaprio", "Tom Hardy", "Domhnall Gleeson"));

        movie3 = new Movie("500 Days of Summer",
                "Romantic comedy", List.of(),
                2009, List.of("Joseph Gordon-Levitt", "Zooey Deschanel"));

        movie4 = new Movie("The Godfather",
                "Patriarch of an organized crime dynasty",
                List.of(), 1972, List.of("Francis Ford Coppola"));

        movie5 = new Movie("Cléo from 5 to 7",
                "A young singer wanders through Paris awaiting the results of a medical test.",
                List.of(), 1962, List.of("Agnès Varda"));

        movie6 = new Movie("Vagabond",
                "The story of a young drifter found frozen to death in a ditch, told through flashbacks.",
                List.of(), 1985, List.of("Agnès Varda"));

        movie7 = new Movie("Lions Love (... and Lies)",
                "An experimental look at Hollywood and the counterculture.",
                List.of(), 1969, List.of("Agnès Varda", "Shirley Clarke"));
    }

    @Test
    void most_popular_actor_should_return_correct_actor() {
        // Given
        List<Movie> movies = List.of(movie1, movie2, movie3);

        // When
        String mostPopular = MovieUtils.getMostPopularActor(movies);

        // Then
        assertEquals("Leonardo DiCaprio", mostPopular);  // Er kommt in 2 Filmen vor
    }

    @Test
    void count_movies_from_returns_correct_movie_amount_from_given_director() {
        // Given
        List<Movie> movies = List.of(movie1, movie2, movie3, movie4, movie5, movie6, movie7);

        // When
        long movieCount = MovieUtils.countMoviesFrom(movies, "Francis Ford Coppola");

        // Then
        assertEquals(1, movieCount);
    }

    @Test
    void count_movies_from_returns_0_if_no_movies_from_director_in_database() {
        // Given
        List<Movie> movies = List.of(movie1, movie2, movie3, movie4, movie5, movie6, movie7);

        // When
        long movieCount = MovieUtils.countMoviesFrom(movies, "Anna Berthold");

        // Then
        assertEquals(0, movieCount);
    }


    @Test
    void get_movies_between_years_returns_correct_movies() {
        // Given
        List<Movie> movies = List.of(movie1, movie2, movie3, movie4, movie5, movie6, movie7);

        // When
        List<Movie> moviesBetween = MovieUtils.getMoviesBetweenYears(movies, 1968, 1973);

        // Then
        List<Movie> expectedMovies = List.of(movie4, movie7);
        assertEquals(expectedMovies, moviesBetween);
    }

}
