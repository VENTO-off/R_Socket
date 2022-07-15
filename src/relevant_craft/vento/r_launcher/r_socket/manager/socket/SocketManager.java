package relevant_craft.vento.r_launcher.r_socket.manager.socket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import relevant_craft.vento.r_launcher.r_socket.Settings;
import relevant_craft.vento.r_launcher.r_socket.manager.stats.StatsManager;
import relevant_craft.vento.r_launcher.r_socket.utils.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class SocketManager {
    private static ServerSocket socket;
    private static Thread socketThread;
    private static Set<Thread> clients = new HashSet<>();
    private static boolean isActive = true;

    public static void initSocket() {
        socketThread = new Thread(SocketManager::runSocket);
        socketThread.start();
    }

    private static void runSocket() {
        try {
            socket = new ServerSocket(Settings.SOCKET_PORT);
            Logger.log("Socket started on port " + Settings.SOCKET_PORT + ".");
        } catch (IOException e) {
            Logger.log("Can't start socket!");
            System.exit(2);
        }

        try {
            while (true) {
                SocketClient client = new SocketClient(socket.accept());
                Thread thread = new Thread(client);
                thread.start();

                clients.add(thread);
                StatsManager.updateStats();
            }
        } catch (Exception ignored) {}
    }

    public static void stopSocket() {
        Logger.log("Stopping socket...");
        isActive = false;

        Thread waitSocket = new Thread(() -> {
            int dots = 0;
            boolean wait = false;
            while (true) {
                if (clients.isEmpty()) {
                    try {
                        socket.close();
                        if (wait) System.out.println("\r");
                        Logger.log("Socket stopped.");
                    } catch (Exception ignored) {}
                    return;
                }
                try {
                    Thread.sleep(250);
                    System.out.print("\rWaiting for " + clients.size() + " clients to finish" + renderDots(dots));
                    dots++; if (dots > 3) dots = 0; wait = true;
                } catch (Exception ignored) {}
            }
        });
        waitSocket.start();
        try {
            waitSocket.join();
        } catch (Exception ignored) {}
    }

    public static void halt() {
        isActive = !isActive;
    }

    public static void removeClient(Thread client) {
        clients.remove(client);
    }

    public static int getClientsAmount() {
        return clients.size();
    }

    public static boolean isActive() {
        return isActive;
    }

    public static String sendErrorMsg(String msg) {
        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        json.addProperty("error", true);
        json.addProperty("errorMessage", msg);
        return gson.toJson(json);
    }

    public static String sendAnswer(JsonObject json) {
        Gson gson = new Gson();
        json.addProperty("error", false);
        return gson.toJson(json);
    }

    private static String renderDots(int amount) {
        StringBuilder dots = new StringBuilder();
        for (int i = 1; i <= 3; i++) {
            if (i <= amount) {
                dots.append(".");
            } else {
                dots.append(" ");
            }
        }

        return dots.toString();
    }
}
