package org.stlpriory.robotics.commands.drivetrain;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.stlpriory.robotics.Robot;

public class DriveForward extends Command {

	double goalDistance = 0.0;
	double startTime;
	double distance;
	double totalDistance = 0.0;
	double timeCurrent;
	Timer timer = new Timer();
	
	public DriveForward(double din) {
        super("DriveWithGamepad");
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.drivetrain);
    	goalDistance = din;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	timer.start();
    	startTime =  timer.get();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Robot.drivetrain.mecanum_drive(-.3,0,0);
        SmartDashboard.putNumber("Robot Speed", Robot.drivetrain.getRobotSpeed());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	timeCurrent = timer.get(); //fix this
    	distance = (Robot.drivetrain.getRobotSpeed() * (timeCurrent - startTime));
    	totalDistance = totalDistance + distance;
    	if (totalDistance >= goalDistance) {
    		return true;
    	}
    	startTime = timer.get();
    	SmartDashboard.putNumber("Distance", totalDistance);
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
		Robot.drivetrain.mecanum_drive(0.0,0,0);

    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
