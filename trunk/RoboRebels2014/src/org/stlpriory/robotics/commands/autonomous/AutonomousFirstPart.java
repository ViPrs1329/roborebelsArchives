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
        // testing revealed that in order for the parallel processing to work, 
        // there has to be at least one sequential command added to the command 
        // group prior to adding the parallel commands
        addSequential(new WaitCommand(0.03));
        
        // hold the ball while driving
        addSequential(new HoldBall());
        
        addParallel(new PrepareLauncher());
        addParallel(new PrepareToShoot());
        addParallel(new ImageProcessing());
    }

}
