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
 */
public class Tank extends Subsystem {

    // The compressor is used to store air in the pneumatics system. What is wanted is for the compressor to fill 
    // the system with air, and then replace any air used to move the piston.  If the compressor is left on and the 
    // piston is moved, pressure will keep building up in the system until something bad happens. This could be the 
    // pneumatics tubing pulling out of the connectors or worse. What we would like is for there to be something 
    // monitoring how much air pressure is in the pneumatics system and shut off the compressor when there is enough 
    // pressure. We then would want the compressor to turn back on when either we have used one of the pistons in the 
    //system or when enough air has leaked out of the system that there wouldn’t be enough air to move our piston.
    // The pressure switch is used to detect if there is too little or too much air in the system. Once the pressure 
    // gets to some maximimum value, the switch will turn off. If the pressure gets below a certain value the switch 
    // will turn on. We can use this to turn the compressor on and off.
    private Compressor compressor;

    public Tank() {
        super("Tank");
        Debug.println("[Tank Subsystem] Instantiating...");

        Debug.println("[Tank Subsystem] Initializing compressor to IO channel "
                + RobotMap.COMPRESSOR_DIGITALIO_CHANNEL + " and relay channel " 
                + RobotMap.COMPRESSOR_RELAY_CHANNEL);
        compressor = new Compressor(RobotMap.COMPRESSOR_DIGITALIO_CHANNEL, RobotMap.COMPRESSOR_RELAY_CHANNEL);

        if (!compressor.enabled()) {
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
