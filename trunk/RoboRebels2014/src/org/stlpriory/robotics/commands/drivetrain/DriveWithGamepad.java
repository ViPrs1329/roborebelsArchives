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
public class DriveWithGamepad extends CommandBase {
    
    public DriveWithGamepad() {
        super("DriveWithGamepad");
        requires(drivetrain); // reserve the drivetrain subsystem
        Debug.println("[DriveWithGamepad] instantiation done.");
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Debug.println("[DriveWithGamepad] initialized");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        drivetrain.driveWithGamepad(oi.getXboxControler());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        drivetrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}