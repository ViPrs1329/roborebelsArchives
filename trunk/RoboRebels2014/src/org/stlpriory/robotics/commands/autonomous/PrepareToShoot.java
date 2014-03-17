/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import org.stlpriory.robotics.commands.drivetrain.DriveForward;
import org.stlpriory.robotics.commands.drivetrain.Shift;
import org.stlpriory.robotics.commands.drivetrain.StopDriving;

/**
 *
 * @author William
 */
public class PrepareToShoot extends CommandGroup {
    
    public PrepareToShoot() { 
        
        // waiting to drive until we have captured the image from the camera to
        // avoid blurring the image
        addSequential(new WaitCommand(.45));
        // drive forward
        addSequential(new DriveForward() );       
        // TODO determine amount of time to drive forward
        addSequential(new WaitCommand(2.7) );
        addSequential(new StopDriving() );
            
    }
    
    
}
