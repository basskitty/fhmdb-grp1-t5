/**
 * API DOCUMENTATION
 * {@link https://prog2.fh-campuswien.ac.at/swagger-ui/index.html}
 */

package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

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

    public MovieAPI() 
    {
    }

    private String buildURL(UUID id) 
    {
        StringBuilder url = new StringBuilder(API_BASE_PATH);
        
        if (id != null) 
        {
            url.append("/").append(id);
        }

        return url.toString();
    }

    private String buildURL(String query, Genre genre, String year, String rating)
    {
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

    /**
     * Get all movies without filtering
     */
    public List<Movie> getAllMovies() throws MovieApiException{
        return getFilteredMovies(null, null, null, null);
    }

    /**
     * Get filtered movies<br/>
     * e.g. movies with "the" in title: MovieAPI.getAllMovies("the", null, null, null);
      */
    public List<Movie> getFilteredMovies(String query, Genre genre, String year, String rating) throws MovieApiException
    {
        Request request = new Request.Builder()
                .header("User-Agent", "http.agent")
                .url(buildURL(query, genre, year, rating))
                .build();

        return parseMovies(request);
    }

    private List<Movie> parseMovies(Request request) throws MovieApiException
    {
        try (Response response = client.newCall(request).execute())
        {
            // 1) HTTP error
            if (!response.isSuccessful()) 
            {
                int code = response.code();

                throw new MovieApiException(
                    "API_HTTP_" + code,
                    "Nicht erfolgreiche Antwort vom Movie Service: HTTP " + code,
                    "Filme konnten nicht geladen werden (Server Fehler)."
                );
            }

            // 2) Body absent
            if (response.body() == null) 
            {
                throw new MovieApiException(
                    "API_NO_BODY",
                    "Leerer Response Body von Movie Service",
                    "Fehlerhafte Antwort vom Filmdienst."
                );
            }


            String json = response.body().string();
            try 
            {
                Movie[] movies = new Gson().fromJson(json, Movie[].class);
                return Arrays.asList(movies);
            } 
            catch (JsonSyntaxException ex) 
            {
                throw new MovieApiException(
                    "API_PARSE_ERROR",
                    "JSON Parsing Fehler: " + ex.getMessage(),
                    "Fehler beim Verarbeiten der Filmdaten."
                , ex);
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get movie by ID
     * @param id ID from the Movie Database
     * @return Movie object
     */
    public Movie getMovieByID(UUID id) throws MovieApiException {
        Request request = new Request.Builder()
                .header("User-Agent", "http.agent")
                .url(buildURL(Objects.requireNonNull(id)))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                int code = response.code();
                throw new MovieApiException(
                    "API_HTTP_" + code,
                    "Nicht‐erfolgreiche Antwort vom Movie‐Service: HTTP " + code,
                    "Filmdetails konnten nicht geladen werden (Server‐Fehler)."
                );
            }
            if (response.body() == null) {
                throw new MovieApiException(
                    "API_NO_BODY",
                    "Leerer Response‐Body von Movie‐Service",
                    "Filmdetails konnten nicht geladen werden."
                );
            }
            String json = response.body().string();
            try {
                return new Gson().fromJson(json, Movie.class);
            } catch (JsonSyntaxException ex) {
                throw new MovieApiException(
                    "API_PARSE_ERROR",
                    "JSON‐Parsing Fehler: " + ex.getMessage(),
                    "Fehler beim Verarbeiten der Filmdaten."
                , ex);
            }
        } catch (IOException ex) {
            throw new MovieApiException(
                "API_IO_ERROR",
                "I/O Fehler beim API‐Aufruf: " + ex.getMessage(),
                "Konnte nicht mit dem Filmdienst verbinden."
            , ex);
        }
    }
}