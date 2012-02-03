/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author dmw
 */
public class RRShooter 
{
    private     int         swj_channel;
    private     int         lwc_channel;
    
    private     Jaguar      shootingWheelJaguar;
    private     Victor      loadingWheelVictor;
    
    
    /**
     * Sets up the speed controllers for the shooter
     * @param swjc
     * @param lwcc 
     */
    public RRShooter(int  swjc, int lwcc)
    {
        swj_channel = swjc;
        lwc_channel = lwcc;
        
        shootingWheelJaguar = new Jaguar(swj_channel);
        loadingWheelVictor = new Victor(lwc_channel);
    }
    
    
    /**
     * Loads one ball into the shooter
     */
    public void loadBall()
    {
        
    }
    
    
    /**
     * Shoots a ball
     */
    public void shootBall()
    {
        
    }
}
