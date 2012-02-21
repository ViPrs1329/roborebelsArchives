/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author dmw
 */
public class RRBridgeArm 
{
    private     int         bav_channel;
    
    private     Victor      bridgeArmVictor;
    private     Joystick    js;
    
    /**
     * Set's up BridgeArm victor
     * 
     * @param bav The Victor controlling the BridgeArm
     */
    public RRBridgeArm(Joystick js, int bavc)
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
    
    public void arm()
    {
        
    }
    
    /**
     * Lowers the BridgeArm
     */
    public void LowerBridgeArm()
    {
        
    }
    
    /**
     * Raises the BridgeArm
     */
    public void RaiseBridgeArm()
    {
        
    }
}
