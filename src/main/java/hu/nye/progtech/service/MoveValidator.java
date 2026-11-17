package hu.nye.progtech.service;

import hu.nye.progtech.model.Board;
import hu.nye.progtech.model.Direction;
import hu.nye.progtech.model.Move;
import hu.nye.progtech.model.Orientation;
import hu.nye.progtech.model.Position;
import hu.nye.progtech.model.Vehicle;
import java.util.ArrayList;
import java.util.List;

/**
 * A lépésszabályok: egy jármű csak a saját tengelye mentén mozoghat,
 * csak üres mezőkön át, és nem hagyhatja el a táblát.
 */
public class MoveValidator {

    /**
     * Ellenőrzi a lépést. Ha szabálytalan, InvalidMoveException-t dob, amelynek üzenete megmondja az okát.
     */
    public void validate(Board board, Move move) {
        Vehicle vehicle = board.findVehicle(move.getVehicleId());
        if (vehicle == null) {
            throw new InvalidMoveException("Nincs " + move.getVehicleId() + " jelű jármű a táblán.");
        }
        if (vehicle.getOrientation() != move.getDirection().getOrientation()) {
            throw new InvalidMoveException("A(z) " + vehicle.getId() + " jármű csak "
                    + allowedDirections(vehicle) + " mozoghat.");
        }
        if (move.getSteps() < 1) {
            throw new InvalidMoveException("A lépésszámnak legalább 1-nek kell lennie.");
        }
        if (!isPathFree(board, vehicle, move.getDirection(), move.getSteps())) {
            throw new InvalidMoveException("Ezt a lépést nem lehet megtenni: az út nem szabad.");
        }
    }

    /**
     * Visszaadja a jármű összes szabályos lépését (mindkét irányba, minden lehetséges távolságra).
     */
    public List<Move> getPossibleMoves(Board board, Vehicle vehicle) {
        List<Move> moves = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if (direction.getOrientation() != vehicle.getOrientation()) {
                continue;
            }
            int steps = 1;
            while (isPathFree(board, vehicle, direction, steps)) {
                moves.add(new Move(vehicle.getId(), direction, steps));
                steps++;
            }
        }
        return moves;
    }

    /**
     * Megmondja, hogy a jármű előtt a megadott irányban steps darab mező szabad-e.
     */
    private boolean isPathFree(Board board, Vehicle vehicle, Direction direction, int steps) {
        for (int distance = 1; distance <= steps; distance++) {
            if (!board.isFree(cellAhead(vehicle, direction, distance))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Visszaadja azt a mezőt, amely a jármű eleje előtt distance távolságra van a megadott irányban.
     */
    private Position cellAhead(Vehicle vehicle, Direction direction, int distance) {
        Position start = vehicle.getStart();
        int lastOffset = vehicle.getLength() - 1;
        if (direction == Direction.UP) {
            return new Position(start.getRow() - distance, start.getColumn());
        } else if (direction == Direction.DOWN) {
            return new Position(start.getRow() + lastOffset + distance, start.getColumn());
        } else if (direction == Direction.LEFT) {
            return new Position(start.getRow(), start.getColumn() - distance);
        } else {
            return new Position(start.getRow(), start.getColumn() + lastOffset + distance);
        }
    }

    private String allowedDirections(Vehicle vehicle) {
        if (vehicle.getOrientation() == Orientation.HORIZONTAL) {
            return "balra vagy jobbra";
        }
        return "fel vagy le";
    }
}
