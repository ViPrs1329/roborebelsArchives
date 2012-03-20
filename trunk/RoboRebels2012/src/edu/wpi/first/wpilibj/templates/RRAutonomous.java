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
    private     RRDIPSwitch     dipSwitch;
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
        
        RoboRebels.autonomous_mode_tracking = true;
    }
    
    void auton_periodic()  // called repeatedly suring Autonomous period
    {
                         
        System.out.println("Periodic State: " + RoboRebels.azimuth_lock + " " + RoboRebels.elevation_lock + " " + 
                RoboRebels.muzzle_velocity_lock + " " + RoboRebels.autonomous_mode_tracking + " " + RoboRebels.isShooting + " " +  
                RoboRebels.isFinishedShooting + " " +  RoboRebels.shot_first_ball + " " +  RoboRebels.delay_between_balls + " " + 
                RoboRebels.shot_second_ball + " " +  RoboRebels.delay_after_two_balls + " " + RoboRebels.driving_to_bridge + " " +
                RoboRebels.autonomous_complete
                );
        
        System.out.println("DIP Switch State: " + dipSwitch.getState(0) + " " +  dipSwitch.getState(1) + " " + 
                dipSwitch.getState(2) + " " +  dipSwitch.getState(3));
       
        //lock onto correct target
        
        tracker.trackTarget(target_selected);
        shooter.shoot();
        gatherer.gather();
        
        if (RoboRebels.autonomous_tracking_failed)  // If tracking failed, end shooting
            RoboRebels.autonomous_complete = true;   // TODO: Make robot still drive towards bridge to get balls
        
        //check to see if target locked
        
       if (shooter.locked() && RoboRebels.no_balls_shot && !RoboRebels.shot_first_ball &&
               !RoboRebels.isShooting && !RoboRebels.driving_to_bridge && !RoboRebels.autonomous_complete)
       {
           shooter.auton_shoot();
           shooter.shootBall();
           RoboRebels.no_balls_shot = false;
           
           System.out.println("Auton Starting shooting first ball now");
       }
        
       else if (!RoboRebels.no_balls_shot && RoboRebels.isFinishedShooting && !RoboRebels.shot_first_ball && !RoboRebels.autonomous_complete)
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
        
       else if (RoboRebels.delay_between_balls && !RoboRebels.autonomous_complete)
       {
       
        // wait for ball to shoot and score with no tracking on
            System.out.println("Auton Delaying between balls");
                        
            double time_current = Timer.getFPGATimestamp();
            
            if (time_current > (RoboRebels.time_started_waiting + RoboRebels.DELAY_BETWEEN_SHOTS))
            {
                RoboRebels.delay_between_balls = false;  // Done waiting              
                System.out.println("Auton Finished delaying between balls");
                RoboRebels.autonomous_mode_tracking = true;                     // Start tracking again
            }       
       }
       
        //shoot a second time
       else if (shooter.locked() && RoboRebels.shot_first_ball && !RoboRebels.second_ball_started_shoot &&
               !RoboRebels.shot_second_ball && !RoboRebels.delay_between_balls
               && !RoboRebels.isShooting && !RoboRebels.autonomous_complete)
       {
           shooter.auton_shoot();
           shooter.shootBall();
           RoboRebels.second_ball_started_shoot = true;
           
           System.out.println("Auton Starting shooting second ball now");
       }
    
       else if (RoboRebels.isFinishedShooting && RoboRebels.shot_first_ball && RoboRebels.second_ball_started_shoot
               && !RoboRebels.shot_second_ball && !RoboRebels.autonomous_complete)
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
        
       else if (RoboRebels.delay_after_two_balls && !RoboRebels.autonomous_complete)
       {
       
        // wait for ball to score while re-calculating trajectory
            System.out.println("Auton Delaying after second ball");
           
            double time_current = Timer.getFPGATimestamp();
            
            if (time_current > (RoboRebels.time_started_waiting + RoboRebels.DELAY_BETWEEN_SHOTS))
            {
                System.out.println("Auton Delaying after second ball is now Complete");
           
                RoboRebels.delay_after_two_balls = false;  // Done waiting
                RoboRebels.driving_to_bridge = true;
                
                RoboRebels.time_started_driving = Timer.getFPGATimestamp();  // Now start driving timer
                
                // Drive, robot, drive!!
             }
       }
       else if (RoboRebels.driving_to_bridge && !RoboRebels.autonomous_complete)
       {
       
        // drive towards bridge.
           
            System.out.println("Driving to bridge...");
           
            double time_current = Timer.getFPGATimestamp();
            
            if (time_current > (RoboRebels.time_started_driving + RoboRebels.DRIVE_TIME_TO_BRIDGE))
            {
                System.out.println("Driving to bridge is complete - stopping");
           
                RoboRebels.driving_to_bridge = false; 
               
                // Stop driving!!
                
                System.out.println("Auton now complete!");
                RoboRebels.autonomous_complete = true;
             }     
       }
        // drive backwards about 5-10 feet (to be closer than other robots to the balls on the bridge)
        
        //EXPLODE
    
        // Pwn other robots
        
        
    }
    
}

