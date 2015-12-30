package de.thomas.chess_app.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class DebugHelper {
    private static PrintStream stream = System.out;
    public static final int DEBUG_LEVEL = 2;
    public static boolean useFile = false;


    static {
        if (useFile) {
            try {
                stream = new PrintStream(new FileOutputStream("output.txt"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }



    public static void debug(String string, int level) {
        debug(string, level, 0);
    }

    public static void debug(String string, int level, int indent) {
        if (level <= DEBUG_LEVEL) {
            String ind = "";
            for (int k = 0; k < indent; k++) {
                ind += "   ";
            }

            stream.println(ind + string);
        }
    }
}
