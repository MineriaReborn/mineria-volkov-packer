package fr.mineria.volkov;

import java.util.Arrays;

public class VolkovUtils {

    public static String getArg(String[] args, String name) {
        return Arrays.stream(args)
                .filter(arg -> arg.startsWith("--" + name + "="))
                .map(arg -> arg.substring(name.length() + 3))
                .findFirst()
                .orElse(null);
    }
    
    public static boolean contains(String[] args, String flag) {
        for (String arg : args) {
            if (arg.equalsIgnoreCase(flag)) return true;
        }
        return false;
    }
}
