/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package roborebels.example.misc;

/**
 *
 */
public class Debug {

    private static boolean kDebugMode = true;

    /**
     * If in debug mode, prints specified string without a newline
     *
     * @param s
     */
    public static void print(String s) {
        if (kDebugMode) {
            System.out.print(s);
        }
    }

    /**
     * If in debug mode, prints specified string with a newline
     *
     * @param s
     */
    public static void println(String s) {
        if (kDebugMode) {
            System.out.println(s);
        }
    }

    /**
     * If in debug mode, prints specified string with a newline
     *
     * @param s
     */
    public static void err(String s) {
        if (kDebugMode) {
            System.err.println(s);
        }
    }

    /**
     * Returns true if in debug mode, false otherwise.
     *
     * @return kDebugMode
     */
    public static boolean getMode() {
        return kDebugMode;
    }

    /**
     * Set the value for kDebugMode
     */
    public static void setMode(boolean mode) {
        kDebugMode = mode;
    }
}
