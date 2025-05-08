package fr.mineria.volkov;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.nio.file.*;

/**
 * VolkovUnpacker – Déchiffreur de fichiers .enc vers .jar
 *
 * Ce programme déchiffre un fichier .enc généré par VolkovPacker
 * en utilisant AES (128 ou 256 bits) pour restaurer le fichier .jar d'origine.
 *
 *
 * Arguments :
 * --input=        Chemin du fichier .enc à déchiffrer
 * --output=       Chemin de sortie pour le fichier .jar restauré
 * --key=          Clé secrète utilisée pour le chiffrement (16 ou 32 caractères pour AES)
 *
 * Exemple :
 * java -jar volkov-unpacker.jar --unpack --input=client.enc --output=restored-client.jar --key=SuperSecretKey42
 *
 * @author CipheR_
 * @project Mineria / Volkov Encryption
 */
public class VolkovUnpacker {

    public static void execute(String[] args) {
        String inputPath = VolkovUtils.getArg(args, "input");
        String outputPath = VolkovUtils.getArg(args, "output");
        String keyString = VolkovUtils.getArg(args, "key");

        if (inputPath == null || outputPath == null || keyString == null) {
            System.out.println("Usage: java -jar volkov-unpacker.jar --input=client.enc --output=restored.jar --key=YourSecretKey");
            System.exit(1);
        }

        try {
            if (keyString.length() != 16 && keyString.length() != 32) {
                System.err.println("[VOLKOV] >> Erreur : la clé doit faire 16 (AES-128) ou 32 (AES-256) caractères.");
                System.exit(1);
            }

            byte[] keyBytes = keyString.getBytes("UTF-8");
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] encryptedData = Files.readAllBytes(Paths.get(inputPath));
            byte[] decrypted = cipher.doFinal(encryptedData);

            Files.write(Paths.get(outputPath), decrypted);
            System.out.println("[VOLKOV] >> Jar restauré avec succès : " + outputPath);

        } catch (Exception e) {
            System.err.println("[VOLKOV] >> Erreur lors du déchiffrement : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
