/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.stlpriory.robotics.misc.Debug;

/**
 * A drive train that does no operations
 */
public class NoOpDriveTrain extends Subsystem {

    public NoOpDriveTrain() {
        super("NoOpDriveTrain");
        Debug.println("[NoOpDriveTrain Subsystem] Instantiating...");
        Debug.println("[NoOpDriveTrain Subsystem] Instantiation complete.");
    }

    public void initDefaultCommand() {
    }

    public void stop() {
    }
    
    public void tankDrive(double leftValue, double rightValue) {
    }

    public void arcadeDrive(double moveValue, double rotateValue) {
    }

    public void mecanumDrive(Joystick joystick) {
    }

    public void driveWithJoystick(Joystick joystick) {
    }

    public void driveWithGamepad(Joystick joystick) {
    }

    public void shiftGears() {
    }

  

}