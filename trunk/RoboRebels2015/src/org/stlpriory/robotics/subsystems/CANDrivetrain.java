package org.stlpriory.robotics.subsystems;

import org.stlpriory.robotics.RobotMap;
import org.stlpriory.robotics.commands.drivetrain.Drive_with_gamepad;
import org.stlpriory.robotics.utils.Debug;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.can.CANNotInitializedException;
import edu.wpi.first.wpilibj.command.Subsystem;

public class CANDrivetrain extends Subsystem {
	 // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	CANTalon left_front, right_front, left_rear, right_rear;
	RobotDrive drive;
	
	public CANDrivetrain() {
		super("CANDriveTrain");
		Debug.println("[CANDriveTrain Subsystem] Instantiating...");
		Debug.println("[CANDriveTrain Subsystem[] CANJaguar control mode is " + toString(Constants.JAGUAR_CONTROL_MODE));
	
		try
		{
			Debug.println("[CANDrivetrain Subsystem] Initializing left front CAN to CAN bus address" + RobotMap.LEFT_FRONT_CAN_TALON_CHANNEL);
			left_front = new CANTalon(RobotMap.LEFT_FRONT_CAN_TALON_CHANNEL);
			initTalon(left_front);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		try
		{
			Debug.println("[CANDrivetrain Subsystem] Initializing right front CAN to CAN bus address" + RobotMap.RIGHT_FRONT_CAN_TALON_CHANNEL);
			right_front = new CANTalon(RobotMap.RIGHT_FRONT_CAN_TALON_CHANNEL);
			initTalon(right_front);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		try
		{
			Debug.println("[CANDrivetrain Subsystem] Initializing left rear CAN to CAN bus address" + RobotMap.LEFT_REAR_CAN_TALON_CHANNEL);
			left_rear = new CANTalon(RobotMap.LEFT_REAR_CAN_TALON_CHANNEL);
			initTalon(left_rear);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		try
		{
			Debug.println("[CANDrivetrain Subsystem] Initializing right rear CAN to CAN bus address" + RobotMap.RIGHT_REAR_CAN_TALON_CHANNEL);
			right_rear = new CANTalon(RobotMap.RIGHT_REAR_CAN_TALON_CHANNEL);
			initTalon(right_rear);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		//gearBoxes go here
		
		Debug.println("[CANDriveTrain Subsystem] Initializing RobotDrive");
		Debug.println("[CANDriveTrain Subsystem] MAX OUTPUT = " + Constants.DRIVE_MAX_OUTPUT);
		drive = new RobotDrive(left_front,left_rear,right_front,right_rear);
		drive.setSafetyEnabled(false);
		drive.setExpiration(0.1);
		drive.setSensitivity(0.5);
		drive.setMaxOutput(Constants.DRIVE_MAX_OUTPUT);
		drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
		drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
	}
	private void initTalon(CANTalon talon)
	{
		talon.setPID(Constants.TALON_PROPORTION, Constants.TALON_INTEGRATION, Constants.TALON_DIFFERENTIAL, Constants.TALON_FEEDFORWARD, 0, 0, 0);
	}
	public void mecanum_drive(double forward, double right, double rotation) {
		drive.mecanumDrive_Cartesian(right, forward, rotation, 0);
	}
	public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new Drive_with_gamepad());   
    	}
}
