package at.ac.fhcampuswien.fhmdb.models;

import java.util.*;

public class Movie {
    private final String title;
    private final String description;
    private final List<Genre> genres;
    private final String id;
    private final int releaseYear;
    private final String imgUrl;
    private final int lengthInMinutes;
    private final double rating;
    private final List<String> directors = new ArrayList<>();
    private final List<String> mainCast = new ArrayList<>();
    private final List<String> writers = new ArrayList<>();

    // Constructor for manual creation
    public Movie(String title, String description, List<Genre> genres) {
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.id = null;
        this.releaseYear = 0;
        this.imgUrl = null;
        this.lengthInMinutes = 0;
        this.rating = 0;
    }

    // Constructor for MovieAPI
    public Movie(String id, String title, List<Genre> genres, int releaseYear, String description, String imgUrl, int lengthInMinutes, double rating) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.releaseYear = releaseYear;
        this.imgUrl = imgUrl;
        this.lengthInMinutes = lengthInMinutes;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Genre> getGenres(){ return genres;}

    public String getId() {
        return id;
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

    public List<String> getDirectors() {
        return directors;
    }

    public List<String> getMainCast() {
        return mainCast;
    }

    public List<String> getWriters() {
        return writers;
    }

    public static List<Movie> initializeMovies(){
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Better Man", "Robbie Williams, starred by an ape.", Arrays.asList(Genre.DRAMA, Genre.BIOGRAPHY)));
        movies.add(new Movie("Harry Potter", "Avada Kedavra!", Arrays.asList(Genre.FANTASY, Genre.DRAMA)));
        movies.add(new Movie("Barbie", "I'm a Barbie Girl, in a Barbie World..", Arrays.asList(Genre.ADVENTURE)));
        movies.add(new Movie("The Matrix", "MISTER ANDERSON!", Arrays.asList(Genre.SCIENCE_FICTION, Genre.ACTION)));
        movies.add(new Movie ("American Pie", "Warm as an apple pie.", Arrays.asList(Genre.COMEDY)));
        movies.add(new Movie ("The Lobster", "Yorgos Lanthimos becoming a shrimp.", Arrays.asList(Genre.DRAMA)));
        return movies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return Objects.equals(getTitle(), movie.getTitle()) &&
                Objects.equals(getDescription(), movie.getDescription()) &&
                Objects.equals(getGenres(), movie.getGenres());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getDescription(), getGenres());
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
