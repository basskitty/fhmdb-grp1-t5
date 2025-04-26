package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class WatchlistRepository {
    private static final List<Movie> watchlist = new ArrayList<>();

    public static List<Movie> getWatchlist() {
        return new ArrayList<>(watchlist);
    }

    public static void addMovie(Movie movie) {
        if (!watchlist.contains(movie)) {
            watchlist.add(movie);
        }
    }

    public static void removeMovie(Movie movie) {
        if (watchlist.contains(movie)) {
            watchlist.remove(movie);
        }
    }

    public static void clearWatchlist() {
        watchlist.clear();
    }

}
