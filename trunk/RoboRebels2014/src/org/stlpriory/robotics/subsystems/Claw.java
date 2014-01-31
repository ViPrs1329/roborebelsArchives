/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.Compressor;
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

    private static final double MOTOR_SPEED = 0.5;
    private Talon wheel_left;
    private Talon wheel_right;
    private Talon wheel_center;
    private static Compressor compressor;
    private static Solenoid value1;
    private static Solenoid value2;

    public Claw() {
        super("Claw");
        Debug.println("[Claw] Instantiating...");

        Debug.println("[Claw Subsystem] Initializing left wheel talon to channel " + RobotMap.CLAW_WHEEL_LEFT_PWM_CHANNEL);
        Debug.println("[Claw Subsystem] Initializing center wheel talon to channel " + RobotMap.CLAW_WHEEL_CENTRAL_PWM_CHANNEL);
        Debug.println("[Claw Subsystem] Initializing right wheel talon to channel " + RobotMap.CLAW_WHEEL_RIGHT_PWM_CHANNEL);
        wheel_left = new Talon(RobotMap.CLAW_WHEEL_LEFT_PWM_CHANNEL);
        wheel_right = new Talon(RobotMap.CLAW_WHEEL_RIGHT_PWM_CHANNEL);
        wheel_center = new Talon(RobotMap.CLAW_WHEEL_CENTRAL_PWM_CHANNEL);
        
        Debug.println("[Claw Subsystem] Initializing compressor to IO channel "
                + RobotMap.COMPRESSOR_DIGITALIO_CHANNEL + " and relay channel " + RobotMap.COMPRESSOR_RELAY_CHANNEL);
        compressor = new Compressor(RobotMap.COMPRESSOR_DIGITALIO_CHANNEL, RobotMap.COMPRESSOR_RELAY_CHANNEL);

        Debug.println("[Claw Subsystem] Initializing first compressor solenoid to channel " + RobotMap.CLAW_VALVE1_CHANNEL);
        Debug.println("[Claw Subsystem] Initializing second compressor solenoid to channel " + RobotMap.CLAW_VALVE2_CHANNEL);
        value1 = new Solenoid(RobotMap.CLAW_VALVE1_CHANNEL);
        value2 = new Solenoid(RobotMap.CLAW_VALVE2_CHANNEL);

        if (! compressor.enabled()) {
            Debug.println("[Claw Subsystem] Starting compressor ...");
            compressor.start();
        }

        Debug.println("[Claw Subsystem] Instantiation complete.");
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public void startClawMotors() {
        wheel_left.set(MOTOR_SPEED);
        wheel_right.set(MOTOR_SPEED);
        wheel_center.set(MOTOR_SPEED);
    }

    public void stopClawMotors() {
        wheel_left.set(0);
        wheel_right.set(0);
        wheel_center.set(0);
    }

    public boolean isCompressorStarted() {
        return compressor.enabled();
    }

    /**
     * Start the compressor. This method will allow the polling loop to actually operate the compressor. The is stopped
     * by default and won't operate until starting it.
     */
    public void startCompressor() {
        if (!isCompressorStarted()) {
            compressor.start();
        }
    }

    /**
     * Stop the compressor. This method will stop the compressor from turning on.
     */
    public void stopCompressor() {
        if (isCompressorStarted()) {
            compressor.stop();
        }
    }

    /**
     * Delete the Compressor object. Delete the allocated resources for the compressor and kill the compressor task that
     * is polling the pressure switch.
     */
    public void freeCompressor() {
        compressor.free();
    }

    public void extendPiston() {
        value1.set(true);    //turns the Solenoid on
        value2.set(false);  //turns the Solenoid off
    }

    public void retractPiston() {
        value1.set(false);
        value2.set(true);
    }
}