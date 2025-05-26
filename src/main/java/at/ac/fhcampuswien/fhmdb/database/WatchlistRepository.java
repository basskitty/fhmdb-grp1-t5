package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.observer.Observable;
import at.ac.fhcampuswien.fhmdb.observer.Observer;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WatchlistRepository  implements Observable<Movie> {
    // singleton instance
    private static WatchlistRepository instance = null;

    // list of registered observers
    private final List<Observer<Movie>> observers = new ArrayList<>();

    // constructor is private to prevent creating new instances
    private WatchlistRepository() throws DatabaseException{
        this.dao = DatabaseManager.getInstance().getWatchlistDao();
    }

    // singleton control method
    public static WatchlistRepository getInstance() throws DatabaseException {
        if (instance == null) {
            instance = new WatchlistRepository();
        }
        return instance;
    }

    private final Dao<WatchlistMovieEntity, Long> dao;

    // Observable<Movie> methods
    @Override
    public void addObserver(Observer<Movie> observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer<Movie> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Movie data, boolean success, String message) {
        for (Observer<Movie> obs : observers) {
            obs.update(data, success, message);
        }
    }

    public List<WatchlistMovieEntity> getWatchlist() throws DatabaseException{
        try {
            return dao.queryForAll();
        } catch (SQLException  e) {
            throw new DatabaseException(
                "DB_WATCHLIST_QUERY_ERROR",
                "Error querying watchlist: " + e.getMessage(),
                "Ihre Watchlist konnte nicht geladen werden.",
                e
            );
        }
    }

    public void addMovie(Movie movie) throws DatabaseException {
        try {
            WatchlistMovieEntity existing = dao.queryBuilder()
                    .where()
                    .eq("apiId", movie.getId())
                    .queryForFirst();

            if (existing == null) {
                WatchlistMovieEntity entity = new WatchlistMovieEntity(movie.getId());
                dao.create(entity);
                notifyObservers(movie, true, "Film erfolgreich zur Watchlist hinzugefügt.");
            } else {
                // already in watchlist
                notifyObservers(movie, false, "Film ist bereits in Ihrer Watchlist.");
            }
        } catch (SQLException e) {
            throw new DatabaseException(
                    "DB_WATCHLIST_ADD_ERROR",
                    "Error adding movie to watchlist: " + e.getMessage(),
                    "Der Film konnte nicht Ihrer Watchlist hinzugefügt werden.",
                    e
            );
        }
    }

    public void removeMovie(Movie movie) throws DatabaseException {
        try {
            List<WatchlistMovieEntity> moviesToDelete = dao.queryBuilder()
                    .where()
                    .eq("apiId", movie.getId())
                    .query();

            dao.delete(moviesToDelete);
            // notify removal as success
            notifyObservers(movie, true, "Film aus der Watchlist entfernt.");
        } catch (SQLException e) {
            throw new DatabaseException(
                    "DB_WATCHLIST_REMOVE_ERROR",
                    "Error removing movie from watchlist: " + e.getMessage(),
                    "Der Film konnte nicht aus Ihrer Watchlist entfernt werden.",
                    e
            );
        }
    }

    public List<Movie> getMoviesFromWatchlist() throws DatabaseException{
        try {
            List<WatchlistMovieEntity> entities = dao.queryForAll();
            MovieRepository movieRepository = MovieRepository.getInstance();

            List<Movie> movies = new ArrayList<>();
            for (WatchlistMovieEntity entity : entities) {
                MovieEntity movieEntity = movieRepository.getMovie(entity.getApiId());
                if (movieEntity != null) {
                    movies.add(new Movie(
                            movieEntity.getApiId(),
                            movieEntity.getTitle(),
                            movieEntity.getGenres(),
                            movieEntity.getReleaseYear(),
                            movieEntity.getDescription(),
                            movieEntity.getImgUrl(),
                            movieEntity.getLengthInMinutes(),
                            movieEntity.getRating()
                    ));
                }
            }
            return movies;
        } 
        catch (SQLException e) 
        {
            throw new DatabaseException(
                "DB_WATCHLIST_LOAD_ERROR",
                "Error loading watchlist movies: " + e.getMessage(),
                "Ihre Watchlist-Filme konnten nicht geladen werden.",
                e
            );
        }
    }
}
