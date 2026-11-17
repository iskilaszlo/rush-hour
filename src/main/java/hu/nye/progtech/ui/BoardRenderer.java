package hu.nye.progtech.ui;

import hu.nye.progtech.model.Board;
import hu.nye.progtech.model.Position;
import hu.nye.progtech.model.Vehicle;

/**
 * A tábla szöveges kirajzolása a konzolra.
 * Az oszlopokat betűk (a, b, c...), a sorokat számok (1, 2, 3...) jelölik.
 * A jobb oldali '>' az A autó kijárata, a bal oldali '<' a B autóé.
 */
public class BoardRenderer {

    private static final char EMPTY = '.';

    /**
     * Visszaadja a tábla kirajzolt képét szövegként.
     */
    public String render(Board board) {
        int playerExitRow = board.findVehicle(Board.PLAYER_CAR_ID).getStart().getRow();
        int computerExitRow = board.findVehicle(Board.COMPUTER_CAR_ID).getStart().getRow();
        String newLine = System.lineSeparator();
        StringBuilder text = new StringBuilder();

        text.append("      ");
        for (int column = 0; column < board.getColumns(); column++) {
            text.append((char) ('a' + column)).append(' ');
        }
        text.append(newLine);

        String border = "    +" + "-".repeat(board.getColumns() * 2 + 1) + "+";
        text.append(border).append(newLine);

        for (int row = 0; row < board.getRows(); row++) {
            text.append(String.format("%3d ", row + 1));
            text.append(row == computerExitRow ? '<' : '|').append(' ');
            for (int column = 0; column < board.getColumns(); column++) {
                Vehicle vehicle = board.getVehicleAt(new Position(row, column));
                text.append(vehicle == null ? EMPTY : vehicle.getId()).append(' ');
            }
            text.append(row == playerExitRow ? '>' : '|').append(newLine);
        }
        text.append(border);
        return text.toString();
    }
}
