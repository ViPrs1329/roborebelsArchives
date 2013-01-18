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

    protected void initialize() {
        setTimeout(timeout);
        Debug.print("[" + this.getName() + "] Speed: " + this.speed);
        Debug.print("\tTimeout: " + timeout);
    }

    protected void execute() {
        drivetrain.turnLeft();
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
