package hu.nye.progtech.service;

import hu.nye.progtech.model.Board;
import hu.nye.progtech.model.Orientation;
import hu.nye.progtech.model.Position;
import hu.nye.progtech.model.Vehicle;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Szöveges pályaleírásból Board objektumot készít.
 * Jelölések: '.' üres mező, nagybetű: jármű (azonos betű = ugyanaz a jármű).
 * Az A a játékos, a B a gép célautója, mindkettőnek vízszintesnek kell lennie.
 */
public class MapParser {

    private static final char EMPTY = '.';

    /**
     * Feldolgozza a pálya sorait. Hibás pálya esetén InvalidMapException-t dob.
     */
    public Board parse(List<String> lines) {
        if (lines.isEmpty()) {
            throw new InvalidMapException("A pálya üres.");
        }
        int rows = lines.size();
        int columns = lines.get(0).length();
        Map<Character, List<Position>> cellsById = collectCells(lines, columns);

        List<Vehicle> vehicles = new ArrayList<>();
        for (Map.Entry<Character, List<Position>> entry : cellsById.entrySet()) {
            vehicles.add(createVehicle(entry.getKey(), entry.getValue()));
        }

        Board board = new Board(rows, columns, vehicles);
        checkHeroCar(board, Board.PLAYER_CAR_ID);
        checkHeroCar(board, Board.COMPUTER_CAR_ID);
        return board;
    }

    /**
     * Összegyűjti, hogy melyik betű mely mezőkön szerepel.
     */
    private Map<Character, List<Position>> collectCells(List<String> lines, int columns) {
        Map<Character, List<Position>> cellsById = new LinkedHashMap<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            if (line.length() != columns) {
                throw new InvalidMapException("A(z) " + (row + 1) + ". sor hossza eltér az első sorétól.");
            }
            for (int column = 0; column < columns; column++) {
                char cell = line.charAt(column);
                if (cell == EMPTY) {
                    continue;
                }
                if (cell < 'A' || cell > 'Z') {
                    throw new InvalidMapException("Ismeretlen karakter a pályán: " + cell);
                }
                if (!cellsById.containsKey(cell)) {
                    cellsById.put(cell, new ArrayList<>());
                }
                cellsById.get(cell).add(new Position(row, column));
            }
        }
        return cellsById;
    }

    /**
     * Egy betű mezőiből járművet készít, és ellenőrzi, hogy szabályos-e.
     * A mezők soronként balról jobbra érkeznek, ezért az első mező a jármű kezdete.
     */
    private Vehicle createVehicle(char id, List<Position> cells) {
        int length = cells.size();
        if (length < 2 || length > 3) {
            throw new InvalidMapException("A(z) " + id + " jármű hossza nem 2 vagy 3.");
        }
        Position start = cells.get(0);
        Orientation orientation;
        if (cells.get(1).getRow() == start.getRow()) {
            orientation = Orientation.HORIZONTAL;
        } else {
            orientation = Orientation.VERTICAL;
        }
        Vehicle vehicle = new Vehicle(id, start, length, orientation);
        // Ha a jármű nem egyenes, összefüggő vonal, a kiszámolt mezők eltérnek a beolvasottaktól.
        if (!vehicle.getCells().equals(cells)) {
            throw new InvalidMapException("A(z) " + id + " jármű nem egyenes, összefüggő vonal.");
        }
        return vehicle;
    }

    /**
     * Ellenőrzi, hogy a célautó szerepel-e a pályán és vízszintes-e.
     */
    private void checkHeroCar(Board board, char id) {
        Vehicle hero = board.findVehicle(id);
        if (hero == null) {
            throw new InvalidMapException("Hiányzik a(z) " + id + " célautó a pályáról.");
        }
        if (hero.getOrientation() != Orientation.HORIZONTAL) {
            throw new InvalidMapException("A(z) " + id + " célautónak vízszintesnek kell lennie.");
        }
    }
}
