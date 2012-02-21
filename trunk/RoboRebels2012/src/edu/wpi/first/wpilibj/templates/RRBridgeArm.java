/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * This class has the following components:
 * 
 * - Bridge arm
 * 
 * TODO:  
 * 
 * 
 * 
 * @author dmw
 */
public class RRBridgeArm 
{
    private final double    ARM_SPEED = 0.5;
    
    private     int         bav_channel;
    
    private     double      armSpeed = 0.0;
    
    private     Victor      bridgeArmVictor;
    private     Joystick    js;
    
    /**
     * Set's up BridgeArm victor
     * 
     * @param bavc The Victor controlling the BridgeArm
     */
    public RRBridgeArm(int bavc, Joystick js)
    {
        bav_channel = bavc;
        
        if ( js != null )
            this.js = js;
        else
        {
            throw new NullPointerException("RRBridgeArm was passed a null Joystick object (js)! ");
        }
        
        bridgeArmVictor = new Victor(bav_channel);
    }
    
    
    /**
     * Joystick Buttons/axis	Action	                This button layout is if the auto ball sensing is not functional
	
8	                Bridge arm down	
9	                Bridge arm up	
     */
    
    private void gatherInputStates()
    {
        // Get Bridge Arm button states
        if ( js.getRawButton(8) )
        {
            armSpeed = ARM_SPEED;
        }
        else if ( js.getRawButton(9) )
        {
            armSpeed = -1.0 * ARM_SPEED;
        }
        else if ( !js.getRawButton(8) && !js.getRawButton(9) )
        {
            armSpeed = 0.0;
        }
    }
    
    
    public void arm()
    {
        gatherInputStates();
        
        setBridgeArmSpeeds();
    }
    
    
    private void setBridgeArmSpeeds()
    {
        bridgeArmVictor.set(armSpeed);
    }
    
    /**
     * Lowers the BridgeArm
     */
    public void LowerBridgeArm()
    {
        System.out.println("LowerBridgeArm has not been implemented yet!");
    }
    
    /**
     * Raises the BridgeArm
     */
    public void RaiseBridgeArm()
    {
        System.out.println("RaiseBridgeArm has not been implemented yet!");
    }
}
