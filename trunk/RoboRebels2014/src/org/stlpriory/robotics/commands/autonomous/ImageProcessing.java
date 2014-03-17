/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import org.stlpriory.robotics.commands.vision.DetermineHotGoal;

/**
 *
 * @author William
 */
public class ImageProcessing extends CommandGroup {
    
    public ImageProcessing() {

        // TODO the wait time should be adjusted to value based on practice
        // the purpose of the wait is to allow time for the non-hot-goal tape
        // to rotate to point up since at the start of the autonomous phase both
        // left and right side will have tape pointing out horizontally toward
        // the robot and we don't want to capture an image until after tape rotation
        // has completed
        addSequential(new WaitCommand(.4) ); //Warning: Do not modify time without also modifying WaitCommand at beginning of PrepareToShoot by same amount 
        addSequential(new DetermineHotGoal() );
    }
    
    
}
