/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.stlpriory.robotics.RobotMap;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 * @author William
 */
public class Tank extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    Compressor compressor;
    
    public Tank() {
        super("Tank");
        Debug.println("[Tank] Instantiating...");
        
        Debug.println("[Tank Subsystem] Initializing compressor to IO channel "
                + RobotMap.COMPRESSOR_DIGITALIO_CHANNEL + " and relay channel " + RobotMap.COMPRESSOR_RELAY_CHANNEL);
        compressor = new Compressor(RobotMap.COMPRESSOR_DIGITALIO_CHANNEL, RobotMap.COMPRESSOR_RELAY_CHANNEL);
        
        if (! compressor.enabled()) {
            Debug.println("[Tank Subsystem] Starting compressor ...");
            compressor.start();
        }
        
                Debug.println("[Tank Subsystem] Instantiation complete.");

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

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
