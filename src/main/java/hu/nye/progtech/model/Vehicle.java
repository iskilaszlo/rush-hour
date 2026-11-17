package hu.nye.progtech.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Egy jármű a táblán.
 * A start a jármű bal szélső (vízszintesnél) vagy legfelső (függőlegesnél) mezője.
 * Az osztály megváltoztathatatlan: mozgatáskor új Vehicle objektum jön létre.
 */
public final class Vehicle {

    private final char id;
    private final Position start;
    private final int length;
    private final Orientation orientation;

    public Vehicle(char id, Position start, int length, Orientation orientation) {
        this.id = id;
        this.start = start;
        this.length = length;
        this.orientation = orientation;
    }

    public char getId() {
        return id;
    }

    public Position getStart() {
        return start;
    }

    public int getLength() {
        return length;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Visszaadja az összes mezőt, amelyet a jármű elfoglal.
     */
    public List<Position> getCells() {
        List<Position> cells = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            if (orientation == Orientation.HORIZONTAL) {
                cells.add(new Position(start.getRow(), start.getColumn() + i));
            } else {
                cells.add(new Position(start.getRow() + i, start.getColumn()));
            }
        }
        return cells;
    }

    /**
     * Visszaad egy új járművet, amely a tengelye mentén steps mezővel arrébb van.
     * Pozitív érték: jobbra vagy lefelé, negatív érték: balra vagy felfelé.
     */
    public Vehicle moveBy(int steps) {
        Position newStart;
        if (orientation == Orientation.HORIZONTAL) {
            newStart = new Position(start.getRow(), start.getColumn() + steps);
        } else {
            newStart = new Position(start.getRow() + steps, start.getColumn());
        }
        return new Vehicle(id, newStart, length, orientation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vehicle vehicle = (Vehicle) o;
        return id == vehicle.id
                && length == vehicle.length
                && start.equals(vehicle.start)
                && orientation == vehicle.orientation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, start, length, orientation);
    }

    @Override
    public String toString() {
        return "Vehicle{id=" + id + ", start=" + start + ", length=" + length
                + ", orientation=" + orientation + "}";
    }
}