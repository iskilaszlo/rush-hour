package hu.nye.progtech.ui;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * A konzolos be- és kimenet.
 * Azért külön osztály, hogy a tesztekben a billentyűzet helyett egy szövegből olvashassunk.
 */
public class ConsoleIO {

    private final Scanner scanner;
    private final PrintStream out;

    public ConsoleIO(Scanner scanner, PrintStream out) {
        this.scanner = scanner;
        this.out = out;
    }

    public void println(String text) {
        out.println(text);
    }

    /**
     * Kiírja a kérdést, és beolvas egy sort (a szélein lévő szóközök nélkül).
     * Ha nincs több bemenet, null-t ad vissza.
     */
    public String readLine(String prompt) {
        out.print(prompt);
        if (!scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine().trim();
    }
}
