/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author aidan/alan
 */
public class RRAutonomous {

    public RRAutonomous() {
    }

    /*
     * called once at start of Autonomous period
     */
    void auton_init() {
        RoboRebels.time_started_waiting = Timer.getFPGATimestamp();
    }

    /*
     * called repeatedly suring Autonomous period
     */
    void auton_periodic() {

        if (RoboRebels.autonomous_tracking_failed) {
            // If tracking failed, end shooting
            RoboRebels.autonomous_complete = true;   // TODO: Make robot still drive towards bridge to get balls
        }
    }
}
