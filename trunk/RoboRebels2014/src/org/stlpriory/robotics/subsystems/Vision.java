/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import org.stlpriory.robotics.commands.vision.DetermineHotGoal;
import org.stlpriory.robotics.misc.Debug;

/**
 * Stubbed in subsystem for now
 * @author randytielking
 */
public class Vision extends Subsystem {
    
    private DetermineHotGoal determineHotGoal;
    
    public Vision () {
        super("Vision");
        Debug.println("Vision subsystem constructor");
    }

    protected void initDefaultCommand() {
        Debug.println("[Vision.initDefaultCommand()] Setting default command to " + DetermineHotGoal.class.getName());
        determineHotGoal = new DetermineHotGoal();
        setDefaultCommand(determineHotGoal);
    }
    
}
