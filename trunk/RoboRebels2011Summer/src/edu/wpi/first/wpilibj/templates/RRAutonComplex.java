/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;


    

/**
 *
 * CHALLENGE!  Use what you have learned so far to combine
 * the three different sensors to complete the course
 * 
 * @author Derek Ward
 */
public class RRAutonComplex implements RRAuton 
{
    private Timer           m_timer;
    private Gyro            m_gyro;
    private Encoder         m_encoder;
    private RRLineTracker   m_lineTracker;
    private RRMecanumDrive  m_drive;
    
    private     int                 m_gyroChannel;
    
    private     int                 m_encoderChannel1,
                                    m_encoderChannel2;
    
    
    private     int                 m_leftLineChannel,
                                    m_middleLineChannel,
                                    m_rightLineChannel;
    
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
     * Initializes some objects
     */
    public void init() 
    {
        // NOTE: Fill this out
        // Set distance per pulse for encoder
        m_encoder.setDistancePerPulse(0.1);
        
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
                
                //System.out.println("STOP");
                m_drive.stop();
                break;
                
            default:
                
                m_drive.stop();
                break;
        }
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
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public RRAutonComplex(RRMecanumDrive d)
    {
        if ( d != null )
            m_drive = d;
        
        m_encoderChannel1 = 1;
        m_encoderChannel2 = 2;
        
        // Default Gyro channel is 1
        m_gyroChannel = 1;
        
        Setup();
    }
    
    public void Setup()
    {
        SetupTimer();
        SetupEncoder();
        SetupGyro();
        SetupLineTracker();
        reset();
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
     * Creates a new Gyro object for our class
     * 
     * NOTE: This should not be modified!
     * 
     */
    private void SetupGyro()
    {
        m_gyro = new Gyro( m_gyroChannel );
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
     * Creates a new RRLineTracker object.
     * 
     * NOTE: This should not be modified!
     * 
     */
    private void SetupLineTracker()
    {
        m_lineTracker = new RRLineTracker(m_leftLineChannel, m_middleLineChannel, m_rightLineChannel);
    }
    
    
    
    public void reset() 
    {
        // reset encoder
        m_encoder.reset();
        
        // reset timer
        m_timer.reset();
        
        
        // reset gyro heading!
        m_gyro.reset();
        
        // reset drive variables
        m_startDriveTime = 0;
        m_startDriveCount = 0;
    }

    public double getTime() 
    {
        if ( m_timer != null )
            return m_timer.get();
        
        return -1.0;
    }

    public int getCount() 
    {
        if ( m_encoder != null )
            return m_encoder.get();
        
        return -1;
    }

    public double getAngle() 
    {
        if ( m_gyro != null )
            return m_gyro.getAngle();
        else
            return 0.0;
    }

    public String getLineSensor() 
    {
        return m_lineTracker.activeSensor();
    }
    
    
}
