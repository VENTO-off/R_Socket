package relevant_craft.vento.r_launcher.r_socket.manager.console.commands;

import relevant_craft.vento.r_launcher.r_socket.manager.download.DownloadManager;
import relevant_craft.vento.r_launcher.r_socket.manager.download.exceptions.CloudFlareException;
import relevant_craft.vento.r_launcher.r_socket.manager.download.exceptions.ConnectionException;
import relevant_craft.vento.r_launcher.r_socket.manager.download.exceptions.FileDeletedException;
import relevant_craft.vento.r_launcher.r_socket.utils.Logger;
import relevant_craft.vento.r_launcher.r_socket.utils.UrlUtils;

public class wget {

    private String url;

    public wget(String[] args) {
        if (args.length < 1) {
            Logger.log("Not enough arguments. Syntax: wget <url>");
            return;
        }

        url = args[0];

        try {
            Logger.log(UrlUtils.fixUrl(DownloadManager.downloadFile((url))));
        } catch (Exception e) {
            if (e instanceof CloudFlareException) {
                Logger.log("CloudFlare blocked download.");
            } else if (e instanceof ConnectionException) {
                Logger.log("Can't download file.");
            } else if (e instanceof FileDeletedException) {
                Logger.log("File deleted.");
            } else {
                Logger.log("Unknown error.");
            }
            e.printStackTrace();
        }
    }
}
