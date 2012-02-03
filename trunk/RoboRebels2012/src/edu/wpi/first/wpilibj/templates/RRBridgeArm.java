/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author dmw
 */
public class RRBridgeArm 
{
    private     int         bav_channel;
    
    private     Victor      bridgeArmVictor;
    
    /**
     * Set's up BridgeArm victor
     * 
     * @param bav The Victor controlling the BridgeArm
     */
    public RRBridgeArm(int bavc)
    {
        bav_channel = bavc;
        
        bridgeArmVictor = new Victor(bav_channel);
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
