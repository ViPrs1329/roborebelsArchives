/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 */
public class ExamplePneumaticSubsystem extends Subsystem {

    // The GPIO channel that the pressure switch is attached to.
    private static final int PRESSURE_SWITCH_CHANNEL = 1;
    // The relay channel that the compressor relay is attached to.
    private static final int COMPRESSOR_RELAY_CHANNEL = 2;
    // The first solenoid channel for the piston.
    private static final int COMPRESSOR_SOLENOID_CHANNEL_ONE = 1;
    // The first solenoid channel for the piston.
    private static final int COMPRESSOR_SOLENOID_CHANNEL_TWO = 2;
    private static Compressor mainCompressor;
    private static Solenoid pistonExtend;
    private static Solenoid pistonRetract;

    public ExamplePneumaticSubsystem() {
        super("ExamplePneumaticsSubsystem");
        Debug.println("[ExamplePneumaticsSubsystem] Instantiating...");

        Debug.println("[ExamplePneumaticsSubsystem] Initializing compressor to GPIO channel "
                + PRESSURE_SWITCH_CHANNEL + " and relay channel " + COMPRESSOR_RELAY_CHANNEL);

        // The compressor can take up to four parameters, because it has two 'things' to set: the location 
        // of the pressure switch (what reads the pressure and tells the robot when it's 'full'), and 
        // the location of the relay (what turns the compressor on or off). After that, start() the 
        // compressor object and it should automatically regulate itself, turning off when the pressure 
        // is high enough, then restarting when it drops. You can get the value of the pressure switch 
        // and the state of the compressor with getPressureSwitchValue() and enabled(), respectively, but 
        // for safety reasons you cannot control the actual state of the compressor manually.
        mainCompressor = new Compressor(PRESSURE_SWITCH_CHANNEL, COMPRESSOR_RELAY_CHANNEL);

        Debug.println("[ExamplePneumaticsSubsystem] Initializing first compressor solenoid to channel "
                + COMPRESSOR_SOLENOID_CHANNEL_ONE);
        // For the Solenoids, the first thing to do is to create the Solenoid object. The constructor 
        // follows the general WPI format, with options to create it based on slot (of the Solenoid module) 
        // and channel, or just by channel.
        pistonExtend = new Solenoid(COMPRESSOR_SOLENOID_CHANNEL_ONE);

        Debug.println("[ExamplePneumaticsSubsystem] Initializing second compressor solenoid to channel "
                + COMPRESSOR_SOLENOID_CHANNEL_TWO);
        pistonRetract = new Solenoid(COMPRESSOR_SOLENOID_CHANNEL_TWO);

        Debug.println("[Shooter] Instantiation complete.");
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public boolean isCompressorStarted() {
        return mainCompressor.enabled();
    }

    /**
     * Start the compressor. This method will allow the polling loop to actually operate the compressor. The is stopped
     * by default and won't operate until starting it.
     */
    public void startCompressor() {
        if (!isCompressorStarted()) {
            mainCompressor.start();
        }
    }

    /**
     * Stop the compressor. This method will stop the compressor from turning on.
     */
    public void stopCompressor() {
        if (isCompressorStarted()) {
            mainCompressor.stop();
        }
    }

    /**
     * Delete the Compressor object. Delete the allocated resources for the compressor 
     * and kill the compressor task that is polling the pressure switch.
     */
    public void freeCompressor() {
        mainCompressor.free();

    }

    public void extendPiston() {
        pistonExtend.set(true);    //turns the Solenoid on
        pistonRetract.set(false);  //turns the Solenoid off
    }

    public void retractPiston() {
        pistonExtend.set(false);
        pistonRetract.set(true);
    }
}