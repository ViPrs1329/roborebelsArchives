/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.stlpriory.robotics.commands.vision;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.stlpriory.robotics.commands.CommandBase;

/**
 *
 * Stubbed in command for now
 * @author randytielking
 */
public class DetermineHotGoal extends CommandBase {
    
    public DetermineHotGoal ( ) {
        super("DetermineHotGoal");
        requires(vision);
        // uncomment this in order to test the command with the smart dashboard
        //SmartDashboard.putData(this);
    }
    
    protected void initialize ( ) {
        
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
