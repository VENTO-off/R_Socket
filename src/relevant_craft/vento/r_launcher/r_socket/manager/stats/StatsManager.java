package relevant_craft.vento.r_launcher.r_socket.manager.stats;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StatsManager {

    private static int currentHour;
    private static int currentStats;

    public static void initStats() {
        Thread statsThread = new Thread(StatsManager::runStats);
        statsThread.start();
    }

    public static void saveStats() {
        logStats();
    }

    private static void runStats() {
        currentHour = getCurrentHour();
        currentStats = 0;

        while (true) {
            if (currentHour != getCurrentHour()) {
                logStats();
                currentHour = getCurrentHour();
                currentStats = 0;
            }

            try {
                Thread.sleep(1000 * 60);
            } catch (Exception ignored) {}
        }
    }

    public static void updateStats() {
        currentStats++;
    }

    private static void logStats() {
        File stats = new File("stats");
        if (!stats.exists()) { stats.mkdir(); }

        try (FileWriter writer = new FileWriter(stats + File.separator + getCurrentDate() + ".log", true)) {
            writer.append(getCurrentTime() + ": " + currentStats + "\n");
            writer.close();
        } catch (IOException ignored) {}
    }

    private static int getCurrentHour() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.HOUR_OF_DAY);
    }

    private static String getCurrentTime() {
        Date date = new Date(System.currentTimeMillis() - 1000 * 60 * 60);
        DateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MM.yy");
        return dateFormat.format(date);
    }

    public static String getCurrentDate() {
        Date date = new Date(System.currentTimeMillis() - 1000 * 60);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
}
