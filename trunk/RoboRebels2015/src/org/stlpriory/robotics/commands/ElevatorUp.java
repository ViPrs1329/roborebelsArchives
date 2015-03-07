package org.stlpriory.robotics.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.stlpriory.robotics.Robot;
import org.stlpriory.robotics.utils.Constants;

public class ElevatorUp extends Command {

	public ElevatorUp() {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.elevator);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.elevator.holdUp(Constants.ELEVATOR_SPEED);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return true;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
