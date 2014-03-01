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

    // Drive train speed controller mappings to PWM channels on the digital sidecar module
    public static final int LEFT_FRONT_DRIVE_MOTOR_PWM_CHANNEL  = 7;  // PWM channel
    public static final int LEFT_REAR_DRIVE_MOTOR_PWM_CHANNEL   = 8;  // PWM channel
    public static final int RIGHT_FRONT_DRIVE_MOTOR_PWM_CHANNEL = 5;  // PWM channel
    public static final int RIGHT_REAR_DRIVE_MOTOR_PWM_CHANNEL  = 6;  // PWM channel
    
    // Talon channels for the claw wheel motors
    public static final int CLAW_WHEEL_LEFT_PWM_CHANNEL = 2;
    public static final int CLAW_WHEEL_RIGHT_PWM_CHANNEL = 3;
    public static final int CLAW_WHEEL_CENTRAL_PWM_CHANNEL = 1;
    
    public static final int LAUNCHER_JAGUAR1_PWM_CHANNEL = 4;
    //public static final int LAUNCHER_JAGUAR2_PWM_CHANNEL = 9;
    
    // Digital I/O and relay channels for the compressor
    public static final int COMPRESSOR_DIGITALIO_CHANNEL = 1;
    public static final int COMPRESSOR_RELAY_CHANNEL = 1;
    
    // Drive train speed controller mappings to the addresses of the Jaguars on the CAN bus
    public static final int LEFT_REAR_DRIVE_MOTOR_CAN_BUS_ADDRESS   = 14;  // CAN bus channel (slave)
    public static final int LEFT_FRONT_DRIVE_MOTOR_CAN_BUS_ADDRESS  = 13;  // CAN bus channel (master)
    public static final int RIGHT_REAR_DRIVE_MOTOR_CAN_BUS_ADDRESS  = 12;  // CAN bus channel (slave)
    public static final int RIGHT_FRONT_DRIVE_MOTOR_CAN_BUS_ADDRESS = 11;  // CAN bus channel (master)
    
    //Solenoid relay channel on the cRIO channels for the sonic shifter gear boxes
    public static final int GEARBOX1_VALVE_CHANNEL = 1;
    public static final int GEARBOX2_VALVE_CHANNEL = 2;
    
    // Solenoid relay channel on the cRIO channels for the ball launcher
    public static final int LAUNCHER_VALVE1_CHANNEL = 5;
    public static final int LAUNCHER_VALVE2_CHANNEL = 6;
    
    // Solenoid relay channel on the cRIO for tilting the claw assembly back and forth
    public static final int CLAW_VALVE1_CHANNEL = 3;
    public static final int CLAW_VALVE2_CHANNEL = 4;
    
    // Solenoid relay channel on the cRIO for tilting the ball grabber wheels out of the way
    public static final int CLAW_WHEEL_VALVE1_CHANNEL = 7;
    public static final int CLAW_WHEEL_VALVE2_CHANNEL = 8;
    
    // DigitalModule number of the Digital Sidecar
    public static final int DIGITAL_SIDECAR_MODULE_NUMBER = 2;
    
    // Address for I2C with the Arduino
    public static final int I2C_ADRESS_NUMBER = 4;
    
    public static final int LAUNCHER_PUNTER_LIMIT_SWITCH_DIGITAL_IO_CHANNEL =  1;
    
}