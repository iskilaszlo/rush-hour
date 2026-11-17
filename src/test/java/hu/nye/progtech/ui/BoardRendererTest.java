package hu.nye.progtech.ui;

import static org.junit.jupiter.api.Assertions.assertTrue;

import hu.nye.progtech.model.Board;
import hu.nye.progtech.service.MapParser;
import java.util.List;
import org.junit.jupiter.api.Test;

class BoardRendererTest {

    @Test
    void renderShouldShowCoordinatesVehiclesAndExits() {
        Board board = new MapParser().parse(List.of("AA..", "..BB"));

        String text = new BoardRenderer().render(board);

        assertTrue(text.contains("a b c d"));
        assertTrue(text.contains("1 | A A . . >"));
        assertTrue(text.contains("2 < . . B B |"));
    }
}
