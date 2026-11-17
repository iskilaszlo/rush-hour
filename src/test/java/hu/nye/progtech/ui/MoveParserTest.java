package hu.nye.progtech.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import hu.nye.progtech.model.Direction;
import hu.nye.progtech.model.Move;
import hu.nye.progtech.service.InvalidMoveException;
import org.junit.jupiter.api.Test;

class MoveParserTest {

    private final MoveParser parser = new MoveParser();

    @Test
    void parseShouldReadValidMove() {
        assertEquals(new Move('E', Direction.DOWN, 2), parser.parse("E le 2"));
    }

    @Test
    void parseShouldAcceptLowercaseLetterAndExtraSpaces() {
        assertEquals(new Move('C', Direction.RIGHT, 1), parser.parse("  c   JOBBRA  1 "));
    }

    @Test
    void parseShouldThrowWhenAPartIsMissing() {
        assertThrows(InvalidMoveException.class, () -> parser.parse("E le"));
    }

    @Test
    void parseShouldThrowWhenVehicleIdIsLongerThanOneLetter() {
        assertThrows(InvalidMoveException.class, () -> parser.parse("EE le 2"));
    }

    @Test
    void parseShouldThrowOnUnknownDirection() {
        assertThrows(InvalidMoveException.class, () -> parser.parse("E elore 2"));
    }

    @Test
    void parseShouldThrowWhenStepsIsNotANumber() {
        assertThrows(InvalidMoveException.class, () -> parser.parse("E le ketto"));
    }
}
