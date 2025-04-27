package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WatchlistRepository {
    Dao<WatchlistMovieEntity, Long> dao;

    public WatchlistRepository() {
        this.dao = dao;
    }

//    public List<WatchlistMovieEntity> getWatchlist() {
//        try {
//            return dao.queryForAll();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    public int addToWatchlist(WatchlistMovieEntity movie) {
        try {
            return dao.create(movie);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int removeFromWatchlist(String apiId) {
        try {
            return dao.delete(dao.queryBuilder().where().eq("apiId", apiId).query());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

 // OLD CODE WITHOUT DATABASE
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
