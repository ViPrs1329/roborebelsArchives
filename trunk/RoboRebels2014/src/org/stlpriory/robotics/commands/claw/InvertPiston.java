/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.claw;

import org.stlpriory.robotics.commands.CommandBase;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 * @author William
 */
public class InvertPiston extends CommandBase {
    
    boolean commandExecuted;
    public InvertPiston() {
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
        if (claw.getValveState()) {
            claw.retractPiston();
            commandExecuted = true;
        }
        else if (! claw.getValveState()) {
            claw.extendPiston();
            commandExecuted = true;

        }
        else {
            Debug.println("Error, failed to detect claw valve state");
        }             
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
