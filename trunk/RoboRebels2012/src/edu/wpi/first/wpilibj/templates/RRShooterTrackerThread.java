package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.Timer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * Still need to come up with a CLEAN way to implement this.
 * 
 * @author dmw
 */
public class RRShooterTrackerThread extends Thread {

    private RRShooter shooter;
    private RRTracker tracker;
    private RRDriveThread driveThread;
    private RRDrive drive;
    private boolean run = true;
    private boolean shootAndTrack = false;

    public RRShooterTrackerThread(int swjc, int lsvc, int tltvc, int tltlsc,
                                  RRBallSensor ballSensor, RRDIPSwitch d, RRGatherer gr,
                                  ADXL345_I2C a, RRDriveThread dr, RRDrive drv) {
        tracker = new RRTracker(a, d, dr, drv);
        shooter = new RRShooter(swjc, lsvc, tltvc, tltlsc, tracker, ballSensor, gr);
        tracker.setShooter(shooter);

    }

    public void run() {
        while (run) {
//            RRLogger.logDebug(this.getClass(),"run()","Running...");
            if (shootAndTrack) {
                RRLogger.logDebug(this.getClass(),"run()","Shooting and Tracking... " + Timer.getFPGATimestamp());
                tracker.trackTarget(RoboRebels.AUTO_TARGET);
                if (!shooter.tracking) // Don't check for user input or shoot if we are tracking target.
                {
                    shooter.shoot();
                }
            }
        }
    }

    public void free() {
        run = false;
    }

    public void enable() {
        shootAndTrack = true;
    }

    public void disable() {
        shootAndTrack = false;
    }

    public void resetShooter() {
        if (shooter != null) {
            shooter.reset();
        }
    }
    
    public void resetShooterAfterAuton() {
        if (shooter != null) {
            shooter.reset_after_auton();
        }
    }

    public RRShooter getShooter() {
        return shooter;
    }

    public RRTracker getTracker() {
        return tracker;
    }
}
