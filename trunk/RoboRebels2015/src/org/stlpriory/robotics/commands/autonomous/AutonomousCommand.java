package org.stlpriory.robotics.commands.autonomous;

import org.stlpriory.robotics.commands.*;
import org.stlpriory.robotics.commands.drivetrain.*;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonomousCommand extends CommandGroup {

	public  AutonomousCommand() {
		// Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both
    	//the chassis and the
        // arm.
		//addSequential(new DriveForward(2, true));
		//addSequential(new ElevatorUp(), 0.25); // this tells the elevator to go up for 1 second.
		//addSequential(new HoldElevatorUp(true));
//		addSequential(new DriveForward(2, true));
//		addSequential(new ElevatorDown(), 1); //this lowers the elevator for 1 second
//		addSequential(new Release());
//		addSequential(new DriveForward(2, false));
//		addSequential(new Strafe(1, false));
		addSequential(new Rotate(90,true));
		addSequential(new Rotate(90, false));
//		addSequential(new DriveForward(2, true));
//		addSequential(new Grab());
//		addSequential(new ElevatorUp(), 1);
		
	}
}
