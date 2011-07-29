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
    
    private     final int           START = 0,
                                    STOP = 1;

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

    private void Setup()
    {
        SetupTimer();
        SetupEncoder();
        reset();
    }
    
    private void SetupTimer()
    {
        m_timer = new Timer();
    }
    
    private void SetupEncoder()
    {
        m_encoder = new Encoder(m_encoderChannel1, m_encoderChannel2);
    }

    
    public void init()
    {
        m_timer.start();
    }
    
    
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
    
    
    public void reset()
    {
        m_timer.reset();
        
        DriveState = START;
    }
}
