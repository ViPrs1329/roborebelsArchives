/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package roborebels.example.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import roborebels.example.commands.CommandBase;
import roborebels.example.commands.drivetrain.DriveWithJoystick;
import roborebels.example.misc.Constants;
import roborebels.example.misc.Debug;
import roborebels.example.robot.RobotMap;

/**
 *
 */
public class DriveTrain extends Subsystem implements Constants {

    private RobotDrive drive;
    private Jaguar leftJag, rightJag;
    private AnalogChannel sonar;
    private static double direction = 1;
    private boolean isBalancedOnBridge = false;

    public DriveTrain() {
        super("DriveTrain");
        Debug.println("[DriveTrain] Initializing left jaguar on channel " + RobotMap.kDriveLeftMotor);
        leftJag = new Jaguar(RobotMap.kDriveLeftMotor);
        Debug.println("[DriveTrain] Initializing right jaguar on channel " + RobotMap.kDriveRightMotor);
        rightJag = new Jaguar(RobotMap.kDriveRightMotor);

        drive = new RobotDrive(leftJag, rightJag);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        drive.setSafetyEnabled(false);

        sonar = new AnalogChannel(RobotMap.kSonarChannel);
        Debug.println("[DriveTrain] Initializing sonar on channel " + RobotMap.kSonarChannel);

    }

    public void initDefaultCommand() {
        setDefaultCommand(new DriveWithJoystick());
    }

    public double getSonarDistance() {
// MaxSonar EZ1 input units are in (Vcc/512) / inch; multiply by (512/Vcc) to get inches.
        return sonar.getVoltage() * 512 / 5;	// Double check .1v == 1 feet || .01v == 1 inch
    }

    public AnalogChannel getSonar() {
        return sonar;
    }

    public void setForwards() {
        direction = 1;
    }

    public void setBackwards() {
        direction = -1;
    }

    public void stop() {
        drive.tankDrive(0.0, 0.0);
    }

    public boolean canDrive() {
        return !(CommandBase.oi.getDS().getDS().getDigitalIn(kDSDigitalInputDisableDrive));
    }

    public void tankDrive(double leftValue, double rightValue) {
        leftValue *= direction;
        rightValue *= direction;
        if (canDrive()) {
            drive.tankDrive(leftValue, rightValue);
        }
    }

    public void arcadeDrive(double moveValue, double rotateValue) {
        moveValue *= direction;
        rotateValue *= direction;
        if (canDrive()) {
            drive.arcadeDrive(moveValue, rotateValue);
        }
    }

    public void straight(double speed) {
        speed *= direction;
        if (canDrive()) {
            drive.tankDrive(speed, speed * 0.75);
        }
    }
}
