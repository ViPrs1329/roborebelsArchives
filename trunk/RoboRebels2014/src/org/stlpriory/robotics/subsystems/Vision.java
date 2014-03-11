/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.stlpriory.robotics.misc.Debug;

/**
 */
public class Vision extends Subsystem {
    
    private static Vision instance;
    
    public static Vision getInstance() {
        if ( instance == null ) {
            synchronized(Vision.class) {
                // double check that instance is still null inside synchronized block
                if ( instance == null ) {
                    instance = new Vision();
                }
            }
        }
        return instance;
    }
    
    // will be null if not known, else will be a double
    // in the range of -1 to 1 with -1=far left of image,
    // 0=center of the image, and 1=far right of image
    private Double hotGoalNormalizedX;
    
    private Vision () {
        super("Vision");
        Debug.println("Vision subsystem constructor");
        initializeVisionSubsystem();
    }

    protected void initDefaultCommand() {
        Debug.println("Vision initDefaultCommand called");
    }
    
    /**
     * 
     * @return null if the hot goal normalized X value is not known
     * else a double in the range of -1 to 1.  Note that this is
     * the normalized X value of the center of the horizontal
     * retroreflective tape above the low goal and is not the center
     * of the high or low goals themselves.
     */
    public synchronized Double getHotGoalNormalizedX ( ) {
        return hotGoalNormalizedX;
    }
    
    public synchronized void setHotGoalNormalizedX ( Double value ) {
        hotGoalNormalizedX = value;
    }
    
    private void initializeVisionSubsystem ( ) {
        Debug.println("Initializing AxisCamera singleton");
        // it is necessary to ask for the instance of the AxisCamera singleton
        // instance in robotInit so that during autonoumous the getImage()
        // will actually return an image instead of throwing a "no image available"
        // exception.  This method will be called during robotInit because
        // the CommandBase init static method is called, which loads the
        // CommandBase class which has a static reference to the Vision singleton
        // object
        AxisCamera.getInstance();
        Debug.println("AxisCamera singleton initialized");
    }
    
}
