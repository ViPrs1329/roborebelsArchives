/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.claw;

import org.stlpriory.robotics.commands.CommandBase;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 * @author admin
 */
public class InvertWheelPiston extends CommandBase {
    boolean commandExecuted;
    public InvertWheelPiston() {
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
        if (claw.getClawWheelValveState()) {
            claw.retractWheelPiston();
            commandExecuted = true;
        }
        else if(!claw.getClawTiltValveState()) {
            claw.extendWheelPiston();
            commandExecuted = true;
        }
        else {
             Debug.println("Error, failed to detect claw wheel valve state");
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