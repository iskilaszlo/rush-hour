package hu.nye.progtech.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hu.nye.progtech.model.Board;
import hu.nye.progtech.model.Direction;
import hu.nye.progtech.model.MatchResult;
import hu.nye.progtech.model.Move;
import hu.nye.progtech.persistence.MapWriter;
import hu.nye.progtech.service.ComputerPlayer;
import hu.nye.progtech.service.MapParser;
import hu.nye.progtech.service.MoveValidator;
import hu.nye.progtech.service.WinChecker;
import hu.nye.progtech.ui.BoardRenderer;
import hu.nye.progtech.ui.ConsoleIO;
import hu.nye.progtech.ui.MoveParser;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MatchTest {

    private ComputerPlayer computerPlayer;
    private ByteArrayOutputStream output;
    private Board board;

    @BeforeEach
    void setUp() {
        // a gépi játékost mockoljuk, hogy a tesztben mi döntsük el, mit lép
        computerPlayer = mock(ComputerPlayer.class);
        output = new ByteArrayOutputStream();
        board = new MapParser().parse(List.of(
                "AA...",
                "..BB."));
    }

    private Match createMatch(String input) {
        ConsoleIO console = new ConsoleIO(new Scanner(input),
                new PrintStream(output, true, StandardCharsets.UTF_8));
        return new Match(console, new BoardRenderer(), new MoveParser(), new MoveValidator(),
                new WinChecker(), computerPlayer, new MapWriter());
    }

    private String printedText() {
        return output.toString(StandardCharsets.UTF_8);
    }

    @Test
    void playerShouldWinWhenCarAReachesTheExit() {
        MatchResult result = createMatch("A jobbra 3\n").play(board);

        assertEquals(MatchResult.PLAYER_WON, result);
    }

    @Test
    void computerShouldWinWhenCarBReachesTheExit() {
        when(computerPlayer.chooseMove(any())).thenReturn(new Move('B', Direction.LEFT, 2));

        MatchResult result = createMatch("A jobbra 1\n").play(board);

        assertEquals(MatchResult.COMPUTER_WON, result);
        assertTrue(printedText().contains("A gép lépése: B balra 2"));
    }

    @Test
    void quitCommandShouldEndTheMatch() {
        assertEquals(MatchResult.QUIT, createMatch("kilep\n").play(board));
    }

    @Test
    void endOfInputShouldEndTheMatch() {
        assertEquals(MatchResult.QUIT, createMatch("").play(board));
    }

    @Test
    void invalidInputShouldPrintErrorAndAskAgain() {
        MatchResult result = createMatch("rossz\nkilep\n").play(board);

        assertEquals(MatchResult.QUIT, result);
        assertTrue(printedText().contains("Hibás formátum"));
    }

    @Test
    void blockedMoveShouldBeRejected() {
        MatchResult result = createMatch("A balra 1\nkilep\n").play(board);

        assertEquals(MatchResult.QUIT, result);
        assertTrue(printedText().contains("az út nem szabad"));
    }

    @Test
    void computerWithoutPossibleMoveShouldSkipItsTurn() {
        // a mock alapból null-t ad vissza, vagyis a gép nem tud lépni
        MatchResult result = createMatch("A jobbra 1\nkilep\n").play(board);

        assertEquals(MatchResult.QUIT, result);
        assertTrue(printedText().contains("A gép nem tud lépni"));
    }

    @Test
    void saveCommandShouldWriteTheBoardToFile(@TempDir Path tempDir) {
        Path file = tempDir.resolve("mentes.txt");

        createMatch("ment " + file + "\nkilep\n").play(board);

        assertTrue(Files.exists(file));
    }
}
