package org.stlpriory.robotics.subsystems;

import org.stlpriory.robotics.RobotMap;
import org.stlpriory.robotics.commands.drivetrain.DriveWithGamepad;
import org.stlpriory.robotics.utils.Constants;
import org.stlpriory.robotics.utils.Debug;

import edu.wpi.first.wpilibj.CANTalon;
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

    public void mecanum_drive(final double forward, final double right, final double rotation) {
        this.drive.mecanumDrive_Cartesian(right, forward, rotation, 0);
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new DriveWithGamepad());
    }
}
