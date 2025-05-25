package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;


public class MovieRepository {
    // singleton instance
    private static MovieRepository instance = null;

    // constructor is private to prevent creating new instances
    private MovieRepository() throws DatabaseException{
        this.dao = DatabaseManager.getInstance().getMovieDao();
    }

    // singleton control method
    public static MovieRepository getInstance() throws DatabaseException {
        if (instance == null) {
            instance = new MovieRepository();
        }
        return instance;
    }

    Dao<MovieEntity, Long> dao;

    /**
     * Retrieves all movies from the database.
     *
     * @return a list of all MovieEntity
     * @throws DatabaseException on any SQL error
     */
    public List<MovieEntity> getAllMovies() throws DatabaseException {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new DatabaseException(
                "DB_QUERY_ALL_ERROR",
                "Error querying all movies: " + e.getMessage(),
                "Filme konnten nicht geladen werden.",
                e
            );
        }
    }


     /**
     * Deletes all movies from the database.
     *
     * @return the number of deleted rows
     * @throws DatabaseException on any SQL error
     */
    public int removeAll() throws DatabaseException {
        try {
            return dao.deleteBuilder().delete();
        } catch (SQLException e) {
            throw new DatabaseException(
                "DB_DELETE_ALL_ERROR",
                "Error deleting all movies: " + e.getMessage(),
                "Alle Filme konnten nicht gelöscht werden.",
                e
            );
        }
    }

   
    /**
     * Finds the first movie matching the given API ID.
     *
     * @param apiId the external ID to look up
     * @return the matching MovieEntity, or null if none found
     * @throws DatabaseException on any SQL error
     */
    public MovieEntity getMovie(String apiId) throws DatabaseException {
        try {
            return dao.queryBuilder()
                      .where()
                      .eq("apiId", apiId)
                      .queryForFirst();
        } catch (SQLException e) {
            throw new DatabaseException(
                "DB_QUERY_BY_APIID_ERROR",
                "Error querying movie by apiId='" + apiId + "': " + e.getMessage(),
                "Film konnte nicht gefunden werden.",
                e
            );
        }
    }
    

    public int addAllMovies(List<Movie> movies) throws DatabaseException
    {
        try
        {
            List<MovieEntity> movieEntities = MovieEntity.fromMovies(movies);
            return dao.create(movieEntities);
        }
        catch(SQLException e)
        {
            throw new DatabaseException(
                "DB_INSERT_ERROR",
                "Error inserting movies: " + e.getMessage(),
                "Filme konnten nicht gespeichert werden.",
                e
            );
        }
        
    }


}
