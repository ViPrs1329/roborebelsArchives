/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Timer;

/**
 *
 * !!!!!!!!!!!!!!!!!!!!!!!
 * !!! NOTE !!!  The middle line sensor is defective!  So, the only
 * output that you will be receiving from RRLineTracker will be:
 * 
 * "left-middle", "right-middle" or "middle"
 * 
 * !!!!!!!!!!!!!!!!!!!!!!!
 * 
 * @author 
 */
public class RRAutonLineSensor implements RRAuton
{
    private     RRMecanumDrive      m_drive;
    private     RRLineTracker       m_lineTracker;
    private     Timer               m_timer;
    
    private     int                 m_leftLineChannel,
                                    m_middleLineChannel,
                                    m_rightLineChannel;
    
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
     * Initializes the object.  Right now it just starts the 
     * built in timer.
     * 
     * NOTE: This may be modified!
     * 
     */
    public void init()
    {
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

                if ( driveOnLine(0.25, 0.25, true, 5) == true )
                {
                    m_drive.stop();
                    DriveState = STOP;
                }
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
                
                
                break;
                
            default:
                
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
     * Makes the robot strafe at the passed speed, either left or right, 
     * and for the passed duration.
     * @param speed Speed of the strafing (0.0 - 1.0)
     * @param left True = strafe left, False = strafe right
     * @param duration Duration of strafing in seconds
     * @return True = criteria met, False criteria not met
     */
    public boolean strafeFor( double speed, boolean left, double duration)
    {
        double  currentTime = m_timer.get();
        
        int     reverseModifier;
        
        if ( left == true )
        {
            reverseModifier = -1;
        }
        else
        {
            reverseModifier = 1;
        }
        
        
        if ( currentTime - m_startDriveTime < duration )
        {
            m_drive.drive(reverseModifier * speed, 0.0, 0.0);
            
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
     * Drives the robot in the direction specified, at the speed
     * specified for the duration specified.  It also uses the
     * specified veerCorrection value to rotate the robot if it
     * starts to loose the line.
     * 
     * NOTE:
     * 
     *   - Use the created RRLineTracker object, m_lineTracker, to get the state
     *     of the two line sensors
     * 
     *   - Use the methods in m_lineTracker, activeSensor() [which returns a 
     *     string: "both", "left", "right" or "none"] or the individual sensor 
     *     methods: getL(), getR() to get the sensor state.  
     * 
     * 
     * @param speed Speed of the strafing (0.0 - 1.0)
     * @param veerCorrect The amount of veer (rotation) to introduce to stay on the line (0.0 - 1.0)
     * @param forward True = forward, False = backward
     * @param duration How long in seconds do you want the robot to drive for?
     * @return True = criteria met, False = criteria not met
     */
    public boolean driveOnLine( double speed, double veerCorrect, boolean forward, double duration )
    {
        double      currentTime = m_timer.get();
        double      spd = 0.0;
        double      rot = 0.0;
        String      sensor = m_lineTracker.activeSensor();
        
        if ( forward == true )
            spd = speed;
        else
            spd = -speed;
        
        
        if ( sensor.compareTo("both") == 0 )
        {
            // left-middle should go straight
            m_drive.drive(0.0, spd, rot);
        }
        else if ( sensor.compareTo("right") == 0 )
        {
            // middle should veer to the right
            m_drive.drive(0.0, spd / 2, veerCorrect);
        }
        else if ( sensor.compareTo("left") == 0 )
        {
            // left should veer to the left
            m_drive.drive(0.0, spd / 2, -veerCorrect);
        }
        else if ( sensor.compareTo("none") == 0 )
        {
            // none should veer opposite of the last known veer direction
            // for now just stop
            m_drive.stop();
        }
        
        if ( currentTime - m_startDriveTime < duration )
        {
            return false;
        }
        else
            return true;
    }
    
    /**
     * NOT USED FOR THIS CAMP!
     * 
     * @param rotationSpeed
     * @param clockwise
     * @param duration
     * @return 
     */
    public boolean rotateUntilTapeIsFound( double rotationSpeed, boolean clockwise, double duration )
    {
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
     * Constructor for RRAutonLineSensor.  Sets the default 
     * line sensor channels to 4, 5, 6.  Also, retrieves the
     * passed RRMecanumDrive object.
     * 
     * NOTE: This should not be modified!
     * 
     * @param d A previously created RRMecanumObject.
     */
    public RRAutonLineSensor(RRMecanumDrive d)
    {
        // default ports for the line tracker is 4, 5, 6
        m_leftLineChannel = 4;
        m_middleLineChannel = 5;
        m_rightLineChannel = 6;
        
        // Only collect the MecanumDrive object
        // if it's not null
        if ( d != null )
            m_drive = d;

        // Set up necessary objects and vars
        Setup();
    }
    
    /**
     * Constructor for RRAutonLineSensor.  Sets the line sensor
     * channels to those that are passed in.  Also, retrieves the
     * passed RRMecanumDrive object.
     * 
     * NOTE: This should not be modified!
     * 
     * @param d A previously created RRMecanumObject.
     * @param l Left channel
     * @param m Middle channel
     * @param r Right channel
     */
    public RRAutonLineSensor(RRMecanumDrive d, int l, int m, int r)
    {
        m_leftLineChannel = l;
        m_middleLineChannel = m;
        m_rightLineChannel = r;
        
        // Only collect the MecanumDrive object
        // if it's not null
        if ( d != null )
            m_drive = d;

        // Set up necessary objects and vars
        Setup();
    }
    
    /**
     * Calls various other internal Setup methods.
     * 
     * NOTE: This should not be modified!
     * 
     */
    private void Setup()
    {
        SetupTimer();
        SetupLineTracker();
        reset();
    }
    
    /**
     * Creates a new Timer object.
     * 
     * NOTE: This should not be modified!
     * 
     */
    private void SetupTimer()
    {
        m_timer = new Timer();
    }
    
    /**
     * Creates a new RRLineTracker object.
     * 
     * NOTE: This should not be modified!
     * 
     */
    private void SetupLineTracker()
    {
        m_lineTracker = new RRLineTracker(m_leftLineChannel, m_middleLineChannel, m_rightLineChannel);
    }

    
    
    /**
     * This method resets the state of the autonomous
     * class.  Sets the DriveState to START and resets the 
     * timer and startDriveTime variable.
     * 
     * NOTE: This should not be modified!
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
     * Returns the current time of the Timer object
     * @return 
     */
    public double getTime()
    {
        if ( m_timer != null )
            return m_timer.get();
        else
            return -1.0;
    }
    
    /**
     * UNUSED
     * @return 
     */
    public double getAngle()
    {
        return -1.0;
    }
    
    /**
     * UNUSED
     * @return 
     */
    public int getCount()
    {
        return -1;
    }
    
    
    public String getLineSensor()
    {
        return m_lineTracker.activeSensor();
    }
}
