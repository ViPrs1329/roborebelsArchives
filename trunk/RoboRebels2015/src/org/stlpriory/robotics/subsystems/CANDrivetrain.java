package org.stlpriory.robotics.subsystems;

import org.stlpriory.robotics.RobotMap;
import org.stlpriory.robotics.commands.drivetrain.DriveWithGamepad;
import org.stlpriory.robotics.utils.Constants;
import org.stlpriory.robotics.utils.Debug;
import org.stlpriory.robotics.utils.Utils;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;

public class CANDrivetrain extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    CANTalon left_front, right_front, left_rear, right_rear;
    RobotDrive drive;

    public CANDrivetrain() {
        super("CANDriveTrain");
        Debug.println("[CANDriveTrain Subsystem] Instantiating...");
        Debug.println("[CANDriveTrain Subsystem] CANTalon control mode is " + (Constants.TALON_CONTROL_MODE));

        try {
            Debug.println("[CANDrivetrain Subsystem] Initializing left front CAN to CAN bus address"
                          + RobotMap.LEFT_FRONT_CAN_TALON_CHANNEL);
            this.left_front = new CANTalon(RobotMap.LEFT_FRONT_CAN_TALON_CHANNEL);
            initTalon(this.left_front);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Debug.println("[CANDrivetrain Subsystem] Initializing right front CAN to CAN bus address"
                          + RobotMap.RIGHT_FRONT_CAN_TALON_CHANNEL);
            this.right_front = new CANTalon(RobotMap.RIGHT_FRONT_CAN_TALON_CHANNEL);
            initTalon(this.right_front);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Debug.println("[CANDrivetrain Subsystem] Initializing left rear CAN to CAN bus address" + RobotMap.LEFT_REAR_CAN_TALON_CHANNEL);
            this.left_rear = new CANTalon(RobotMap.LEFT_REAR_CAN_TALON_CHANNEL);
            initTalon(this.left_rear);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Debug.println("[CANDrivetrain Subsystem] Initializing right rear CAN to CAN bus address"
                          + RobotMap.RIGHT_REAR_CAN_TALON_CHANNEL);
            this.right_rear = new CANTalon(RobotMap.RIGHT_REAR_CAN_TALON_CHANNEL);
            initTalon(this.right_rear);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //gearBoxes go here

        Debug.println("[CANDriveTrain Subsystem] Initializing RobotDrive");
        Debug.println("[CANDriveTrain Subsystem] MAX OUTPUT = " + Constants.DRIVE_MAX_OUTPUT);
        this.drive = new RobotDrive(this.left_front, this.left_rear, this.right_front, this.right_rear);
        this.drive.setSafetyEnabled(false);
        this.drive.setExpiration(0.1);
        this.drive.setSensitivity(0.5);
        this.drive.setMaxOutput(Constants.DRIVE_MAX_OUTPUT);
        this.drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        this.drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
    }

    private void initTalon(final CANTalon talon) {
        talon.setPID(Constants.TALON_PROPORTION, Constants.TALON_INTEGRATION, Constants.TALON_DIFFERENTIAL, Constants.TALON_FEEDFORWARD, 0,
                     0, 0);
    }

    public void mecanum_drive( double forward,  double right,  double rotation) {
    	forward = Utils.scale(forward);
    	right = Utils.scale(right);
    	rotation = Utils.scale(rotation);
        this.drive.mecanumDrive_Cartesian(right, forward, rotation, 0);
    }
    
    public void mecanum_drive(Joystick joystick){
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

            double right = scaleInputValue(-rawLeftX, 0);
            double forward = scaleInputValue(rawLeftY, 1);
            double rotation = scaleInputValue(-rawZ, 2);
            double clockwise = scaleInputValue(rawZ, 3);

            checkTalonForReset(left_front);
            checkTalonForReset(left_rear);
            checkTalonForReset(right_front);
            checkTalonForReset(right_rear);

            drive.mecanumDrive_Cartesian(-right, forward, rotation, clockwise);

            //printTalonOutputCurrent();
            //printTalonOutputVoltage();
            //printTalonSpeed();
    }
    private double scaleInputValue(double rawZ, int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void driveWithJoystick(Joystick joystick) {
        checkTalonForReset(leftRearTalon);
        checkTalonForReset(leftRearTalon);
        checkTalonForReset(rightFrontTalon);
        checkTalonForReset(rightRearTalon);
        
        int mode = Constants.TALON_CONTROL_MODE;
        try {
            switch (mode) {
                case 1:  // kCurrent
                    leftRearTalon.setX(leftFrontTalon.getOutputCurrent());
                    rightRearTalon.setX(rightFrontTalon.getOutputCurrent());
System.out.println("Output left talon current = "+leftFrontTalon.getOutputCurrent());
System.out.println("Output right talon current = "+rightFrontTalon.getOutputCurrent());
                    break;
                case 4:  // kVoltage
                    leftRearTalon.setX(leftFrontTalon.getOutputVoltage() / leftFrontTalon.getBusVoltage());
                    rightRearTalon.setX(rightFrontTalon.getOutputVoltage() / rightFrontTalon.getBusVoltage());
System.out.println("Output left talon output voltage = "+leftFrontTalon.getOutputVoltage());
System.out.println("Output right talon output voltage = "+rightFrontTalon.getOutputVoltage());
                    break;
                case 0:  // kPercentVbus
                case 2:  // kSpeed
                case 3:  // kPosition
                default:
                    arcadeDrive(joystick.getAxis(Joystick.AxisType.kY), joystick.getAxis(Joystick.AxisType.kX));
                    break;
            }
        } 
        catch (Exception e) {
        	e.printStackTrace();
        }
        }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new DriveWithGamepad());
    }
}
