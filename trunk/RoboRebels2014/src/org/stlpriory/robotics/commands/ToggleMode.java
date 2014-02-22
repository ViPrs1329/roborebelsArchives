/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands;

/**
 *
 * @author William
 */
public class ToggleMode extends CommandBase {
    boolean commandExecuted;
    public ToggleMode() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        commandExecuted = false;
    
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        mode.toggleState();
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
