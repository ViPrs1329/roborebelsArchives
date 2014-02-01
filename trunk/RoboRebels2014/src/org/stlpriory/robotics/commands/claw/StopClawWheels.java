/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.claw;

import org.stlpriory.robotics.commands.CommandBase;

/**
 *
 * @author William
 */
public class StopClawWheels extends CommandBase {
    
    boolean commandExecuted;
    
    public StopClawWheels() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(claw);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        commandExecuted = false;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        claw.stopClawMotors();
        commandExecuted = true;
        
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return commandExecuted;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
