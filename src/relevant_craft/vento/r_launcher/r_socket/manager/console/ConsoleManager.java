package relevant_craft.vento.r_launcher.r_socket.manager.console;

import relevant_craft.vento.r_launcher.r_socket.manager.console.commands.*;
import relevant_craft.vento.r_launcher.r_socket.utils.Logger;

import java.util.Arrays;
import java.util.Scanner;

public class ConsoleManager {

    public static void initConsoleManager() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            runCommand(sc.nextLine());
        }
    }

    private static void runCommand(String command) {
        Logger.logSilent("Admin dispatched command: " + command);
        String[] args = command.split(" ");
        String commandName = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);

        if (commandName.equalsIgnoreCase("list")) {
            new list(args);
        } else if (commandName.equalsIgnoreCase("stop")) {
            new stop(args);
        } else if (commandName.equalsIgnoreCase("wget")) {
            new wget(args);
        } else if (commandName.equalsIgnoreCase("disk")) {
            new disk(args);
        } else if (commandName.equalsIgnoreCase("mem")) {
            new mem(args);
        } else if (commandName.equalsIgnoreCase("halt")) {
            new halt(args);
        } else {
            Logger.log("Unknown command '" + commandName + "'.");
        }
    }
}
