/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package roborebels.example.commands.shooter;

import roborebels.example.commands.CommandBase;
import roborebels.example.misc.Debug;

/**
 *
 */
public class ShooterFeed extends CommandBase {

    private boolean forceFeed = false;
    private double timeout;

    public ShooterFeed(double timeout) {
        this.timeout = timeout;
    }

    public ShooterFeed(double timeout, boolean forceFeed) {
        this(timeout);
        this.forceFeed = forceFeed;
    }

    protected void initialize() {
        Debug.print("[" + this.getName() + "] initialize");
        Debug.print("\tTimeout: " + timeout);
        Debug.print("\tForceFeed: " + forceFeed);
        setTimeout(timeout);
    }

    protected void execute() {
        if (shooter.getSpeed() > 0.2 || forceFeed) {
            shooter.setFeederForward();
        } else {
            Debug.print("\t[ERROR]: Shooter not fast enough! " + shooter.getSpeed());
        }
    }

    protected boolean isFinished() {
        return isTimedOut();
    }

    protected void end() {
        Debug.println("\t\tDONE");
        shooter.stopFeeder();
    }

    protected void interrupted() {
        Debug.println("[interrupted] " + getName());
        shooter.stopFeeder();
    }
}
