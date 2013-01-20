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
    public static final int JOYSTICK1 = 1;	// Port 1
    public static final int JOYSTICK2 = 2;	// Port 2
    public static final int JOYSTICK3 = 3;	// Port 3
    public static final int JOYSTICK4 = 4;	// Port 4

    // Motors/sensors used by the shooter
    public static final int DISC_LOADER_CHANNEL = 1;	// PWM OUT Port 1

    // Motors/sensors used by the arm
//    public static final int kArmChannel = 5;	// PWM 5
//    public static final int kArmLatchChannel = 7;	// PWM 7
//    public static final int kArmSensorExtracted = 5;	// Digital IO 5
//    public static final int kArmSensorRetracted = 2;	// Digital IO 2
//    public static final int kArmSensorLatch = 3;	// Digital IO 3

    // Motor used by the ball grabber
//    public static final int kGrabberChannel = 6;	// PWM 6

    // Motor used by the shooter
//    public static final int kShooterFlyWheelChannel = 4;	// PWM 4
//    public static final int kShooterFeederChannel = 3;	// PWM 3
//    public static final int kShooterAnglerChannel = 1;	// Relay 1

    // Drive motors (2 motor drive)
    public static final int DRIVE_LEFT_MOTOR = 2;	// PWM 2
    public static final int DRIVE_RIGHT_MOTOR = 1;	// PWM 1

    // Drive motors (4 motor mechanum drive)
    public static final int DRIVE_FRONT_LEFT_MOTOR  = 1;	// PWM 1
    public static final int DRIVE_FRONT_RIGHT_MOTOR = 2;	// PWM 2
    public static final int DRIVE_BACK_LEFT_MOTOR   = 3;	// PWM 3
    public static final int DRIVE_BACK_RIGHT_MOTOR  = 4;	// PWM 4

    // Sonar slot
//    public static final int kSonarChannel = 2;	// Analog 2

    // Gyroscope slot
//    public static final int kGyroChannel = 1;	// Analog 1

}
