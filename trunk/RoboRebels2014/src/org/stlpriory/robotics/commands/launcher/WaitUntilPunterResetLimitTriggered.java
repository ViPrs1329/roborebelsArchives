/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.launcher;

import org.stlpriory.robotics.commands.CommandBase;

/**
 *
 * @author William
 */
public class WaitUntilPunterResetLimitTriggered extends CommandBase {
    
    public WaitUntilPunterResetLimitTriggered() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(launcher);
    }
    boolean commandExecuted;

    // Called just before this Command runs the first time
    protected void initialize() {
        commandExecuted = false;
        setTimeout(2);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if(launcher.isPunterLimitReached()){
            commandExecuted =  true;
        }
            
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
