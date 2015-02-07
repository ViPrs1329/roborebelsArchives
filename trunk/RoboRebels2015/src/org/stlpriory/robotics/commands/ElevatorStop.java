package org.stlpriory.robotics.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.stlpriory.robotics.Robot;

public class ElevatorStop extends Command {
	boolean commandExecuted;

	public ElevatorStop() {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.elevator);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		commandExecuted = false;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		commandExecuted = Robot.elevator.stop();
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
