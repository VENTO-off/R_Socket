package relevant_craft.vento.r_launcher.r_socket.utils;

import java.math.BigInteger;

public class FormatUtils {

    private static final String[] BINARY_UNITS = { "B", "kB", "MB", "GB", "TB", "PB", "EB" };

    public static String formatSize(BigInteger bytes) {
        final String[] units = BINARY_UNITS;
        final int base = 1024;

        if (bytes.doubleValue() < base) {
            return bytes + " " + units[0];
        }

        final int exponent = (int) (Math.log(bytes.doubleValue()) / Math.log(base));
        final String unit = units[exponent];
        return String.format("%.1f %s", bytes.doubleValue() / Math.pow(base, exponent), unit);
    }
}

