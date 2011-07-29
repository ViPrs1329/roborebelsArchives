/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;

/**
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
    
    private     int                 m_startDriveTime = 0;
    
    private     final int           START = 0,
                                    STOP = 1;

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
        m_encoder = new Encoder(m_encoderChannel1, m_encoderChannel2);
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
     * to add usefull logic make your states additional number
     * cases.  It is good practice to encapsulate your logic into
     * additional private methods (see driveFor or rotate methods).
     */
    public void run()
    {
        switch ( DriveState )
        {
            case START:
                
                
                break;
                
            case STOP:
                
                
                break;
                
            default:
                
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
    }
}
