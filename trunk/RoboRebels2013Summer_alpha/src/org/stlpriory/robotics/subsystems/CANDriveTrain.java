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
    private static CANJaguar leftJag1;
    private static CANJaguar leftJag2;
    private static CANJaguar rightJag1;
    private static CANJaguar rightJag2;
    private static double direction = 1;

    // History values for mecanum drive
    private double[] currentValues = new double[4];

    public CANDriveTrain() {
        super("CANDriveTrain");
        Debug.println("[CANDriveTrain] Instantiating...");

        try {
            Debug.println("[CANDriveTrain] Initializing left front CANJaguar to CAN bus address "
                    + RobotMap.LEFT_FRONT_DRIVE_MOTOR_CAN_BUS_ADDRESS);
            leftJag1 = new CANJaguar(RobotMap.LEFT_FRONT_DRIVE_MOTOR_CAN_BUS_ADDRESS);
            initJaguar(leftJag1);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }

        try {
            Debug.println("[CANDriveTrain] Initializing left rear CANJaguar to CAN bus address "
                    + RobotMap.LEFT_REAR_DRIVE_MOTOR_CAN_BUS_ADDRESS);
            leftJag2 = new CANJaguar(RobotMap.LEFT_REAR_DRIVE_MOTOR_CAN_BUS_ADDRESS);
            initJaguar(leftJag2);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        try {
            Debug.println("[CANDriveTrain] Initializing right front CANJaguar to CAN bus address "
                    + RobotMap.RIGHT_FRONT_DRIVE_MOTOR_CAN_BUS_ADDRESS);
            rightJag1 = new CANJaguar(RobotMap.RIGHT_FRONT_DRIVE_MOTOR_CAN_BUS_ADDRESS);
            initJaguar(rightJag1);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }

        try {
            Debug.println("[CANDriveTrain] Initializing right rear CANJaguar to CAN bus address "
                    + RobotMap.RIGHT_REAR_DRIVE_MOTOR_CAN_BUS_ADDRESS);
            rightJag2 = new CANJaguar(RobotMap.RIGHT_REAR_DRIVE_MOTOR_CAN_BUS_ADDRESS);
            initJaguar(rightJag2);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }

        Debug.println("[CANDriveTrain] Initializing RobotDrive");
        Debug.println("[CANDriveTrain] MAX OUTPUT = " + Constants.DRIVE_MAX_OUTPUT);
        drive = new RobotDrive(leftJag1, leftJag2, rightJag1, rightJag2);
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

        //printJaguarProperties(jaguar);
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

    public void turn(double rotation, double gyroAngle) {
        drive.mecanumDrive_Cartesian(0, 0, rotation, gyroAngle);
    }

    public void straight(double speed) {
        speed *= direction;
        if (canDrive()) {
            drive.mecanumDrive_Cartesian(0, speed, 0, 0);
        }
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
        
        double limit = Constants.DRIVE_MAX_SPEED;

        double rawLeftX = joystick.getRawAxis(1);
        double rawLeftY = joystick.getRawAxis(2);
        double rawZ = joystick.getRawAxis(3);

        double scaledLeftX = Utils.scale(rawLeftX);
        double scaledLeftY = Utils.scale(rawLeftY);

        double right     = scaleInputValue(-scaledLeftX, 0);
        double forward   = scaleInputValue( scaledLeftY, 1);
        double rotation  = scaleInputValue(-rawZ, 2);
        double clockwise = scaleInputValue( rawZ, 3);

        checkJaguarForReset(leftJag1);
        checkJaguarForReset(leftJag2);
        checkJaguarForReset(rightJag1);
        checkJaguarForReset(rightJag2);
        
        right = Math.min(right,  limit);
        right = Math.max(right, -limit);
        forward = Math.min(forward,  limit);
        forward = Math.max(forward, -limit);
        rotation = Math.min(rotation,  limit);
        rotation = Math.max(rotation, -limit);


        drive.mecanumDrive_Cartesian(-right, forward, rotation, clockwise);

        //printJaguarOutputCurrent();
        //printJaguarOutputVoltage();
        //printJaguarSpeed();
    }

    private double scaleInputValue(double targetValue, int index) {
        double currentValue = this.currentValues[index];
        double change = targetValue - currentValue;
        double limit;
        double testCValue = Math.abs(currentValue);
        double testTValue = Math.abs(targetValue);
        if (testCValue > testTValue) {
            // The signs are the same, so the robot is accelerating
            limit = Constants.MOTOR_RAMP_DOWN_INCREMENT;
        } else if ((targetValue > 0) && (currentValue < 0)){
            limit = Constants.MOTOR_RAMP_DOWN_INCREMENT;
        } else if ((targetValue < 0) && (currentValue > 0)){
            limit = Constants.MOTOR_RAMP_DOWN_INCREMENT;
        } else {
            limit = Constants.MOTOR_RAMP_INCREMENT;
        }
        change = Math.min(change,  limit);
        change = Math.max(change, -limit);
        this.currentValues[index] += change;
        //System.out.println("currentValue="+currentValue+", targetValue="+targetValue+", scaledValue="+this.currentValues[index]);
        return this.currentValues[index];
    }

    private double scaleInputValue2(double targetValue, int index) {
        return targetValue;
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
            System.out.println("Output current LF " + leftJag1.getOutputCurrent()
                    + ", LR " + leftJag2.getOutputCurrent()
                    + ", RF " + rightJag1.getOutputCurrent()
                    + ", RR " + rightJag2.getOutputCurrent());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    private void printJaguarOutputVoltage() {
        try {
            System.out.println("Output voltage LF " + leftJag1.getOutputVoltage()
                    + ", LR " + leftJag2.getOutputVoltage()
                    + ", RF " + rightJag1.getOutputVoltage()
                    + ", RR " + rightJag2.getOutputVoltage());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    private void printJaguarSpeed() {
        try {
            System.out.println("Speed LF " + leftJag1.getSpeed()
                    + ", LR " + leftJag2.getSpeed()
                    + ", RF " + rightJag1.getSpeed()
                    + ", RR " + rightJag2.getSpeed());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
}
