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

    static void logDebug(Class c, String methodName, String msg) {
        if (!RoboRebels.DEBUG_ON) {
            return;
        }
        
        if (c.equals(RoboRebels.class)) {
            print("DEBUG","RoboRebels",methodName,msg);

        } else if (c.equals(RRAction.class)) {
            print("DEBUG","RRAction",methodName,msg);

        } else if (c.equals(RRAutonomous.class)) {
            print("DEBUG","RRAutonomous",methodName,msg);

        } else if (c.equals(RRBallSensor.class)) {
            print("DEBUG","RRBallSensor",methodName,msg);

        } else if (c.equals(RRBridgeArm.class)) {
            print("DEBUG","RRBridgeArm",methodName,msg);

        } else if (c.equals(RRDIPSwitch.class)) {
            print("DEBUG","RRDIPSwitch",methodName,msg);

        } else if (c.equals(RRDrive.class)) {
            print("DEBUG","RRDrive",methodName,msg);

        } else if (c.equals(RRGatherer.class)) {
            print("DEBUG","RRGatherer",methodName,msg);

        } else if (c.equals(RRMecanumDrive.class)) {
            print("DEBUG","RRMecanumDrive",methodName,msg);

        } else if (c.equals(RRShooter.class)) {
            print("DEBUG","RRShooter",methodName,msg);

        } else if (c.equals(RRSlider.class)) {
            print("DEBUG","RRSlider",methodName,msg);
            
        } else if (c.equals(RRTracker.class)) {
            print("DEBUG","RRTracker",methodName,msg);

        } else if (c.equals(RRBridgeArmThread.class)) {
            print("DEBUG","RRBridgeArmThread",methodName,msg);

        } else if (c.equals(RRCameraThread.class)) {
            print("DEBUG","RRCameraThread",methodName,msg);

        } else if (c.equals(RRGathererThread.class)) {
            print("DEBUG","RRGathererThread",methodName,msg);

        } else if (c.equals(RRShooterTrackerThread.class)) {
            print("DEBUG","RRShooterTrackerThread",methodName,msg);
        }
    }

    static void logError(Class c, String methodName, String msg) {
        print("ERROR",c.getName(),methodName,msg);
    }

    static void logError(Class c, String methodName, Exception e) {
        String msg = (e != null ? e.getMessage() : "");
        print("ERROR",c.getName(),methodName,msg);
        if (e != null) {
            e.printStackTrace();           
        }
    }

    private static void print(String level, String className, String methodName, String msg) {
        StringBuffer sb = new StringBuffer(200);
        sb.append(level).append(": ").append(className).append(".").append(methodName).append(" - ");
        if (msg != null) {
            sb.append(msg);
        }
        System.out.println(sb.toString());
    }
}
