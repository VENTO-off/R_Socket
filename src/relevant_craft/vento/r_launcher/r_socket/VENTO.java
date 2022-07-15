package relevant_craft.vento.r_launcher.r_socket;

import relevant_craft.vento.r_launcher.r_socket.manager.console.ConsoleManager;
import relevant_craft.vento.r_launcher.r_socket.manager.database.DatabaseManager;
import relevant_craft.vento.r_launcher.r_socket.manager.cloudflare.CloudFlareManager;
import relevant_craft.vento.r_launcher.r_socket.manager.socket.SocketManager;
import relevant_craft.vento.r_launcher.r_socket.manager.stats.StatsManager;
import relevant_craft.vento.r_launcher.r_socket.utils.Logger;

public class VENTO {

    private static final String VERSION = "v1.5";

    public static void main(String[] args) {
        Logger.log("**********************************"           );
        Logger.log("*         R-Socket " + VERSION + "          *");
        Logger.log("*  Powered by Relevant-Craft.SU  *"           );
        Logger.log("**********************************"           );

        DatabaseManager.initDatabase();
        StatsManager.initStats();
        CloudFlareManager.initPinger();
        SocketManager.initSocket();
        ConsoleManager.initConsoleManager();

        Logger.log("R-Socket started.");
    }
}
