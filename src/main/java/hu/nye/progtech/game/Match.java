package hu.nye.progtech.game;

import hu.nye.progtech.model.Board;
import hu.nye.progtech.model.MatchResult;
import hu.nye.progtech.model.Move;
import hu.nye.progtech.persistence.MapWriter;
import hu.nye.progtech.service.ComputerPlayer;
import hu.nye.progtech.service.InvalidMoveException;
import hu.nye.progtech.service.MoveValidator;
import hu.nye.progtech.service.WinChecker;
import hu.nye.progtech.ui.BoardRenderer;
import hu.nye.progtech.ui.ConsoleIO;
import hu.nye.progtech.ui.MoveParser;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Egy játszma lebonyolítása.
 * Felváltva lép a játékos és a gép, amíg valamelyik célautó ki nem jut, vagy a játékos ki nem lép.
 */
public class Match {

    private static final Logger LOGGER = LoggerFactory.getLogger(Match.class);
    private static final String QUIT_COMMAND = "kilep";
    private static final String SAVE_COMMAND = "ment ";

    private final ConsoleIO console;
    private final BoardRenderer renderer;
    private final MoveParser moveParser;
    private final MoveValidator moveValidator;
    private final WinChecker winChecker;
    private final ComputerPlayer computerPlayer;
    private final MapWriter mapWriter;

    public Match(ConsoleIO console, BoardRenderer renderer, MoveParser moveParser, MoveValidator moveValidator,
                 WinChecker winChecker, ComputerPlayer computerPlayer, MapWriter mapWriter) {
        this.console = console;
        this.renderer = renderer;
        this.moveParser = moveParser;
        this.moveValidator = moveValidator;
        this.winChecker = winChecker;
        this.computerPlayer = computerPlayer;
        this.mapWriter = mapWriter;
    }

    /**
     * Lejátszik egy játszmát a megadott kezdőpályán, és visszaadja az eredményét.
     */
    public MatchResult play(Board startBoard) {
        Board board = startBoard;
        printHelp();
        MatchResult result = MatchResult.IN_PROGRESS;

        while (result == MatchResult.IN_PROGRESS) {
            console.println(renderer.render(board));
            String input = console.readLine("Lépésed: ");
            if (input == null || input.equalsIgnoreCase(QUIT_COMMAND)) {
                LOGGER.info("A játékos kilépett a játszmából.");
                return MatchResult.QUIT;
            }
            if (input.startsWith(SAVE_COMMAND)) {
                saveBoard(board, input.substring(SAVE_COMMAND.length()).trim());
                continue;
            }

            try {
                Move move = moveParser.parse(input);
                moveValidator.validate(board, move);
                board = board.withMoveApplied(move);
                LOGGER.info("Játékos lépése: {}", move);
            } catch (InvalidMoveException e) {
                console.println(e.getMessage());
                continue;
            }

            result = winChecker.check(board);
            if (result == MatchResult.IN_PROGRESS) {
                board = playComputerTurn(board);
                result = winChecker.check(board);
            }
        }

        console.println(renderer.render(board));
        LOGGER.info("A játszma véget ért: {}", result);
        return result;
    }

    private Board playComputerTurn(Board board) {
        Move move = computerPlayer.chooseMove(board);
        if (move == null) {
            console.println("A gép nem tud lépni, kihagyja a kört.");
            return board;
        }
        console.println("A gép lépése: " + move.getVehicleId() + " " + move.getDirection().getWord()
                + " " + move.getSteps());
        LOGGER.info("Gép lépése: {}", move);
        return board.withMoveApplied(move);
    }

    private void saveBoard(Board board, String fileName) {
        try {
            mapWriter.write(Path.of(fileName), board);
            console.println("Pálya elmentve: " + fileName);
            LOGGER.info("Pálya elmentve: {}", fileName);
        } catch (IOException | InvalidPathException e) {
            console.println("Nem sikerült menteni a pályát: " + e.getMessage());
            LOGGER.error("Nem sikerült menteni a pályát: {}", fileName, e);
        }
    }

    private void printHelp() {
        console.println("Te vagy az A autó, a kijáratod jobbra van (>).");
        console.println("A gép a B autó, az ő kijárata balra van (<).");
        console.println("Lépés: <jármű betűje> <irány> <lépésszám>, például: E le 2");
        console.println("Irányok: fel, le, balra, jobbra");
        console.println("Pálya mentése: ment <fájlnév>, például: ment palya.txt");
        console.println("Kilépés a játszmából: kilep");
    }
}
