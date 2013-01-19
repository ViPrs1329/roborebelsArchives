/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.stlpriory.robotics.RobotMap;
import org.stlpriory.robotics.commands.drivetrain.DriveWithJoystick;
import org.stlpriory.robotics.misc.Constants;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 */
public class DriveTrain extends Subsystem implements Constants {

    private RobotDrive drive;
    private Jaguar leftJag, rightJag;
    private static double direction = 1;

    public DriveTrain() {
        super("DriveTrain");
        Debug.println("[DriveTrain] Initializing left jaguar on channel " + RobotMap.DRIVE_LEFT_MOTOR);
        leftJag = new Jaguar(RobotMap.DRIVE_LEFT_MOTOR);
        Debug.println("[DriveTrain] Initializing right jaguar on channel " + RobotMap.DRIVE_RIGHT_MOTOR);
        rightJag = new Jaguar(RobotMap.DRIVE_RIGHT_MOTOR);

        drive = new RobotDrive(leftJag, rightJag);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        drive.setSafetyEnabled(false);
    }

    /**
     * Initialize and set default command
     */
    public void initDefaultCommand() {
        setDefaultCommand(new DriveWithJoystick());
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
        return true;
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

    public void turnLeft() { // sets the motor speeds to start a left turn
        arcadeDrive(0.0, 1.0);
    }

    public void driveWithJoystick(Joystick stick) {
        drive.arcadeDrive(stick);
    }
}
