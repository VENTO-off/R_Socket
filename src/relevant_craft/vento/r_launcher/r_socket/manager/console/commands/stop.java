package relevant_craft.vento.r_launcher.r_socket.manager.console.commands;

import relevant_craft.vento.r_launcher.r_socket.manager.database.DatabaseManager;
import relevant_craft.vento.r_launcher.r_socket.manager.cloudflare.CloudFlareManager;
import relevant_craft.vento.r_launcher.r_socket.manager.socket.SocketManager;
import relevant_craft.vento.r_launcher.r_socket.manager.stats.StatsManager;
import relevant_craft.vento.r_launcher.r_socket.utils.Logger;

public class stop {

    public stop(String[] args) {
        Logger.log("Stopping...");
        CloudFlareManager.stopPinger();
        SocketManager.stopSocket();
        DatabaseManager.stopDatabase();
        StatsManager.saveStats();
        Logger.log("All done.");
        System.exit(0);
    }
}
