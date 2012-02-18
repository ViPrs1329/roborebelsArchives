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


import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.MotorSafetyHelper;

import java.lang.NullPointerException;


public class RRDrive implements MotorSafety
{
    private     MotorSafetyHelper       motorSafetyHelper;
    
    private     boolean                 arcade;
    private     Joystick                m_xboxStick;
    private     Jaguar                  leftMotor,
                                        rightMotor;
    
    public static final double kDefaultExpirationTime = 0.1;

//    RobotDrive m_robotDrive;
//    Joystick m_rightStick;
//    Joystick m_leftStick;
//
//    public RRDrive(RobotDrive robotDrive, Joystick rightStick, Joystick leftStick)
//    {
//        m_robotDrive = robotDrive;
//        m_rightStick = rightStick;
//        m_leftStick = leftStick;
//    }
//
//    public RRDrive(RobotDrive robotDrive)
//    {
//        m_robotDrive = robotDrive;
//        m_rightStick = m_leftStick = null;
//    }
//
//    public RRDrive(RobotDrive robotDrive, Joystick j)
//    {
//        m_robotDrive = robotDrive;
//        m_leftStick = j;
//        m_rightStick = null;
//    }
//
//    public void drive( boolean tankDrive )
//    {
//        
//        System.out.println("RRDrive.drive()");
//        if ( m_rightStick == null || m_leftStick == null )
//            return;
//
//        if ( tankDrive )
//            m_robotDrive.tankDrive(m_leftStick, m_rightStick);
//        else
//            m_robotDrive.arcadeDrive(m_rightStick);
//    }
//
//    public void drive(double speed, double curve)
//    {
//        m_robotDrive.drive(speed, curve);
//    }

    public RRDrive(Joystick xbox, int leftChannel, int rightChannel)
    {
        NullPointerException        ex;
        
        System.out.println("RRDrive() " + leftChannel + " " + rightChannel);
        
        if ( xbox != null )
            m_xboxStick = xbox;
        else
        {
            throw new NullPointerException("RRDrive was passed a null Joystick object!");
        }
        
        leftMotor = new Jaguar(leftChannel);
        rightMotor = new Jaguar(rightChannel);
        
        setupMotorSafety();
    }
    
    public void drive(boolean tankDrive)
    {
        System.out.println("drive()");
           double l_xVal  = m_xboxStick.getRawAxis(1);
           double l_yVal  = m_xboxStick.getRawAxis(2);

           double r_xVal  = m_xboxStick.getRawAxis(4);
           double r_yVal  = m_xboxStick.getRawAxis(5);
           
        if (!tankDrive)
        {
           
           if (Math.abs(l_xVal) < .13)
           {
               l_xVal = 0;
           }

           if (Math.abs(l_yVal)< .13)
           {
               l_yVal = 0;
           }

           if (Math.abs(r_xVal) < .13)
           {
               r_xVal = 0;
           }

           if (Math.abs(r_yVal)< .13)
           {
               r_yVal = 0;
           }

           //Change the range of the joystick values to account for the dead zone
           if (l_xVal > 0)
           {
               l_xVal = (l_xVal-.13)/(1-.13);
           }
           else if (l_xVal < 0)
           {
            l_xVal = (l_xVal+.13)/(1-.13);
            }

            if (l_yVal > 0)
            {
               l_yVal = (l_yVal-.13)/(1-.13);
           }
           else if (l_yVal < 0)
           {
               l_yVal = (l_yVal+.13)/(1-.13);
           }

           if (r_xVal > 0)
           {
               r_xVal = (r_xVal-.13)/(1-.13);
           }
           else if (r_xVal < 0)
           {
               r_xVal = (r_xVal+.13)/(1-.13);
           }

           if (r_yVal > 0)
           {
               r_yVal = (r_yVal-.13)/(1-.13);
           }
           else if (r_yVal < 0)
           {
               r_yVal = (r_yVal+.13)/(1-.13);
           }
           
           System.out.println("x y" + l_yVal + " " + l_xVal);
           arcadeDrive(l_yVal, l_xVal); 
        }
        else
        {
           //tank mode
        }
    }
    public void arcadeDrive(double moveValue, double rotateValue)
    {
        double rightMotorSpeed;
        double leftMotorSpeed;
        
        if (moveValue > 0.0) 
        {
            if (rotateValue > 0.0) 
            {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = Math.max(moveValue, rotateValue);
            } 
            else 
            {
                leftMotorSpeed = Math.max(moveValue, -rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            }
        } 
        else 
        {
            if (rotateValue > 0.0) 
            {
                leftMotorSpeed = -Math.max(-moveValue, rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            } 
            else 
            {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
            }
        }
        
        System.out.println("Left Motor Speed: " + leftMotorSpeed + "RMS: " + rightMotorSpeed);
        setLeftRightMotorValue(leftMotorSpeed, rightMotorSpeed);
    }
    
    public void setLeftRightMotorValue(double left, double right)
    {
        if (leftMotor == null || rightMotor == null)
            throw new NullPointerException("Null motor provided");
        leftMotor.set(left);
        rightMotor.set(right);
        
        motorSafetyHelper.feed();
    }

    public void setExpiration(double timeout) {
        motorSafetyHelper.setExpiration(timeout);
    }

    public double getExpiration() {
        return motorSafetyHelper.getExpiration();
    }

    public boolean isAlive() {
        return motorSafetyHelper.isAlive();
    }

    public boolean isSafetyEnabled() {
        return motorSafetyHelper.isSafetyEnabled();
    }

    public void setSafetyEnabled(boolean enabled) {
        motorSafetyHelper.setSafetyEnabled(enabled);
    }
    
    public String getDescription() {
        return "RR Robot Drive";
    }

    public void stopMotor() 
    {
        if (leftMotor != null) {
            leftMotor.set(0.0);
        }
        if (rightMotor != null) {
            rightMotor.set(0.0);
        }
        
    }
    
    private void setupMotorSafety() 
    {
        motorSafetyHelper = new MotorSafetyHelper(this);
        motorSafetyHelper.setExpiration(kDefaultExpirationTime);
        motorSafetyHelper.setSafetyEnabled(false);
    }
}
