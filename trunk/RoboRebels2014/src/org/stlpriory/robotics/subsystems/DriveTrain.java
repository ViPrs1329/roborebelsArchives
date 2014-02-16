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
import org.stlpriory.robotics.commands.CommandBase;
import org.stlpriory.robotics.commands.drivetrain.DriveWithGamepad;
import org.stlpriory.robotics.commands.drivetrain.DriveWithJoystick;
import org.stlpriory.robotics.misc.Constants;
import org.stlpriory.robotics.misc.Debug;
import org.stlpriory.robotics.misc.Utils;

/**
 *
 */
public class DriveTrain extends Subsystem {

    private RobotDrive drive;
    private Jaguar leftFrontJag;
    private Jaguar rightFrontJag;
    private Jaguar leftRearJag;
    private Jaguar rightRearJag;
    private double direction = 1;
    private GearBox gearBoxes;


    public DriveTrain() {
        super("DriveTrain");
        Debug.println("[DriveTrain Subsystem] Instantiating...");

        Debug.println("[DriveTrain Subsystem] Initializing left front Jaguar to PWM channel " + RobotMap.LEFT_FRONT_DRIVE_MOTOR_PWM_CHANNEL);
        leftFrontJag = new Jaguar(RobotMap.LEFT_FRONT_DRIVE_MOTOR_PWM_CHANNEL);

        Debug.println("[DriveTrain Subsystem] Initializing left rear Jaguar to PWM channel " + RobotMap.LEFT_REAR_DRIVE_MOTOR_PWM_CHANNEL);
        leftRearJag = new Jaguar(RobotMap.LEFT_REAR_DRIVE_MOTOR_PWM_CHANNEL);

        Debug.println("[DriveTrain Subsystem] Initializing right front Jaguar to PWM channel " + RobotMap.RIGHT_FRONT_DRIVE_MOTOR_PWM_CHANNEL);
        rightFrontJag = new Jaguar(RobotMap.RIGHT_FRONT_DRIVE_MOTOR_PWM_CHANNEL);

        Debug.println("[DriveTrain Subsystem] Initializing right rear Jaguar to PWM channel " + RobotMap.RIGHT_REAR_DRIVE_MOTOR_PWM_CHANNEL);
        rightRearJag = new Jaguar(RobotMap.RIGHT_REAR_DRIVE_MOTOR_PWM_CHANNEL);

        Debug.println("[DriveTrain Subsystem] Initializing GearBoxes");
        gearBoxes = new GearBox();

        Debug.println("[DriveTrain Subsystem] Initializing RobotDrive");
        drive = new RobotDrive(leftFrontJag, leftRearJag, rightFrontJag, rightRearJag);
        drive.setSafetyEnabled(false);
        drive.setExpiration(0.1);
        drive.setSensitivity(0.5);
        drive.setMaxOutput(Constants.DRIVE_MAX_OUTPUT);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true); 

        Debug.println("[DriveTrain Subsystem] Instantiation complete.");
    }

    public void initDefaultCommand() {
        Debug.println("[DriveTrain.initDefaultCommand()] Setting default command to " + DriveWithGamepad.class.getName());
        setDefaultCommand(new DriveWithJoystick());
    }

    public void stop() {
        drive.stopMotor();
    }


    public void tankDrive(double leftValue, double rightValue) {
        leftValue *= direction;
        rightValue *= direction;
            drive.tankDrive(leftValue, rightValue);
        
    }

    public void arcadeDrive(double moveValue, double rotateValue) {
        moveValue *= direction;
        rotateValue *= direction;
            drive.arcadeDrive(moveValue, rotateValue);
        
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

        double right = -scaledLeftX;
        double forward = scaledLeftY;
        double rotation = -rawZ;
        double clockwise = rawZ;

        drive.mecanumDrive_Cartesian(right, -forward, rotation, clockwise);
    }

    public void driveWithJoystick(Joystick joystick) {
        arcadeDrive(joystick.getAxis(Joystick.AxisType.kY), joystick.getAxis(Joystick.AxisType.kX));

//        double xValue  = joystick.getAxis(Joystick.AxisType.kX);
//        double yValue  = joystick.getAxis(Joystick.AxisType.kY);
//        
//        double rhsSpeed = 0;
//        double lhsSpeed = 0;
//        leftFrontJag.set(-lhsSpeed);
//        leftRearJag.set(lhsSpeed);
//        rightFrontJag.set(rhsSpeed);
//        rightRearJag.set(-rhsSpeed);
//
        // For CAN Jaguar control with 1 encoder for 2 jaguars
        // see http://www.chiefdelphi.com/forums/showthread.php?t=89282
        // leftRearJag.set(leftFrontJag.getOutputVoltage() / leftFrontJag.getBusVoltage()); //For voltage control mode
        // leftRearJag.set(leftFrontJag.getOutputCurrent()); //For current control mode

    }

    public void driveWithGamepad(Joystick joystick) {
        tankDrive(joystick.getAxis(Joystick.AxisType.kY), joystick.getAxis(Joystick.AxisType.kX));
    }

    public void shiftGears() {
        CommandBase.updateDriverStationLCD(1,1,"Executing gear shift");
        gearBoxes.shiftBoxes();
    }

  

}