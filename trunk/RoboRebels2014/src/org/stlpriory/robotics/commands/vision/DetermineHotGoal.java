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
    
    private boolean isFinished = false;
    
    public DetermineHotGoal ( ) {
        super("DetermineHotGoal");
        requires(vision);
        Debug.println("DetermineHotGoal constructor called"); 
    }
    
    protected void initialize ( ) {
        
    }
    
    protected void execute ( ) {

        try {
            isFinished = false;
            ColorImage image;
            Debug.println("DetermineHotGoal execute start");
            image = new RGBImage("/center.jpg");
            BinaryImage thresholdImage = image.thresholdHSV(130, 170, 0, 255, 250, 255);  
            thresholdImage.write("/threshold.bmp");
            thresholdImage.free();
            image.free();
            Debug.println("DetermineHotGoal execute finished");
            isFinished = true;
        } catch (Exception e) {
            Debug.err("Error in DetermineHotGoal execute " + e.getMessage());
        }
    }
    
    protected boolean isFinished ( ) {
        return isFinished;
    }
    
    protected void end ( ) {
        
    }
    
    protected void interrupted ( ) {
        
    }
    
}

