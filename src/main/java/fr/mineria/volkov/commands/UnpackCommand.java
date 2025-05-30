package fr.mineria.volkov.commands;

import picocli.CommandLine;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@CommandLine.Command(name = "unpack", description = "Unpack an encrypted .enc file into a JAR file")
public class UnpackCommand implements Runnable {

    @CommandLine.Option(names = "--input", required = true, description = "Input encrypted file")
    private String inputPath;

    @CommandLine.Option(names = "--output", required = true, description = "Output JAR file")
    private String outputPath;

    @CommandLine.Option(names = "--key", required = true, description = "AES decryption key (16 or 32 characters)")
    private String key;

    @Override
    public void run() {
        try {
            byte[] keyBytes = key.getBytes("UTF-8");
            if (keyBytes.length != 16 && keyBytes.length != 32) {
                throw new IllegalArgumentException("Key must be 16 (AES-128) or 32 (AES-256) bytes after UTF-8 encoding.");
            }
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

            byte[] encryptedData = Files.readAllBytes(Paths.get(inputPath));
            if (encryptedData.length < 16) {
                throw new IllegalArgumentException("Encrypted data is too short to contain an IV.");
            }

            byte[] iv = Arrays.copyOfRange(encryptedData, 0, 16);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            byte[] cipherData = Arrays.copyOfRange(encryptedData, 16, encryptedData.length);
            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            byte[] decrypted = cipher.doFinal(cipherData);

            Files.write(Paths.get(outputPath), decrypted);
            System.out.println("[VOLKOV] >> Successfully unpacked: " + outputPath);

        } catch (Exception e) {
            System.err.println("[VOLKOV] >> Error during unpacking: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
        }
    }
}