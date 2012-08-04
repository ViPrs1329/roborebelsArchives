/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author dfuglsan
 */
public class RRLogger {

    private RRLogger() {
        // Do not allow anyone to create an instance
    }

    static void logInfo(Class c, String methodName, String msg) {
        logMsg("INFO", c, methodName, msg);
    }

    static void logDebug(Class c, String methodName, String msg) {
        if (!RoboRebels.DEBUG_ON) {
            return;
        }
        logMsg("DEBUG", c, methodName, msg);
    }

    static void logError(Class c, String methodName, String msg) {
        print("ERROR", c.getName(), methodName, msg);
    }

    static void logError(Class c, String methodName, Exception e) {
        String msg = (e != null ? e.getMessage() : "");
        print("ERROR", c.getName(), methodName, msg);
        if (e != null) {
            e.printStackTrace();
        }
    }

    private static void logMsg(String level, Class c, String methodName, String msg) {
        if (c.equals(RoboRebels.class)) {
            print(level, "RoboRebels", methodName, msg);

        } else if (c.equals(RRAction.class)) {
            print(level, "RRAction", methodName, msg);

        } else if (c.equals(RRAutonomous.class)) {
            print(level, "RRAutonomous", methodName, msg);

        } else if (c.equals(RRDrive.class)) {
            print(level, "RRDrive", methodName, msg);

        } else if (c.equals(RRGatherer.class)) {
            print(level, "RRGatherer", methodName, msg);

        } else if (c.equals(RRMecanumDrive.class)) {
            print(level, "RRMecanumDrive", methodName, msg);
        }
    }

    private static void print(String level, String className, String methodName, String msg) {
        StringBuffer sb = new StringBuffer(200);
        sb.append(level).append(": ").append(className).append(".").append(methodName).append(" - ");
        if (msg != null) {
            sb.append(msg);
        }
//        synchronized(RRLogger.class) {
        System.out.println(sb.toString());
//        }
    }
}
