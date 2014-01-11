/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

/**
 *
 * Xbox button map
 * 0
 * 1 A
 * 2 B
 * 3 X
 * 4 Y
 * 5 L Bumper
 * 6 R Bumper
 * 7 Back
 * 8 Start
 * 9 L Stick Click
 * 10 R Stick Click
 * 
 * Axis
 * 
 *  ¥1: Left Stick X Axis
-Left:Negative ; Right: Positive
¥2: Left Stick Y Axis
-Up: Negative ; Down: Positive
¥3: Triggers
-Left: Positive ; Right: Negative
¥4: Right Stick X Axis
-Left: Negative ; Right: Positive
¥5: Right Stick Y Axis
-Up: Negative ; Down: Positive
¥6: Directional Pad (Not recommended, buggy)
 * 
 * 
 * 
 * @author dmw
 */
public class RoboDrive {
    
    Jaguar      leftJag1, leftJag2, rightJag1, rightJag2;
    Compressor  compressor; 
    Solenoid    leftShiftSol, rightShiftSol;
    Joystick    xboxController;
    Encoder     leftEncoder, rightEncoder;
    
    boolean     highGear = false;
    boolean     gearSwitched = false;
    boolean     driveStraight = false;
    boolean     driveStraightSwitched = false;
    
    public RoboDrive() {
        xboxController = new Joystick(1);
        leftJag1 = new Jaguar(1);
        leftJag2 = new Jaguar(3);
        rightJag1 = new Jaguar(2);
        rightJag2 = new Jaguar(4);
        compressor = new Compressor(1, 1);
        leftShiftSol = new Solenoid(1);
        rightShiftSol = new Solenoid(2);
    }
    
    public void drive() {
        if ( xboxController == null )
            return;
        
        /* handle gear shift button */
        handleShiftButton();
        
        /* handle drive straight button */
        handleDriveStraightButton();
        
        /* handle joysticks */
        handleJoysticks();
    }
    
    /**
     * 
     */
    
    public void handleShiftButton() {
        if (xboxController.getRawButton(3) && !gearSwitched)
        {
            if (highGear)
                highGear = false;
            else
                highGear = true;
            
            gearSwitched = true;
        }
        else if (!xboxController.getRawButton(3))
        {
            gearSwitched = false;
        }
        
        if (highGear)
            engadgeHighGear();
        else
            engadgeLowGear();
    }
    
    /**
     * 
     */
    public void handleDriveStraightButton() {
        
    }
    
    /**
     * 
     */
    public void handleJoysticks() {
        
    }
    
    /**
     * 
     */
    public void startUpCompressor() {
        if (! compressor.enabled()) {
            compressor.start();
        }
    }
    
    /**
     * 
     */
    public void shutDownCompressor() {
        if (compressor.enabled()) {
            compressor.stop();
        }

    }
    
    /**
     * 
     */
    private void engadgeHighGear() {
        leftShiftSol.set(true);
        rightShiftSol.set(true);
    }

    /**
     * 
     */
    private void engadgeLowGear() {
        leftShiftSol.set(false);
        rightShiftSol.set(false);
    }
    
}
//test