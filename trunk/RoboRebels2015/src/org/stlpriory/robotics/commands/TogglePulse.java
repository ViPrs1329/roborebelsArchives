package org.stlpriory.robotics.commands;

import org.stlpriory.robotics.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class TogglePulse extends Command{

	@Override
	protected void initialize() {
		requires(Robot.drivetrain);
		
	}

	@Override
	protected void execute() {
		Robot.drivetrain.isPulsing = !Robot.drivetrain.isPulsing;
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		
	}

}
