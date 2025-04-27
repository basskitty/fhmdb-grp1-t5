package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;


public class MovieRepository {
    Dao<MovieEntity, Long> dao;

    public MovieRepository() {
        this.dao = DatabaseManager.getInstance().getMovieDao();
    }

    public List<MovieEntity> getAllMovies() throws SQLException {
        return dao.queryForAll();
    }

    public int removeAll() {
        try {
            return dao.deleteBuilder().delete();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MovieEntity getMovie(String apiId) throws SQLException {
        return dao.queryBuilder().where().eq("apiId", apiId).queryForFirst();
    }

    public int addAllMovies(List<Movie> movies) throws SQLException {
        List<MovieEntity> movieEntities = MovieEntity.fromMovies(movies);
        return dao.create(movieEntities);
    }


}
