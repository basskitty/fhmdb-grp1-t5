package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@DatabaseTable(tableName = "movies")
public class MovieEntity {
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private String apiId;

    @DatabaseField
    private String title;

    @DatabaseField
    private String description;

    @DatabaseField
    private String genres;

    @DatabaseField
    private int releaseYear;

    @DatabaseField
    private String imgUrl;

    @DatabaseField
    private int lengthInMinutes;

    @DatabaseField
    private double rating;

    public MovieEntity() {}

    public MovieEntity(String apiId, String title, String description, int releaseYear, List<Genre> genres, String imgUrl, int lengthInMinutes, double rating) {
        this.apiId = apiId;
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.genres = genresToString(genres);
        this.imgUrl = imgUrl;
        this.lengthInMinutes = lengthInMinutes;
        this.rating = rating;
    }

    private String genresToString(List<Genre> genres) {
        StringBuilder sb = new StringBuilder();
        for (Genre genre : genres) {
            sb.append(genre.name());
            sb.append(",");
        }
        return sb.toString();
    }

    public static List<MovieEntity> fromMovies(List<Movie> movies) {
        List<MovieEntity> movieEntities = new ArrayList<>();
        for (Movie movie : movies) {
            movieEntities.add(
                    new MovieEntity(
                            movie.getId(),
                            movie.getTitle(),
                            movie.getDescription(),
                            movie.getReleaseYear(),
                            movie.getGenres(),
                            movie.getImgUrl(),
                            movie.getLengthInMinutes(),
                            movie.getRating()));
        }
        return movieEntities;
    }

    public static List<Movie> toMovies(List<MovieEntity> movieEntities) {
        List<Movie> movies = new ArrayList<>();
        for (MovieEntity movieEntity : movieEntities) {
            movies.add(
                    new Movie(
                            movieEntity.getApiId(),
                            movieEntity.getTitle(),
                            movieEntity.getGenres(),
                            movieEntity.getReleaseYear(),
                            movieEntity.getDescription(),
                            movieEntity.getImgUrl(),
                            movieEntity.getLengthInMinutes(),
                            movieEntity.getRating()));
        }
        return movies;
    }

    public String getApiId() {
        return apiId;
    }

    public String getDescription() {
        return description;
    }

    public List<Genre> getGenres() {
        return Arrays.stream(genres.split(",")).map(Genre::valueOf).toList();
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getLengthInMinutes() {
        return lengthInMinutes;
    }

    public double getRating() {
        return rating;
    }

    public String getTitle() {
        return title;
    }
}