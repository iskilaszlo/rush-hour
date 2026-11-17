package hu.nye.progtech.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hu.nye.progtech.model.Board;
import hu.nye.progtech.model.MatchResult;
import java.util.List;
import org.junit.jupiter.api.Test;

class WinCheckerTest {

    private final MapParser parser = new MapParser();
    private final WinChecker winChecker = new WinChecker();

    @Test
    void checkShouldReturnInProgressWhenNobodyIsAtTheExit() {
        Board board = parser.parse(List.of("AA..", "..BB"));

        assertEquals(MatchResult.IN_PROGRESS, winChecker.check(board));
    }

    @Test
    void checkShouldReturnPlayerWonWhenCarAIsAtTheRightEdge() {
        Board board = parser.parse(List.of("..AA", "..BB"));

        assertEquals(MatchResult.PLAYER_WON, winChecker.check(board));
    }

    @Test
    void checkShouldReturnComputerWonWhenCarBIsAtTheLeftEdge() {
        Board board = parser.parse(List.of("AA..", "BB.."));

        assertEquals(MatchResult.COMPUTER_WON, winChecker.check(board));
    }
}
