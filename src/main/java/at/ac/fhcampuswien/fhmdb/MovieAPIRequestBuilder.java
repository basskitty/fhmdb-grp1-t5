package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;

import java.util.UUID;

public class MovieAPIRequestBuilder {
    private static final String API_BASE_PATH = "https://prog2.fh-campuswien.ac.at/movies";
    private static final String URL_DELIMITER = "&";

    private UUID id;
    private String query;
    private Genre genre;
    private String year;
    private String rating;

    // Private constructor to enforce the use of the static factory method
    private MovieAPIRequestBuilder() {
    }

    public static MovieAPIRequestBuilder newBuilder() {
        return new MovieAPIRequestBuilder();
    }

    public MovieAPIRequestBuilder id(UUID id) {
        this.id = id;
        return this;
    }

    public MovieAPIRequestBuilder query(String query) {
        this.query = query;
        return this;
    }

    public MovieAPIRequestBuilder genre(Genre genre) {
        this.genre = genre;
        return this;
    }

    public MovieAPIRequestBuilder year(String year) {
        this.year = year;
        return this;
    }

    public MovieAPIRequestBuilder rating(String rating) {
        this.rating = rating;
        return this;
    }

    public String build() {
        StringBuilder url = new StringBuilder(API_BASE_PATH);

        // If ID is set, append it to the base path (ID-based request)
        if (id != null) {
            url.append("/").append(id);
            return url.toString();
        }

        // Otherwise check if any filter parameters are provided
        boolean hasParameters = false;

        if ((query != null && !query.isEmpty()) || genre != null || year != null || rating != null) {
            url.append("?");
            hasParameters = true;

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

            // Remove trailing delimiter if present
            if (hasParameters) {
                int length = url.length();
                if (length > 0 && url.charAt(length - 1) == URL_DELIMITER.charAt(0)) {
                    url.deleteCharAt(length - 1);
                }
            }
        }

        String finalUrl = url.toString();
        System.out.println("Query URL: " + finalUrl);
        return finalUrl;
    }
}