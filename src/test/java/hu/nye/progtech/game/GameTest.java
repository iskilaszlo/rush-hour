package hu.nye.progtech.game;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hu.nye.progtech.model.Board;
import hu.nye.progtech.model.HighScore;
import hu.nye.progtech.model.MatchResult;
import hu.nye.progtech.persistence.DatabaseException;
import hu.nye.progtech.persistence.HighScoreRepository;
import hu.nye.progtech.persistence.MapLoader;
import hu.nye.progtech.service.MapParser;
import hu.nye.progtech.ui.ConsoleIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTest {

    private MapLoader mapLoader;
    private Match match;
    private HighScoreRepository highScoreRepository;
    private ByteArrayOutputStream output;
    private Board board;

    @BeforeEach
    void setUp() {
        // a Game függőségeit mockoljuk, így csak a menü működését teszteljük
        mapLoader = mock(MapLoader.class);
        match = mock(Match.class);
        highScoreRepository = mock(HighScoreRepository.class);
        output = new ByteArrayOutputStream();
        board = new MapParser().parse(List.of("AA..", "..BB"));
        when(mapLoader.loadOrDefault(any())).thenReturn(board);
    }

    private void runGame(String input) {
        ConsoleIO console = new ConsoleIO(new Scanner(input),
                new PrintStream(output, true, StandardCharsets.UTF_8));
        new Game(console, mapLoader, match, highScoreRepository).start();
    }

    private String printedText() {
        return output.toString(StandardCharsets.UTF_8);
    }

    @Test
    void startShouldRegisterThePlayerAndQuit() {
        runGame("Laci\n4\n");

        verify(highScoreRepository).addPlayerIfMissing("Laci");
        assertTrue(printedText().contains("Viszlát!"));
    }

    @Test
    void emptyNameShouldBeAskedAgain() {
        runGame("\nLaci\n4\n");

        verify(highScoreRepository).addPlayerIfMissing("Laci");
    }

    @Test
    void wonMatchShouldBeSaved() {
        when(match.play(board)).thenReturn(MatchResult.PLAYER_WON);

        runGame("Laci\n1\n4\n");

        verify(highScoreRepository).addWin("Laci");
    }

    @Test
    void lostMatchShouldNotBeSaved() {
        when(match.play(board)).thenReturn(MatchResult.COMPUTER_WON);

        runGame("Laci\n1\n4\n");

        verify(highScoreRepository, never()).addWin(anyString());
        assertTrue(printedText().contains("A gép nyert"));
    }

    @Test
    void quitMatchShouldPrintMessage() {
        when(match.play(board)).thenReturn(MatchResult.QUIT);

        runGame("Laci\n1\n4\n");

        assertTrue(printedText().contains("Kiléptél a játszmából"));
    }

    @Test
    void loadedMapShouldBeUsedInTheNextMatch() throws IOException {
        Board otherBoard = new MapParser().parse(List.of("AA...", "...BB"));
        when(mapLoader.loadFromFile(Path.of("masik.txt"))).thenReturn(otherBoard);
        when(match.play(otherBoard)).thenReturn(MatchResult.QUIT);

        runGame("Laci\n2\nmasik.txt\n1\n4\n");

        verify(match).play(otherBoard);
    }

    @Test
    void failedMapLoadShouldPrintError() throws IOException {
        when(mapLoader.loadFromFile(any())).thenThrow(new IOException("nincs ilyen fájl"));

        runGame("Laci\n2\nrossz.txt\n4\n");

        assertTrue(printedText().contains("Nem sikerült betölteni a pályát"));
    }

    @Test
    void highScoresShouldBePrinted() {
        when(highScoreRepository.findAll()).thenReturn(List.of(new HighScore("Anna", 3)));

        runGame("Laci\n3\n4\n");

        assertTrue(printedText().contains("Anna"));
    }

    @Test
    void emptyHighScoreListShouldPrintMessage() {
        runGame("Laci\n3\n4\n");

        assertTrue(printedText().contains("Még nincs mentett eredmény"));
    }

    @Test
    void unknownMenuItemShouldPrintMessage() {
        runGame("Laci\n9\n4\n");

        assertTrue(printedText().contains("Nincs ilyen menüpont"));
    }

    @Test
    void databaseErrorShouldNotStopTheGame() {
        doThrow(new DatabaseException("hiba", null)).when(highScoreRepository).addPlayerIfMissing("Laci");

        runGame("Laci\n4\n");

        assertTrue(printedText().contains("Viszlát!"));
    }

    @Test
    void endOfInputShouldStopTheGame() {
        runGame("Laci\n");

        assertTrue(printedText().contains("Viszlát!"));
    }
}
