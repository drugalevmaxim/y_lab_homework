package xyz.drugalev.adapters.in.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A collection of utils to simplify user input parsing
 *
 * @author Drugalev Maxim
 */
public class InputUtil {
    /**
     * String that prints to indicate user input.
     */
    public static final String PROMPT = "> ";
    private final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Reads user input until correct date is given.
     *
     * @return given date.
     */
    public LocalDate getLocalDate() {
        while (true) {
            try {
                String dateFormat = "dd.MM.yyyy";
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                System.out.print("(" + dateFormat + ")" + PROMPT);
                return LocalDate.parse(br.readLine(), formatter);
            } catch (DateTimeException e) {
                System.err.println("Invalid input. Try again!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Reads user input until correct int is given.
     *
     * @return given int.
     */
    public int getInt() {
        while (true) {
            try {
                System.out.print(PROMPT);
                return Integer.parseInt(br.readLine());
            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Try again!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Reads line from user input.
     *
     * @return given int.
     */
    public String getLine() {
        try {
            System.out.print(PROMPT);
            return br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads password from user input.
     * If app is executed in console, user input will be masked.
     *
     * @return given password string.
     */
    public String getPassword() {
        System.out.print(PROMPT);
        if (System.console() != null) {
            return String.valueOf(System.console().readPassword());
        }
        return getLine();
    }


}
