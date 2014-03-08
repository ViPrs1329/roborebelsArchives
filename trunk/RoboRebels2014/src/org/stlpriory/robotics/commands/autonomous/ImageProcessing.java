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
        addSequential(new WaitCommand(.25) {
            protected void execute ( ) {
                System.out.println("ImageProcessing WaitCommand at " + System.currentTimeMillis());
                super.execute();
            }
            protected void end() {
            System.out.println("ImageProcessing WaitCommand ending at " + System.currentTimeMillis());
            super.end();
        }
        });
        addSequential(new DetermineHotGoal() {
            protected void execute ( ) {
                System.out.println("ImageProcessing DetermineHotGoal at " + System.currentTimeMillis());
                super.execute();
            }
            protected void end() {
            System.out.println("ImageProcessing DetermineHotGoal ending at " + System.currentTimeMillis());
            super.end();
        }
        });
    }
    
    protected void execute ( ) {
        System.out.println("ImageProcessing at " + System.currentTimeMillis());
        super.execute();
    }
    protected void end() {
            System.out.println("ImageProcessing ending at " + System.currentTimeMillis());
            super.end();
        }
}
