package hu.nye.progtech.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hu.nye.progtech.model.Board;
import hu.nye.progtech.model.Direction;
import hu.nye.progtech.model.Move;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComputerPlayerTest {

    private final MapParser parser = new MapParser();
    private ComputerPlayer computerPlayer;

    @BeforeEach
    void setUp() {
        // a mockolt Random mindig 0-t ad, így mindig a lista első elemét választja
        Random random = mock(Random.class);
        when(random.nextInt(anyInt())).thenReturn(0);
        computerPlayer = new ComputerPlayer(new MoveValidator(), random);
    }

    @Test
    void chooseMoveShouldReturnAPossibleMove() {
        Board board = parser.parse(List.of(
                "..C...",
                "AAC...",
                "..C.BB",
                "......"));

        assertEquals(new Move('C', Direction.DOWN, 1), computerPlayer.chooseMove(board));
    }

    @Test
    void chooseMoveShouldSkipVehicleWithoutPossibleMoves() {
        // az A az első a listában, de nem tud lépni, ezért a C-t kell választania
        Board board = parser.parse(List.of(
                "AAC",
                "..C",
                "BB."));

        assertEquals(new Move('C', Direction.DOWN, 1), computerPlayer.chooseMove(board));
    }

    @Test
    void chooseMoveShouldReturnNullWhenNoVehicleCanMove() {
        Board board = parser.parse(List.of("AABB"));

        assertNull(computerPlayer.chooseMove(board));
    }
}
