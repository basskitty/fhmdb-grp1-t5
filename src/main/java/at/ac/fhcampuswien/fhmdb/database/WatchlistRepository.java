package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WatchlistRepository {
    private final Dao<WatchlistMovieEntity, Long> dao;
 
    public WatchlistRepository() throws DatabaseException{
        this.dao = DatabaseManager.getInstance().getWatchlistDao();
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

    public void addMovie(Movie movie) throws DatabaseException{
        try {
            WatchlistMovieEntity existing = dao.queryBuilder()
                    .where()
                    .eq("apiId", movie.getId())
                    .queryForFirst();

            if (existing == null) {
                WatchlistMovieEntity entity = new WatchlistMovieEntity(movie.getId());
                dao.create(entity);
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
            MovieRepository movieRepository = new MovieRepository();

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
