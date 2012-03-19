/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Timer;

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
    private     RRGatherer      gatherer;

    private     boolean         delay_shooting  = true;
    public  RRAutonomous(RRDIPSwitch ds, RRTracker tr, RRShooter sh, RRBallSensor sr, RRGatherer gr)
    {
        dipSwitch = ds; 
        tracker = tr;
        shooter = sh;
        sensor = sr;
        gatherer = gr;
    }
    
    void auton_init()   // called once at start of Autonomous period
    {
        //check dip switches
        
        if (dipSwitch.getState(1))
        {
            target_selected = RoboRebels.HIGHEST_TARGET;   // Read first DIP Switch
            System.out.println("COOLIO! We're locked onto the top target! =)");
        }   
        
        else if (dipSwitch.getState(0) && dipSwitch.getState(3))
        {
            target_selected = RoboRebels.LOWEST_TARGET;
            System.out.println("We're locked onto the lowest target! =)");
        }
        
        else if (dipSwitch.getState(0))
        {
            target_selected = RoboRebels.LEFT_TARGET;
            System.out.println("COOLIO! We're locked onto the left target! =)");
        }
            
        else if (dipSwitch.getState(3))
        {
            target_selected = RoboRebels.RIGHT_TARGET;
            System.out.println("COOLIO! We're locked onto the right target! =)");
        }
            
        if (dipSwitch.getState(2))
        {
            System.out.println("OK! Let's wait for the other team to shoot first...)");
            delay_shooting = true;
        }   

    }
    
    void auton_periodic()  // called repeatedly suring Autonomous period
    {
                         
        System.out.println("Auton Periodic State: " + RoboRebels.azimuth_lock + " " + RoboRebels.elevation_lock + " " + 
                RoboRebels.muzzle_velocity_lock + " " +  RoboRebels.isShooting + " " +  
                RoboRebels.isFinishedShooting + " " +  RoboRebels.shot_first_ball + " " +  RoboRebels.delay_between_balls + " " + 
                RoboRebels.shot_second_ball + " " +  RoboRebels.delay_after_two_balls + " " + RoboRebels.driving_to_bridge
                );
        
        System.out.println("DIP Switch State: " + dipSwitch.getState(0) + " " +  dipSwitch.getState(1) + " " + 
                dipSwitch.getState(2) + " " +  dipSwitch.getState(3));
       
        //lock onto correct target
        
        tracker.trackTarget(target_selected);
        shooter.shoot();
        gatherer.gather();
        
        //check to see if target locked
        
       if ((RoboRebels.azimuth_lock && RoboRebels.elevation_lock && RoboRebels.muzzle_velocity_lock)
               && (!RoboRebels.shot_first_ball) && (!RoboRebels.isShooting))
       {
           shooter.shootBall();
           
           System.out.println("Auton Starting shooting first ball now");
       }
        
       if ((RoboRebels.isFinishedShooting) && (!RoboRebels.shot_first_ball))
       {
        /*
         *Need to delay so we don't shoot again immediately 
         */
           
           System.out.println("Auton Shooting first ball is now Complete!! " + RoboRebels.isFinishedShooting + " " + RoboRebels.shot_first_ball);
           RoboRebels.delay_between_balls = true;
           RoboRebels.isFinishedShooting = false;
           RoboRebels.shot_first_ball = true;
           RoboRebels.time_started_waiting = Timer.getFPGATimestamp();
        }
        
       if (RoboRebels.delay_between_balls)
       {
       
        // wait for ball to score while re-calculating trajectory
            System.out.println("Auton Delaying between balls");
            
            double time_current = Timer.getFPGATimestamp();
            
            if (time_current > (RoboRebels.time_started_waiting + RoboRebels.TICKS_FOR_3_SECONDS))
            {
                RoboRebels.delay_between_balls = false;  // Done waiting              
                System.out.println("Auton Finished delaying between balls");
            }       
       }
       
        //shoot a second time
       if ((RoboRebels.azimuth_lock && RoboRebels.elevation_lock && RoboRebels.muzzle_velocity_lock)
               && (RoboRebels.shot_first_ball) && (!RoboRebels.isShooting))
       {
           shooter.shootBall();
           
           System.out.println("Auton Starting shooting second ball now");
       }
    
       if ((RoboRebels.isFinishedShooting) && (RoboRebels.shot_first_ball))
       {
        /*
         *Need to delay so we don't shoot again immediately 
         */
           
           // shooter.isFinishedShooting = false;
           System.out.println("Auton Shooting second ball is now Complete");
           RoboRebels.delay_after_two_balls = true;
           RoboRebels.isFinishedShooting = false;
           RoboRebels.shot_second_ball = true;
           RoboRebels.time_started_waiting = Timer.getFPGATimestamp();
        }
        
       if (RoboRebels.delay_after_two_balls)
       {
       
        // wait for ball to score while re-calculating trajectory
            System.out.println("Auton Delaying after second ball");
           
            double time_current = Timer.getFPGATimestamp();
            
            if (time_current > (RoboRebels.time_started_waiting + RoboRebels.TICKS_FOR_3_SECONDS))
            {
                System.out.println("Auton Delaying after second ball is now Complete");
           
                RoboRebels.delay_after_two_balls = false;  // Done waiting
                
                RoboRebels.time_started_waiting = Timer.getFPGATimestamp();  // Now start driving timer
                
                // Drive, robot, drive!!
             }
       }
       if (RoboRebels.driving_to_bridge)
       {
       
        // drive towards bridge.
           
            System.out.println("Driving to bridge...");
           
            double time_current = Timer.getFPGATimestamp();
            
            if (time_current > (RoboRebels.time_started_waiting + RoboRebels.DRIVE_TIME_TO_BRIDGE))
            {
                System.out.println("Driving to bridge is complete - stopping");
           
                RoboRebels.driving_to_bridge = false; 
               
                // Stop driving!!
             }
 
           
       }
        // drive backwards about 5-10 feet (to be closer than other robots to the balls on the bridge)
        
        //EXPLODE
    
        // Pwn other robots
        
        
    }
    
}

