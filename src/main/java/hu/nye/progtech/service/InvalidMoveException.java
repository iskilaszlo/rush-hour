package hu.nye.progtech.service;

/**
 * Akkor dobjuk, ha egy lépés szabálytalan vagy hibásan van megadva.
 */
public class InvalidMoveException extends RuntimeException {

    public InvalidMoveException(String message) {
        super(message);
    }
}
