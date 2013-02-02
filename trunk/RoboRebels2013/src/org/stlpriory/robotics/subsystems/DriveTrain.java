/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.tables.ITable;
import org.stlpriory.robotics.RobotMap;
import org.stlpriory.robotics.commands.drivetrain.DriveWithGamepad;
import org.stlpriory.robotics.misc.Debug;
import org.stlpriory.robotics.misc.Utils;

/**
 *
 */
public class DriveTrain extends Subsystem {

    private RobotDrive drive;
    private static Jaguar leftFrontJag;
    private static Jaguar rightFrontJag;
    private static Jaguar leftRearJag;
    private static Jaguar rightRearJag;
    private static double direction = 1;

    public DriveTrain() {
        super("DriveTrain");
        Debug.println("[DriveTrain] Instantiating...");

        Debug.println("[DriveTrain] Initializing left front jaguar to PWM channel " + RobotMap.LEFT_FRONT_DRIVE_MOTOR_PWM_CHANNEL);
        leftFrontJag = new Jaguar(RobotMap.LEFT_FRONT_DRIVE_MOTOR_PWM_CHANNEL);

        Debug.println("[DriveTrain] Initializing left rear jaguar to PWM channel " + RobotMap.LEFT_REAR_DRIVE_MOTOR_PWM_CHANNEL);
        leftRearJag = new Jaguar(RobotMap.LEFT_REAR_DRIVE_MOTOR_PWM_CHANNEL);

        Debug.println("[DriveTrain] Initializing right front jaguar to PWM channel " + RobotMap.RIGHT_FRONT_DRIVE_MOTOR_PWM_CHANNEL);
        rightFrontJag = new Jaguar(RobotMap.RIGHT_FRONT_DRIVE_MOTOR_PWM_CHANNEL);

        Debug.println("[DriveTrain] Initializing right rear jaguar to PWM channel " + RobotMap.RIGHT_REAR_DRIVE_MOTOR_PWM_CHANNEL);
        rightRearJag = new Jaguar(RobotMap.RIGHT_REAR_DRIVE_MOTOR_PWM_CHANNEL);

        Debug.println("[DriveTrain] Initializing RobotDrive");
        drive = new RobotDrive(leftFrontJag, leftRearJag, rightFrontJag, rightRearJag);
        drive.setSafetyEnabled(false);
        drive.setExpiration(0.1);
        drive.setSensitivity(0.5);
        drive.setMaxOutput(1.0);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        //drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        //drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);

        Debug.println("[DriveTrain] Instantiation complete.");
    }

    /**
     * Initialize and set default command
     */
    public void initDefaultCommand() {
        Debug.println("[DriveTrain.initDefaultCommand()] Setting default command to " + DriveWithGamepad.class.getName());
        setDefaultCommand(new DriveWithGamepad());
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

    /**
     * Drive method for Mecanum wheeled robots.
     */
    public void mecanumDrive(Joystick joystick) {
        /*
         * Three-axis joystick mecanum control.
         * Let x represent strafe left/right
         * Let y represent rev/fwd
         * Let z represent spin CCW/CW axes
         * where each varies from -1 to +1.
         * So:
         * y = -1 corresponds to full speed reverse,
         * y= +1 corresponds to full speed forward,
         * x= -1 corresponds to full speed strafe left,
         * x= +1 corresponds to full speed strafe right,
         * z= -1 corresponds to full speed spin CCW,
         * z= +1 corresponds to full speed spin CW
         *
         * Axis indexes:
         * 1 - LeftX
         * 2 - LeftY
         * 3 - Triggers (Each trigger = 0 to 1, axis value = right - left)
         * 4 - RightX
         * 5 - RightY
         * 6 - DPad Left/Right
         */

        double rawLeftX = joystick.getRawAxis(1);
        double rawLeftY = joystick.getRawAxis(2);
        double rawZ = joystick.getRawAxis(3);

        double scaledLeftX = Utils.scale(rawLeftX);
        double scaledLeftY = Utils.scale(rawLeftY);

        double right     = scaledLeftX;
        double forward   =  -scaledLeftY;
        double rotation  = rawZ;
        double clockwise =  -rawZ;

         drive.mecanumDrive_Cartesian(right, forward, rotation, clockwise);
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

    public void driveWithJoystick(Joystick joystick) {
        drive.arcadeDrive(joystick);
    }

    public void driveWithGamepad(Joystick joystick) {
        mecanumDrive(joystick);
    }
}
