package org.stlpriory.robotics.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.stlpriory.robotics.Robot;

/**
 *
 */
public class ElevatorDown extends Command {
	
	public ElevatorDown() {
        // Use requires() here to declare subsystem dependencies
        requires(Elevator);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute(double distance, double speed) {
    	if (speed >0.0) {
    		Elevator.goUP(distance, speed*(-1))
    	}
    	else {
    		Elevator.goUP(distance, speed)
    	}
    	commandExecuted = true
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