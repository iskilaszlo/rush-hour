package hu.nye.progtech.persistence;

import hu.nye.progtech.model.Board;
import hu.nye.progtech.model.Position;
import hu.nye.progtech.model.Vehicle;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * A pálya mentése szövegfájlba, ugyanabban a formátumban, amit a MapParser beolvas.
 */
public class MapWriter {

    private static final char EMPTY = '.';

    /**
     * Elmenti a táblát a megadott fájlba.
     */
    public void write(Path path, Board board) throws IOException {
        Files.write(path, toLines(board), StandardCharsets.UTF_8);
    }

    /**
     * A táblát szöveges sorokká alakítja: '.' az üres mező, a betű a jármű.
     */
    public List<String> toLines(Board board) {
        List<String> lines = new ArrayList<>();
        for (int row = 0; row < board.getRows(); row++) {
            StringBuilder line = new StringBuilder();
            for (int column = 0; column < board.getColumns(); column++) {
                Vehicle vehicle = board.getVehicleAt(new Position(row, column));
                if (vehicle == null) {
                    line.append(EMPTY);
                } else {
                    line.append(vehicle.getId());
                }
            }
            lines.add(line.toString());
        }
        return lines;
    }
}
