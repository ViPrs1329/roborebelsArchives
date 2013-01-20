/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.stlpriory.robotics.RobotMap;
import org.stlpriory.robotics.misc.Constants;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 */
public class Shooter extends Subsystem {

    private Servo servo = null;

    public Shooter() {
        super("Shooter");
        Debug.println("[Shooter] Initializing loader servo on channel " + RobotMap.LOADER_CHANNEL);
        // Set the servo to the PWM channel on the digital module
        // to which the servo is attached.
        servo = new Servo(RobotMap.LOADER_CHANNEL);
    }

    private boolean canLoadDisc() {
        return true;
    }

    public void loadDisc() {
        if (canLoadDisc()) {
            servo.setAngle(Constants.MAX_LOADER_SERVO_ANGLE);
        }
    }

    public void resetLoader() {
        servo.setAngle(Constants.MIN_LOADER_SERVO_ANGLE);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
