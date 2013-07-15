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
import edu.wpi.first.wpilibj.Solenoid;

/**
 * The buttons on the controller follow this mapping
 *
 * 1: A
 * 2: B
 * 3: X
 * 4: Y
 * 5: Left Bumper
 * 6: Right Bumper
 * 7: Back
 * 8: Start
 * 9: Left Joystick
 * 10: Right Joystick
 *
 * The axis on the controller follow this mapping (all output is between -1 and 1)
 *
 * 1: Left Stick X Axis -Left:Negative ; Right: Positive
 * 2: Left Stick Y Axis -Up: Negative ; Down: Positive
 * 3: Triggers -Left: Positive ; Right: Negative
 * 4: Right Stick X Axis -Left: Negative ; Right: Positive
 * 5: Right Stick Y Axis -Up:   Negative ; Down: Positive
 * 6: Directional Pad (Not recommended, buggy)
 */


public class RRDrive
{
    RobotDrive m_robotDrive;
    Joystick m_xboxController;
    Solenoid leftSol, rightSol;
    
    boolean     m_highGear;
    boolean     m_gearSwitched;

    public RRDrive(RobotDrive robotDrive, Joystick j, Solenoid ls, Solenoid rs)
    {
        m_robotDrive = robotDrive;
        m_xboxController = j;
        m_highGear = false;
        m_gearSwitched = false;
        leftSol = ls;
        rightSol = rs;
    }

    public void drive( boolean tankDrive )
    {
        if ( m_xboxController == null )
            return;
        
        if (m_xboxController.getRawButton(3) && !m_gearSwitched)
        {
            if (m_highGear)
                m_highGear = false;
            else
                m_highGear = true;
            
            m_gearSwitched = true;
        }
        else if (!m_xboxController.getRawButton(3))
        {
            m_gearSwitched = false;
        }
        
        if (m_highGear)
            engadgeHighGear();
        else
            engadgeLowGear();

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

    private void engadgeHighGear() {
        
    }

    private void engadgeLowGear() {
        
    }
}
