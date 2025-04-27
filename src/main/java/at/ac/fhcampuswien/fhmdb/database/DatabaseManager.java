package at.ac.fhcampuswien.fhmdb.database;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;

import org.h2.engine.Database;

import java.sql.SQLException;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:h2:file:./db/fhmdb";
    private static final String USER        = "dbuser";
    private static final String PASSWORD    = "dbpassword";

    private static ConnectionSource connectionSource;
    private static DatabaseManager instance;

    private final Dao<MovieEntity, Long>         movieDao;
    private final Dao<WatchlistMovieEntity, Long> watchlistDao;

    /**
     * Initialize the connection, tables, and DAOs.
     * @throws DatabaseException on any SQL error
     */
    private DatabaseManager() throws DatabaseException {
        try {
            initConnection();
            createTables();
            this.movieDao     = DaoManager.createDao(connectionSource, MovieEntity.class);
            this.watchlistDao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
        } catch (SQLException e) {
            throw new DatabaseException(
                "DB_INIT_ERROR",
                "Error initializing database: " + e.getMessage(),
                "Datenbank konnte nicht initialisiert werden.",
                e
            );
        }
    }

    /** Lazily create and return the singleton instance */
    public static synchronized DatabaseManager getInstance() throws DatabaseException {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /** (Re)create the JDBC connection source */
    private void initConnection() throws DatabaseException {
        try {
            connectionSource = new JdbcConnectionSource(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new DatabaseException(
                "DB_CONN_ERROR",
                "Cannot open connection to DB: " + e.getMessage(),
                "Fehler bei der Verbindung zur Datenbank.",
                e
            );
        }
    }

    /** Ensure our tables exist */
    private void createTables() throws DatabaseException {
        try {
            TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, WatchlistMovieEntity.class);
        } catch (SQLException e) {
            throw new DatabaseException(
                "DB_TABLE_ERROR",
                "Cannot create tables: " + e.getMessage(),
                "Datenbanktabellen konnten nicht angelegt werden.",
                e
            );
        }
    }

    /** Expose the movie DAO */
    public Dao<MovieEntity, Long> getMovieDao() {
        return movieDao;
    }

    /** Expose the watchlist DAO */
    public Dao<WatchlistMovieEntity, Long> getWatchlistDao() {
        return watchlistDao;
    }
}
