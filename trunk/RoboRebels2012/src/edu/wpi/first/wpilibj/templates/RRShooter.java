/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.AnalogChannel;
import com.sun.squawk.util.MathUtils;

/**
 *
 * @author dmw
 */
public class RRShooter 
{
    private     int         swj_channel;
    private     int         lwc_channel;
    private     int         ss_channel;
            
    private     Jaguar      shootingWheelJaguar;
    private     Victor      loadingWheelVictor;
    private     AnalogChannel   sonicSensor;
    
    
    /**
     * Sets up the speed controllers for the shooter
     * @param swjc
     * @param lwcc 
     */
    public RRShooter(int  swjc, int lwcc, int ssc)
    {
        swj_channel = swjc;
        lwc_channel = lwcc;
        ss_channel = ssc;
        
        shootingWheelJaguar = new Jaguar(swj_channel);
        loadingWheelVictor = new Victor(lwc_channel);
        sonicSensor = new AnalogChannel(ss_channel);
    }
    
    
    /**
     * Loads one ball into the shooter
     */
    public void loadBall()
    {
        
    }
    
    public void determineTrajectory() 
    {
     double muzzleVelocity = 9; //meters per second
     
     double gravity = 9.81;  //meters per (second)^2
     double yLower = .466953;
     double yMiddle = 1.30515;
     double yHigher = 2.24485;
     double xDistance = 3.6576; // distance to base of basket (as if shooting from key)
     double shooterHeight = 1.016; // meters off the ground
     double y = yMiddle; // define by asking driver "top, middle, or bottom?"
     /**
            double tempSqrtEquationHigher = Math.sqrt((muzzleVelocity*muzzleVelocity)-(2*gravity*muzzleVelocity*muzzleVelocity*yHigher)-(gravity*gravity*xDistance));
            double tempSqrtEquationMiddle = Math.sqrt((muzzleVelocity*muzzleVelocity)-(2*gravity*muzzleVelocity*muzzleVelocity*yMiddle)-(gravity*gravity*xDistance));
            double tempSqrtEquationLower = Math.sqrt((muzzleVelocity*muzzleVelocity)-(2*gravity*muzzleVelocity*muzzleVelocity*yLower)-(gravity*gravity*xDistance));
     **/
     
     double tempSqrtEquation = Math.sqrt((muzzleVelocity*muzzleVelocity)-(2*gravity*muzzleVelocity*muzzleVelocity*y)-(gravity*gravity*xDistance));
     
     double theta = MathUtils.atan(((muzzleVelocity*muzzleVelocity)+(tempSqrtEquation))/(gravity*xDistance));
     
   
    }
   
    /**
     * Shoots a ball
     */
    public void shootBall()
    {
        
    }
}
