/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import java.lang.Math;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RoboRebels extends IterativeRobot 
{
    private boolean         disabledStateBroadcasted = false;
    private boolean         teleopStateBroadcasted = false;
    private boolean         autonomousStateBroadcasted = false;
    
    private Joystick        leftStick, rightStick, xboxController;
    private ADXL345_I2C     accel;
    
    private RRDrive         drive;
    private RRButtonMap     buttonMap;
    private RRTestThread    threadTest;
    
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    
    public RoboRebels()
    {
        System.out.println("RoboRebels()");
    }
    
    public void robotInit() 
    {
        System.out.println("robotInit()");
        
        leftStick = new Joystick(1);
        rightStick = new Joystick(2);
        xboxController = new Joystick(3);
        
        accel = new ADXL345_I2C(1, ADXL345_I2C.DataFormat_Range.k2G); // slot number is actually module number
        
        buttonMap = new RRButtonMap(leftStick, rightStick, xboxController);
        
//        drive = new RRDrive(2, 1);
        
        threadTest = new RRTestThread();
        
        threadTest.start();
        
        System.out.println(Thread.MIN_PRIORITY + " | " + Thread.NORM_PRIORITY + " | " + Thread.MAX_PRIORITY);
    }
    
    public void disabledInit()
    {
        System.out.println("disabledInit()");
        
        resetBroadcastedFlags();
    }
    
    public void autonomousInit()
    {
        System.out.println("autonomousInit()");
        
        resetBroadcastedFlags();
    }
    
    public void teleopInit()
    {
        System.out.println("teleopInit()");
        
        resetBroadcastedFlags();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() 
    {
        if ( !autonomousStateBroadcasted )
        {
            System.out.println("Autonomous mode...");
            autonomousStateBroadcasted = true;
        }
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() 
    {
        if ( !teleopStateBroadcasted )
        {
            System.out.println("Teleop mode...");
            teleopStateBroadcasted = true;
        }
        
        for (int i = 0; i < 100000; i++)
            Math.sqrt(i);
        
//        drive.drive(false);
    }
    
    
    public void disabledPeriodic()
    {
        if ( !disabledStateBroadcasted )
        {
            System.out.println("Disabled mode...");
            disabledStateBroadcasted = true;
        }
    }
    
    public void autonomousContinuous()
    {
        
    }
    
    public void teleopContinuous()
    {
        
    }
    
    public void disabledContinuous()
    {
        
    }
    
    private void resetBroadcastedFlags()
    {
        disabledStateBroadcasted = false;
        teleopStateBroadcasted = false;
        autonomousStateBroadcasted = false;
    }
    
}
