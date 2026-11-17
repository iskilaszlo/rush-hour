package hu.nye.progtech;

import hu.nye.progtech.game.Game;
import hu.nye.progtech.game.Match;
import hu.nye.progtech.persistence.HighScoreRepository;
import hu.nye.progtech.persistence.MapLoader;
import hu.nye.progtech.persistence.MapWriter;
import hu.nye.progtech.service.ComputerPlayer;
import hu.nye.progtech.service.MapParser;
import hu.nye.progtech.service.MoveValidator;
import hu.nye.progtech.service.WinChecker;
import hu.nye.progtech.ui.BoardRenderer;
import hu.nye.progtech.ui.ConsoleIO;
import hu.nye.progtech.ui.MoveParser;
import java.util.Random;
import java.util.Scanner;

/**
 * A Rush Hour játék belépési pontja.
 * Itt csak létrehozzuk és összekötjük az objektumokat, maga a játék a Game osztályban fut.
 */
public class Main {

    private static final String DATABASE_URL = "jdbc:h2:./rush-hour-db";

    public static void main(String[] args) {
        ConsoleIO console = new ConsoleIO(new Scanner(System.in), System.out);
        MoveValidator moveValidator = new MoveValidator();

        Match match = new Match(console, new BoardRenderer(), new MoveParser(), moveValidator,
                new WinChecker(), new ComputerPlayer(moveValidator, new Random()), new MapWriter());
        Game game = new Game(console, new MapLoader(new MapParser()), match,
                new HighScoreRepository(DATABASE_URL));

        game.start();
    }
}
