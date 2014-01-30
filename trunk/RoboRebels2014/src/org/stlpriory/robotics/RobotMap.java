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
    
    public static final int LAUNCHER_JAGUAR1_PWM_CHANNEL = 1;
    public static final int LAUNCHER_JAGUAR2_PWM_CHANNEL = 2;
    
    // Digital I/O and relay channels for the compressor
    public static final int COMPRESSOR_DIGITALIO_CHANNEL = 1;
    public static final int COMPRESSOR_RELAY_CHANNEL = 1;
    
    // Solenoid channels for the ball launcher
    public static final int LAUNCHER_VALVE1_CHANNEL = 1;
    public static final int LAUNCHER_VALVE2_CHANNEL = 2;
    
    // Solenoid channels for the tilting the claw assembly
    public static final int CLAW_VALVE1_CHANNEL = 3;
    public static final int CLAW_VALVE2_CHANNEL = 4;
    
    // Talon channels for the claw wheel motors
    public static final int CLAW_WHEEL_LEFT_PWM_CHANNEL = 5;
    public static final int CLAW_WHEEL_RIGHT_PWM_CHANNEL = 6;
    public static final int CLAW_WHEEL_CENTRAL_PWM_CHANNEL = 7;
    
}