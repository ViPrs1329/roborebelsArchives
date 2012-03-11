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
    // Reduced the retraction (UP) speed of the arm by half
    private final double    ARM_DOWN_SPEED = -1.0;
    private final double    ARM_UP_SPEED = 0.5;
    
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
        if ( js.getRawButton(RRButtonMap.BRIDGE_ARM_DOWN) )
        {
            System.out.println("Bridge lower arm");
            armSpeed = ARM_DOWN_SPEED;
        }
        else if ( js.getRawButton(RRButtonMap.BRIDGE_ARM_UP) )
        {
            System.out.println("Bridge lower arm");
            armSpeed = ARM_UP_SPEED;
        }
        else if ( !js.getRawButton(RRButtonMap.BRIDGE_ARM_DOWN) && !js.getRawButton(RRButtonMap.BRIDGE_ARM_UP) )
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
        //System.out.println("armSpeed: " + armSpeed);
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
