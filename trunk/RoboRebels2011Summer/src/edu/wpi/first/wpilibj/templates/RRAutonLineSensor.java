/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author Robot-1
 */
public class RRAutonLineSensor 
{
    private     RRMecanumDrive      m_drive;
    private     RRLineTracker       m_lineTracker;
    private     Timer               m_timer;
    
    private     int                 m_ltChannel1,
                                    m_ltChannel2,
                                    m_ltChannel3;
    
    private     int                 DriveState;
    
    private     final int           START = 0,
                                    STOP = 1;
    
    public RRAutonLineSensor(RRMecanumDrive d)
    {
        // default ports for the line tracker is 4, 5, 6
        m_ltChannel1 = 4;
        m_ltChannel2 = 5;
        m_ltChannel3 = 6;
        
        // Only collect the MecanumDrive object
        // if it's not null
        if ( d != null )
            m_drive = d;

        // Set up necessary objects and vars
        Setup();
    }
    
    public RRAutonLineSensor(RRMecanumDrive d, int l1, int l2, int l3)
    {
        m_ltChannel1 = l1;
        m_ltChannel2 = l2;
        m_ltChannel3 = l3;
        
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
        SetupLineTracker();
        reset();
    }
    
    private void SetupTimer()
    {
        m_timer = new Timer();
    }
    
    
    private void SetupLineTracker()
    {
        m_lineTracker = new RRLineTracker(m_ltChannel1, m_ltChannel1, m_ltChannel1);
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
