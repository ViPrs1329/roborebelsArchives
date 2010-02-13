/*
 * St. Louis Priory School Robotics Club
 * RoboRebels 2010
 *
 * This class implements the robot drive object which is
 * in charge of encapsulating all driving functionality.
 *
 * Maintainer:  David Sescleifer
 *
 * Objects used:
 *
 *      - RobotDrive, Joystick (references of), encoders (for speed),
 *        gyroscope (position [maybe])
 *
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;


public class RRDrive {
    RobotDrive m_robotDrive;
    Joystick m_rightStick;
    Joystick m_leftStick;

    public RRDrive(RobotDrive robotDrive, Joystick rightStick, Joystick leftStick)
    {
        m_robotDrive = robotDrive;
        m_rightStick = rightStick;
        m_leftStick = leftStick;


        /*
        m_robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        m_robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        m_robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        m_robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        */


    }

    public void drive( boolean tankDrive )
    {
        if ( tankDrive )
            m_robotDrive.tankDrive(m_leftStick, m_rightStick);
        else
            m_robotDrive.arcadeDrive(m_leftStick);
    }
    
}
