package fr.mineria.volkov;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.nio.file.*;

public class VolkovPacker {

	/**
	 * VolkovPacker – Chiffreur de fichiers .jar vers .enc
	 *
	 * Ce programme chiffre un fichier .jar avec AES (128 ou 256 bits) et produit un fichier .enc sécurisé.
	 * Utilisé pour sécuriser des .jar contre la rétro-ingénierie.
	 *
	 *
	 * Arguments :
	 * --input=        Chemin du fichier .jar à chiffrer
	 * --output=       Chemin de sortie pour le fichier chiffré .enc
	 * --key=          Clé secrète (16 ou 32 caractères pour AES)
	 *
	 * Exemple :
	 * java -jar volkov-packer.jar --pack --input=client.jar --output=client.enc --key=SuperSecretKey42
	 *
	 * @author CipheR_
	 * @project Mineria / Volkov Encryption
	 */
	
    public static void execute(String[] args) {
        String inputPath = VolkovUtils.getArg(args, "input");
        String outputPath = VolkovUtils.getArg(args, "output");
        String keyString = VolkovUtils.getArg(args, "key");

        if (inputPath == null || outputPath == null || keyString == null) {
            System.out.println("Usage: java -jar volkov-packer.jar --input=client.jar --output=client.enc --key=YourSecretKey");
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
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] data = Files.readAllBytes(Paths.get(inputPath));
            byte[] encrypted = cipher.doFinal(data);

            Files.write(Paths.get(outputPath), encrypted);
            System.out.println("[VOLKOV] >> Jar packé avec succès : " + outputPath);

        } catch (Exception e) {
            System.err.println("[VOLKOV] >> Erreur lors du chiffrement : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
