package org.example.unit.service;

import org.example.service.EncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para EncryptionService
 *
 * Cobertura:
 * - Cifrado de datos
 * - Descifrado de datos
 * - Validación de entrada
 * - Casos extremos
 */
@DisplayName("Pruebas Unitarias - EncryptionService")
class EncryptionServiceTest {

    private EncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        encryptionService = new EncryptionService();
        // Configurar valores usando reflection (simula @Value)
        ReflectionTestUtils.setField(encryptionService, "algorithm", "AES");
        ReflectionTestUtils.setField(encryptionService, "secretKey", "MySecretKey12345");
    }

    @Test
    @DisplayName("Debe cifrar y descifrar correctamente")
    void testCifrarDescifrar_Success() {
        // Arrange
        String originalText = "Información sensible del calendario";

        // Act
        String encrypted = encryptionService.cifrar(originalText);
        String decrypted = encryptionService.descifrar(encrypted);

        // Assert
        assertNotNull(encrypted);
        assertNotEquals(originalText, encrypted);
        assertEquals(originalText, decrypted);
    }

    @Test
    @DisplayName("Debe producir el mismo resultado al descifrar")
    void testCifrarDescifrar_Consistencia() {
        // Arrange
        String text = "Texto a cifrar";

        // Act
        String encrypted1 = encryptionService.cifrar(text);
        String decrypted1 = encryptionService.descifrar(encrypted1);

        String encrypted2 = encryptionService.cifrar(text);
        String decrypted2 = encryptionService.descifrar(encrypted2);

        // Assert - El descifrado debe ser igual al original
        assertEquals(text, decrypted1);
        assertEquals(text, decrypted2);
        assertEquals(decrypted1, decrypted2);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "Texto corto",
        "Este es un texto mucho más largo que contiene información sensible y confidencial del usuario",
        "12345",
        "!@#$%^&*()_+-=[]{}|;:',.<>?/",
        "Texto con números 123 y símbolos @#$"
    })
    @DisplayName("Debe manejar diferentes tipos de texto")
    void testCifrarDescifrar_DiferentesTiposDeTexto(String text) {
        // Act
        String encrypted = encryptionService.cifrar(text);
        String decrypted = encryptionService.descifrar(encrypted);

        // Assert
        assertEquals(text, decrypted);
        assertNotEquals(text, encrypted);
    }

    @Test
    @DisplayName("Debe manejar entrada vacía al descifrar")
    void testDescifrar_EntradaVacia() {
        // Act
        String result = encryptionService.descifrar("");

        // Assert
        assertEquals("", result);
    }

    @Test
    @DisplayName("Debe manejar entrada nula al descifrar")
    void testDescifrar_EntradaNula() {
        // Act
        String result = encryptionService.descifrar(null);

        // Assert
        assertEquals("", result);
    }

    @Test
    @DisplayName("Debe lanzar excepción al cifrar texto nulo")
    void testCifrar_TextoNulo() {
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            encryptionService.cifrar(null);
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción al descifrar texto inválido")
    void testDescifrar_TextoInvalido() {
        // Arrange
        String textoInvalido = "esto-no-es-un-texto-cifrado-valido";

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            encryptionService.descifrar(textoInvalido);
        });
    }

    @Test
    @DisplayName("Debe cifrar texto con caracteres especiales")
    void testCifrar_CaracteresEspeciales() {
        // Arrange
        String textWithSpecialChars = "Contraseña: P@ssw0rd! €ñÑáéíóú";

        // Act
        String encrypted = encryptionService.cifrar(textWithSpecialChars);
        String decrypted = encryptionService.descifrar(encrypted);

        // Assert
        assertEquals(textWithSpecialChars, decrypted);
    }

    @Test
    @DisplayName("Debe manejar texto Unicode")
    void testCifrar_Unicode() {
        // Arrange
        String unicodeText = "日本語 中文 한글 العربية עברית Ελληνικά";

        // Act
        String encrypted = encryptionService.cifrar(unicodeText);
        String decrypted = encryptionService.descifrar(encrypted);

        // Assert
        assertEquals(unicodeText, decrypted);
    }

    @Test
    @DisplayName("Debe manejar texto muy largo")
    void testCifrar_TextoMuyLargo() {
        // Arrange
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longText.append("Esta es una línea de texto número ").append(i).append(". ");
        }
        String text = longText.toString();

        // Act
        String encrypted = encryptionService.cifrar(text);
        String decrypted = encryptionService.descifrar(encrypted);

        // Assert
        assertEquals(text, decrypted);
    }

    @Test
    @DisplayName("Debe descifrar calendario correctamente")
    void testDescifrarCalendario() {
        // Arrange
        String originalText = "Información del calendario";
        String encrypted = encryptionService.cifrar(originalText);

        // Act
        String decrypted = encryptionService.descifrarCalendario(encrypted);

        // Assert
        assertEquals(originalText, decrypted);
    }

    @Test
    @DisplayName("El texto cifrado debe estar en Base64")
    void testCifrar_FormatoBase64() {
        // Arrange
        String text = "Texto para cifrar";

        // Act
        String encrypted = encryptionService.cifrar(text);

        // Assert
        // Verificar que el texto cifrado sea Base64 válido
        assertDoesNotThrow(() -> {
            java.util.Base64.getDecoder().decode(encrypted);
        });
    }

    @Test
    @DisplayName("Debe cifrar texto vacío")
    void testCifrar_TextoVacio() {
        // Arrange
        String emptyText = "";

        // Act
        String encrypted = encryptionService.cifrar(emptyText);
        String decrypted = encryptionService.descifrar(encrypted);

        // Assert
        assertNotNull(encrypted);
        assertEquals(emptyText, decrypted);
    }

    @Test
    @DisplayName("Diferentes textos deben producir diferentes cifrados")
    void testCifrar_DiferentesTextos() {
        // Arrange
        String text1 = "Texto 1";
        String text2 = "Texto 2";

        // Act
        String encrypted1 = encryptionService.cifrar(text1);
        String encrypted2 = encryptionService.cifrar(text2);

        // Assert
        assertNotEquals(encrypted1, encrypted2);
    }

    @Test
    @DisplayName("Mismo texto debe producir mismo cifrado (con mismo algoritmo y clave)")
    void testCifrar_MismoTexto() {
        // Arrange
        String text = "Texto consistente";

        // Act
        String encrypted1 = encryptionService.cifrar(text);
        String encrypted2 = encryptionService.cifrar(text);

        // Assert - Con AES sin IV aleatorio, debe ser igual
        assertEquals(encrypted1, encrypted2);
    }

    @Test
    @DisplayName("Debe manejar saltos de línea")
    void testCifrar_SaltosDeLinea() {
        // Arrange
        String textWithNewlines = "Línea 1\nLínea 2\nLínea 3";

        // Act
        String encrypted = encryptionService.cifrar(textWithNewlines);
        String decrypted = encryptionService.descifrar(encrypted);

        // Assert
        assertEquals(textWithNewlines, decrypted);
    }
}

