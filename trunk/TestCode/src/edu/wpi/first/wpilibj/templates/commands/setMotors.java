/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author William
 */
public class setMotors extends CommandBase {

    public setMotors() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(motors);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (motors.getState() == 0) {
            motors.setMotorsPos();
        } else if (motors.getState() == 1) {
            motors.setMotorsNeg();
        } else if (motors.getState() == 2) {
            motors.setMotorsStop();
        }
        motors.incrementState();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
