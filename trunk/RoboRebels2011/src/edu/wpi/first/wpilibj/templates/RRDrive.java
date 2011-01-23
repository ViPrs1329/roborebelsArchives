/*
 * St. Louis Priory School Robotics Club
 * RoboRebels 2010
 *
 * This class implements the robot drive object which is
 * in charge of encapsulating all driving functionality.
 *
 * Maintainer:  
 *
 * NOTE:
 *
 *  - There is code within RobotDrive which can control
 *    Mecanum wheels!  Look at the javadoc.
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;


public class RRDrive
{
    RobotDrive m_robotDrive;
    Joystick m_xboxStick;

    public RRDrive(RobotDrive d, Joystick j){
        m_robotDrive = d;
        m_xboxStick = j;
    }

    public void drive(){// 1: , 2: rotation, 3: y, 4;
        //m_robotDrive.mecanumDrive_Cartesian(0.2, 0.0, 0.0, 0.0);

    }

    
    
}
