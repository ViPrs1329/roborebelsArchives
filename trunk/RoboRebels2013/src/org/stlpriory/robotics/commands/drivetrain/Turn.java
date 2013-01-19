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
public class Turn extends CommandBase {

    private double timeout;
    private double speed;

    public Turn(double speed, double timeout) {
        requires(drivetrain);
        this.speed = speed;
        this.timeout = timeout;
    }

    /**
     * Command initialization logic
     */
    protected void initialize() {
        setTimeout(timeout);
        Debug.print("[" + this.getName() + "] Speed: " + this.speed);
        Debug.print("\tTimeout: " + timeout);
    }

    /**
     * Called repeatedly while the command is running
     */
    protected void execute() {
        drivetrain.turnLeft();
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
        Debug.println("\t\tDONE");
        drivetrain.stop();
    }

    /**
     * Called if the command is preempted or
     * canceled
     */
    protected void interrupted() {
        Debug.println("\t[interrupted] " + getName());
        drivetrain.stop();
    }
}
