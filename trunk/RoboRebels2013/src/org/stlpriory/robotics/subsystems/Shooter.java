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
            shooterServo.setAngle(Constants.MAX_LOADER_SERVO_ANGLE);
        }
    }

    public void resetLoader() {
        shooterServo.setAngle(Constants.MIN_LOADER_SERVO_ANGLE);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
