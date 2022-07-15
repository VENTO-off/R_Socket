package relevant_craft.vento.r_launcher.r_socket.manager.console.commands;

import relevant_craft.vento.r_launcher.r_socket.manager.socket.SocketManager;
import relevant_craft.vento.r_launcher.r_socket.utils.Logger;

public class halt {

    public halt(String[] args) {
        SocketManager.halt();
        Logger.log("Halt success. (socket status = " + (SocketManager.isActive() ? "active" : "halted") + ")");
    }
}
