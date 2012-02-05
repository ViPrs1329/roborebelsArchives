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
    
    static double determineAngle(double distance,int targetID) 
            
    // distance is distance to basket in feet
    // targetID indicates if lower (0), middle (1), or upper (2) is the target
            
    {
     double muzzleVelocity = 9; //meters per second
     
     double gravity = 9.81;  //meters per (second)^2
     double yLower = .466953;
     double yMiddle = 1.30515;
     double yHigher = 2.24485;
     double xDistance = 3.6576; // distance to base of basket (as if shooting from key)
     
     xDistance = distance /3.28; // converts feet into meters
     
     double shooterHeight = 1.016; // meters off the ground
     
     double y; // define by asking driver "top, middle, or bottom?" (BELOW)
     
     if (targetID == 0)
     {
         y = yLower; 
                 
         } 
     else if (targetID == 1)
         {
         y = yMiddle;
           }
     else if ( targetID == 2 )
         {
         y = yHigher;
        
        }
     else 
         y = yMiddle;
     /**
            double tempSqrtEquationHigher = Math.sqrt((muzzleVelocity*muzzleVelocity)-(2*gravity*muzzleVelocity*muzzleVelocity*yHigher)-(gravity*gravity*xDistance));
            double tempSqrtEquationMiddle = Math.sqrt((muzzleVelocity*muzzleVelocity)-(2*gravity*muzzleVelocity*muzzleVelocity*yMiddle)-(gravity*gravity*xDistance));
            double tempSqrtEquationLower = Math.sqrt((muzzleVelocity*muzzleVelocity)-(2*gravity*muzzleVelocity*muzzleVelocity*yLower)-(gravity*gravity*xDistance));
     **/
     
     double tempSqrtEquation = Math.sqrt((muzzleVelocity*muzzleVelocity*muzzleVelocity*muzzleVelocity)-(2*gravity*muzzleVelocity*muzzleVelocity*y)-(gravity*gravity*xDistance*xDistance));
     
     double theta = MathUtils.atan(((muzzleVelocity*muzzleVelocity)+(tempSqrtEquation))/(gravity*xDistance));
     
     theta = theta * ( 180 / 3.14159265); // converts radians to degreese
   
     return(theta);
    }
   
    /**
     * Shoots a ball
     */
    public void shootBall()
    {
        
    }
}
