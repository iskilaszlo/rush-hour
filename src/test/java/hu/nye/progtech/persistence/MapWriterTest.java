package hu.nye.progtech.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hu.nye.progtech.model.Board;
import hu.nye.progtech.service.MapParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MapWriterTest {

    private static final List<String> MAP = List.of("..C.", "AAC.", "..BB");

    private final MapWriter writer = new MapWriter();
    private final Board board = new MapParser().parse(MAP);

    @Test
    void toLinesShouldDrawVehiclesAndEmptyCells() {
        assertEquals(MAP, writer.toLines(board));
    }

    @Test
    void writeShouldCreateFileWithTheMap(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("mentes.txt");

        writer.write(file, board);

        assertEquals(MAP, Files.readAllLines(file));
    }
}
