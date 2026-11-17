package hu.nye.progtech.game;

import hu.nye.progtech.model.Board;
import hu.nye.progtech.model.HighScore;
import hu.nye.progtech.model.MatchResult;
import hu.nye.progtech.persistence.DatabaseException;
import hu.nye.progtech.persistence.HighScoreRepository;
import hu.nye.progtech.persistence.MapLoader;
import hu.nye.progtech.service.InvalidMapException;
import hu.nye.progtech.ui.ConsoleIO;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A játék keretprogramja: bekéri a nevet, betölti a pályát, és a menüből indítja a játszmákat.
 */
public class Game {

    private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);
    private static final Path START_MAP_FILE = Path.of("map.txt");

    private final ConsoleIO console;
    private final MapLoader mapLoader;
    private final Match match;
    private final HighScoreRepository highScoreRepository;

    private String playerName;
    private Board currentMap;

    public Game(ConsoleIO console, MapLoader mapLoader, Match match, HighScoreRepository highScoreRepository) {
        this.console = console;
        this.mapLoader = mapLoader;
        this.match = match;
        this.highScoreRepository = highScoreRepository;
    }

    /**
     * Elindítja a játékot, és a menüt mutatja, amíg a játékos ki nem lép.
     */
    public void start() {
        console.println("=== Rush Hour ===");
        playerName = askName();
        LOGGER.info("A játékos neve: {}", playerName);
        registerPlayer();
        currentMap = mapLoader.loadOrDefault(START_MAP_FILE);

        boolean running = true;
        while (running) {
            printMenu();
            String choice = console.readLine("Válassz: ");
            if (choice == null || choice.equals("4")) {
                running = false;
            } else if (choice.equals("1")) {
                playMatch();
            } else if (choice.equals("2")) {
                loadMap();
            } else if (choice.equals("3")) {
                printHighScores();
            } else {
                console.println("Nincs ilyen menüpont.");
            }
        }
        console.println("Viszlát!");
    }

    private String askName() {
        String name = console.readLine("Add meg a neved: ");
        while (name != null && name.isEmpty()) {
            name = console.readLine("A név nem lehet üres. Add meg a neved: ");
        }
        if (name == null) {
            return "Névtelen";
        }
        return name;
    }

    private void registerPlayer() {
        try {
            highScoreRepository.createTableIfMissing();
            highScoreRepository.addPlayerIfMissing(playerName);
        } catch (DatabaseException e) {
            LOGGER.error("Adatbázis hiba", e);
            console.println("Figyelem: az adatbázis nem érhető el, az eredmények nem mentődnek.");
        }
    }

    private void printMenu() {
        console.println("");
        console.println("--- Menü ---");
        console.println("1. Új játszma");
        console.println("2. Pálya betöltése fájlból");
        console.println("3. Ranglista");
        console.println("4. Kilépés");
    }

    private void playMatch() {
        MatchResult result = match.play(currentMap);
        if (result == MatchResult.PLAYER_WON) {
            console.println("Gratulálok, " + playerName + ", nyertél!");
            saveWin();
        } else if (result == MatchResult.COMPUTER_WON) {
            console.println("A gép nyert. Próbáld újra!");
        } else {
            console.println("Kiléptél a játszmából.");
        }
    }

    private void saveWin() {
        try {
            highScoreRepository.addWin(playerName);
        } catch (DatabaseException e) {
            LOGGER.error("Nem sikerült menteni a győzelmet", e);
            console.println("A győzelmet nem sikerült elmenteni.");
        }
    }

    private void loadMap() {
        String fileName = console.readLine("A pályafájl neve: ");
        if (fileName == null || fileName.isEmpty()) {
            console.println("Nem adtál meg fájlnevet.");
            return;
        }
        try {
            currentMap = mapLoader.loadFromFile(Path.of(fileName));
            console.println("Pálya betöltve: " + fileName);
            LOGGER.info("Pálya betöltve: {}", fileName);
        } catch (IOException | InvalidMapException | InvalidPathException e) {
            console.println("Nem sikerült betölteni a pályát: " + e.getMessage());
            LOGGER.warn("Nem sikerült betölteni a pályát: {}", fileName, e);
        }
    }

    private void printHighScores() {
        List<HighScore> scores;
        try {
            scores = highScoreRepository.findAll();
        } catch (DatabaseException e) {
            LOGGER.error("Nem sikerült beolvasni a ranglistát", e);
            console.println("A ranglista nem érhető el.");
            return;
        }
        console.println("--- Ranglista ---");
        if (scores.isEmpty()) {
            console.println("Még nincs mentett eredmény.");
        }
        for (HighScore score : scores) {
            console.println(String.format("%-20s %d győzelem", score.getName(), score.getWins()));
        }
    }
}
