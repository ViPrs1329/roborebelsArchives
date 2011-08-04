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
public class RRAutonLineSensor2 implements RRAuton
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

                // collect the drive start time
                collectDriveStartTime();
                DriveState = STEP_2;

                break;

            case STEP_2:

                // drive forward following the line until
                // either the robot drives past the end of
                // the line or fails to track the line
                if ( followLine(0.25)  == true)
                {
                    m_drive.stop();
                    DriveState = STEP_3;
                }

                break;

            case STEP_3:

                reset();
                DriveState = STEP_4;
                
                break;
                
            case STEP_4:

                DriveState = STOP;
                
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
     * Called periodically, this method tracks a line on the ground
     * by measuring the values of three sensors.  If the middle sensor
     * reads the line then the robot drives straight.  If the left
     * sensor reads the line then the robot corrects by rotating CCW
     * (counter-clockwise).  If the right sensor reads the line then
     * the robot corrects by rotating CW (clockwise).  If none of the
     * sensors read the line then the robot stops.
     *
     * The method returns true while the robot is tracking the
     * line otherwise it returns false.
     *
     * HINTS:
     *
     *   - The sensor objects are: m_left, m_middle, m_right
     *
     * NOTE: This should be modified!
     *
     * @param forwardSpeed What speed do you want to drive at (0.0 to 1.0)
     * @return True = criteria met, False = criteria not met
     */
    public boolean followLine( double forwardSpeed )
    {

        //gets the values of the line sensors NOTE: Inverted so true is now on line
        boolean lineOnLeft   = !m_lineTracker.getL();
        boolean lineOnMiddle = !m_lineTracker.getM();
        boolean lineOnRight  = !m_lineTracker.getR();

        double scaledForwardSpeed = 0.1*forwardSpeed;
        if (lineOnLeft) {
            // Rotate the robot CCW (counter clockwise) to correct
            m_drive.drive(scaledForwardSpeed, 0, -0.1);
            return true;

        } else if (lineOnRight) {
            // Rotate the robot CW (clockwise) to correct
            m_drive.drive(scaledForwardSpeed, 0, 0.1);
            return true;

        } else if (lineOnMiddle) {
            // Drive straight
            m_drive.drive(forwardSpeed, 0, 0);
            return true;

        }

        return false;
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
    public RRAutonLineSensor2(RRMecanumDrive d)
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
    public RRAutonLineSensor2(RRMecanumDrive d, int l, int m, int r)
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
