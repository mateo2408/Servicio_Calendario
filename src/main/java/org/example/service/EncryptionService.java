package org.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Servicio de Cifrado - ServicioCifrado
 * Responsable de cifrar y descifrar datos sensibles
 * Utilizado para proteger calendarios y eventos en BD_Calendarios
 */
@Service
public class EncryptionService {

    @Value("${encryption.algorithm:AES}")
    private String algorithm;

    @Value("${encryption.key:MySecretKey12345}")
    private String secretKey;

    /**
     * Cifra datos sensibles antes de almacenarlos
     */
    public String cifrar(String datos) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(
                secretKey.getBytes(StandardCharsets.UTF_8),
                algorithm
            );
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            byte[] encrypted = cipher.doFinal(datos.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error al cifrar datos: " + e.getMessage(), e);
        }
    }

    /**
     * Descifra datos antes de mostrarlos al usuario
     */
    public String descifrar(String datosCifrados) {
        try {
            if (datosCifrados == null || datosCifrados.isEmpty()) {
                return "";
            }

            SecretKeySpec keySpec = new SecretKeySpec(
                secretKey.getBytes(StandardCharsets.UTF_8),
                algorithm
            );
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            byte[] decoded = Base64.getDecoder().decode(datosCifrados);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error al descifrar datos: " + e.getMessage(), e);
        }
    }

    /**
     * Descifra calendario completo antes de preparar vista
     */
    public String descifrarCalendario(String calendarioCifrado) {
        return descifrar(calendarioCifrado);
    }
}

