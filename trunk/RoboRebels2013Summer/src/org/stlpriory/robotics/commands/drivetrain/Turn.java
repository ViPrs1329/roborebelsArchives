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
    private double rotation;
    private double gyroAngle;

    /**
     * @param rotation The rate of rotation for the robot that is completely independent of
     * the translation. [-1.0..1.0]
     * @param gyroAngle The current angle reading from the gyro.  Use this to implement field-oriented controls.
     * @param timeout The command timeout in seconds
     */
    public Turn(double rotation, double gyroAngle, double timeout) {
        super("Turn");
        requires(drivetrain);
        this.rotation  = rotation;
        this.gyroAngle = gyroAngle;
        this.timeout   = timeout;
    }

    /**
     * Command initialization logic
     */
    protected void initialize() {
        setTimeout(timeout);
        Debug.print("[" + this.getName()
                + "] rotation: " + this.rotation
                + ", gyroAngle: " + this.gyroAngle
                + ", timeout: " + this.timeout);
    }

    /**
     * Called repeatedly while the command is running
     */
    protected void execute() {
        drivetrain.turn(rotation,gyroAngle);
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
