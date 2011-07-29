/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;

/**
 * 
 * NOTES:  
 * 
 *   - 350 ticks per yard
 *   - 117 ticks per ft
 *   - 10 ticks per inch  
 *   - These are estimates and are rounded up!
 *
 * @author Derek Ward
 */
public class RRAutonEncoder
{
    private     RRMecanumDrive      m_drive;
    private     Encoder             m_encoder;
    private     Timer               m_timer;
    
    private     int                 m_encoderChannel1,
                                    m_encoderChannel2;
    
    private     int                 DriveState;
    
    private     double              m_startDriveTime = 0;
    
    private     int                 m_startDriveCount = 0;
    
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
     * A constructor which takes a RRMecanumDrive object
     * and uses the default encoder channels of 1 and 2.
     * @param d The already created RRMecanumDrive object
     */
    public RRAutonEncoder(RRMecanumDrive d)
    {
        m_encoderChannel1 = 1;
        m_encoderChannel2 = 2;
        
        // Only collect the MecanumDrive object
        // if it's not null
        if ( d != null )
            m_drive = d;

        // Set up necessary objects and vars
        Setup();
    }
    
    /**
     * A constructor which takes a RRMecanumDrive object
     * and uses the default encoder channels that are passed
     * in.
     * @param d The already created RRMecanumDrive object
     * @param encChannel1 Channel 1 of encoder
     * @param encChannel2 Channel 2 of encoder
     */
    public RRAutonEncoder(RRMecanumDrive d, int encChannel1, int encChannel2)
    {
        m_encoderChannel1 = encChannel1;
        m_encoderChannel2 = encChannel2;
        
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
        SetupTimer();
        SetupEncoder();
        reset();
    }
    
    /**
     * Creates a new Timer object for our class
     */
    private void SetupTimer()
    {
        m_timer = new Timer();
    }
    
    /**
     * Creates a new Encoder object for our class
     */
    private void SetupEncoder()
    {
        m_encoder = new Encoder(m_encoderChannel1, m_encoderChannel2, false);
    }

    /**
     * Initializes necessary objects.
     */
    public void init()
    {
        // start encoder
        m_encoder.start();
        
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

                // collect the drive start time
                collectDriveStartCount();
                DriveState = STEP_2;
                break;

            case STEP_2:

                // drive forward for about 1 yard at 0.25 of full throttle
                if ( driveForByCount(0.25, 350, 5) == true )
                {
                    m_drive.stop();
                    DriveState = STEP_3;
                }
                break;

            case STEP_3:
                
                // reset encoder counts, collect encoder start
                reset();
                collectDriveStartCount();
                DriveState = STEP_4;
                break;
                
            case STEP_4:
                
                // drive in reverse for about 1 yard at 0.25 of full throttle
                if ( driveForByCount(-0.25, -350, 5) == true )
                {
                    m_drive.stop();
                    DriveState = STOP;
                }
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
     * class.  Sets the DriveState to START, resets
     * the encoder and resets the timer and startDriveTime
     * variable.
     */
    public void reset()
    {
        // reset encoder
        m_encoder.reset();
        
        // reset timer
        m_timer.reset();
        
        // reset drive state
        DriveState = START;
        
        // reset drive variables
        m_startDriveTime = 0;
        m_startDriveCount = 0;
    }
    
    
    public int getCount()
    {
        if ( m_encoder != null )
            return m_encoder.get();
        
        return -1;
    }
    
    
    public double getTime()
    {
        if ( m_timer != null )
            return m_timer.get();
        
        return -1.0;
    }
    
    
    /**
     * Drives the robot forward in a certain speed, and counting
     * from the start time for the passed duration.  Returns true
     * when the criteria is met, false otherwise.  Make sure you call
     * collectDriveStartTime method before you call this method!!!
     * @param forwardSpeed What speed do you want to drive at (0.0 - 1.0)
     * @param duration How long in seconds do you want the robot to drive for?
     * @return True = criteria met, False = criteria not met
     */
    public boolean driveFor( double forwardSpeed, double duration )
    {
        double  currentTime = m_timer.get();

        if ( currentTime - m_startDriveTime < duration )
        {
            m_drive.drive(0.0, forwardSpeed, 0.0);
            return false;
        }
        else
            return true;
    }
    
    
    public boolean driveForByCount( double forwardSpeed, int countEnd, int range )
    {
        int     currentCount = m_encoder.get();
        
        if ( currentCount <= (countEnd + range) && currentCount >= (countEnd - range) )
        {
            return true;
        }
        else
        {
            m_drive.drive(0.0, forwardSpeed, 0.0);
            return false;
        }
    }
    
    
    public void collectDriveStartCount()
    {
        m_startDriveCount = m_encoder.get();
    }
    
    
    
    /**
     * Collects the current time into the variable m_startDriveTime.  Call this
     * before you use the driveFor method!
     */
    public void collectDriveStartTime()
    {
        m_startDriveTime = m_timer.get();
    }
}
