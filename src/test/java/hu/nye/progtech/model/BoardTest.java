package hu.nye.progtech.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        // A: vízszintes autó a 2. sorban, az 1-2. oszlopban
        Vehicle player = new Vehicle('A', new Position(2, 1), 2, Orientation.HORIZONTAL);
        // C: függőleges teherautó a 4. oszlopban, a 0-2. sorban
        Vehicle truck = new Vehicle('C', new Position(0, 4), 3, Orientation.VERTICAL);
        board = new Board(6, 12, List.of(player, truck));
    }

    @Test
    void isInsideShouldReturnTrueForPositionOnTheBoard() {
        assertTrue(board.isInside(new Position(5, 11)));
    }

    @Test
    void isInsideShouldReturnFalseForPositionOutsideTheBoard() {
        assertFalse(board.isInside(new Position(6, 0)));
        assertFalse(board.isInside(new Position(0, -1)));
    }

    @Test
    void getVehicleAtShouldReturnVehicleOnThePosition() {
        Vehicle result = board.getVehicleAt(new Position(2, 4));
        assertEquals('C', result.getId());
    }

    @Test
    void getVehicleAtShouldReturnNullForEmptyPosition() {
        assertNull(board.getVehicleAt(new Position(5, 5)));
    }

    @Test
    void isFreeShouldReturnFalseForOccupiedPosition() {
        assertFalse(board.isFree(new Position(2, 2)));
    }

    @Test
    void findVehicleShouldReturnNullForUnknownId() {
        assertNull(board.findVehicle('X'));
    }

    @Test
    void withVehicleMovedShouldMoveOnlyTheGivenVehicle() {
        Board moved = board.withVehicleMoved('A', 2);

        assertEquals(new Position(2, 3), moved.findVehicle('A').getStart());
        assertEquals(new Position(0, 4), moved.findVehicle('C').getStart());
        // az eredeti tábla nem változott
        assertEquals(new Position(2, 1), board.findVehicle('A').getStart());
    }

    @Test
    void withMoveAppliedShouldMoveVehicleInTheGivenDirection() {
        Board moved = board.withMoveApplied(new Move('C', Direction.DOWN, 2));

        assertEquals(new Position(2, 4), moved.findVehicle('C').getStart());
    }

    @Test
    void equalBoardsShouldHaveSameHashCode() {
        Board copy = new Board(6, 12, board.getVehicles());

        assertEquals(board, copy);
        assertEquals(board.hashCode(), copy.hashCode());
    }
}
