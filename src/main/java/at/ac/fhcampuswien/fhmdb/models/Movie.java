package at.ac.fhcampuswien.fhmdb.models;

import java.util.*;

public class Movie {
    private final String title;
    private final String description;
    private final List<Genre> genres;

    public Movie(String title, String description, List<Genre> genres) {
        this.title = title;
        this.description = description;
        this.genres= genres;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Genre> getGenres(){ return genres;}

    public static List<Movie> initializeMovies(){
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Better Man", "Robbie Williams, starred by an ape.", Arrays.asList(Genre.DRAMA, Genre.BIOGRAPHY)));
        movies.add(new Movie("Harry Potter", "Avada Kedavra!", Arrays.asList(Genre.FANTASY, Genre.DRAMA)));
        movies.add(new Movie("Barbie", "I'm a Barbie Girl, in a Barbie World..", Arrays.asList(Genre.ADVENTURE)));
        movies.add(new Movie("The Matrix", "MISTER ANDERSON!", Arrays.asList(Genre.SCIENCE_FICTION, Genre.ACTION)));
        movies.add(new Movie ("American Pie", "Warm as an apple pie.", Arrays.asList(Genre.COMEDY)));

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


}
