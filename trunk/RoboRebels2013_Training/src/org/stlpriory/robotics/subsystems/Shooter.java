/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.stlpriory.robotics.RobotMap;
import org.stlpriory.robotics.misc.Constants;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 */
public class Shooter extends Subsystem {

    protected static SpeedController shooterVictor;
    protected static SpeedController loaderVictor;

    private static DigitalInput startLimitSwitch;
    private static DigitalInput stopLimitSwitch;

    private static Relay rumbler;

    private static final double loadDiscTimeOut    = Constants.LOAD_DISC_TIMEOUT_IN_SECS;
    private static final double resetLoaderTimeOut = Constants.RESET_LOADER_TIMEOUT_IN_SECS;
    private static final double rumblerTimeout     = Constants.RUMBLER_TIMEOUT_IN_SECS;

    private static Timer loadDiscTimer;
    private static Timer resetLoaderTimer;
    private static Timer rumblerTimer;

    private static boolean loadDiscIsFinished;
    private static boolean resetLoaderIsFinished;

    public Shooter() {
        super("Shooter");
        Debug.println("[Shooter] Instantiating...");

        Debug.println("[Shooter] Initializing loader motor speed controller to PWM channel "
                + RobotMap.LOADER_MOTOR_PWM_CHANNEL + " on the digital module.");
        loaderVictor = new Victor(RobotMap.LOADER_MOTOR_PWM_CHANNEL);


        Debug.println("[Shooter] Initializing loader start position limit switch to I/O channel "
                + RobotMap.LOADER_START_POSITION_LIMIT_SWITCH_DIGITAL_IO_CHANNEL);
        startLimitSwitch = new DigitalInput(1, RobotMap.LOADER_START_POSITION_LIMIT_SWITCH_DIGITAL_IO_CHANNEL);
        Debug.println("[Shooter] Initializing loader stop position limit switch to I/O channel "
                + RobotMap.LOADER_STOP_POSITION_LIMIT_SWITCH_DIGITAL_IO_CHANNEL);
        stopLimitSwitch = new DigitalInput(1, RobotMap.LOADER_STOP_POSITION_LIMIT_SWITCH_DIGITAL_IO_CHANNEL);


        /*Debug.println("[Shooter] Initializing shooter wheel motor speed controller to PWM channel "
                + RobotMap.SHOOTER_WHEEL_MOTOR_PWM_CHANNEL + " on the digital module.");
        shooterVictor = new Victor(RobotMap.SHOOTER_WHEEL_MOTOR_PWM_CHANNEL);*/


        Debug.println("[Shooter] Initializing shaker motor to relay channel "
                + RobotMap.SHAKER_MOTOR_RELAY_CHANNEL + " on the digital module.");
        rumbler = new Relay(RobotMap.SHAKER_MOTOR_RELAY_CHANNEL);


        loadDiscTimer = new Timer();
        resetLoaderTimer = new Timer();
        rumblerTimer = new Timer();

        Debug.println("[Shooter] Instantiation complete.");
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public void loadDisc(double speed) {
        loadDiscIsFinished = false;

        // Until the stop position limit switch is triggered
        // the value returned will be false
        while (!stopLimitSwitch.get()) {
            loaderVictor.set(-speed);
        }
        loaderVictor.set(0);

        loadDiscIsFinished = true;
        printLimitSwitchValues();
    }

    public void resetLoader(double speed) {
        resetLoaderIsFinished = false;

        // Until the start position limit switch is triggered
        // the value returned will be false
        while (!startLimitSwitch.get()) {
            loaderVictor.set(speed);
        }
        loaderVictor.set(0);

        resetLoaderIsFinished = true;
        printLimitSwitchValues();
    }

    public void loadDiscWithTimeout(double speed) {
        // Measure the execution time for loading the disc.
        // If we exceed preset timeout value then stop the
        // action and allow us to try and reset the loader.
        loadDiscIsFinished = false;
        loadDiscTimer.reset();
        loadDiscTimer.start();

        // Until the stop position limit switch is triggered
        // the value returned will be false
        while ((!stopLimitSwitch.get()) && (loadDiscTimer.get() < loadDiscTimeOut)) {
            loaderVictor.set(-speed);
        }

        loaderVictor.set(0);
        loadDiscTimer.stop();
        loadDiscIsFinished = true;

        printLimitSwitchValues();
    }

    public void resetLoaderWithTimeout(double speed) {
        // Measure the execution time for resetting the loader.
        // If we exceed preset timeout value then stop the action
        // and allow us to try and load a disc.
        resetLoaderIsFinished = false;
        resetLoaderTimer.reset();
        resetLoaderTimer.start();

        // Until the start position limit switch is triggered
        // the value returned will be false
        while ((!startLimitSwitch.get()) && (resetLoaderTimer.get() < resetLoaderTimeOut)) {
            loaderVictor.set(speed);
        }

        loaderVictor.set(0);
        resetLoaderTimer.stop();
        resetLoaderIsFinished = true;

        printLimitSwitchValues();
    }

    public boolean isLoadDiscFinished() {
        return loadDiscIsFinished;
    }

    public boolean isResetLoaderFinished() {
        return resetLoaderIsFinished;
    }

    public void rumble() {
        rumblerTimer.reset();
        rumblerTimer.start();
        while (rumblerTimer.get() < rumblerTimeout) {
            rumbler.set(Relay.Value.kOn);
        }
        rumbler.set(Relay.Value.kOff);
        rumblerTimer.stop();
    }

    public void startShooter(double speed) {
//      shooterEncoder.start();
        shooterVictor.set(speed);
//      printEncoderValues();
    }

    public void stopShooter() {
        shooterVictor.set(0);
        resetLoader(Constants.LOADER_MOTOR_SPEED);
//      shooterEncoder.stop();
    }

    public void printLimitSwitchValues() {
        Debug.println("start switch = " + startLimitSwitch.get() + ", stop switch = "+ stopLimitSwitch.get());
    }

}
