package relevant_craft.vento.r_launcher.r_socket.manager.socket.commands;

import com.google.gson.JsonObject;
import relevant_craft.vento.r_launcher.r_socket.manager.download.DownloadManager;
import relevant_craft.vento.r_launcher.r_socket.manager.download.exceptions.CloudFlareException;
import relevant_craft.vento.r_launcher.r_socket.manager.download.exceptions.ConnectionException;
import relevant_craft.vento.r_launcher.r_socket.manager.download.exceptions.FileDeletedException;
import relevant_craft.vento.r_launcher.r_socket.manager.socket.SocketManager;
import relevant_craft.vento.r_launcher.r_socket.utils.ErrorUtils;
import relevant_craft.vento.r_launcher.r_socket.utils.UrlUtils;

import java.io.PrintWriter;

public class curseforge_download {

    public curseforge_download(JsonObject json, PrintWriter out) {
        if (!json.has("link")) {
            out.println(SocketManager.sendErrorMsg("Указаны не все аргументы."));
            return;
        }

        String url = json.get("link").getAsString();
        if (!url.startsWith("https://minecraft.curseforge.com") &&
            !url.startsWith("http://minecraft.curseforge.com") &&
            !url.startsWith("https://curseforge.com") &&
            !url.startsWith("http://curseforge.com") &&
            !url.startsWith("https://www.curseforge.com") &&
            !url.startsWith("http://www.curseforge.com")
        ) {
            out.println(SocketManager.sendErrorMsg("Неверная ссылка на файл."));
            return;
        }

        try {
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("response", UrlUtils.fixUrl(DownloadManager.downloadFile(url)));
            out.println(SocketManager.sendAnswer(jsonResponse));
        } catch (Exception e) {
            if (e instanceof CloudFlareException) {
                ErrorUtils.logError("CloudFlare blocked download." + " => " + url);
                out.println(SocketManager.sendErrorMsg("CloudFlare заблокировал скачивание.\nПожалуйста, сообщите об этом администрации."));
            } else if (e instanceof ConnectionException) {
                ErrorUtils.logError("Can't download file." + " => " + url);
                out.println(SocketManager.sendErrorMsg("Ошибка при скачивании файла.\nПопробуйте, пожалуйста, позже."));
            } else if (e instanceof FileDeletedException) {
                ErrorUtils.logError("File deleted." + " => " + url);
                out.println(SocketManager.sendErrorMsg("К сожалению, этот `файл удален` на сервере.\nПопробуйте скачать другую версию этого файла."));
            } else {
                ErrorUtils.logError("Unknown error." + " => " + url);
                out.println(SocketManager.sendErrorMsg("Неизвестная ошибка при скачивании файла.\nПопробуйте скачать другую версию этого файла."));
            }
        }
    }
}
