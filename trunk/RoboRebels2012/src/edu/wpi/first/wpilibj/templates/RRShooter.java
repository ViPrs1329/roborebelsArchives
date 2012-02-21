/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Joystick;
import com.sun.squawk.util.MathUtils;

/**
 *
 * The shooter class has the following components:
 * 
 * - Shooting wheel
 * - Getting/setting shooting wheel speed
 * - Tilting mechanism
 * - Lazy Susan
 * 
 * @author dmw
 */
public class RRShooter 
{
    private final double        DEFAULT_SHOOTING_SPEED = 0.6;
    private final double        MAX_SHOOTING_SPEED = 0.8;
    private final double        LS_SPEED = 0.4;
    private final double        TILT_SPEED = 0.4;
    
    private     int             swj_channel;
    private     int             lwv_channel;
    private     int             spwv_channel;
    
    private     double          shootingWheelSpeed = DEFAULT_SHOOTING_SPEED;
    
    private     boolean         shootingWheelState;
    
    private     boolean         shootingButtonPressed;
            
    private     Jaguar          shootingWheelJaguar;
    private     Victor          loadingWheelVictor;
    private     Victor          spinnerWheelVictor;
    private     Joystick        shootingJoystick;
    
    
    /**
     * Sets up the speed controllers for the shooter
     * @param swjc Shooting Wheel Jaguar Channel
     * @param lwcc Loading Wheel Victor Channel
     * @param spwvc Spinner Wheel Victor Channel 
     * @param js Joystick to monitor for button/axis events
     */
    public RRShooter(int  swjc, int lwvc, int spwvc, Joystick js)
    {
        swj_channel = swjc;
        lwv_channel = lwvc;
        spwv_channel = spwvc;
        
        shootingWheelState = false;         // start with the shooting wheel off!
        
        shootingButtonPressed = false;      // indicates if the 
        
        if ( js != null )
            shootingJoystick = js;
        else
            throw new NullPointerException("RRShooter was passed a null Joystick object!");
        
        shootingWheelJaguar = new Jaguar(swj_channel);
        loadingWheelVictor = new Victor(lwv_channel);
        spinnerWheelVictor = new Victor(spwv_channel);
    }
    
    
    
    // distance is distance to basket in feet
    // targetID indicates if lower (0), middle (1), or upper (2) is the target
    static double determineAngle(double distance,int targetID) 
    {            
        double muzzleVelocity = 9; //meters per second

        double gravity = 9.81;  //meters per (second)^2
        double yLower = .466953;
        double yMiddle = 1.30515;
        double yHigher = 2.24485;
        double xDistance = 3.6576; // distance to base of basket (as if shooting from key)
        double shooterHeight = 1.016; // meters off the ground
        double y; // define by asking driver "top, middle, or bottom?" (BELOW)

        xDistance = distance /3.28; // converts feet into meters

        

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
        {
            y = yMiddle;
        }
        
        /*
                double tempSqrtEquationHigher = Math.sqrt((muzzleVelocity*muzzleVelocity)-(2*gravity*muzzleVelocity*muzzleVelocity*yHigher)-(gravity*gravity*xDistance));
                double tempSqrtEquationMiddle = Math.sqrt((muzzleVelocity*muzzleVelocity)-(2*gravity*muzzleVelocity*muzzleVelocity*yMiddle)-(gravity*gravity*xDistance));
                double tempSqrtEquationLower = Math.sqrt((muzzleVelocity*muzzleVelocity)-(2*gravity*muzzleVelocity*muzzleVelocity*yLower)-(gravity*gravity*xDistance));
        */

        double tempSqrtEquation = Math.sqrt((muzzleVelocity*muzzleVelocity*muzzleVelocity*muzzleVelocity)-
                                            (2*gravity*muzzleVelocity*muzzleVelocity*y)-(gravity*gravity*xDistance*xDistance));

        double theta = MathUtils.atan(((muzzleVelocity*muzzleVelocity)+(tempSqrtEquation))/(gravity*xDistance));

        theta = theta * ( 180 / 3.14159265); // converts radians to degreese

        return theta;
    }
    
   
    
    /*
     * General action method for this objec.
     * 
     * Joystick Buttons/axis	Action	                This button layout is if the auto ball sensing is not functional
        1	                Shoot on, off           ie. Manual control
        2                       Loader Up	
        3	                Loader Down	        Also, this is in right hand config mode
        4	                Lazy Susan left	
        5	                Lazy Susan right	
        6	                Tilt up	
        7	                Tilt down	
        8	                Bridge arm down	
        9	                Bridge arm up	
        10	                Spinner in	
        11	                Spinner out	
        Z	                Shooter speed control	
        Axis	                Drive (Arcade)	
     * 
     */
    
    public void shoot()
    {
        double tempZ;
        
        // Spin up if trigger is pressed (button 1)
        if ( shootingJoystick.getRawButton(1) && !shootingButtonPressed )
        {
            if ( shootingWheelState )
                shootingWheelState = false;
            else
                shootingWheelState = true;
        }
        else if ( !shootingJoystick.getRawButton(1) )
        {
            shootingButtonPressed = false;
        }
        
        // get Z value from joystick
        
        tempZ = shootingJoystick.getZ();

        // transform z posititon which is dependant on the
        // max speed of the shooter.  This will 

        shootingWheelSpeed = MAX_SHOOTING_SPEED * tempZ;
        
        // Check for tilting button left, right (button 6, 7)
        if ( )
    }
    
    
    private void processShootingStates()
    {
        
    }
}
