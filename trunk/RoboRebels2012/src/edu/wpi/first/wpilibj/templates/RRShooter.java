/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 * The shooter class has the following components:
 * 
 * - Shooting wheel
 * - Getting/setting shooting wheel speed
 * - Tilting mechanism
 * - Lazy Susan
 * 
 * Currently, all manual control has been developed for
 * controlling the various parts of the shooter module.  
 * 
 * TODO:
 * 
 * - Test!
 * - Implement accelerometer code
 * - Extend public methods for autonomous control
 * 
 * @author dmw
 */
public class RRShooter 
{
    private final double        MAX_SHOOTING_SPEED = 1.0;
    private final double        LS_SPEED = 0.2;
    private final double        TILT_SPEED = 0.4;
    
    private     int             swj_channel;        // Shooter Wheel Jaguar channel
    private     int             lsv_channel;        // Lazy Susan Victor channel
    private     int             tltv_channel;       // Tilter Victor channel
    private     int             tltls_channel;      // Tilter Limit Swith channel
    private     int             os_top_channel;     // Top Optical Sensor channel
    private     int             os_bottom_channel;  // Bottom Optical Sensor channel

    private     double          shootingWheelSpeed = 0.0;
    private     double          lazySusanSpeed = 0.0;
    private     double          tiltSpeed = 0.0;
    
    private     boolean         shootingWheelState;
    
    private     boolean         shootingButtonPressed;
            
    private     Jaguar          shootingWheelJaguar;
    private     Victor          tiltVictor;
    private     Victor          lsVictor;
    private     DigitalInput    tiltLimitSwitch;
    private     DigitalInput    topOpticalSensor;
    private     DigitalInput    bottomOpticalSensor;
    
    private     Joystick        shootingJoystick;
    
    private     RRTracker       tracker;
    private     RRBallSensor    sensor;
    
    private     boolean         tracking = false; // Indicates if robot is tracking target
    //private     boolean         elevation_tracking = false;  // I don't think we need separate tracking
                                                               // for azimuth and elevation.
    
    //int target_direction = 1;
    
    /**
     * Sets up the speed controllers for the shooter
     * @param swjc Shooting Wheel Jaguar Channel
     * @param lsvc Lazy Susan Victor Channel
     * @param tltvc Tilter Victor Channel 
     * @param tltlsc Tilter Limit Switch Channel
     * @param js Joystick to monitor for button/axis events
     */
    public RRShooter(int  swjc, int lsvc, int tltvc, int tltlsc, RRTracker t, RRBallSensor ballSensor)
    {
        swj_channel = swjc;
        lsv_channel = lsvc;
        tltv_channel = tltvc;
        tltls_channel = tltlsc;
        
        sensor = ballSensor;
        
        shootingWheelState = false;         // start with the shooting wheel off!
        
        shootingButtonPressed = false;      // indicates if the 
        
        
        
        if ( t != null )
            tracker = t;
        else
            throw new NullPointerException("RRShooter was passed a null RRTracker object!");
        
        shootingWheelJaguar = new Jaguar(swj_channel);
        tiltVictor = new Victor(tltv_channel);
        lsVictor = new Victor(lsv_channel);
        //tiltLimitSwitch = new DigitalInput(tltls_channel);
    }
    
    
    /**
     * 
     * @param distance distance is distance to basket in feet
     * @param targetID targetID indicates if lower (0), middle (1), or upper (2) is the target
     * @return Returns the angle for which to move the shooter at
     */
    
    static double determineAngle(double distance, double muzzleVelocity, int targetID)
    {            
//        double muzzleVelocity = 8.1; //meters per second
//      double muzzleVelocity = 7.1; //meters per second
   
        double gravity = 9.81;  //meters per (second)^2
        double yLower = .466953;
        double yMiddle = 1.30515;
        double yHigher = 2.24485;
        double xDistance = 3.6576; // distance to base of basket (as if shooting from key)
        double shooterHeight = .914; // meters off the ground
        double y = yHigher; // determined by targetID (see below)
        double theta = 0;
                
        xDistance = distance / 3.28; // converts feet into meters
        
        if (targetID == RoboRebels.LOWEST)
            y = yLower; 
        else if (targetID == RoboRebels.MIDDLE)
             y = yMiddle;
        else if ( targetID == RoboRebels.HIGHEST)
             y = yHigher;
                 
        System.out.println("d: " + distance + "v: " + muzzleVelocity + "y: " + y + "x: " + xDistance);

        double tempSqrtEquation = (muzzleVelocity*muzzleVelocity*muzzleVelocity*muzzleVelocity)-
                         (2*gravity*muzzleVelocity*muzzleVelocity*y)-(gravity*gravity*xDistance*xDistance);
        
        if (tempSqrtEquation > 0)
        {
       //     System.out.println("tempSqrtEq: " + tempSqrtEquation);
            theta = MathUtils.atan(((muzzleVelocity*muzzleVelocity)+(Math.sqrt(tempSqrtEquation)))/(gravity*xDistance));
        }
        else
            theta = 0;    // There is no angle for this muzzle velocity
        
        theta = theta * ( 180.0 / 3.14159265); // converts radians to degreese

        return theta;
    }
    
    
    /**
     * Gathers input states and sets up the necessary motor speeds
     */
    private void gatherInputStates()
    {
        RRAction aoS = RRButtonMap.getActionObject(RRButtonMap.SHOOTER_ENABLED);
        boolean  shooterButtonState = aoS.getButtonState();
        
        RoboRebels.printLCD(3, "SS: " + shootingWheelJaguar.get());
        //RoboRebels.printLCD(4, "Z:" + this.getTransformedZValue());
        System.out.println("Shooting Speed: " + shootingWheelJaguar.get());
        System.out.println("Z: " + this.getTransformedZValue());
        //System.out.println("Limit Switch: " + tiltLimitSwitch.get());
        
        
        // Spin up if trigger is pressed (button 1)
        //if ( shootingJoystick.getRawButton(RRButtonMap.SHOOT) && !shootingButtonPressed )
        if ( shooterButtonState && !shootingButtonPressed )
        {
            System.out.println("Trigger");
            shootingWheelState = !shootingWheelState;
        }
        //else if ( !shootingJoystick.getRawButton(RRButtonMap.SHOOT) )
        else if ( !shooterButtonState )
        {
            shootingButtonPressed = false;
        }
        
        if ( shootingWheelState ) {
            shootingWheelSpeed = this.getTransformedZValue();
        } else {
            shootingWheelSpeed = 0.0;
        }
        
        
        // Check for tilting button up, down (button 6, 7)
        if ( shootingJoystick.getRawButton(RRButtonMap.TILT_UP) )
        {
            System.out.println("Tilt up");
            
            tiltSpeed = -1.0 * TILT_SPEED;
            
        }
        else if ( shootingJoystick.getRawButton(RRButtonMap.TILT_DOWN) )
        {
            if (RoboRebels.tilt_angle > RoboRebels.MIN_TILT_ANGLE)   // Check to make sure accel tilt angle isn't too low
            {        
                System.out.println("Tilt down");

                /*if (!tiltLimitSwitch.get())
                {
                    tiltSpeed = TILT_SPEED;
                } else {
                    System.out.println("Tilter limit switch pressed!");
                    tiltSpeed = 0.0;
                }*/
                tiltSpeed = TILT_SPEED;
            }
            else
            {
                System.out.println("Min tilt angle exceeded!");
                tiltSpeed = 0.0;              
            }
        }
        else if ( !shootingJoystick.getRawButton(RRButtonMap.TILT_UP) && !shootingJoystick.getRawButton(RRButtonMap.TILT_UP) )
        {
            tiltSpeed = 0.0;
        }
        
        // Check for lazy susan button left, right (button 4, 5)
        if ( shootingJoystick.getRawButton(RRButtonMap.LAZY_SUSAN_LEFT) )
        {
            System.out.println("Lazy susan left"); //TODO: should this be lazy susan left or right
            lazySusanSpeed = 1.0 * LS_SPEED;
        }
        else if ( shootingJoystick.getRawButton(RRButtonMap.LAZY_SUSAN_RIGHT) )
        {
            System.out.println("Lazy susan left"); //TODO: should this be lazy susan left or right
            lazySusanSpeed = -1.0 * LS_SPEED;
        }
        else if ( !shootingJoystick.getRawButton(RRButtonMap.LAZY_SUSAN_LEFT) && !shootingJoystick.getRawButton(RRButtonMap.LAZY_SUSAN_RIGHT) )
        {
            lazySusanSpeed = 0.0;
        }
        
     
        
        if (shootingJoystick.getRawButton(RRButtonMap.TRACK_TARGET))  // Target Tracking when joystick button 11 is pressed and held 
        {
            System.out.println("Track Target Button Pressed");
                        
            if (RoboRebels.target_azimuth == RoboRebels.LEFT)       // Left slowly
            {
                System.out.println("Auto Lazy susan left"); 
                lazySusanSpeed = -0.15;  // was -0.75 * LS_SPEED
                tracking = true;
                RoboRebels.azimuth_lock = false;         // No azimuth target lock
            }
             if (RoboRebels.target_azimuth == RoboRebels.FAR_LEFT)   // Left fast
            {
                System.out.println("Auto Lazy susan left fast"); 
                lazySusanSpeed = -0.3;  
                tracking = true;
                RoboRebels.azimuth_lock = false;         // No azimuth target lock
            }
            else if (RoboRebels.target_azimuth == RoboRebels.RIGHT)     // Right slowly
            {
                System.out.println("Auto Lazy susan right"); 
                lazySusanSpeed = 0.15;
                tracking = true;
                RoboRebels.azimuth_lock = false;         // No azimuth target lock
            }
           else if (RoboRebels.target_azimuth == RoboRebels.FAR_RIGHT)  // Right fast
            {
                System.out.println("Auto Lazy susan right fast"); 
                lazySusanSpeed = 0.30;
                tracking = true;
                RoboRebels.azimuth_lock = false;         // No azimuth target lock
            }
            else if (RoboRebels.target_azimuth == RoboRebels.LOCK)
            {
                System.out.println("Auto Lazy susan Lock"); 
                lazySusanSpeed = 0.0;
                RoboRebels.azimuth_lock = true;         // Indicate azimuth target lock
                tracking = true;
            } 
            else              // Must be set to HOLD
            {
                System.out.println("Auto Lazy susan Hold"); 
                lazySusanSpeed = 0.0;
                RoboRebels.azimuth_lock = false;         // No azimuth target lock
                tracking = true;
            }
             
            System.out.println("Tilt value:" + RoboRebels.target_elevation); 
           
            if (RoboRebels.target_elevation == RoboRebels.UP)  // Track target elevation (up/down)
            {
                System.out.println("Auto Tilt Up"); 
                tiltSpeed = -1.0 * TILT_SPEED * 0.5;
                RoboRebels.elevation_lock = false;         // No elevation target lock
            }
            else if (RoboRebels.target_elevation == RoboRebels.FAR_UP)  // Track target elevation (up/down)
            {
                System.out.println("Auto Tilt Far Up"); 
                tiltSpeed = -1.0 * TILT_SPEED * 0.75;
                RoboRebels.elevation_lock = false;         // No elevation target lock
            }
            else if (RoboRebels.target_elevation == RoboRebels.DOWN)
            {
                System.out.println("Auto Tilt Down"); 
                tiltSpeed = 1.0 * TILT_SPEED * 0.5;
                RoboRebels.elevation_lock = false;         // No elevation target lock
           }
            else if (RoboRebels.target_elevation == RoboRebels.FAR_DOWN)
            {
                System.out.println("Auto Tilt Far Down"); 
                tiltSpeed = 1.0 * TILT_SPEED * 0.75;
                RoboRebels.elevation_lock = false;         // No elevation target lock
           }
            else if (RoboRebels.target_elevation == RoboRebels.LOCK)
            {
                tiltSpeed = 0.0;                      // Stop Tilting
                RoboRebels.elevation_lock = true;     // Indicate elevation target lock
            }
            else             // is HOLD
            {
               tiltSpeed = 0.0;
               RoboRebels.elevation_lock = false;         // No elevation target lock
            }
            
            if (RoboRebels.target_muzzle_velocity == RoboRebels.FASTER)
            {
                System.out.println("Auto Shooting Speed Up"); 
               // Don't know how to do this
                RoboRebels.muzzle_velocity_lock = false;         // No muzzle velocity target lock
            }
            else if (RoboRebels.target_muzzle_velocity == RoboRebels.SLOWER)
            {
                System.out.println("Auto Shooting Speed Down"); 
               // Don't know how to do this
                RoboRebels.muzzle_velocity_lock = false;         // No muzzle velocity target lock
            }
            else if (RoboRebels.target_muzzle_velocity == RoboRebels.LOCK)
            {
                // Muzzle velocity is fine - don't change
                RoboRebels.muzzle_velocity_lock = true;     // muzzle velocity target lock
            }
            else
            {
                  RoboRebels.muzzle_velocity_lock = false;         // No muzzle velocity target lock
            }
        
        } else if (tracking)   // Was tracking target, but now no longer tracking
        {
             lazySusanSpeed = 0.0;      // Stop azimuth tracking
             tiltSpeed = 0.0;           // Stop elevation tracking
                                        // Stop shooter speed tracking
             tracking = false;
             RoboRebels.target_azimuth = RoboRebels.HOLD;   //  Put hold on tracking until tracking is re-initiated
             RoboRebels.target_elevation = RoboRebels.HOLD;
             RoboRebels.target_muzzle_velocity = RoboRebels.HOLD;
        }
        
        System.out.println("Target_Azimuth:" + RoboRebels.target_azimuth + " Targeting: " +
                tracking + " lazySusanSpeed: " + lazySusanSpeed + " tiltSpeed: " + tiltSpeed);

       if (RoboRebels.azimuth_lock && RoboRebels.elevation_lock && RoboRebels.muzzle_velocity_lock)
       {
            RoboRebels.printLCD(6, "All Locked!                ");  
            shootBall();
       }
       else if (RoboRebels.azimuth_lock && RoboRebels.muzzle_velocity_lock)
            RoboRebels.printLCD(6, "Azimuth & Speed Locked!    "); 
       else if (RoboRebels.azimuth_lock && RoboRebels.elevation_lock)
            RoboRebels.printLCD(6, "Azimuth & Elevation Locked!"); 
       else if (RoboRebels.elevation_lock && RoboRebels.muzzle_velocity_lock)
            RoboRebels.printLCD(6, "Elevation & Speed Locked!"); 
       else if (tracking) 
            RoboRebels.printLCD(6, "Tracking Target            "); 
       else 
            RoboRebels.printLCD(6, "                           ");   // Clear the display
    }
    
   
    
    /**
     * This is the general shoot periodic method for shooting functions.
     */
    
    public void shoot()
    {
        // Process input from joystick and other inputs
        gatherInputStates();
        
        // Process shooter states
        setShooterSpeeds();
        
        // track target
        //tracker.trackTarget();    // trackTarget was getting called twice
    }
    
    
    /**
     * This private method sets the determined speeds for the various
     * mechanisms used by the shooter module.  
     */
    
    private void setShooterSpeeds()
    {
        shootingWheelJaguar.set(-1.0 * shootingWheelSpeed);
        lsVictor.set(lazySusanSpeed);
        System.out.println("s: " + tiltSpeed);
        tiltVictor.set(tiltSpeed);
    }
    
    /**
     * This method gets the value of the Z dial on a joystick and
     * transforms it to fit within the speed range of the shooter.
     * 
     * @return transformed speed from the corresponding Z value
     */
    
    private double getTransformedZValue()
    {
        RRAction        aoS = RRButtonMap.getActionObject(RRButtonMap.SHOOTER_ENABLED);
        return MAX_SHOOTING_SPEED * (aoS.js.getZ() + 1.0) / 2.0;
    }
    
        /**
     * This public immediately stops the lazySusan to prevent overshoot
     * mechanisms used by the tracker module  
     */
    
    public void stopLazySusan()
    {
        lazySusanSpeed = 0.0;
        lsVictor.set(0.0);
        System.out.println("Halting LazySusan!");
    }
    
    public boolean shootBall()
    {
        System.out.println("Ball Sensor: " + sensor.getShootSensor());
        
        return (true);  // ball shot!
    }
}