package relevant_craft.vento.r_launcher.r_socket.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ErrorUtils {

    private static File dir = new File("curseforge");

    public static void logError(String error) {
        if (!dir.exists()) {
            dir.mkdir();
        }

        try (FileWriter writer = new FileWriter(dir + File.separator + "errors.txt", true)) {
            if (error == null) {
                writer.append("\n");
            } else {
                writer.append("[R-Socket Error " + TimeUtils.getCurrentTime() + "] " + error + "\n");
            }
            writer.close();
        } catch (IOException ignored) {}
    }
}
