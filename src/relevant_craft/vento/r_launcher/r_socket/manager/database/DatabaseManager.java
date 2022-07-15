package relevant_craft.vento.r_launcher.r_socket.manager.database;

import relevant_craft.vento.r_launcher.r_socket.manager.download.CF_File;
import relevant_craft.vento.r_launcher.r_socket.utils.Logger;

import java.io.File;
import java.sql.*;

public class DatabaseManager {

    private static Connection connection = null;

    private static final File dir = new File("curseforge");
    private static final File DATABASE = new File(dir + File.separator + "files.db");
    private static final String TABLE_NAME = "cf_files";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_URL = "fileurl";
    private static final String COLUMN_DIRECT_URL = "directurl";
    private static final String COLUMN_DOWNLOADS = "downloads";

    public static void initDatabase() {
        String url = "jdbc:sqlite:" + DATABASE.getPath();

        if (!dir.exists()) {
            dir.mkdir();
        }

        boolean doCreate = false;
        if (!DATABASE.exists()) {
            doCreate = true;
        }

        try {
            connection = DriverManager.getConnection(url);

            if (doCreate) {
                String sqlCreate = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        COLUMN_ID + " integer PRIMARY KEY, " +
                        COLUMN_URL + " text NOT NULL, " +
                        COLUMN_DIRECT_URL + " text NOT NULL, " +
                        COLUMN_DOWNLOADS + " integer);";
                Statement stmt = connection.createStatement();
                stmt.execute(sqlCreate);
                stmt.close();
                Logger.log("Database created.");
            }
        } catch (SQLException e) {
            Logger.log("Database corrupted. Creating new...");
            DATABASE.renameTo(new File(DATABASE.getName() + ".corr"));
            System.exit(1);
        }
    }

    public static void stopDatabase() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {}
        }
    }

    public static void addFile(CF_File file) {
        String sqlInsert = "INSERT INTO " + TABLE_NAME + " (" +
                COLUMN_URL + ", " +
                COLUMN_DIRECT_URL + ", " +
                COLUMN_DOWNLOADS + ") VALUES (" +
                "\"" + file.getUrl() + "\", " +
                "\"" + file.getDirectUrl() + "\", " +
                "1" + ")";

        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sqlInsert);
            stmt.close();
        } catch (SQLException e) {
            Logger.log(e.getMessage());
        }
    }

    public static CF_File getFile(String url) {
        String sqlSelect = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_URL + " = " + "'" + url + "'";

        try (Statement stmt = connection.createStatement();
             ResultSet result = stmt.executeQuery(sqlSelect)) {

            if (result.next()) {
                return new CF_File(result.getInt(COLUMN_ID), result.getString(COLUMN_URL), result.getString(COLUMN_DIRECT_URL));
            } else {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public static void updateFile(String url) {
        String sqlUpdate = "UPDATE " + TABLE_NAME + " SET " + COLUMN_DOWNLOADS + " = " + COLUMN_DOWNLOADS + " + 1" + " WHERE " + COLUMN_URL + " = " + "'" + url + "'";

        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sqlUpdate);
            stmt.close();
        } catch (SQLException e) {
            Logger.log(e.getMessage());
        }
    }

    public static void deleteFile(CF_File file) {
        String sqlDelete = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + "'" + file.getId() + "'";

        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sqlDelete);
            stmt.close();
        } catch (SQLException e) {
            Logger.log(e.getMessage());
        }
    }

    public static int getAmountFiles() {
        String sqlCount = "SELECT COUNT(*) AS rowcount FROM " + TABLE_NAME;

        try (Statement stmt = connection.createStatement();
             ResultSet result = stmt.executeQuery(sqlCount)) {

            if (result.next()) {
                return result.getInt("rowcount");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            return 0;
        }
    }
}
