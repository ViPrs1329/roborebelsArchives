/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.stlpriory.robotics.commands.vision;

import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.RGBImage;
import org.stlpriory.robotics.commands.CommandBase;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 * Stubbed in command for now
 * @author randytielking
 */
public class DetermineHotGoal extends CommandBase {
    
    public DetermineHotGoal ( ) {
        super("DetermineHotGoal");
        Debug.println("DetermineHotGoal constructor called"); 
    }
    
    protected void initialize ( ) {
        try {
            ColorImage image;
            image = new RGBImage("/center.jpg");
            BinaryImage thresholdImage = image.thresholdHSV(105, 137, 230, 255, 133, 183);  
            thresholdImage.write("/threshold.bmp");
            
        } catch (Exception e) {
            Debug.err("Error in DetermineHotGoal initialize " + e.getMessage());
        }
    }
    
    protected void execute ( ) {
        
    }
    
    protected boolean isFinished ( ) {
        return true;
    }
    
    protected void end ( ) {
        
    }
    
    protected void interrupted ( ) {
        
    }
    
}

