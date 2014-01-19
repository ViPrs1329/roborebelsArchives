/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {
    
    RRDrive     drive;
    Joystick    xboxController;
    RobotDrive  rdrive;
    Jaguar      leftJag1, leftJag2, rightJag1, rightJag2;
    Compressor  compressor; 
    Solenoid    leftShiftSol, rightShiftSol;
    
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        xboxController = new Joystick(1);
        leftJag1 = new Jaguar(1);
        leftJag2 = new Jaguar(3);
        rightJag1 = new Jaguar(2);
        rightJag2 = new Jaguar(4);
        compressor = new Compressor(1, 1);
        leftShiftSol = new Solenoid(1);
        rightShiftSol = new Solenoid(2);
        rdrive = new RobotDrive(leftJag1, leftJag2, rightJag1, rightJag2);        // L: 1, 3   R: 2, 4
         drive = new RRDrive(rdrive, xboxController, leftShiftSol, rightShiftSol);
        
        startUpCompressor();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        drive.drive(false);
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
     public void startUpCompressor() {
        if (! compressor.enabled()) {
            compressor.start();
        }
    }
    
    public void shutDownCompressor() {
        if (compressor.enabled()) {
            compressor.stop();
        }

    }
    
}
