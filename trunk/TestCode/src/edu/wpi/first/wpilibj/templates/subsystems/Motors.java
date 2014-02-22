/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.templates.commands.DriveMotors;

/**
 *
 * @author William
 */
public class Motors extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    Talon motor1;
    Talon motor2;
    Talon motor3;
    public int state;

    public Motors() {
        motor1 = new Talon(1);
        motor2 = new Talon(2);
        motor3 = new Talon(3);
        state = 0;
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        //setDefaultCommand(new DriveMotors());
    }

    public void driveMotors(Joystick joystick) {
        double rawY = joystick.getRawAxis(2);
        double scaledY = scale(rawY);
        System.out.println("Motors: " + scaledY);
        motor1.set(scaledY);
        motor2.set(scaledY);

    }

    public static double scale(double x) {
        if (Math.abs(x) < 0.2) {
            return 0;
        }
        return x;
    }

    public void setMotorsPos() {
        motor1.set(1);
        motor2.set(1);
        motor3.set(1);
    }

    public void setMotorsNeg() {
        motor1.set(-1);
        motor2.set(-1);
        motor3.set(-1);
    }

    public void setMotorsStop() {
        motor1.set(0);
        motor2.set(0);
        motor3.set(0);
    }

    public void incrementState() {
        state++;
        if (state > 2) {
            state = 0;
        }
    }

    public int getState() {
        return state;
    }
}
