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
        isFinished = false;
        Debug.println("DetermineHotGoal execute start");
        ColorImage image = null;
        BinaryImage thresholdImage = null;
        try {
            image = new RGBImage("/center.jpg");
            thresholdImage = image.thresholdHSV(99, 163, 0, 255, 230, 255);  
            thresholdImage.write("/threshold.bmp");
        } catch (Exception e) {
            Debug.err("Error in DetermineHotGoal execute " + e.getMessage());
        } finally {
            if ( image != null ) {
                try {
                    image.free();
                } catch ( Exception e ) {
                    Debug.err("Exception while trying to free image " + e.getMessage());
                }
            }
            if ( thresholdImage != null ) {
                try {
                    thresholdImage.free();
                } catch ( Exception e ) {
                    Debug.err("Exception while trying to free threshold image " + e.getMessage());
                }
            }
            Debug.println("DetermineHotGoal execute finished");
            isFinished = true;
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

