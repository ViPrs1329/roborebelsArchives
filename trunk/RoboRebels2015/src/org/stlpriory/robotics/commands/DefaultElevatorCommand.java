package org.stlpriory.robotics.commands;

import org.stlpriory.robotics.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DefaultElevatorCommand extends Command {

	public DefaultElevatorCommand() {
		requires(Robot.elevator);
	}

	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		if (Robot.elevator.atBottom() && Robot.elevator.getSpeed() > 0) {
			Robot.elevator.stop();
		} else if (Robot.elevator.atTop() && Robot.elevator.getSpeed() < 0) {
			Robot.elevator.stop();
		}
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
	}

}
