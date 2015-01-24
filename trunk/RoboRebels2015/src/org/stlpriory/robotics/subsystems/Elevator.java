package org.stlpriory.robotics.subsystems;

import org.stlpriory.robotics.utils.Debug;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Elevator extends Subsystem{
	CANTalon elevatorMotor;
	Encoder elevatorEncoder;
	
	public Elevator() {
		super("Elevator");
		Debug.println("[Elevator Subsystem] Instantiating...");
		Debug.println("[Elevator Subsystem] CANTalon control mode is " + (Constants.TALON_CONTROL_MODE));
		
		try {
			Debug.println("[Elevator Subsystem] Initializing elevator CAN to CAN bus address" 
					+ RobotMap.ELEVATOR_CAN_TALON_CHANNEL);
			this.elevatorMotor = new CANTalon(RobotMap.ELEAVATOR_CAN_TALON_CHANNEL);
			initTalon(this.elevatorMotor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Debug.println("[Elevator Subsystem] Initializing elevator Encoder with channels A and B:" 
					+ RobotMap.ELEVATOR_ENCODER_CHANNEL_A + ", " + RobotMap.ELEVATOR_ENCODER_CHANNEL_B);
			this.elevatorEncoder = new Encoder(RobotMap.ELEAVATOR_ENCODER_CHANNEL_A, RobotMap.ELEAVATOR_ENCODER_CHANNEL_B);
			// initialization?
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}


	//Move elevator certain up distance at certain speed.
	public boolean goUp(double distance, double speed) {
		boolean success = false;
		double time = distance/speed; //Time motor should be turned on
		Debug.println("[Elevator Subsystem] Trying to go up " + distance + " at " + speed);
		//TO DO
		//Check if elevator is at top, use encoder to find location.
		//Check if there is enough space above to go up for given time at given speed.
		//Turn motor on at given speed.
		//Begin timer.
		//Wait(time)
		//Stop motor when timer is done.
		//Check height to confirm correct distance has been traveled.
		//Update height of elevator with encoder.
		
		
		
		
		
		
		
		
		return success;
	}
	
	
}
