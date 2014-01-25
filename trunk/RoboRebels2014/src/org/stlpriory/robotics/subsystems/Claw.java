/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.stlpriory.robotics.RobotMap;

/**
 *
 * @author admin
 */
public class Claw extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    Victor wheel_left;
    Victor wheel_right;
    Victor wheel_central;
    
    public Claw() {
        wheel_left = new Victor(RobotMap.CLAW_WHEEL_LEFT_PWM_CHANNEL);
        wheel_right = new Victor(RobotMap.CLAW_WHEEL_RIGHT_PWM_CHANNEL);
        wheel_central = new Victor(RobotMap.CLAW_WHEEL_CENTRAL_PWM_CHANNEL);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}