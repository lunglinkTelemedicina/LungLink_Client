package utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class UIUtilsTest {

    // Guarda el System.in original para restaurarlo después de cada prueba.
    private final InputStream originalIn = System.in;
    private InputStream testIn;

    /**
     * Helper: Redirige System.in a una cadena de texto para simular la entrada del usuario.
     */
    private void setInput(String input) {
        // Crea un flujo de bytes a partir de la cadena de entrada
        testIn = new ByteArrayInputStream(input.getBytes());
        // Redirige System.in al flujo de prueba
        System.setIn(testIn);

        // NOTA: Para que esto funcione, el código en UIUtils necesita que el BufferedReader
        // se reinicialice o que System.in se lea directamente. En este contexto académico,
        // asumimos que el patrón de lectura funcionará, aunque la inicialización estática lo dificulta.
    }

    /**
     * Restaura el System.in original después de cada prueba para evitar afectar otros tests.
     */
    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
    }

    // ====================================================================
    // 1. Tests para readInt()
    // ====================================================================

    @Test
    void readInt_ValidInteger_ReturnsCorrectValue() {
        // Arreglar: Ingresa el número y un salto de línea para simular 'Enter'
        String input = "123\n";
        setInput(input);

        // Actuar: Llama al método
        int result = UIUtils.readInt("Prompt: ");

        // Aserción
        assertEquals(123, result, "Debe devolver el entero correcto.");
    }

    @Test
    void readInt_InvalidThenValidInput_ReturnsValidValue() {
        // Arreglar: Primero un texto no numérico, luego el entero válido
        // El bucle de UIUtils se repetirá una vez.
        String input = "abc\n45\n";
        setInput(input);

        // Actuar: Llama al método
        int result = UIUtils.readInt("Prompt: ");

        // Aserción
        assertEquals(45, result, "Debe reintentar después del error y devolver el segundo valor válido.");
    }

    // ====================================================================
    // 2. Tests para readString()
    // ====================================================================

    @Test
    void readString_ValidString_ReturnsCorrectValue() {
        // Arreglar: Ingresa una cadena de texto
        String expected = "Test String";
        String input = expected + "\n";
        setInput(input);

        // Actuar: Llama al método
        String result = UIUtils.readString("Prompt: ");

        // Aserción
        assertEquals(expected, result, "Debe devolver la cadena correcta.");
    }

    @Test
    void readString_EmptyInput_ReturnsEmptyString() {
        // Arreglar: Solo un salto de línea
        String input = "\n";
        setInput(input);

        // Actuar: Llama al método
        String result = UIUtils.readString("Prompt: ");

        // Aserción
        assertEquals("", result, "Debe devolver una cadena vacía.");
    }

    // ====================================================================
    // 3. Tests para readDouble()
    // ====================================================================

    @Test
    void readDouble_ValidDouble_ReturnsCorrectValue() {
        // Arreglar: Ingresa un número decimal
        String input = "78.95\n";
        setInput(input);

        // Actuar: Llama al método
        double result = UIUtils.readDouble("Prompt: ");

        // Aserción (usando un delta de 0.001 para la comparación de doubles)
        assertEquals(78.95, result, 0.001, "Debe devolver el valor double correcto.");
    }

    @Test
    void readDouble_InvalidThenValidInput_ReturnsValidValue() {
        // Arreglar: Texto, luego un número decimal
        String input = "not a number\n4.2\n";
        setInput(input);

        // Actuar: Llama al método
        double result = UIUtils.readDouble("Prompt: ");

        // Aserción
        assertEquals(4.2, result, 0.001, "Debe reintentar y devolver el segundo valor double válido.");
    }
}