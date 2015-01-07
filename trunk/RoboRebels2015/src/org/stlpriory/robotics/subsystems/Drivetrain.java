package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.stlpriory.robotics.RobotMap;
import org.stlpriory.robotics.commands.drivetrain.Drive_with_gamepad;

/**
 *
 */
public class Drivetrain extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	Jaguar left_front, right_front, left_rear, right_rear;
	RobotDrive drive;
	
	public Drivetrain() {
		left_front = new Jaguar(RobotMap.LEFT_FRONT_WHEEL_JAGUAR_PWM_CHANNEL);
		right_front = new Jaguar(RobotMap.RIGHT_FRONT_WHEEL_JAGUAR_PWM_CHANNEL);
		left_rear = new Jaguar(RobotMap.LEFT_REAR_WHEEL_JAGUAR_PWM_CHANNEL);
		right_rear = new Jaguar(RobotMap.RIGHT_REAR_WHEEL_JAGUAR_PWM_CHANNEL);
		
		drive = new RobotDrive(left_front,left_rear,right_front,right_rear);
	}
	
	public void mecanum_drive(double forward, double right, double rotation) {
		drive.mecanumDrive_Cartesian(right, forward, rotation, 0);
	}
	
	public void arcade_drive(double forward, double rotation) {
		
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new Drive_with_gamepad());   
    	}
}

