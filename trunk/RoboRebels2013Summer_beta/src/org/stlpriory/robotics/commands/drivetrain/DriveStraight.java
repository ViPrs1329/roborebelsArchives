/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.drivetrain;

import org.stlpriory.robotics.commands.CommandBase;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 */
public class DriveStraight extends CommandBase {

    private double timeout;
    private double speed;

    /**
     * @param speed The forward speed of the robot [-1.0..1.0]
     * @param timeout The command timeout in seconds
     */
    public DriveStraight(double speed, double timeout) {
        super("DriveStraight");
        requires(drivetrain);
        this.speed = speed;
        this.timeout = timeout;
    }

    /**
     * Command initialization logic
     */
    protected void initialize() {
        setTimeout(timeout);
        Debug.print("[" + this.getName()
                + "] speed: " + this.speed
                + ", timeout: " + this.timeout);

        Debug.print("\tTimeout: " + timeout);
    }

    /**
     * Called repeatedly while the command is running
     */
    protected void execute() {
        drivetrain.straight(speed);
    }

    /**
     * Called repeatedly to determine if the command is
     * finished executing
     * @return true if the command execution is finished
     */
    protected boolean isFinished() {
        return isTimedOut();
    }

    /**
     * Called one after isFinished() returns true
     */
    protected void end() {
        Debug.print("[" + getName() + "] end");
        drivetrain.stop();
    }

    /**
     * Called if the command is preempted or
     * canceled
     */
    protected void interrupted() {
        Debug.print("[" + getName() + "] interrupted");
        drivetrain.stop();
    }
}
