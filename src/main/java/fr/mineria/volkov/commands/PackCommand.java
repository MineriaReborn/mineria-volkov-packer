package fr.mineria.volkov.commands;

import picocli.CommandLine;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Paths;

@CommandLine.Command(name = "pack", description = "Pack a JAR file into an encrypted .enc file")
public class PackCommand implements Runnable {

    @CommandLine.Option(names = "--input", required = true, description = "Input JAR file")
    private String inputPath;

    @CommandLine.Option(names = "--output", required = true, description = "Output encrypted file")
    private String outputPath;

    @CommandLine.Option(names = "--key", required = true, description = "AES encryption key (16 or 32 characters)")
    private String key;

    @Override
    public void run() {
        try {
            if (key.length() != 16 && key.length() != 32) {
                throw new IllegalArgumentException("Key must be 16 (AES-128) or 32 (AES-256) characters long.");
            }

            byte[] keyBytes = key.getBytes("UTF-8");
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] data = Files.readAllBytes(Paths.get(inputPath));
            byte[] encrypted = cipher.doFinal(data);

            Files.write(Paths.get(outputPath), encrypted);
            System.out.println("[VOLKOV] >> Successfully packed: " + outputPath);

        } catch (Exception e) {
            System.err.println("[VOLKOV] >> Error during packing: " + e.getMessage());
            e.printStackTrace();
        }
    }
}