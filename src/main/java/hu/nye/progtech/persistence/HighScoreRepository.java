package hu.nye.progtech.persistence;

import hu.nye.progtech.model.HighScore;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * A játékosok és győzelmeik tárolása adatbázisban, JDBC-vel.
 * Minden metódus saját kapcsolatot nyit, amit a try-with-resources automatikusan lezár.
 */
public class HighScoreRepository {

    private static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS players (name VARCHAR(100) PRIMARY KEY, wins INT NOT NULL)";
    private static final String COUNT_PLAYER = "SELECT COUNT(*) FROM players WHERE name = ?";
    private static final String INSERT_PLAYER = "INSERT INTO players (name, wins) VALUES (?, 0)";
    private static final String ADD_WIN = "UPDATE players SET wins = wins + 1 WHERE name = ?";
    private static final String SELECT_ALL = "SELECT name, wins FROM players ORDER BY wins DESC, name";

    private final String url;

    public HighScoreRepository(String url) {
        this.url = url;
    }

    /**
     * Létrehozza a táblát, ha még nem létezik.
     */
    public void createTableIfMissing() {
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_TABLE);
        } catch (SQLException e) {
            throw new DatabaseException("Nem sikerült létrehozni a táblát.", e);
        }
    }

    /**
     * Elmenti a játékost 0 győzelemmel, ha még nincs az adatbázisban.
     */
    public void addPlayerIfMissing(String name) {
        try (Connection connection = DriverManager.getConnection(url)) {
            if (!playerExists(connection, name)) {
                try (PreparedStatement insert = connection.prepareStatement(INSERT_PLAYER)) {
                    insert.setString(1, name);
                    insert.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Nem sikerült menteni a játékost.", e);
        }
    }

    /**
     * Eggyel növeli a játékos győzelmeinek számát.
     */
    public void addWin(String name) {
        addPlayerIfMissing(name);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement update = connection.prepareStatement(ADD_WIN)) {
            update.setString(1, name);
            update.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Nem sikerült menteni a győzelmet.", e);
        }
    }

    /**
     * Visszaadja az összes játékost, a legtöbb győzelemmel rendelkezővel kezdve.
     */
    public List<HighScore> findAll() {
        List<HighScore> scores = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {
            while (resultSet.next()) {
                scores.add(new HighScore(resultSet.getString("name"), resultSet.getInt("wins")));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Nem sikerült beolvasni a ranglistát.", e);
        }
        return scores;
    }

    private boolean playerExists(Connection connection, String name) throws SQLException {
        try (PreparedStatement select = connection.prepareStatement(COUNT_PLAYER)) {
            select.setString(1, name);
            try (ResultSet resultSet = select.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        }
    }
}
