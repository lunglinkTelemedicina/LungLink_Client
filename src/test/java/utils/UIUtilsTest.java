package utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class UIUtilsTest {

    private final InputStream originalIn = System.in;
    private InputStream testIn;


    private void setInput(String input) {
        testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);

        }


    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
    }


    @Test
    void readInt_ValidInteger_ReturnsCorrectValue() {
        String input = "123\n";
        setInput(input);

        int result = UIUtils.readInt("Prompt: ");

        assertEquals(123, result, "Debe devolver el entero correcto.");
    }

    @Test
    void readInt_InvalidThenValidInput_ReturnsValidValue() {
       String input = "abc\n45\n";
        setInput(input);

        int result = UIUtils.readInt("Prompt: ");

        assertEquals(45, result, "Debe reintentar después del error y devolver el segundo valor válido.");
    }

    @Test
    void readString_ValidString_ReturnsCorrectValue() {
        String expected = "Test String";
        String input = expected + "\n";
        setInput(input);

        String result = UIUtils.readString("Prompt: ");

        assertEquals(expected, result, "Debe devolver la cadena correcta.");
    }

    @Test
    void readString_EmptyInput_ReturnsEmptyString() {
        String input = "\n";
        setInput(input);

        String result = UIUtils.readString("Prompt: ");

        assertEquals("", result, "Debe devolver una cadena vacía.");
    }

    @Test
    void readDouble_ValidDouble_ReturnsCorrectValue() {
        String input = "78.95\n";
        setInput(input);

        double result = UIUtils.readDouble("Prompt: ");

        assertEquals(78.95, result, 0.001, "Debe devolver el valor double correcto.");
    }

    @Test
    void readDouble_InvalidThenValidInput_ReturnsValidValue() {
        String input = "not a number\n4.2\n";
        setInput(input);

        double result = UIUtils.readDouble("Prompt: ");

        assertEquals(4.2, result, 0.001, "Debe reintentar y devolver el segundo valor double válido.");
    }
}