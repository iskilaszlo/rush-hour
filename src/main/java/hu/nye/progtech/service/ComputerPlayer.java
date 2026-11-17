package hu.nye.progtech.service;

import hu.nye.progtech.model.Board;
import hu.nye.progtech.model.Move;
import hu.nye.progtech.model.Vehicle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Az egyszerű gépi játékos.
 * Véletlenszerűen kiválaszt egy járművet, és ha annak van szabályos lépése, abból választ egyet véletlenszerűen.
 * Ha a kiválasztott jármű nem tud lépni, egy másikat választ, amíg talál mozgathatót.
 */
public class ComputerPlayer {

    private final MoveValidator moveValidator;
    private final Random random;

    public ComputerPlayer(MoveValidator moveValidator, Random random) {
        this.moveValidator = moveValidator;
        this.random = random;
    }

    /**
     * Kiválasztja a gép lépését. Ha egyik jármű sem tud lépni, null-t ad vissza.
     */
    public Move chooseMove(Board board) {
        List<Vehicle> candidates = new ArrayList<>(board.getVehicles());
        while (!candidates.isEmpty()) {
            // a kipróbált járművet kivesszük a listából, így nem választjuk kétszer
            Vehicle vehicle = candidates.remove(random.nextInt(candidates.size()));
            List<Move> moves = moveValidator.getPossibleMoves(board, vehicle);
            if (!moves.isEmpty()) {
                return moves.get(random.nextInt(moves.size()));
            }
        }
        return null;
    }
}
