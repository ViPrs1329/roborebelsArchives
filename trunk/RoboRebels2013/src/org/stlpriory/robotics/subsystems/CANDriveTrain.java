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
import org.stlpriory.robotics.misc.Constants;
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

    // History values for mecanum drive
    private double[] currentValues = new double[4];

    public CANDriveTrain() {
        super("CANDriveTrain");
        Debug.println("[CANDriveTrain] Instantiating...");

        try {
            Debug.println("[CANDriveTrain] Initializing left front CANJaguar to CAN bus address "
                    + RobotMap.LEFT_FRONT_DRIVE_MOTOR_CAN_BUS_ADDRESS);
            leftFrontJag = new CANJaguar(RobotMap.LEFT_FRONT_DRIVE_MOTOR_CAN_BUS_ADDRESS);
            initJaguar(leftFrontJag);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }

        try {
            Debug.println("[CANDriveTrain] Initializing left rear CANJaguar to CAN bus address "
                    + RobotMap.LEFT_REAR_DRIVE_MOTOR_CAN_BUS_ADDRESS);
            leftRearJag = new CANJaguar(RobotMap.LEFT_REAR_DRIVE_MOTOR_CAN_BUS_ADDRESS);
            initJaguar(leftRearJag);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        try {
            Debug.println("[CANDriveTrain] Initializing right front CANJaguar to CAN bus address "
                    + RobotMap.RIGHT_FRONT_DRIVE_MOTOR_CAN_BUS_ADDRESS);
            rightFrontJag = new CANJaguar(RobotMap.RIGHT_FRONT_DRIVE_MOTOR_CAN_BUS_ADDRESS);
            initJaguar(rightFrontJag);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }

        try {
            Debug.println("[CANDriveTrain] Initializing right rear CANJaguar to CAN bus address "
                    + RobotMap.RIGHT_REAR_DRIVE_MOTOR_CAN_BUS_ADDRESS);
            rightRearJag = new CANJaguar(RobotMap.RIGHT_REAR_DRIVE_MOTOR_CAN_BUS_ADDRESS);
            initJaguar(rightRearJag);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }

        Debug.println("[CANDriveTrain] Initializing RobotDrive");
        drive = new RobotDrive(leftFrontJag, leftRearJag, rightFrontJag, rightRearJag);
        drive.setSafetyEnabled(false);
        drive.setExpiration(0.1);
        drive.setSensitivity(0.5);
        drive.setMaxOutput(Constants.DRIVE_MAX_OUTPUT);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);

        Debug.println("[CANDriveTrain] Instantiation complete.");
    }

    private void initJaguar(CANJaguar jaguar) throws CANTimeoutException {
        jaguar.configNeutralMode(Constants.JAGUAR_NEUTRAL_MODE);
        jaguar.changeControlMode(Constants.JAGUAR_CONTROL_MODE);
        jaguar.setSpeedReference(Constants.JAGUAR_SPEED_REFERENCE);
        jaguar.configEncoderCodesPerRev(Constants.ENCODER_CODES_PER_REV);
        jaguar.configMaxOutputVoltage(Constants.JAGUAR_MAX_OUTPUT_VOLTAGE);
        jaguar.setVoltageRampRate(Constants.JAGUAR_VOLTAGE_RAMP_RATE);
        jaguar.configPotentiometerTurns(Constants.ENCODER_POTENTIOMETER_TURNS);
        jaguar.setPID(Constants.KP, Constants.KI, Constants.KD);
        jaguar.enableControl();

        printJaguarProperties(jaguar);
    }

    private void checkJaguarForReset(CANJaguar jaguar) {
        try {
            if (jaguar.getPowerCycled()) {
                Debug.println("[CANDriveTrain] Re-initializing CANJaguar "+jaguar.getDescription());
                initJaguar(jaguar);
            }
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
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

        double right     = scaleInputValue(-scaledLeftX, 0);
        double forward   = scaleInputValue( scaledLeftY, 1);
        double rotation  = scaleInputValue(-rawZ, 2);
        double clockwise = scaleInputValue( rawZ, 3);

        checkJaguarForReset(leftRearJag);
        checkJaguarForReset(leftRearJag);
        checkJaguarForReset(rightFrontJag);
        checkJaguarForReset(rightRearJag);

        drive.mecanumDrive_Cartesian(-right, forward, rotation, clockwise);

        //printJaguarOutputCurrent();
        //printJaguarOutputVoltage();
        //printJaguarSpeed();
    }

    private double scaleInputValue(double targetValue, int index) {
        double currentValue = this.currentValues[index];
        double change = targetValue - currentValue;
        double limit;
        if ((currentValue < 0 && change < 0) ||
            (currentValue > 0 && change > 0)) {
            // The signs are the same, so the robot is accelerating
            limit = 0.02;
        } else {
            limit = 0.1;
        }
        change = Math.min(change,  limit);
        change = Math.max(change, -limit);
        this.currentValues[index] += change;
        System.out.println("currentValue="+currentValue+", targetValue="+targetValue+", scaledValue="+this.currentValues[index]);
        return this.currentValues[index];
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

    private void printJaguarProperties(CANJaguar jaguar) throws CANTimeoutException {
        Debug.println("[CANDriveTrain] CANJaguar configuration properties: ");
        Debug.println("                Bus address         = "+jaguar.getDescription());
        Debug.println("                ControlMode         = "+toString(Constants.JAGUAR_CONTROL_MODE));
        Debug.println("                SpeedReference      = "+toString(Constants.JAGUAR_SPEED_REFERENCE));
        Debug.println("                NeutralMode         = "+toString(Constants.JAGUAR_NEUTRAL_MODE));
        Debug.println("                PID values          = "+jaguar.getP() + ", " + jaguar.getI() + ", " + jaguar.getD());
        Debug.println("                codesPerRev         = "+Constants.ENCODER_CODES_PER_REV);
        Debug.println("                maxOutputVoltage    = "+Constants.JAGUAR_MAX_OUTPUT_VOLTAGE);
        Debug.println("                potentiometer turns = "+Constants.ENCODER_POTENTIOMETER_TURNS);
        Debug.println("                voltage ramp        = "+Constants.JAGUAR_VOLTAGE_RAMP_RATE);
        Debug.println("                drive max output    = "+Constants.DRIVE_MAX_OUTPUT);
        Debug.println("                firmware version    = "+jaguar.getFirmwareVersion());
        Debug.println("                hardware version    = "+jaguar.getHardwareVersion());
    }

    private String toString(CANJaguar.ControlMode controlMode) {
        switch (controlMode.value) {
            case 0: return "kPercentVbus";
            case 1: return "kCurrent";
            case 2: return "kSpeed";
            case 3: return "kPosition";
            case 4: return "kVoltage";
            default: return "n/a";
        }
    }

    private String toString(CANJaguar.NeutralMode neutralMode) {
        switch (neutralMode.value) {
            case 0: return "kJumper";
            case 1: return "kBrake";
            case 2: return "kCoast";
            default: return "n/a";
        }
    }

    private String toString(CANJaguar.SpeedReference speedReference) {
        switch (speedReference.value) {
            case 0: return "kEncoder";
            case 2: return "kInvEncoder";
            case 3: return "kQuadEncoder";
            default: return "n/a";
        }
    }

    private void printJaguarOutputCurrent() {
        try {
            System.out.println("Output current LF " + leftFrontJag.getOutputCurrent()
                    + ", LR " + leftRearJag.getOutputCurrent()
                    + ", RF " + rightFrontJag.getOutputCurrent()
                    + ", RR " + rightRearJag.getOutputCurrent());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    private void printJaguarOutputVoltage() {
        try {
            System.out.println("Output voltage LF " + leftFrontJag.getOutputVoltage()
                    + ", LR " + leftRearJag.getOutputVoltage()
                    + ", RF " + rightFrontJag.getOutputVoltage()
                    + ", RR " + rightRearJag.getOutputVoltage());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    private void printJaguarSpeed() {
        try {
            System.out.println("Speed LF " + leftFrontJag.getSpeed()
                    + ", LR " + leftRearJag.getSpeed()
                    + ", RF " + rightFrontJag.getSpeed()
                    + ", RR " + rightRearJag.getSpeed());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

}
