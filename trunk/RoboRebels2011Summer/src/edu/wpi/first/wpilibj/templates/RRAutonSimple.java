/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author Derek Ward
 */
public class RRAutonSimple 
{
    private     RRMecanumDrive      m_drive;
    private     Timer               m_timer;
    
    private     int                 DriveState;
    
    private     double              m_startDriveTime = 0;
    
    private     final int           START = 0,
                                    STOP = 1,
                                    STEP_1 = 2,
                                    STEP_2 = 3,
                                    STEP_3 = 4,
                                    STEP_4 = 5,
                                    STEP_5 = 6,
                                    STEP_6 = 7,
                                    STEP_7 = 8,
                                    STEP_8 = 9,
                                    STEP_9 = 10,
                                    STEP_10 = 11;
    
    /**
     * This simple Auton class only uses a passed in
     * RRMecanumDrive object.
     * @param d Previously created RRMecanumDrive object.
     */
    public RRAutonSimple(RRMecanumDrive d)
    {
        // Only collect the MecanumDrive object
        // if it's not null
        if ( d != null )
            m_drive = d;

        // Set up necessary objects and vars
        Setup();
    }
    
    /**
     * Calls other setup methods and reset()
     */
    private void Setup()
    {
        reset();
    }
    
    /**
     * Initializes necessary objects.
     */
    public void init()
    {
        // start timer
        m_timer.start();
    }
    
    /**
     * This method should be called continuously to properly
     * run the robot in autonomous code.  This is also where
     * autonomous logic should be placed into.
     *
     * You should keep the START and STOP case states, however,
     * to add useful logic make your states additional number
     * cases.  It is good practice to encapsulate your logic into
     * additional private methods (see driveFor or rotate methods).
     */
    public void run()
    {
        switch ( DriveState )
        {
            case START:
                
                reset();
                DriveState = STEP_1;
                
                break;
                
            case STEP_1:

                
                break;

            case STEP_2:

                
                break;

            case STEP_3:
                
                
                break;
                
            case STEP_4:
                
                
                break;
                
            case STEP_5:
                
                
                break;
             
            case STEP_6:
                
                
                break;
                
            case STEP_7:
                
                
                break;
                
            case STEP_8:
                
                
                break;
                
            case STEP_9:
                
                
                break;
                
            case STEP_10:
                
                
                break;
                
            case STOP:
                
                m_drive.stop();
                break;
                
            default:
                
                m_drive.stop();
                break;
        }
    }
    
    /**
     * This method resets the state of the autonomous
     * class.  Sets the DriveState to START and resets 
     * the timer and startDriveTime variable.
     */
    public void reset()
    {
        // reset timer
        m_timer.reset();
        
        // reset drive state
        DriveState = START;
        
        // reset drive variables
        m_startDriveTime = 0;
    }
}
