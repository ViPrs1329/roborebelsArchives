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
public class RRAutonSimple implements RRAuton
{
    // Objects used to represent physical objects/sensors 
    // on the robot
    private     RRMecanumDrive      m_drive;
    private     Timer               m_timer;
    
    // Used to hold the state of the autonomous logic.  Uses
    // the constants below
    private     int                 DriveState;
    
    // Used to contain the start of a drive command based on time
    private     double              m_startDriveTime = 0;
    
    // Constants used in the autonomous' logic
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
     * Initializes necessary objects.
     * 
     * NOTE: This may be modified!
     * 
     */
    public void init()
    {
        // start timer
        m_timer.start();
        
        // reset drive state
        DriveState = START;
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
     * 
     * NOTE: The general skeleton of this method should not be 
     * modified, however, please, fill in logic calls here!
     * 
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

                collectDriveStartTime();
                DriveState = STEP_2;
                break;

            case STEP_2:

                if ( driveFor(0.25, true, 1.5) == true )
                {
                    m_drive.stop();
                    DriveState = STEP_3;
                }
                break;

            case STEP_3:
                
                collectDriveStartTime();
                DriveState = STEP_4;
                break;
                
            case STEP_4:
                
                if ( rotateFor(0.25, true, 3.0) == true )
                {
                    m_drive.stop();
                    DriveState = STEP_5;
                }
                break;
                
            case STEP_5:
                
                collectDriveStartTime();
                DriveState = STEP_6;
                break;
             
            case STEP_6:
                
                
                if ( driveFor(0.25, false, 1.5) == true )
                {
                    m_drive.stop();
                    DriveState = STOP;
                }
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
     * Drives the robot in a certain speed, in the direction
     * that is specified, and counting
     * from the start time for the passed duration.  Returns true
     * when the criteria is met, false otherwise.  Make sure you call
     * collectDriveStartTime method before you call this method!!!
     * 
     * NOTE: This should be modified!
     * 
     * @param speed What speed do you want to drive at (0.0 - 1.0)
     * @param forward True = forward, False = backward
     * @param duration How long in seconds do you want the robot to drive for?
     * @return True = criteria met, False = criteria not met
     */
    public boolean driveFor( double speed, boolean forward, double duration )
    {
        double  currentTime = m_timer.get();
        
        int     reverseModifier;
        
        if ( forward == true )
        {
            reverseModifier = 1;
        }
        else
        {
            reverseModifier = -1;
        }

        if ( currentTime - m_startDriveTime < duration )
        {
            m_drive.drive(0.0, reverseModifier * speed, 0.0);
            
            return false;
        }
        else
            return true;
    }
    
    /**
     * Rotates the robot in the specified direction at the specified
     * speed for the specified duration.  Returns true when the 
     * criteria is met, false otherwise.  Make sure you call 
     * collectDriveStartTime before you start making calls to this method!!!
     * 
     * NOTE: This should be modified!
     * 
     * @param rotateSpeed What speed do you want the robot to spin at.  0.0 - 1.0
     * @param clockwise True = clockwise, False = counter-clockwise
     * @param duration Duration of rotation in seconds
     * @return True = criteria met, False = criteria not met
     */
    public boolean rotateFor( double rotateSpeed, boolean clockwise, double duration )
    {
        double  currentTime = m_timer.get();

        if ( currentTime - m_startDriveTime < duration )
        {
            if ( clockwise )
                m_drive.drive(0.0, 0.0, rotateSpeed);
            else
                m_drive.drive(0.0, 0.0, -1.0 * rotateSpeed);
            return false;
        }
        else
            return true;
    }
    
    /**
     * Collects the current time into the variable m_startDriveTime.  Call this
     * before you use the driveFor method!
     * 
     * NOTE: This should be modified!
     * 
     */
    public void collectDriveStartTime()
    {
        m_startDriveTime = m_timer.get();
    }
    
    
    // ====================================================
    // D O   N O T  E D I T  B E L O W  T H I S  L I N E
    // ====================================================
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * This simple RRAuton class only uses a passed in
     * RRMecanumDrive object.
     * 
     * NOTE: This should not be modified!
     * 
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
     * 
     * NOTE: This should not be modified!
     * 
     */
    private void Setup()
    {
        SetupTimer();
        reset();
    }
    
    /**
     * Creates a new Timer object for our class
     * 
     * NOTE: This should not be modified!
     * 
     */
    private void SetupTimer()
    {
        m_timer = new Timer();
    }
    
    /**
     * This method resets the state of the autonomous
     * class.  Sets the DriveState to START and resets 
     * the timer and startDriveTime variable.
     * 
     * NOTE:  This should not be modified!
     * 
     */
    public void reset()
    {
        // reset timer
        m_timer.reset();
        
        // reset drive variables
        m_startDriveTime = 0;
    }
    
    /**
     * Returns the current time of this object's Timer
     * @return 
     */
    public double getTime()
    {
        if ( m_timer != null )
            return m_timer.get();
        
        return -1.0;
    }
    
    /**
     * UNUSED!
     * @return 
     */
    public double getAngle()
    {
        return -1.0;
    }
    
    /**
     * UNUSED!
     * @return 
     */
    public int getCount()
    {
        return -1;
    }
    
}
