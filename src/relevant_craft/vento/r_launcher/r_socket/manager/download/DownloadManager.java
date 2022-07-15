package relevant_craft.vento.r_launcher.r_socket.manager.download;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import relevant_craft.vento.r_launcher.r_socket.manager.database.DatabaseManager;
import relevant_craft.vento.r_launcher.r_socket.manager.download.exceptions.ConnectionException;
import relevant_craft.vento.r_launcher.r_socket.manager.download.exceptions.FileDeletedException;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DownloadManager {

    public static CookieManager cookieManager = null;
    public static long activeCookies = 0;

    public static String downloadFile(String url) throws Exception {
        String fileUrl = url.substring(url.indexOf("/minecraft/") + "/minecraft/".length());

        //Check file in database
        CF_File file = DatabaseManager.getFile(fileUrl);
        if (file != null) {
            DatabaseManager.updateFile(fileUrl);
            return file.getDirectUrl();
        }

        //Get direct link of file
        String redirectUrl = getDirectLinkAndBypassCloudflare(url);

        //Check file deleted
        if (redirectUrl.equals(url)) {
            throw new FileDeletedException();
        }

        //Write to database
        DatabaseManager.addFile(new CF_File(fileUrl, redirectUrl));

        return redirectUrl;
    }

    private static String getDirectLinkAndBypassCloudflare(String url) throws Exception {
        if (cookieManager != null && activeCookies > System.currentTimeMillis()) {
            return getDirectLink(url);
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
        Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);

        try {
            Page page = client.getPage(url);

            if (page instanceof HtmlPage) {
                if (((HtmlPage) page).getBody().asText().contains("Cloudflare")) {
                    synchronized (page) {
                        page.wait(6000);
                    }
                }

                cookieManager = client.getCookieManager();
                activeCookies = System.currentTimeMillis() + 1024 * 60 * 30;

                return client.getPage(new URL(url)).getUrl().toString();
            } else if (page instanceof UnexpectedPage) {
                return page.getUrl().toString();
            }

        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + url);
            throw new ConnectionException();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        return url;
    }

    private static String getDirectLink(String url) throws Exception {
        Connection.Response response;
        try {
            response = Jsoup.connect(url)
                    .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .header("accept-encoding", "gzip, deflate, sdch, br")
                    .header("accept-language", "en-US,en;q=0.8")
                    .header("cache-control", "max-age=0")
                    .header("user-agent", BrowserVersion.CHROME.getUserAgent())
                    .header("sec-fetch-user", "?1")
                    .header("upgrade-insecure-requests", "1")
                    .ignoreHttpErrors(true)
                    .ignoreContentType(true)
                    .followRedirects(true)
                    .cookies(getCookies())
                    .method(Connection.Method.GET)
                    .timeout(5000)
                    .execute();
        } catch (IOException ex) {
            throw new ConnectionException();
        }

        return response.url().toString();
    }

    private static Map<String, String> getCookies() {
        Map<String, String> cookies = new HashMap<>();
        for (Cookie cookie : cookieManager.getCookies()) {
            cookies.put(cookie.getName(), cookie.getValue());
        }

        return cookies;
    }
}
