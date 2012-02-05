/*
 * St. Louis Priory School Robotics Club
 * RoboRebels 2010
 *
 * This class implements the robot drive object which is
 * in charge of encapsulating all driving functionality.
 *
 * Maintainer:  David Sescleifer
 *
 * ;;;
 */

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;


public class RRDrive
{
    RobotDrive m_robotDrive;
    Joystick m_rightStick;
    Joystick m_leftStick;

    public RRDrive(RobotDrive robotDrive, Joystick rightStick, Joystick leftStick)
    {
        m_robotDrive = robotDrive;
        m_rightStick = rightStick;
        m_leftStick = leftStick;
    }

    public RRDrive(RobotDrive robotDrive)
    {
        m_robotDrive = robotDrive;
        m_rightStick = m_leftStick = null;
    }

    public RRDrive(RobotDrive robotDrive, Joystick j)
    {
        m_robotDrive = robotDrive;
        m_leftStick = j;
        m_rightStick = null;
    }

    public void drive( boolean tankDrive )
    {
        
        System.out.println("RRDrive.drive()");
        if ( m_rightStick == null || m_leftStick == null )
            return;

        if ( tankDrive )
            m_robotDrive.tankDrive(m_leftStick, m_rightStick);
        else
            m_robotDrive.arcadeDrive(m_rightStick);
    }

    public void drive(double speed, double curve)
    {
        m_robotDrive.drive(speed, curve);
    }

    
    
}
