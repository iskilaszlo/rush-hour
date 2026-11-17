package hu.nye.progtech.persistence;

/**
 * Akkor dobjuk, ha az adatbázis-művelet nem sikerült.
 */
public class DatabaseException extends RuntimeException {

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
