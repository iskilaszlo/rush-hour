package hu.nye.progtech.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import hu.nye.progtech.model.HighScore;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HighScoreRepositoryTest {

    private HighScoreRepository repository;

    @BeforeEach
    void setUp() {
        // minden teszt saját, memóriában lévő adatbázist kap
        repository = new HighScoreRepository("jdbc:h2:mem:test" + System.nanoTime() + ";DB_CLOSE_DELAY=-1");
        repository.createTableIfMissing();
    }

    @Test
    void addPlayerIfMissingShouldSavePlayerWithZeroWins() {
        repository.addPlayerIfMissing("Laci");

        assertEquals(List.of(new HighScore("Laci", 0)), repository.findAll());
    }

    @Test
    void addPlayerIfMissingShouldNotDuplicatePlayer() {
        repository.addPlayerIfMissing("Laci");
        repository.addPlayerIfMissing("Laci");

        assertEquals(1, repository.findAll().size());
    }

    @Test
    void addWinShouldIncreaseTheNumberOfWins() {
        repository.addWin("Laci");
        repository.addWin("Laci");

        assertEquals(List.of(new HighScore("Laci", 2)), repository.findAll());
    }

    @Test
    void findAllShouldStartWithThePlayerWithMostWins() {
        repository.addWin("Anna");
        repository.addWin("Laci");
        repository.addWin("Laci");

        List<HighScore> scores = repository.findAll();

        assertEquals("Laci", scores.get(0).getName());
        assertEquals("Anna", scores.get(1).getName());
    }

    @Test
    void wrongDatabaseUrlShouldThrowDatabaseException() {
        HighScoreRepository broken = new HighScoreRepository("jdbc:nincsilyen:adatbazis");

        assertThrows(DatabaseException.class, () -> broken.createTableIfMissing());
        assertThrows(DatabaseException.class, () -> broken.addPlayerIfMissing("Laci"));
        assertThrows(DatabaseException.class, () -> broken.findAll());
    }
}
