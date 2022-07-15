package relevant_craft.vento.r_launcher.r_socket.manager.console.commands;

import relevant_craft.vento.r_launcher.r_socket.utils.Logger;

public class mem {

    public mem(String[] args) {
        long max = Runtime.getRuntime().maxMemory();
        long total = Runtime.getRuntime().totalMemory();
        long free = Runtime.getRuntime().freeMemory();
        long used = total - free;

        Logger.log("Used memory: " + used / 1024L / 1024L + " MB" + " (" + used * 100L / max + "%)");
        Logger.log("Total memory: " + total / 1024L / 1024L + " MB");
        Logger.log("Max memory: " + max / 1024L / 1024L + " MB");
    }
}
