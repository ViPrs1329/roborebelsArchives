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
import org.stlpriory.robotics.misc.Debug;

/**
 *
 */
public class DriveTrain extends Subsystem {

    private RobotDrive drive;
    private Jaguar leftFrontJag;
    private Jaguar rightFrontJag;
    private Jaguar leftRearJag;
    private Jaguar rightRearJag;
    private static double direction = 1;

    public DriveTrain() {
        super("DriveTrain");
        Debug.println("[DriveTrain] Initializing left front jaguar on channel " + RobotMap.DRIVE_FRONT_LEFT_MOTOR);
        leftFrontJag = new Jaguar(RobotMap.DRIVE_FRONT_LEFT_MOTOR);
        Debug.println("[DriveTrain] Initializing right front jaguar on channel " + RobotMap.DRIVE_FRONT_RIGHT_MOTOR);
        rightFrontJag = new Jaguar(RobotMap.DRIVE_FRONT_RIGHT_MOTOR);

        Debug.println("[DriveTrain] Initializing left rear jaguar on channel " + RobotMap.DRIVE_BACK_LEFT_MOTOR);
        leftRearJag = new Jaguar(RobotMap.DRIVE_BACK_LEFT_MOTOR);
        Debug.println("[DriveTrain] Initializing right rear jaguar on channel " + RobotMap.DRIVE_BACK_RIGHT_MOTOR);
        rightRearJag = new Jaguar(RobotMap.DRIVE_BACK_RIGHT_MOTOR);

        drive = new RobotDrive(leftFrontJag, leftRearJag,rightFrontJag, rightRearJag);
        //drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        //drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
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
        Debug.println("[DriveTrain.stop]");
        drive.tankDrive(0.0, 0.0);
    }

    public boolean canDrive() {
        return true;
    }

    public void tankDrive(double leftValue, double rightValue) {
        leftValue *= direction;
        rightValue *= direction;
        if (canDrive()) {
            Debug.println("[DriveTrain.tankDrive] leftValue = "+leftValue+", rightValue = "+rightValue);
            drive.tankDrive(leftValue, rightValue);
        }
    }

    public void arcadeDrive(double moveValue, double rotateValue) {
        moveValue *= direction;
        rotateValue *= direction;
        if (canDrive()) {
            Debug.println("[DriveTrain.arcadeDrive] moveValue = "+moveValue+", rotateValue = "+rotateValue);
            drive.arcadeDrive(moveValue, rotateValue);
        }
    }

   /**
     * Drive method for Mecanum wheeled robots.
     *
     * A method for driving with Mecanum wheeled robots. There are 4 wheels
     * on the robot, arranged so that the front and back wheels are toed in 45 degrees.
     * When looking at the wheels from the top, the roller axles should form an X across the robot.
     *
     * This is designed to be directly driven by joystick axes.
     *
     * @param xSpeed The speed that the robot should drive in the X direction. [-1.0..1.0]
     * @param ySpeed The speed that the robot should drive in the Y direction.
     * This input is inverted to match the forward == -1.0 that joysticks produce. [-1.0..1.0]
     * @param rotation The rate of rotation for the robot that is completely independent of
     * the translation. [-1.0..1.0]
     * @param gyroAngle The current angle reading from the gyro.  Use this to implement field-oriented controls.
     */
    public void mecanumDrive(double xSpeed, double ySpeed, double rotation, double gyroAngle) {
        if (canDrive()) {
            Debug.println("[DriveTrain.mecanumDrive] x-speed = "+xSpeed+", y-speed = "+ySpeed);
            drive.mecanumDrive_Cartesian(xSpeed, ySpeed, rotation, gyroAngle);
        }
    }

    public void straight(double speed) {
        speed *= direction;
        if (canDrive()) {
            Debug.println("[DriveTrain.straight] speed = "+speed);
            drive.tankDrive(speed, speed * 0.75);
        }
    }

    public void turnLeft() { // sets the motor speeds to start a left turn
        Debug.println("[DriveTrain.turnLeft]");
        arcadeDrive(0.0, 1.0);
    }

    public void driveWithJoystick(Joystick stick) {
        drive.mecanumDrive_Cartesian(stick.getX(), stick.getY(), 0, 0);
        //drive.arcadeDrive(stick);
    }
}
