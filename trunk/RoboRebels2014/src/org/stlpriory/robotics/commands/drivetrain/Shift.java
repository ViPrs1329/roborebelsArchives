/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.drivetrain;

import org.stlpriory.robotics.commands.CommandBase;

/**
 *
 * @author William
 */
public class Shift extends CommandBase {
    
    public Shift() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(drivetrain);
    }

    // Called just before this Command runs the first time
    boolean a;
    protected void initialize() {
        a = false;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        drivetrain.shiftGears();
        a = true;
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return a;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
