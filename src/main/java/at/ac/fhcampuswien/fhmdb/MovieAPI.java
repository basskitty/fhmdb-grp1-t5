/*

** API DOCUMENATION
https://prog2.fh-campuswien.ac.at/swagger-ui/index.html

** USAGE EXAMPLES
* Get all movies:
List<Movie> allMovies = MovieAPI.getAllMovies();

* Query for "the" in title:
List<Movie> filteredMovies = MovieAPI.getAllMovies("the", null, null, null);

* Get movie data by ID:
Movie movieFromID = MovieAPI.getMovieByID(UUID.fromString("a45e4b03-ece7-49e7-8144-4f2a6fe03432"));

 */

package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MovieAPI {
    public static final String API_BASE_PATH = "https://prog2.fh-campuswien.ac.at/movies";
    public static final OkHttpClient client = new OkHttpClient();
    public static final String URL_DELIMITER = "&";

    public MovieAPI() {
    }

    private static String buildURL(UUID id) {
        StringBuilder url = new StringBuilder(API_BASE_PATH);
        if (id != null) {
            url.append("/").append(id);
        }
        return url.toString();
    }

    private static String buildURL(String query, Genre genre, String year, String rating) {
        StringBuilder url = new StringBuilder(API_BASE_PATH);
        // Check if any parameters are added and build URL
        if ((query != null && !query.isEmpty()) || genre != null || year != null || rating != null) {
            url.append("?");

            if (query != null && !query.isEmpty()) {
                url.append("query=").append(query).append(URL_DELIMITER);
            }
            if (genre != null) {
                url.append("genre=").append(genre.getInternalName()).append(URL_DELIMITER);
            }
            if (year != null) {
                url.append("releaseYear=").append(year).append(URL_DELIMITER);
            }
            if (rating != null) {
                url.append("ratingFrom=").append(rating).append(URL_DELIMITER);
            }
        }
        System.out.println("Query URL: " + url);
        return url.toString();
    }

    // Method to get all movies without filtering
    public static List<Movie> getAllMovies() {
        return getAllMovies(null, null, null, null);
    }

    // Method to get filtered movies
    public static List<Movie> getAllMovies(String query, Genre genre, String year, String rating) {
        Request request = new Request.Builder()
                .header("User-Agent", "http.agent")
                .url(buildURL(query, genre, year, rating))
                .build();

        try (Response response = client.newCall(request).execute()) {
            String jsonResponse = Objects.requireNonNull(response.body()).string();
            Gson gson = new Gson();
            Movie[] movies = gson.fromJson(jsonResponse, Movie[].class);
            System.out.println("Returned movies: " + Arrays.toString(movies));

            return Arrays.asList(movies);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    // Method to get movie by ID
    public static Movie getMovieByID(UUID id) {
        Request request = new Request.Builder()
                .header("User-Agent", "http.agent")
                .url(buildURL(id))
                .build();

        try (Response response = client.newCall(request).execute()) {
            String jsonResponse = Objects.requireNonNull(response.body()).string();
            Gson gson = new Gson();
            Movie movie = gson.fromJson(jsonResponse, Movie.class);
            System.out.println("Returned movie: " + movie);

            return movie;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    }