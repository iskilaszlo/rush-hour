package hu.nye.progtech.service;

import hu.nye.progtech.model.Board;
import hu.nye.progtech.model.MatchResult;
import hu.nye.progtech.model.Vehicle;

/**
 * Megállapítja, hogy nyert-e valaki.
 * Egy célautó akkor jut ki, ha eléri a kijárata melletti szélső mezőt:
 * az A autó a jobb szélt, a B autó a bal szélt.
 */
public class WinChecker {

    /**
     * Visszaadja a játszma állapotát: PLAYER_WON, COMPUTER_WON vagy IN_PROGRESS.
     */
    public MatchResult check(Board board) {
        Vehicle playerCar = board.findVehicle(Board.PLAYER_CAR_ID);
        int playerCarEnd = playerCar.getStart().getColumn() + playerCar.getLength() - 1;
        if (playerCarEnd == board.getColumns() - 1) {
            return MatchResult.PLAYER_WON;
        }
        Vehicle computerCar = board.findVehicle(Board.COMPUTER_CAR_ID);
        if (computerCar.getStart().getColumn() == 0) {
            return MatchResult.COMPUTER_WON;
        }
        return MatchResult.IN_PROGRESS;
    }
}
