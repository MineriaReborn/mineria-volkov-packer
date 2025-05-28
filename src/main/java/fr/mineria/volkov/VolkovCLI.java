package fr.mineria.volkov;

import fr.mineria.volkov.commands.*;
import picocli.CommandLine;

@CommandLine.Command(
    name = "volkov",
    mixinStandardHelpOptions = true,
    version = "Volkov 2.0.3",
    subcommands = {PackCommand.class, UnpackCommand.class}
)
public class VolkovCLI implements Runnable {

    @Override
    public void run() {
        CommandLine.usage(new VolkovCLI(), System.out);
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new VolkovCLI()).execute(args);
        System.exit(exitCode);
    }
}