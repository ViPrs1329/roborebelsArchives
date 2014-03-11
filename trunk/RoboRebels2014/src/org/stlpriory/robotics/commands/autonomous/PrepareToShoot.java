/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import org.stlpriory.robotics.commands.claw.HoldBall;
import org.stlpriory.robotics.commands.drivetrain.DriveForward;
import org.stlpriory.robotics.commands.drivetrain.StopDriving;
import org.stlpriory.robotics.commands.launcher.Reset;
import org.stlpriory.robotics.commands.launcher.Stop;

/**
 *
 * @author William
 */
public class PrepareToShoot extends CommandGroup {
    
    public PrepareToShoot() { 
        
        // hold the ball while driving
        addSequential(new HoldBall());
        
        // drive forward
        addSequential(new DriveForward() );       
        // TODO determine amount of time to drive forward
        addSequential(new WaitCommand(2) );
        addSequential(new StopDriving() );
        
        // TODO should we stop holding the ball while retracting the puncher
        // in order to reduce the electrical load?
        
        // retract the puncher to limit switch position and then stop
        addSequential(new Reset());
        addSequential(new Stop());
            
    }
    
    
}
