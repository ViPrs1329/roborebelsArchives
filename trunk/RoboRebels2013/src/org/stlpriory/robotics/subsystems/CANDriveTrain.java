/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.stlpriory.robotics.RobotMap;
import org.stlpriory.robotics.commands.drivetrain.DriveWithGamepad;
import org.stlpriory.robotics.misc.Debug;
import org.stlpriory.robotics.misc.Utils;

/**
 *
 */
public class CANDriveTrain extends Subsystem {

    private RobotDrive drive;
    private static CANJaguar leftFrontJag;
    private static CANJaguar rightFrontJag;
    private static CANJaguar leftRearJag;
    private static CANJaguar rightRearJag;
    private static double direction = 1;

    public CANDriveTrain() {
        super("CANDriveTrain");
        Debug.println("[CANDriveTrain] Instantiating...");

        // CAN Jaguar configuration properties
        CANJaguar.ControlMode controlMode = CANJaguar.ControlMode.kSpeed;
        CANJaguar.NeutralMode neutralMode = CANJaguar.NeutralMode.kBrake;
        CANJaguar.SpeedReference speedReference = CANJaguar.SpeedReference.kQuadEncoder;

        // The proportional gain of the Jaguar's PID controller.
        double pValue = 30;
        // The integral gain of the Jaguar's PID controller.
        double iValue = 0.005;
        // The differential gain of the Jaguar's PID controller.
        double dValue = 0.25;

        try {
            Debug.println("[CANDriveTrain] Initializing left front CANJaguar to CAN bus address "
                    + RobotMap.LEFT_FRONT_DRIVE_MOTOR_CAN_BUS_ADDRESS);
            leftFrontJag = new CANJaguar(RobotMap.LEFT_FRONT_DRIVE_MOTOR_CAN_BUS_ADDRESS,controlMode);
            leftFrontJag.setPID(pValue, iValue, dValue);
            leftFrontJag.setSpeedReference(speedReference);
            leftFrontJag.configNeutralMode(neutralMode);
            leftFrontJag.enableControl();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }

        try {
            Debug.println("[CANDriveTrain] Initializing left rear CANJaguar to CAN bus address "
                    + RobotMap.LEFT_REAR_DRIVE_MOTOR_CAN_BUS_ADDRESS);
            leftRearJag = new CANJaguar(RobotMap.LEFT_REAR_DRIVE_MOTOR_CAN_BUS_ADDRESS,controlMode);
            leftRearJag.setPID(pValue, iValue, dValue);
            leftRearJag.setSpeedReference(speedReference);
            leftRearJag.configNeutralMode(neutralMode);
            leftRearJag.enableControl();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }

        try {
            Debug.println("[CANDriveTrain] Initializing right front CANJaguar to CAN bus address "
                    + RobotMap.RIGHT_FRONT_DRIVE_MOTOR_CAN_BUS_ADDRESS);
            rightFrontJag = new CANJaguar(RobotMap.RIGHT_FRONT_DRIVE_MOTOR_CAN_BUS_ADDRESS,controlMode);
            rightFrontJag.setPID(pValue, iValue, dValue);
            rightFrontJag.setSpeedReference(speedReference);
            rightFrontJag.configNeutralMode(neutralMode);
            rightFrontJag.enableControl();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }

        try {
            Debug.println("[CANDriveTrain] Initializing right rear CANJaguar to CAN bus address "
                    + RobotMap.RIGHT_REAR_DRIVE_MOTOR_CAN_BUS_ADDRESS);
            rightRearJag = new CANJaguar(RobotMap.RIGHT_REAR_DRIVE_MOTOR_CAN_BUS_ADDRESS,controlMode);
            rightRearJag.setPID(pValue, iValue, dValue);
            rightRearJag.setSpeedReference(speedReference);
            rightRearJag.configNeutralMode(neutralMode);
            rightRearJag.enableControl();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }

        // References for CAN programming:
        // http://e2e.ti.com/support/microcontrollers/stellaris_arm/f/471/t/93723.aspx
        // http://e2e.ti.com/support/microcontrollers/stellaris_arm/f/471/t/159475.aspx
        // http://e2e.ti.com/support/microcontrollers/stellaris_arm/f/471/p/91181/770069.aspx#770069
        // http://content.vexrobotics.com/docs/217-3367-VEXpro_Jaguar_GettingStartedGuide_20130125.pdf
        // http://content.vexrobotics.com/docs/217-3367-VEXpro_Jaguar_FAQ.pdf
        //

        Debug.println("[CANDriveTrain] Initializing RobotDrive");
        drive = new RobotDrive(leftFrontJag, leftRearJag, rightFrontJag, rightRearJag);
        drive.setSafetyEnabled(false);
        drive.setExpiration(0.1);
        drive.setSensitivity(0.5);
        drive.setMaxOutput(1.0);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);

        Debug.println("[CANDriveTrain] Instantiation complete.");
    }

    /**
     * Initialize and set default command
     */
    public void initDefaultCommand() {
        Debug.println("[CANDriveTrain.initDefaultCommand()] Setting default command to " + DriveWithGamepad.class.getName());
        setDefaultCommand(new DriveWithGamepad());
    }

    public void setForwards() {
        direction = 1;
    }

    public void setBackwards() {
        direction = -1;
    }

    public void stop() {
        Debug.println("[CANDriveTrain.stop]");
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

        double right     = -scaledLeftX;
        double forward   =  scaledLeftY;
        double rotation  = -rawZ;
        double clockwise =  rawZ;

        drive.mecanumDrive_Cartesian(-right, forward, rotation, clockwise);

        //printJaguarOutputCurrent();
        printJaguarSpeed();
    }

    private void printJaguarOutputCurrent() {
        try {
            System.out.println("LF " + leftFrontJag.getOutputCurrent()
                    + ", LR " + leftRearJag.getOutputCurrent()
                    + ", RF " + rightFrontJag.getOutputCurrent()
                    + ", RR " + rightRearJag.getOutputCurrent());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    private void printJaguarOutputVoltage() {
        try {
            System.out.println("LF " + leftFrontJag.getOutputVoltage()
                    + ", LR " + leftRearJag.getOutputVoltage()
                    + ", RF " + rightFrontJag.getOutputVoltage()
                    + ", RR " + rightRearJag.getOutputVoltage());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    private void printJaguarSpeed() {
        try {
            System.out.println("LF " + leftFrontJag.getSpeed()
                    + ", LR " + leftRearJag.getSpeed()
                    + ", RF " + rightFrontJag.getSpeed()
                    + ", RR " + rightRearJag.getSpeed());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public void straight(double speed) {
        speed *= direction;
        if (canDrive()) {
            drive.mecanumDrive_Cartesian(0, speed, 0, 0);
        }
    }

    public void turnLeft() { // sets the motor speeds to start a left turn
        arcadeDrive(0.0, .3);
    }

    public void driveWithJoystick(Joystick joystick) {
        drive.arcadeDrive(joystick);
    }

    public void driveWithGamepad(Joystick joystick) {
        mecanumDrive(joystick);
    }

}
