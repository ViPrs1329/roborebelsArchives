/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package roborebels.example.commands.drivetrain;

import roborebels.example.commands.CommandBase;
import roborebels.example.misc.Debug;

/**
 *
 */
public class DriveStraight extends CommandBase {

    private double timeout;
    private double speed;

    public DriveStraight(double speed, double timeout) {
        requires(drivetrain);
        this.speed = speed;
        this.timeout = timeout;
    }

    protected void initialize() {
        setTimeout(timeout);
        Debug.print("[" + this.getName() + "] Speed: " + this.speed);
        Debug.print("\tTimeout: " + timeout);
    }

    protected void execute() {
        drivetrain.straight(speed);
    }

    protected boolean isFinished() {
        return isTimedOut();
    }

    protected void end() {
        Debug.println("\t\tDONE");
        drivetrain.stop();
    }

    protected void interrupted() {
        Debug.println("\t[interrupted] " + getName());
        drivetrain.stop();
    }
}
