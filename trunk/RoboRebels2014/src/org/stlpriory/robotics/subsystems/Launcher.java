/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.stlpriory.robotics.RobotMap;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 * @author admin
 */
public class Launcher extends Subsystem {

    private Compressor compressor;
    private Jaguar launch1;
    private Jaguar launch2;
    private Solenoid valve1;
    private Solenoid valve2;

    public Launcher() {
        super("Launcher");
        Debug.println("[Launcher Subsystem] Instantiating...");

        Debug.println("[Launcher Subsystem] Initializing compressor to GPIO channel "
                + RobotMap.COMPRESSOR_DIGITALIO_CHANNEL 
                + " and relay channel " + RobotMap.COMPRESSOR_RELAY_CHANNEL);
        compressor = new Compressor(RobotMap.COMPRESSOR_DIGITALIO_CHANNEL, RobotMap.COMPRESSOR_RELAY_CHANNEL);

        Debug.println("[Launcher Subsystem] Initializing speed controller to channel "
                + RobotMap.LAUNCHER_JAGUAR1_PWM_CHANNEL);
        Debug.println("[Launcher Subsystem] Initializing speed controller to channel "
                + RobotMap.LAUNCHER_JAGUAR2_PWM_CHANNEL);
        launch1 = new Jaguar(RobotMap.LAUNCHER_JAGUAR1_PWM_CHANNEL);
        launch2 = new Jaguar(RobotMap.LAUNCHER_JAGUAR2_PWM_CHANNEL);

        Debug.println("[Launcher Subsystem] Initializing first compressor solenoid to channel "
                + RobotMap.LAUNCHER_VALVE1_CHANNEL);
        Debug.println("[Launcher Subsystem] Initializing second compressor solenoid to channel "
                + RobotMap.LAUNCHER_VALVE2_CHANNEL);
        valve1 = new Solenoid(RobotMap.LAUNCHER_VALVE1_CHANNEL);
        valve2 = new Solenoid(RobotMap.LAUNCHER_VALVE2_CHANNEL);

        if (! compressor.enabled()) {
            Debug.println("[Launcher Subsystem] Starting compressor ...");
            compressor.start();
        }
        
        Debug.println("[Launcher Subsystem] Instantiation complete.");
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public void extendPiston() {
        valve2.set(false);
        valve1.set(true);
    }

    public void retractPiston() {
        valve1.set(false);
        valve2.set(true);
    }

    public void loadLauncher() {
        launch1.set(.6);
        launch2.set(.6);
    }

    public void fireLauncher() {
        launch1.set(0);
        launch2.set(0);
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

}