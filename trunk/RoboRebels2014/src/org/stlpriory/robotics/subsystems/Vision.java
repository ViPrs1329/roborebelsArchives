/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import org.stlpriory.robotics.misc.Debug;

/**
 */
public class Vision extends Subsystem {
    
    // will be null if not known, else will be a double
    // in the range of -1 to 1 with -1=far left of image,
    // 0=center of the image, and 1=far right of image
    private Double hotGoalRelativeX;
    
    public Vision () {
        super("Vision");
        Debug.println("Vision subsystem constructor");
    }

    protected void initDefaultCommand() {
        Debug.println("Vision initDefaultCommand called");
    }
    
    /**
     * 
     * @return null if the hot goal relative X value is not known
     * else a double in the range of -1 to 1
     */
    public synchronized Double getHotGoalRelativeX ( ) {
        return hotGoalRelativeX;
    }
    
    public synchronized void setHotGoalRelativeX ( Double value ) {
        hotGoalRelativeX = value;
    }
    
}
