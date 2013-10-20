/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.misc;

/**
 *
 */
public class Debug {

    private static boolean DEBUG_MODE = true;

    /**
     * If in debug mode, prints specified string without a newline
     *
     * @param s
     */
    public static void print(String s) {
        if (DEBUG_MODE) {
            System.out.print(s);
        }
    }

    /**
     * If in debug mode, prints specified string with a newline
     *
     * @param s
     */
    public static void println(String s) {
        if (DEBUG_MODE) {
            System.out.println(s);
        }
    }

    /**
     * If in debug mode, prints specified string with a newline
     *
     * @param s
     */
    public static void err(String s) {
        if (DEBUG_MODE) {
            System.err.println(s);
        }
    }

    /**
     * Returns true if in debug mode, false otherwise.
     *
     * @return kDebugMode
     */
    public static boolean getMode() {
        return DEBUG_MODE;
    }

    /**
     * Set the value for kDebugMode
     */
    public static void setMode(boolean mode) {
        DEBUG_MODE = mode;
    }
}
