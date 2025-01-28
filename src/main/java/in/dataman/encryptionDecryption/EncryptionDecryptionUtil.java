package in.dataman.encryptionDecryption;


import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

@Getter
@Component
@Setter
@Data
public class EncryptionDecryptionUtil {

    // AES Secret Key
    private SecretKey secretKey;

    /**
     * This method is called automatically after the server starts,
     * generating the AES Secret Key.
     */
    @PostConstruct
    private void initializeKey() {
        try {
            this.secretKey = generateAESKey();
        } catch (Exception e) {
            throw new RuntimeException("Error initializing AES key", e);
        }
    }

    /**
     * Method to generate an AES secret key.
     *
     * @return a newly generated AES Secret Key.
     */
    private SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256); // AES 256-bit key
        return keyGenerator.generateKey();
    }

    /**
     * Method to decrypt an encrypted response using the AES secret key.
     *
     * @param encryptedResponse The encrypted response in Base64.
     * @return The decrypted plain text.
     */
    public String decryptResponse(String encryptedResponse) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] decodedResponse = Base64.getDecoder().decode(encryptedResponse);
        byte[] decryptedBytes = cipher.doFinal(decodedResponse);
        return new String(decryptedBytes);
    }

    /**
     * Method to encrypt a message using the AES secret key.
     *
     * @param message The plain text message.
     * @return The encrypted message in Base64.
     */
    public String encryptMessage(String message) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedBytes = cipher.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Encrypt a message using the provided AES key.
     *
     * @param message The plain text message.
     * @return The encrypted message in Base64 format.
     * @throws Exception If encryption fails.
     */
    public String encryptWithKey(String message, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedBytes = cipher.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Decrypt a message using the provided AES key.
     *
     * @param encryptedMessage The encrypted message in Base64 format.
     * @return The decrypted plain text message.
     * @throws Exception If decryption fails.
     */
    public String decryptWithKey(String encryptedMessage, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] decodedMessage = Base64.getDecoder().decode(encryptedMessage);
        byte[] decryptedBytes = cipher.doFinal(decodedMessage);
        return new String(decryptedBytes);
    }


}