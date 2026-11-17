package hu.nye.progtech.service;

/**
 * Akkor dobjuk, ha a pálya leírása hibás.
 */
public class InvalidMapException extends RuntimeException {

    public InvalidMapException(String message) {
        super(message);
    }
}
