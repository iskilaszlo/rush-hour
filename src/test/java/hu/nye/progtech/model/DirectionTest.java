package hu.nye.progtech.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class DirectionTest {

    @Test
    void fromWordShouldFindDirection() {
        assertEquals(Direction.DOWN, Direction.fromWord("le"));
        assertEquals(Direction.RIGHT, Direction.fromWord("jobbra"));
    }

    @Test
    void fromWordShouldReturnNullForUnknownWord() {
        assertNull(Direction.fromWord("elore"));
    }
}
