/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.stlpriory.robotics.RobotMap;
import org.stlpriory.robotics.misc.Constants;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 */
public class Shooter extends Subsystem {

    private static Servo shooterServo = null;
    private static SpeedController shooterVictor = null;
    boolean button;
    private double servoAngle;

    public Shooter() {
        super("Shooter");
        Debug.println("[Shooter] Instantiating...");

        // Set the servo and victor to the PWM channels on the digital module
        // to which the servo is attached.
        Debug.println("[Shooter] Initializing shooter motor speed controller to PWM channel "
                + RobotMap.SHOOTER_VICTOR_PWM_CHANNEL + " on the digital module.");
        shooterVictor = new Victor(RobotMap.SHOOTER_VICTOR_PWM_CHANNEL);

        Debug.println("[Shooter] Initializing shooter disc loader servo to PWM channel "
                + RobotMap.SHOOTER_SERVO_PWM_CHANNEL + " on the digital module.");
        shooterServo = new Servo(RobotMap.SHOOTER_SERVO_PWM_CHANNEL);

        Debug.println("[Shooter] Instantiation complete.");
    }

    private boolean canLoadDisc() {
        return true;
    }

    public void loadDisc() {
        if (canLoadDisc()) {
            System.out.println("Is loading");
            System.out.println("Servo Angle: " + shooterServo.getAngle());
            servoAngle = shooterServo.getAngle();
            shooterServo.setAngle(Constants.MAX_LOADER_SERVO_ANGLE);
            System.out.println("Servo Angle: " + shooterServo.getAngle());
            System.out.println("done loading");        }
    }

    public void resetLoader() {
        System.out.println("is reseting");
        System.out.println("Servo Angle: " + shooterServo.getAngle());
        servoAngle = shooterServo.getAngle();
        shooterServo.setAngle(Constants.MIN_LOADER_SERVO_ANGLE);
        System.out.println("Servo Anlge :" +shooterServo.getAngle());
        System.out.println("done reseting");
    }

    public void startShooter(double speed) {
        shooterVictor.set(speed);
    }

    public void stopShooter() {
        shooterVictor.set(0);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public void setAngle(double angle) {
        shooterServo.setAngle(angle);
    }
}
