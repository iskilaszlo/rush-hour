package hu.nye.progtech.model;

import java.util.Objects;

/**
 * Egy lépés: melyik járművet, merre és hány mezővel mozgatjuk.
 */
public final class Move {

    private final char vehicleId;
    private final Direction direction;
    private final int steps;

    public Move(char vehicleId, Direction direction, int steps) {
        this.vehicleId = vehicleId;
        this.direction = direction;
        this.steps = steps;
    }

    public char getVehicleId() {
        return vehicleId;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getSteps() {
        return steps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Move move = (Move) o;
        return vehicleId == move.vehicleId
                && steps == move.steps
                && direction == move.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleId, direction, steps);
    }

    @Override
    public String toString() {
        return "Move{vehicleId=" + vehicleId + ", direction=" + direction + ", steps=" + steps + "}";
    }
}
