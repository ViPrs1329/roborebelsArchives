package org.stlpriory.robotics.commands.drivetrain;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.stlpriory.robotics.utils.Utils;
import org.stlpriory.robotics.Robot;

//We may need to make a class in Utils to convert inAngle to whatever units the robot uses for rotation
public class DriveForward extends Command {

	double goalAngle = 0.0;
	double startTime;
	double angle
	double totalAngle = 0.0;
	double timeCurrent;
	Timer timer = new Timer();
	
	public DriveForward(double inAngle) {
        super("DriveWithGamepad");
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        // Variable "inAngle" needs to be in degrees
        //If isFoward is true, it will drive forwards, otherwise it will drive in reverse.
    	requires(Robot.drivetrain);
    	goalAngle = inAngle;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	timer.start();
    	startTime =  timer.get();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Robot.drivetrain.mecanum_drive(0,0,-.3);//this number may need to be fixed
        SmartDashboard.putNumber("Robot Speed", Robot.drivetrain.getRobotSpeed());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	timeCurrent = timer.get(); //fix this
    	angle = (Robot.drivetrain.getRobotSpeed() * (timeCurrent - startTime));
    	totalAngle = totalAngle + angle;
    	if (totalAngle >= goalAngle) {
    		return true;
    	}
    	startTime = timer.get();
    	SmartDashboard.putNumber("Angle", totalAngle);
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
