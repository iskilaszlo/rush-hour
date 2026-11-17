package hu.nye.progtech.persistence;

import hu.nye.progtech.model.Board;
import hu.nye.progtech.service.InvalidMapException;
import hu.nye.progtech.service.MapParser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A pálya betöltése szövegfájlból.
 * Ha a fájl hiányzik vagy hibás, a beégetett alappályát lehet használni.
 */
public class MapLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapLoader.class);

    /** A beégetett alappálya: 6 sor, 12 oszlop. */
    private static final List<String> DEFAULT_MAP = List.of(
            "..CCC..D....",
            "....E..D.F..",
            ".AA.E....F..",
            "...G..H.BB..",
            "...G..H..III",
            "............");

    private final MapParser mapParser;

    public MapLoader(MapParser mapParser) {
        this.mapParser = mapParser;
    }

    /**
     * Betölti a pályát a fájlból. Az üres sorokat kihagyja.
     * Ha a fájl nem olvasható, IOException-t, ha a pálya hibás, InvalidMapException-t dob.
     */
    public Board loadFromFile(Path path) throws IOException {
        List<String> lines = new ArrayList<>();
        for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
            if (!line.isBlank()) {
                lines.add(line.strip());
            }
        }
        return mapParser.parse(lines);
    }

    /**
     * Betölti a pályát a fájlból. Ha a fájl hiányzik vagy hibás, az alappályát adja vissza.
     */
    public Board loadOrDefault(Path path) {
        try {
            Board board = loadFromFile(path);
            LOGGER.info("Pálya betöltve: {}", path);
            return board;
        } catch (IOException | InvalidMapException e) {
            LOGGER.warn("Nem sikerült betölteni a pályát ({}), az alappálya indul.", path, e);
            return loadDefault();
        }
    }

    /**
     * Visszaadja a beégetett alappályát.
     */
    public Board loadDefault() {
        return mapParser.parse(DEFAULT_MAP);
    }
}
