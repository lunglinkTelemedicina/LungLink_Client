package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for handling console input operations.
 * Provides methods for reading different types of user input with validation.
 */
public class UIUtils {

    private static final BufferedReader reader =
            new BufferedReader(new InputStreamReader(System.in));

    /**
     * Reads an integer from console input with validation.
     *
     * @param message The prompt message to display to the user
     * @return The valid integer entered by the user
     */
    public static int readInt(String message) {

        while (true) {
            try {
                System.out.print(message);
                String input = reader.readLine();
                return Integer.parseInt(input.trim());

            } catch (IOException ex) {
                System.out.println("Error reading input. Try again.");

            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Reads a string from console input.
     *
     * @param message The prompt message to display to the user
     * @return The string entered by the user, trimmed of leading and trailing whitespace
     */
    public static String readString (String message) {
        while (true) {
            try {
                System.out.print(message);
                String input = reader.readLine();
                if (input != null) {
                    return input.trim();
                }
            } catch (IOException ex) {
                System.out.println("Error reading input. Try again.");
            }
        }
    }

    /**
     * Reads a double from console input with validation.
     *
     * @param message The prompt message to display to the user
     * @return The valid double entered by the user
     */
    public static double readDouble(String message) {
        while (true) {
            try {
                System.out.print(message);
                String input = reader.readLine();

                return Double.parseDouble(input.trim());

            } catch (IOException ex) {
                System.out.println("Error reading input. Try again.");

            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
