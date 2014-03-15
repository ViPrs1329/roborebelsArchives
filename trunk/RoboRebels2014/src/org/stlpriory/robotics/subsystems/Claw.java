/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.stlpriory.robotics.RobotMap;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 * @author admin
 */
public class Claw extends Subsystem {

    private static final double MAX_MOTOR_SPEED = 1.0;
    private static final double MOTOR_SPEED = 0.75;
    private static final double HOLD_MOTOR_SPEED = 0.3;
    private Talon wheel_left;
    private Talon wheel_right;
    private Talon wheel_center;
    private static Solenoid valve1;
    private static Solenoid valve3;

    public Claw() {
        super("Claw");
        Debug.println("[Claw] Instantiating...");

        Debug.println("[Claw Subsystem] Initializing left wheel talon to channel " + RobotMap.CLAW_WHEEL_LEFT_PWM_CHANNEL);
        Debug.println("[Claw Subsystem] Initializing center wheel talon to channel " + RobotMap.CLAW_WHEEL_CENTRAL_PWM_CHANNEL);
        Debug.println("[Claw Subsystem] Initializing right wheel talon to channel " + RobotMap.CLAW_WHEEL_RIGHT_PWM_CHANNEL);
        wheel_left = new Talon(RobotMap.CLAW_WHEEL_LEFT_PWM_CHANNEL);
        wheel_right = new Talon(RobotMap.CLAW_WHEEL_RIGHT_PWM_CHANNEL);
        wheel_center = new Talon(RobotMap.CLAW_WHEEL_CENTRAL_PWM_CHANNEL);

        Debug.println("[Claw Subsystem] Initializing first solenoid to relay channel " + RobotMap.CLAW_VALVE1_CHANNEL);
        Debug.println("[Claw Subsystem] Initializing second solenoid to relay channel " + RobotMap.CLAW_VALVE2_CHANNEL);
        Debug.println("[Claw Subsystem] Initializing third solenoid to relay channel " + RobotMap.CLAW_WHEEL_VALVE1_CHANNEL);
        Debug.println("[Claw Subsystem] Initializing fourth solenoid to relay channel " + RobotMap.CLAW_WHEEL_VALVE2_CHANNEL);
        valve1 = new Solenoid(RobotMap.CLAW_VALVE1_CHANNEL);
        valve3 = new Solenoid(RobotMap.CLAW_WHEEL_VALVE1_CHANNEL);

        Debug.println("[Claw Subsystem] Instantiation complete.");
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public void startClawMotors() {
        //Todo: Set directions of motors
        wheel_left.set(-1 * MOTOR_SPEED);
        wheel_right.set(MOTOR_SPEED);
        wheel_center.set(MOTOR_SPEED);
    }

    public void startClawMotorsForHold() {
        //Todo: Set directions of motors
        wheel_left.set(-HOLD_MOTOR_SPEED);
        wheel_right.set(HOLD_MOTOR_SPEED);
        wheel_center.set(HOLD_MOTOR_SPEED);
    }

    public void stopClawMotors() {
        wheel_left.set(0);
        wheel_right.set(0);
        wheel_center.set(0);
    }

    public boolean isClawLoweredForPickup() {
        return valve1.get();
    }

    public void lowerClawForPickup() {
        if (isWheelLoweredForPickup()) {
            valve1.set(true);   //turns the Solenoid on
        }
    }

    public void raiseClawForShoot() {
        startClawMotorsForHold();
        if (isWheelLoweredForPickup()) {
            valve1.set(false);
        }
        stopClawMotors();
    }

    public void lowerWheelForPickup() {
        if (!isClawLoweredForPickup()) {
            valve3.set(false);
        }
    }

    public void raiseWheelForShoot() {
        if (!isClawLoweredForPickup()) {
            valve3.set(true);
        }
    }

    public void lowGoalShoot() {
        wheel_left.set(0);
        wheel_right.set(0);
        wheel_center.set(-MAX_MOTOR_SPEED);
    }

    public boolean isWheelLoweredForPickup() {
        return !valve3.get();
    }

    public void calibrateTalonsMax() {
        this.wheel_center.set(1.0);
        this.wheel_left.set(1.0);
        this.wheel_right.set(1.0);
    }

    public void calibrateTalonsMin() {
        this.wheel_center.set(-1.0);
        this.wheel_left.set(-1.0);
        this.wheel_right.set(-1.0);
    }

    public void calibrateTalonsZero() {
        this.wheel_center.set(0.0);
        this.wheel_left.set(0.0);
        this.wheel_right.set(0.0);
    }
}