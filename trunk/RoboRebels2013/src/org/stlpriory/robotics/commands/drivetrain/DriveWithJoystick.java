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
public class DriveWithJoystick extends CommandBase {
    
    public DriveWithJoystick() {
        super("DriveWithJoystick");
        requires(drivetrain); // reserve the drivetrain subsystem
        Debug.println("[DriveWithJoystick] instantiation done.");
    }

    /**
     * Command initialization logic
     */
    protected void initialize() {
        Debug.println("[DriveWithJoystick] initialized");
    }

    /**
     * Called repeatedly while the command is running
     */
    protected void execute() {
        drivetrain.driveWithJoystick(oi.getJoystick());
    }

    /**
     * Called repeatedly to determine if the command is
     * finished executing
     * @return true if the command execution is finished
     */
    protected boolean isFinished() {
        return false;
    }

    /**
     * Called one after isFinished() returns true
     */
    protected void end() {
        drivetrain.stop();
    }

    /**
     * Called if the command is preempted or
     * canceled
     */
    protected void interrupted() {
    }

}