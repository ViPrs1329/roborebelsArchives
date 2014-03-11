/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import org.stlpriory.robotics.commands.claw.HoldBall;

/**
 *
 * @author William
 */
public class AutonomousFirstPart extends CommandGroup {
    
    public AutonomousFirstPart() {
        // testing revealed that in order for the parallel processing of Driving and
        // ImageProcessing to work, there has to be at least one sequential
        // command added to the command group prior to adding the two
        // parallel commands
        addSequential(new HoldBall());
        addParallel(new Driving());
        addParallel(new ImageProcessing());
    }
    

}
