package relevant_craft.vento.r_launcher.r_socket.manager.cloudflare;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import relevant_craft.vento.r_launcher.r_socket.Settings;
import relevant_craft.vento.r_launcher.r_socket.utils.Logger;

import static relevant_craft.vento.r_launcher.r_socket.manager.download.DownloadManager.activeCookies;
import static relevant_craft.vento.r_launcher.r_socket.manager.download.DownloadManager.cookieManager;

public class CloudFlareManager {

    private static Thread pinger;
    private static boolean firstConnect = true;

    public static void initPinger() {
        pinger = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000 * 60 * Settings.CF_UPDATE);
                } catch (InterruptedException e) {}

                authCloudflare();
            }
        });

        authCloudflare();

        pinger.start();
    }

    public static void stopPinger() {
        pinger.stop();
    }

    private static void authCloudflare() {
        if (firstConnect) {
            Logger.log("Establishing connection...");
        }

        WebClient client = new WebClient(BrowserVersion.CHROME);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(true);
        client.getOptions().setThrowExceptionOnFailingStatusCode(false);
        client.getOptions().setRedirectEnabled(true);
        client.getCache().setMaxSize(0);
        if (cookieManager != null) {
            client.setCookieManager(cookieManager);
        }
        client.waitForBackgroundJavaScript(1000);
        client.setJavaScriptTimeout(1000);
        client.waitForBackgroundJavaScriptStartingBefore(1000);
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);

        try {
            Page page = client.getPage(Settings.CF_URL);

            if (page instanceof HtmlPage) {
                if (((HtmlPage) page).getBody().asText().contains("Cloudflare")) {
                    synchronized (page) {
                        page.wait(6000);
                    }
                }
            }

            cookieManager = client.getCookieManager();
            activeCookies = System.currentTimeMillis() + 1024 * 60 * 30;

            if (firstConnect) {
                Logger.log("Connection established.");
                firstConnect = false;
            }
        } catch (Exception ignored) {
            Logger.log("Connection error!");
            System.exit(3);
        }
    }
}
