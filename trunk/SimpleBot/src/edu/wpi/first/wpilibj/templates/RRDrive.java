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
    Joystick m_xboxController;

    public RRDrive(RobotDrive robotDrive, Joystick j)
    {
        m_robotDrive = robotDrive;
        m_xboxController = j;
    }

    public void drive( boolean tankDrive )
    {
        if ( m_xboxController == null )
            return;

        if ( tankDrive )
            //m_robotDrive.tankDrive(m_leftStick, m_rightStick);\
            return;
        else
            m_robotDrive.arcadeDrive(m_xboxController);
    }

    public void drive(double speed, double curve)
    {
        m_robotDrive.drive(speed, curve);
    }
}
