package org.stlpriory.robotics;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // For example to map the left and right motors, you could define the
    // following variables to use with your drivetrain subsystem.
    // public static final int leftMotor = 1;
    // public static final int rightMotor = 2;

    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static final int rangefinderPort = 1;
    // public static final int rangefinderModule = 1;

    // Joysticks
    public static final int DRIVER_STATION_USB_PORT1 = 1;	// USB Port 1 on Driver station PC
    public static final int DRIVER_STATION_USB_PORT2 = 2;	// USB Port 2 on Driver station PC
    public static final int DRIVER_STATION_USB_PORT3 = 3;	// USB Port 3 on Driver station PC
    public static final int DRIVER_STATION_USB_PORT4 = 4;	// USB Port 4 on Driver station PC

    // Motors/sensors used by the shooter
    public static final int SHOOTER_SERVO_PWM_CHANNEL  = 5;	// PWM OUT Port 1
    public static final int SHOOTER_VICTOR_PWM_CHANNEL = 6;	// PWM OUT Port 1

    // Drive motors (4 motor mechanum drive)
    public static final int LEFT_FRONT_DRIVE_MOTOR_PWM_CHANNEL  = 2;	// PWM 1
    public static final int LEFT_REAR_DRIVE_MOTOR_PWM_CHANNEL   = 1;	// PWM 3
    public static final int RIGHT_FRONT_DRIVE_MOTOR_PWM_CHANNEL = 3;	// PWM 2
    public static final int RIGHT_REAR_DRIVE_MOTOR_PWM_CHANNEL  = 4;	// PWM 4

    // Sonar slot
//    public static final int kSonarChannel = 2;	// Analog 2

    // Gyroscope slot
//    public static final int kGyroChannel = 1;	// Analog 1

}
