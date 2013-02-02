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

    // Joysticks/gampads mappings to USB ports on the Driver station PC
    public static final int DRIVER_STATION_USB_PORT1 = 1;  // USB Port
    public static final int DRIVER_STATION_USB_PORT2 = 2;  // USB Port
    public static final int DRIVER_STATION_USB_PORT3 = 3;  // USB Port
    public static final int DRIVER_STATION_USB_PORT4 = 4;  // USB Port

    // Drive train speed controller mappings to PWM channels on the digital sidecar module
    public static final int LEFT_FRONT_DRIVE_MOTOR_PWM_CHANNEL  = 2;  // PWM channel
    public static final int LEFT_REAR_DRIVE_MOTOR_PWM_CHANNEL   = 1;  // PWM channel
    public static final int RIGHT_FRONT_DRIVE_MOTOR_PWM_CHANNEL = 3;  // PWM channel
    public static final int RIGHT_REAR_DRIVE_MOTOR_PWM_CHANNEL  = 4;  // PWM channel

    // Shooter speed controller and server mappings to PWM channels on the digital sidecar module
    public static final int SHOOTER_SERVO_PWM_CHANNEL  = 6;	 // PWM channel
    public static final int SHOOTER_VICTOR_PWM_CHANNEL = 5;	 // PWM channel

}
