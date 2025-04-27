package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WatchlistRepository {
    private final Dao<WatchlistMovieEntity, Long> dao;

    public WatchlistRepository() {
        this.dao = DatabaseManager.getInstance().getWatchlistDao();
    }

    public List<WatchlistMovieEntity> getWatchlist() {
        try {
            return dao.queryForAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addMovie(Movie movie) {
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
            throw new RuntimeException(e);
        }
    }

    public void removeMovie(Movie movie) {
        try {
            List<WatchlistMovieEntity> moviesToDelete = dao.queryBuilder()
                    .where()
                    .eq("apiId", movie.getId())
                    .query();

            dao.delete(moviesToDelete);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Movie> getMoviesFromWatchlist() {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
