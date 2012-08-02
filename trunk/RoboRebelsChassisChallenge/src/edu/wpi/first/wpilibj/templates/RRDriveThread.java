/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author dmw
 */
public class RRDriveThread extends Thread {

    private RRDrive drive;
    private boolean run = true;
    private boolean isDriveable = false;

    public RRDriveThread() {
        drive = new RRDrive(2, 1);
    }

    public void run() {
        while (run) {
//            RRLogger.logDebug(this.getClass(),"run()","Running... " + isDriveable);

            if (isDriveable) {
//                RRLogger.logDebug(this.getClass(),"run()","Driving...");
                drive.drive(false);
            }
        }
    }

    public void enableDrive() {
        isDriveable = true;
    }

    public void disableDrive() {
        isDriveable = false;
    }

    public void free() {
        run = false;
        try {
            this.join();
        } catch (InterruptedException e) {
        }
        drive = null;
    }

    public RRDrive getDrive() {
        return drive;
    }
}
