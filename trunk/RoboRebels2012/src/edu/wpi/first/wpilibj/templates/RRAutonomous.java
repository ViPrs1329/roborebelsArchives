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
    private     RRBallSensor    sensor;
    private     boolean         ball_present = false;
    private     boolean         delay_shooting  = true;
    
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
    }
    
    void auton_periodic()  // called repeatedly suring Autonomous period
    {
                         
        //lock onto correct target
        
        tracker.trackTarget(target_selected);
        
        //calculate trajectory
        
        /*
         * power shooting motor and move loader belts
         * check sensors 'is ball launched' 
         */
        
        System.out.println("Ball Sensor: " + sensor.getShootSensor());  // True if ball is there, false if no ball
        
        if (sensor.getShootSensor() == true)
        {
            ball_present = true; 
        }
        
        else if (sensor.getShootSensor() == false && ball_present == true )
            
        {
            // load motor stop
            
            ball_present = false;
           
        }
        
        }
        
        
        
        // wait for ball to score while re-calculating trajectory
        
        //shoot a second time
    
    
        
        // drive backwards about 5-10 feet (to be closer than other robots to the balls on the bridge)
        
        //EXPLODE
    
        // Pwn other robots
        
        
    }
    
    

