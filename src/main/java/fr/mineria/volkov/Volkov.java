package fr.mineria.volkov;

/**
 * Classe principale du projet Volkov Encryption.
 * Gère la redirection vers les opérations de pack ou d'unpack selon les arguments.
 *
 * Usage :
 * java -jar volkov.jar --pack --input=... --output=... --key=...
 * java -jar volkov.jar --unpack --input=... --output=... --key=...
 *
 * @author CipheR_
 */

public class Volkov {

    public static void main(String[] args) {
        boolean isPack = VolkovUtils.contains(args, "--pack");
        boolean isUnpack = VolkovUtils.contains(args, "--unpack");

        if (isPack == isUnpack) {
            System.err.println("[VOLKOV] >> Erreur : Veuillez spécifier --pack ou --unpack (exclusif).");
            System.exit(1);
        }

        if (isPack) {
            VolkovPacker.execute(args);
        } else {
            VolkovUnpacker.execute(args);
        }
    }
}