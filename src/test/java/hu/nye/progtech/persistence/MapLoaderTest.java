package hu.nye.progtech.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import hu.nye.progtech.model.Board;
import hu.nye.progtech.service.InvalidMapException;
import hu.nye.progtech.service.MapParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MapLoaderTest {

    @TempDir
    Path tempDir;

    private MapLoader loader;

    @BeforeEach
    void setUp() {
        loader = new MapLoader(new MapParser());
    }

    @Test
    void loadFromFileShouldReadValidMapAndSkipEmptyLines() throws IOException {
        Path file = tempDir.resolve("map.txt");
        Files.write(file, List.of("AA..", "", "..BB", ""));

        Board board = loader.loadFromFile(file);

        assertEquals(2, board.getRows());
        assertEquals(4, board.getColumns());
    }

    @Test
    void loadFromFileShouldThrowForInvalidMap() throws IOException {
        Path file = tempDir.resolve("rossz.txt");
        Files.write(file, List.of("xyz"));

        assertThrows(InvalidMapException.class, () -> loader.loadFromFile(file));
    }

    @Test
    void loadOrDefaultShouldLoadValidFile() throws IOException {
        Path file = tempDir.resolve("map.txt");
        Files.write(file, List.of("AA..", "..BB"));

        assertEquals(4, loader.loadOrDefault(file).getColumns());
    }

    @Test
    void loadOrDefaultShouldReturnDefaultMapWhenFileIsMissing() {
        Board board = loader.loadOrDefault(tempDir.resolve("nincs-ilyen.txt"));

        assertEquals(loader.loadDefault(), board);
    }

    @Test
    void loadOrDefaultShouldReturnDefaultMapWhenFileIsInvalid() throws IOException {
        Path file = tempDir.resolve("rossz.txt");
        Files.write(file, List.of("xyz"));

        assertEquals(loader.loadDefault(), loader.loadOrDefault(file));
    }

    @Test
    void defaultMapShouldHaveSixRowsAndTwelveColumns() {
        Board board = loader.loadDefault();

        assertEquals(6, board.getRows());
        assertEquals(12, board.getColumns());
    }
}
