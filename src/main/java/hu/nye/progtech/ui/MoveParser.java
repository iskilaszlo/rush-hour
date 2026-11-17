package hu.nye.progtech.ui;

import hu.nye.progtech.model.Direction;
import hu.nye.progtech.model.Move;
import hu.nye.progtech.service.InvalidMoveException;

/**
 * A játékos által beírt szövegből lépést készít.
 * A várt formátum: jármű betűje, irány, lépésszám, szóközzel elválasztva, például: E le 2
 */
public class MoveParser {

    private static final String FORMAT_HELP = "Hibás formátum. Példa: E le 2";

    /**
     * Feldolgozza a beírt szöveget. Hibás formátum esetén InvalidMoveException-t dob.
     */
    public Move parse(String input) {
        String[] parts = input.trim().split("\\s+");
        if (parts.length != 3 || parts[0].length() != 1) {
            throw new InvalidMoveException(FORMAT_HELP);
        }

        char vehicleId = Character.toUpperCase(parts[0].charAt(0));

        Direction direction = Direction.fromWord(parts[1].toLowerCase());
        if (direction == null) {
            throw new InvalidMoveException("Ismeretlen irány: " + parts[1] + ". Lehetséges: fel, le, balra, jobbra");
        }

        int steps;
        try {
            steps = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            throw new InvalidMoveException("A lépésszám nem szám: " + parts[2]);
        }
        return new Move(vehicleId, direction, steps);
    }
}
