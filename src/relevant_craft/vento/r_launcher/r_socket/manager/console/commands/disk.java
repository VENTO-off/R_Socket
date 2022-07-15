package relevant_craft.vento.r_launcher.r_socket.manager.console.commands;

import relevant_craft.vento.r_launcher.r_socket.manager.database.DatabaseManager;
import relevant_craft.vento.r_launcher.r_socket.utils.Logger;

public class disk {

    public disk(String[] args) {
        Logger.log("CurseForge files: " + DatabaseManager.getAmountFiles());
    }
}
