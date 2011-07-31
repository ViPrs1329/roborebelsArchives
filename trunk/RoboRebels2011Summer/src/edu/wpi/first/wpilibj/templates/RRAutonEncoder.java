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
public class RRAutonEncoder implements RRAuton
{
    // Objects used to represent physical objects/sensors 
    // on the robot
    private     RRMecanumDrive      m_drive;
    private     Encoder             m_encoder;
    private     Timer               m_timer;
    
    private     int                 m_encoderChannel1,
                                    m_encoderChannel2;
    
    // Used to hold the state of the autonomous logic.  Uses
    // the constants below
    private     int                 DriveState;
    
    // Used to contain the start of a drive command based on time
    private     double              m_startDriveTime = 0;
    
    // Used to contain the start count of a drive command based on
    // encoder values
    private     int                 m_startDriveCount = 0;
    
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
     * NOTE: This may modified!
     */
    public void init()
    {
        // start encoder
        m_encoder.start();
        
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
                
                //System.out.println("START");
                reset();
                DriveState = STEP_1;
                
                break;
                
            case STEP_1:

                //System.out.println("Step1");
                // collect the drive start time
                collectDriveStartCount();
                DriveState = STEP_2;
                break;

            case STEP_2:

                //System.out.println("Step2");
                // drive forward for about 1 yard at 0.25 of full throttle
                if ( driveForByCount(0.25, true, 350) == true )
                {
                    m_drive.stop();
                    DriveState = STEP_3;
                }
                break;

            case STEP_3:
                
                //System.out.println("Step3");
                // reset encoder counts, collect encoder start
                reset();
                collectDriveStartCount();
                DriveState = STEP_4;
                break;
                
            case STEP_4:
                
                //System.out.println("Step4");
                // drive in reverse for about 1 yard at 0.25 of full throttle
                if ( driveForByCount(0.25, false, 350) == true )
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
                
                //System.out.println("STOP");
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

        if ( currentTime - m_startDriveTime < duration )
        {
            if ( forward == true )
                m_drive.drive(0.0, speed, 0.0);
            else
                m_drive.drive(0.0, -1.0 * speed, 0.0);
            return false;
        }
        else
            return true;
    }
    
    /**
     * Drives the robot forward in a certain speed, and counting from
     * the start encoder count, for the specified count.  
     * 
     * NOTE: This should be modified!
     * 
     * @param speed The speed in which you want to drive (0.0 - 1.0)
     * @param forward Which direction are we driving?
     * @param countEnd The ending count for when the robot should stop
     * @return 
     */
    public boolean driveForByCount( double speed, boolean forward, int countEnd )
    {
        int     currentCount = m_encoder.get();
        int     reverseModifier;
        
        if ( forward == true )
        {
            reverseModifier = 1;
        }
        else
        {
            reverseModifier = -1;
        }
        
        //System.out.println("Rm: " + reverseModifier + " | " + forward + " CC: " + currentCount + " CS: " + m_startDriveCount + " CE: " + countEnd);
        
        if ( (reverseModifier * (currentCount - m_startDriveCount)) < countEnd  )
        {
            //System.out.println("Criteria not met! " + (currentCount - m_startDriveCount) + " | " + (reverseModifier * countEnd));
            m_drive.drive(0.0, reverseModifier * speed, 0.0);
            return false;
        }
        else
        {
            //System.out.println("Criteria met! " + (currentCount - m_startDriveCount) + " | " + (reverseModifier * countEnd));
            return true;
        }
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
     * Collects the current count of the encoder.  Call this before you
     * call the DriveForByCount method!
     * 
     * NOTE: This should be modified!
     * 
     */
    public void collectDriveStartCount()
    {
        m_startDriveCount = m_encoder.get();
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
     * A constructor which takes a RRMecanumDrive object
     * and uses the default encoder channels of 1 and 2.
     * 
     * NOTE: This should not be modified!
     * 
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
     * 
     * NOTE: This should not be modified!
     * 
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
     * 
     * NOTE: This should not be modified!
     */
    private void Setup()
    {
        SetupTimer();
        SetupEncoder();
        reset();
    }
    
    /**
     * Creates a new Timer object for our class
     * 
     * NOTE: This should not be modified!
     */
    private void SetupTimer()
    {
        m_timer = new Timer();
    }
    
    /**
     * Creates a new Encoder object for our class
     * 
     * NOTE: This should not be modified!
     */
    private void SetupEncoder()
    {
        m_encoder = new Encoder(m_encoderChannel1, m_encoderChannel2, false);
    }
    
    
    /**
     * This method resets the state of the autonomous
     * class.  Sets the DriveState to START, resets
     * the encoder and resets the timer and startDriveTime
     * variable.
     * 
     * NOTE: This should not be modified!
     * 
     */
    public void reset()
    {
        // reset encoder
        m_encoder.reset();
        
        // reset timer
        m_timer.reset();
        
        // reset drive variables
        m_startDriveTime = 0;
        m_startDriveCount = 0;
    }
    
    /**
     * This returns the current count of the encoder.
     * 
     * NOTE: This should not be modified!
     * 
     * @return Returns the count of the encoder
     */
    public int getCount()
    {
        if ( m_encoder != null )
            return m_encoder.get();
        
        return -1;
    }
    
    /**
     * Returns the current time of this objects Timer
     * 
     * NOTE: This should not be modified!
     * 
     * @return Returns the current time of the Timer
     */
    public double getTime()
    {
        if ( m_timer != null )
            return m_timer.get();
        
        return -1.0;
    }
    
    
    /**
     * UNUSED in this class!
     */
    
    public double getAngle()
    {
        return -1.0;
    }
}
