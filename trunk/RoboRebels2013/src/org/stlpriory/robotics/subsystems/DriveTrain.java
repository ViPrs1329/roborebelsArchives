/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.stlpriory.robotics.RobotMap;
import org.stlpriory.robotics.commands.drivetrain.DriveWithJoystick;
import org.stlpriory.robotics.misc.Debug;
import org.stlpriory.robotics.misc.Utils;

/**
 *
 */
public class DriveTrain extends Subsystem {

    private RobotDrive drive;
    private static Jaguar leftFrontJag;
    private static Jaguar rightFrontJag;
    private static Jaguar leftRearJag;
    private static Jaguar rightRearJag;
    private static double direction = 1;

    public DriveTrain() {
        super("DriveTrain");
        Debug.println("[DriveTrain] Instantiating...");

        Debug.println("[DriveTrain] Initializing left front jaguar on channel " + RobotMap.DRIVE_FRONT_LEFT_MOTOR);
        try {
            leftFrontJag = new Jaguar(RobotMap.DRIVE_FRONT_LEFT_MOTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Debug.println("[DriveTrain] Initializing right front jaguar on channel " + RobotMap.DRIVE_FRONT_RIGHT_MOTOR);
        try {
            rightFrontJag = new Jaguar(RobotMap.DRIVE_FRONT_RIGHT_MOTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Debug.println("[DriveTrain] Initializing left rear jaguar on channel " + RobotMap.DRIVE_BACK_LEFT_MOTOR);
        try {
            leftRearJag = new Jaguar(RobotMap.DRIVE_BACK_LEFT_MOTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Debug.println("[DriveTrain] Initializing right rear jaguar on channel " + RobotMap.DRIVE_BACK_RIGHT_MOTOR);
        try {
            rightRearJag = new Jaguar(RobotMap.DRIVE_BACK_RIGHT_MOTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Debug.println("[DriveTrain] Initializing RobotDrive");
        drive = new RobotDrive(leftFrontJag, leftRearJag, rightFrontJag, rightRearJag);
        drive.setSafetyEnabled(false);
        drive.setExpiration(0.1);
        drive.setSensitivity(0.5);
        drive.setMaxOutput(1.0);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        //drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        //drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);

        Debug.println("[DriveTrain] Instantiation complete.");
    }

    /**
     * Initialize and set default command
     */
    public void initDefaultCommand() {
        setDefaultCommand(new DriveWithJoystick());
    }

    public void setForwards() {
        direction = 1;
    }

    public void setBackwards() {
        direction = -1;
    }

    public void stop() {
        Debug.println("[DriveTrain.stop]");
        drive.tankDrive(0.0, 0.0);
    }

    public boolean canDrive() {
        return true;
    }

    public void tankDrive(double leftValue, double rightValue) {
        leftValue *= direction;
        rightValue *= direction;
        if (canDrive()) {
            Debug.println("[DriveTrain.tankDrive] leftValue = "+leftValue+", rightValue = "+rightValue);
            drive.tankDrive(leftValue, rightValue);
        }
    }

    public void arcadeDrive(double moveValue, double rotateValue) {
        moveValue *= direction;
        rotateValue *= direction;
        if (canDrive()) {
            Debug.println("[DriveTrain.arcadeDrive] moveValue = "+moveValue+", rotateValue = "+rotateValue);
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
         * When the joystick is pushed forward, its Y output should be positive.
         * When the joystick is pushed to the right, its X output should be positive.
         * When the joystick is twisted clockwise, its Z output should be positive.
         * If not, add code to invert the sign if necessary.
         *
         * Let w1, w2, w3, and w4 be the commands to send to each of the 4 wheels,
         * where w1 is front left, w2 is front right, w3 is rear left, w4 is rear right,
         * as viewed from the top.
         *
         * Let Kf, Ks, and Kt be tuning parameters (0 to +1) for the fwd/rev, strafe,
         * and spin joystick sensitivities, respectively.
         *
         * set the four wheel speed commands as follows:
         *   w1 = Kf*y + Ks*x + Kt*z
         *   w2 = Kf*y - Ks*x - Kt*z
         *   w3 = Kf*y - Ks*x + Kt*z
         *   w4 = Kf*y + Ks*x - Kt*z
         *
         * Next, normalize the above commands so that their range is -1 to +1. Proceed as follows:
         * look at the magnitude (absolute value) of each of the four commands, and select the MAXIMUM.
         * For sake of discussion, call this maximum value Wmax
         * if Wmax is less than or equal to 1, no scaling is required. Just use the four w commands as-is
         * if Wmax is greater than 1, then divide EACH of the four w values by Wmax.
         *
         * That's all there is to it. Send each w command to the corresponding wheel
         * (where -1 means 100% reverse, and +1 means 100% forward).
         *
         * Set Kf, Ks, and Kt to +1 initially, then adjust each one down (but not less than zero)
         * individually as required to obtain the desired joystick sensitivity to the three motions.
         *
         * One note of caution: make sure the polarity of the motors on each side of the bot is wired
         * correctly (each wheel must be spinning in the "forward" direction when it gets a "+" command).
         * An easy way to observe this is to elevate the bot and push the joystick forward; all four
         * wheels should be spinning forward.
         */
        double kF = 1;
        double kS = 1;
        double kT = 1;

        double rawLeftX  = joystick.getRawAxis(1);
        double rawLeftY  = joystick.getRawAxis(2);
        double rawZ      = joystick.getRawAxis(3);
        double rawRightX = joystick.getRawAxis(4);
        double rawRightY = joystick.getRawAxis(5);

        double leftX  = Utils.scale(rawLeftX);
        double leftY  = Utils.scale(rawLeftY);
        double rightX = Utils.scale(rawRightX);
        double rightY = Utils.scale(rawRightY);

        double l_angle     = Math.toDegrees(MathUtils.atan2(-rawLeftX, -rawLeftY));
        double l_magnitude = Math.sqrt((rawLeftX * rawLeftX) + (rawLeftY * rawLeftY));
        double r_angle     = Math.toDegrees(MathUtils.atan2(-rawRightX, -rawRightY));
        double r_magnitude = Math.sqrt((rawRightX * rawRightX) + (rawRightY * rawRightY));

        if (l_magnitude < .28) {
            l_magnitude = 0;
        }

        if (r_magnitude < .28) {
            r_magnitude = 0;
        }

        double rotation  = -rawZ;
        double forward   = leftY;
        double right     = -leftX;
        double clockwise = rawZ;

        Debug.println("[DriveTrain.mecanumDrive] x-speed = " + right + ", y-speed = " + forward);
        drive.mecanumDrive_Cartesian(right, forward, rotation, clockwise);
    }

    public void straight(double speed) {
        speed *= direction;
        if (canDrive()) {
            Debug.println("[DriveTrain.straight] speed = "+speed);
            drive.tankDrive(speed, speed * 0.75);
        }
    }

    public void turnLeft() { // sets the motor speeds to start a left turn
        Debug.println("[DriveTrain.turnLeft]");
        arcadeDrive(0.0, 1.0);
    }

    public void driveWithJoystick(Joystick joystick) {
        mecanumDrive(joystick);
        //drive.arcadeDrive(stick);
    }
}
