package at.ac.fhcampuswien.fhmdb.database;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.h2.engine.Database;

import java.sql.SQLException;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:h2:file:./db/fhmdb";
    private static final String username = "dbuser";
    private static final String password = "dbpassword";

    private static ConnectionSource connectionSource;
    private static DatabaseManager instance;

    Dao<MovieEntity, Long> movieDao;
    Dao<WatchlistMovieEntity, Long> watchlistDao;

    public DatabaseManager() {
        try {
            createConnectionSource();
            createTables();
            movieDao = DaoManager.createDao(getConnectionSource(), MovieEntity.class);
            watchlistDao = DaoManager.createDao(getConnectionSource(), WatchlistMovieEntity.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createConnectionSource() throws SQLException {
            connectionSource = new JdbcConnectionSource(DB_URL, username, password);
            }

    private ConnectionSource getConnectionSource() {
        if (connectionSource == null) {
            try {
                createConnectionSource();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connectionSource;
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void createTables() throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, WatchlistMovieEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);
    }

    public Dao<WatchlistMovieEntity, Long> getWatchlistDao() {
        return watchlistDao;
    }

    public Dao<MovieEntity, Long> getMovieDao() {
        return movieDao;
    }
}
