package de.thomas.chess_app.util;

public class DebugHelper {
    public static int DEBUG_LEVEL = 2;

    public static void debug(String string, int level) {
        debug(string, level, 0);
    }

    public static void debug(String string, int level, int indent) {
        if (level <= DEBUG_LEVEL) {
            String ind = "";
            for (int k = 0; k < indent; k++) {
                ind += "   ";
            }

            System.out.println(ind + string);
        }
    }
}
