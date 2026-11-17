package hu.nye.progtech.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A játéktábla: a mérete és a rajta lévő járművek.
 * Megváltoztathatatlan: egy lépés után új Board objektum jön létre.
 */
public final class Board {

    /** A játékos célautójának betűjele. Jobbra kell kijutnia. */
    public static final char PLAYER_CAR_ID = 'A';
    /** A gép célautójának betűjele. Balra kell kijutnia. */
    public static final char COMPUTER_CAR_ID = 'B';

    private final int rows;
    private final int columns;
    private final List<Vehicle> vehicles;

    public Board(int rows, int columns, List<Vehicle> vehicles) {
        this.rows = rows;
        this.columns = columns;
        this.vehicles = List.copyOf(vehicles);
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    /**
     * Megkeresi a járművet a betűjele alapján. Ha nincs ilyen, null-t ad vissza.
     */
    public Vehicle findVehicle(char id) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getId() == id) {
                return vehicle;
            }
        }
        return null;
    }

    /**
     * Visszaadja a járművet, amely az adott mezőn áll. Ha a mező üres, null-t ad vissza.
     */
    public Vehicle getVehicleAt(Position position) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getCells().contains(position)) {
                return vehicle;
            }
        }
        return null;
    }

    /**
     * Megmondja, hogy a mező a táblán belül van-e.
     */
    public boolean isInside(Position position) {
        return position.getRow() >= 0 && position.getRow() < rows
                && position.getColumn() >= 0 && position.getColumn() < columns;
    }

    /**
     * Megmondja, hogy a mező a táblán belül van-e és üres-e.
     */
    public boolean isFree(Position position) {
        return isInside(position) && getVehicleAt(position) == null;
    }

    /**
     * Visszaad egy új táblát, amelyen a megadott jármű steps mezővel arrébb áll.
     * A többi jármű a helyén marad.
     */
    public Board withVehicleMoved(char id, int steps) {
        List<Vehicle> newVehicles = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getId() == id) {
                newVehicles.add(vehicle.moveBy(steps));
            } else {
                newVehicles.add(vehicle);
            }
        }
        return new Board(rows, columns, newVehicles);
    }

    /**
     * Visszaad egy új táblát, amelyen a lépés már végre van hajtva.
     * A lépés szabályosságát ez a metódus nem ellenőrzi, azt a MoveValidator végzi.
     */
    public Board withMoveApplied(Move move) {
        return withVehicleMoved(move.getVehicleId(), move.getDirection().getSign() * move.getSteps());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Board board = (Board) o;
        return rows == board.rows
                && columns == board.columns
                && vehicles.equals(board.vehicles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rows, columns, vehicles);
    }

    @Override
    public String toString() {
        return "Board{rows=" + rows + ", columns=" + columns + ", vehicles=" + vehicles + "}";
    }
}
