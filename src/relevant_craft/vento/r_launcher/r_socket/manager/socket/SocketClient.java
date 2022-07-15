package relevant_craft.vento.r_launcher.r_socket.manager.socket;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import relevant_craft.vento.r_launcher.r_socket.manager.socket.commands.curseforge_download;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient implements Runnable {
    private Socket client;
    private Scanner in;
    private PrintWriter out;

    public SocketClient(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            in = new Scanner(client.getInputStream());
            out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), "Windows-1251"), true);

            String request = in.nextLine();

            if (!SocketManager.isActive()) {
                out.println(SocketManager.sendErrorMsg("Скачивание модов/текстур/модпаков временно не доступно.\nПопробуйте через несколько минут.\nУ нас ведутся `тех.работы`."));
//                out.println(SocketManager.sendErrorMsg("Скачивание модов/текстур/модпаков временно не доступно.\nИзвините, но у нас сломалась эта функция :("));
                return;
            }

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(request);
            JsonObject json = element.getAsJsonObject();

            if (json.has("command")) {
                String commandName = json.get("command").getAsString();
                if (commandName.equalsIgnoreCase("curseforge_download")) {
                    new curseforge_download(json, out);
                }
            }
        } catch (Exception ignored) {
            ;
        } finally {
            SocketManager.removeClient(Thread.currentThread());
            try {
                in.close();
                out.close();
                client.close();
            } catch (Exception ignored) {}
        }
    }
}
