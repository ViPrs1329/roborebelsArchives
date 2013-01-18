/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.drivetrain;

import org.stlpriory.robotics.commands.CommandBase;
import org.stlpriory.robotics.misc.Constants;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 */
public class DriveWithJoystick extends CommandBase implements Constants {
    public DriveWithJoystick() {
        super("DriveWithJoystick");
        requires(drivetrain); // reserve the drivetrain subsystem
    }

    protected void initialize() {
        Debug.println("[" + this.getName() + "] initialized");
    }

    protected void execute() { // called repeatedly while the command is running
        drivetrain.driveWithJoystick(oi.getJoystick());
    }

    protected boolean isFinished() { // called repeatedly and determines if the
        return false; // command is finished executing
    }

    // Called once after isFinished returns true
    protected void end() {
        drivetrain.stop();
    }

    protected void interrupted() { // called if the command is preempted or canceled
    }

}
