package org.stlpriory.robotics.commands.autonomous;

import org.stlpriory.robotics.commands.ElevatorStop;
import org.stlpriory.robotics.commands.ElevatorUp;
import org.stlpriory.robotics.commands.drivetrain.DriveForward;
import org.stlpriory.robotics.commands.drivetrain.Rotate;
import org.stlpriory.robotics.commands.drivetrain.ShiftHigh;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

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
//		addSequential(new DriveForward(0.5, true));
//		System.out.println("Drove Forward");
//		addSequential(new ElevatorUp(), 0.25); // this tells the elevator to go up for 1 second.
		//addSequential(new HoldElevatorUp());
//		addSequential(new ElevatorUp());
		addSequential(new ShiftHigh());
		addSequential(new ElevatorUp());
		addSequential(new WaitCommand(1));
		addSequential(new ElevatorStop());
		addParallel(new AutonomousPulse());
//		if (!Robot.drivetrain.isPulsing) {
//			addSequential(new TogglePulse());
//		}
//		addSequential(new DriveForward(2, true));
//		addSequential(new ElevatorDown(), 1); //this lowers the elevator for 1 second
//		addSequential(new Release());
//		addSequential(new DriveForward(2, false));
//		addSequential(new Strafe(1, false));
		addSequential(new Rotate(90, false));
		addSequential(new DriveForward(9,true));
//		addSequential(new Rotate(90, false));
//		addSequential(new DriveForward(2, true));
//		addSequential(new Grab());
//		addSequential(new ElevatorUp(), 1);
		
	}
}
