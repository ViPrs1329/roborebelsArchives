/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author aidan/alan
 */
public class RRAutonomous {
    
    int                         target_selected;
    RRDIPSwitch                 dipSwitch;
    private     RRTracker       tracker;
    private     RRShooter       shooter;
    private     RRBallSensor    sensor;

    private     boolean         delay_shooting  = true;
    public  RRAutonomous(RRDIPSwitch ds, RRTracker tr, RRShooter sh, RRBallSensor sr)
    {
        dipSwitch = ds; 
        tracker = tr;
        shooter = sh;
        sensor = sr;
    }
    
    void auton_init()   // called once at start of Autonomous period
    {
        //check dip switches
        
        if (dipSwitch.getState(0))
        {
            target_selected = RoboRebels.HIGHEST_TARGET;   // Read first DIP Switch
            System.out.println("COOLIO! We're locked onto the top target! =)");
        }   
        
        else if (dipSwitch.getState(1) && dipSwitch.getState(2))
        {
            target_selected = RoboRebels.LOWEST_TARGET;
            System.out.println("We're locked onto the lowest target! =)");
        }
        
        else if (dipSwitch.getState(1))
        {
            target_selected = RoboRebels.LEFT_TARGET;
            System.out.println("COOLIO! We're locked onto the left target! =)");
        }
            
        else if (dipSwitch.getState(2))
        {
            target_selected = RoboRebels.RIGHT_TARGET;
            System.out.println("COOLIO! We're locked onto the right target! =)");
        }
            
        if (dipSwitch.getState(3))
        {
            System.out.println("OK! Let's wait for the other team to shoot first...)");
            delay_shooting = true;
        }   
        
        System.out.println("auton_init");
        
        RoboRebels.isFinishedShooting = false;
    }
    
    void auton_periodic()  // called repeatedly suring Autonomous period
    {
                         
        System.out.println("Auton Periodic!!");
        
        //lock onto correct target
        
        tracker.trackTarget(target_selected);
        
        //check to see if target locked
        
       if (RoboRebels.azimuth_lock && RoboRebels.elevation_lock && RoboRebels.muzzle_velocity_lock)
       {
           RoboRebels.isShooting = true;
           System.out.println("Shooting Now");

       }
        
       if (RoboRebels.isFinishedShooting == true);
       {
        /*
         *Need to delay so we don't shoot again immediately 
         */
           
           // shooter.isFinishedShooting = false;
           System.out.println("Shooting is now Complete");
        }
        
       
        // wait for ball to score while re-calculating trajectory
        
        //shoot a second time
    
    
        
        // drive backwards about 5-10 feet (to be closer than other robots to the balls on the bridge)
        
        //EXPLODE
    
        // Pwn other robots
        
        
    }
    
}

