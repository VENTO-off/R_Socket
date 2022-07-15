package relevant_craft.vento.r_launcher.r_socket.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UrlUtils {

    public static String encodeUrl(String url) {
        try {
            String file = url.substring(url.lastIndexOf('/') + 1);
            url = url.substring(0, url.lastIndexOf('/') + 1);
            url += URLEncoder.encode(file, StandardCharsets.UTF_8.name());
            url = url.replace("+", "%20");
        } catch (Exception ignored) {}

        return url;
    }

    public static String fixUrl(String url) {
        return url.replace(" ", "%20").replace("+", "%20");
    }
}
