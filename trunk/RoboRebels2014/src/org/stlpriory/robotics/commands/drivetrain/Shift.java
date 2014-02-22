/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.drivetrain;

import org.stlpriory.robotics.commands.CommandBase;
import org.stlpriory.robotics.misc.Constants;

/**
 *
 * @author William
 */
public class Shift extends CommandBase {
    
    public Shift() {
        requires(drivetrain);
    }

    // Called just before this Command runs the first time
    boolean executedCommand;
    protected void initialize() {
        executedCommand = false;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        
        if (mode.getState() == Constants.ROBOT_MANUAL_MODE) {
        drivetrain.shiftGears();
        executedCommand = true;
        }
        else if (mode.getState() == Constants.ROBOT_AUTOMATIC_MODE) {
            executedCommand = true;
        }
        else {
            executedCommand = true;
        }
        
        
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return executedCommand;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
