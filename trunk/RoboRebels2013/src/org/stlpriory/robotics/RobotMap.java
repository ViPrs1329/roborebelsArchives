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

    // Loader and shooter speed controllers mappings to PWM channels on the digital sidecar module
    public static final int LOADER_MOTOR_PWM_CHANNEL = 7;	         // PWM channel
    public static final int SHOOTER_WHEEL_MOTOR_PWM_CHANNEL = 6;	 // PWM channel

    public static final int SHOOTER_ENCODER_DIGITAL_IO_CHANNEL_A = 1;
    public static final int SHOOTER_ENCODER_DIGITAL_IO_CHANNEL_B = 2;

    // Limit switches used to extend and retract the loader arm
    public static final int LOADER_START_POSITION_LIMIT_SWITCH_DIGITAL_IO_CHANNEL = 5;//3;
    public static final int LOADER_STOP_POSITION_LIMIT_SWITCH_DIGITAL_IO_CHANNEL = 6;//4;

    public static final int PROXY_SENSOR_IO_CHANNEL = 5;

}
