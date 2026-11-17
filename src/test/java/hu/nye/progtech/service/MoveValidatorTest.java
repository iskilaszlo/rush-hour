package hu.nye.progtech.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hu.nye.progtech.model.Board;
import hu.nye.progtech.model.Direction;
import hu.nye.progtech.model.Move;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MoveValidatorTest {

    private MoveValidator validator;
    private Board board;

    @BeforeEach
    void setUp() {
        validator = new MoveValidator();
        // C: függőleges, 3 hosszú; A és B: vízszintes, 2 hosszú
        board = new MapParser().parse(List.of(
                "..C...",
                "AAC...",
                "..C.BB",
                "......"));
    }

    @Test
    void validMoveShouldBeAccepted() {
        assertDoesNotThrow(() -> validator.validate(board, new Move('C', Direction.DOWN, 1)));
    }

    @Test
    void unknownVehicleShouldBeRejected() {
        assertThrows(InvalidMoveException.class, () -> validator.validate(board, new Move('X', Direction.UP, 1)));
    }

    @Test
    void moveAcrossTheAxisShouldBeRejected() {
        assertThrows(InvalidMoveException.class, () -> validator.validate(board, new Move('A', Direction.UP, 1)));
    }

    @Test
    void zeroStepsShouldBeRejected() {
        assertThrows(InvalidMoveException.class, () -> validator.validate(board, new Move('C', Direction.DOWN, 0)));
    }

    @Test
    void moveThroughAnotherVehicleShouldBeRejected() {
        assertThrows(InvalidMoveException.class, () -> validator.validate(board, new Move('A', Direction.RIGHT, 1)));
    }

    @Test
    void moveOffTheBoardShouldBeRejected() {
        assertThrows(InvalidMoveException.class, () -> validator.validate(board, new Move('C', Direction.DOWN, 2)));
        assertThrows(InvalidMoveException.class, () -> validator.validate(board, new Move('C', Direction.UP, 1)));
    }

    @Test
    void getPossibleMovesShouldListAllFreeMoves() {
        assertEquals(List.of(new Move('C', Direction.DOWN, 1)),
                validator.getPossibleMoves(board, board.findVehicle('C')));
        assertEquals(List.of(new Move('B', Direction.LEFT, 1)),
                validator.getPossibleMoves(board, board.findVehicle('B')));
    }

    @Test
    void getPossibleMovesShouldBeEmptyForBlockedVehicle() {
        assertTrue(validator.getPossibleMoves(board, board.findVehicle('A')).isEmpty());
    }
}
