package org.stlpriory.robotics.commands.autonomous;

import org.stlpriory.robotics.commands.HoldElevatorUp;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousPulse extends CommandGroup {

	public AutonomousPulse() {
		for (int i = 0; i < 1e6; i++)
			addSequential(new HoldElevatorUp());
	}
}
