package hu.nye.progtech.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import hu.nye.progtech.model.Board;
import hu.nye.progtech.model.Orientation;
import hu.nye.progtech.model.Position;
import hu.nye.progtech.model.Vehicle;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MapParserTest {

    private static final List<String> VALID_MAP = List.of(
            "..C.....",
            "AAC.....",
            "..C..BB.");

    private MapParser parser;

    @BeforeEach
    void setUp() {
        parser = new MapParser();
    }

    @Test
    void parseShouldReadBoardSizeAndVehicles() {
        Board board = parser.parse(VALID_MAP);

        assertEquals(3, board.getRows());
        assertEquals(8, board.getColumns());
        assertEquals(3, board.getVehicles().size());
    }

    @Test
    void parseShouldRecognizeHorizontalAndVerticalVehicles() {
        Board board = parser.parse(VALID_MAP);

        assertEquals(new Vehicle('A', new Position(1, 0), 2, Orientation.HORIZONTAL), board.findVehicle('A'));
        assertEquals(new Vehicle('C', new Position(0, 2), 3, Orientation.VERTICAL), board.findVehicle('C'));
    }

    @Test
    void parseShouldThrowWhenMapIsEmpty() {
        assertThrows(InvalidMapException.class, () -> parser.parse(List.of()));
    }

    @Test
    void parseShouldThrowWhenRowsHaveDifferentLength() {
        assertThrows(InvalidMapException.class, () -> parser.parse(List.of("AA..BB", "...")));
    }

    @Test
    void parseShouldThrowOnUnknownCharacter() {
        assertThrows(InvalidMapException.class, () -> parser.parse(List.of("AA.x.BB")));
    }

    @Test
    void parseShouldThrowWhenVehicleIsTooLong() {
        assertThrows(InvalidMapException.class, () -> parser.parse(List.of("AA..BB", "CCCC..")));
    }

    @Test
    void parseShouldThrowWhenVehicleIsNotContinuous() {
        assertThrows(InvalidMapException.class, () -> parser.parse(List.of("AA.C.C.BB")));
    }

    @Test
    void parseShouldThrowWhenPlayerCarIsMissing() {
        assertThrows(InvalidMapException.class, () -> parser.parse(List.of("....BB")));
    }

    @Test
    void parseShouldThrowWhenHeroCarIsVertical() {
        assertThrows(InvalidMapException.class, () -> parser.parse(List.of("A...BB", "A.....")));
    }
}
