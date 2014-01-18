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
    Victor clawVictor;
    public Claw() {
        clawVictor = new Victor(RobotMap.CLAW_VICTOR_PWM_CHANNEL);
    }
    
    public void forward() {
        clawVictor.set(.2);
    }
    public void stop() {
        clawVictor.set(0);
    }
    public void backward() {
        clawVictor.set(-.2);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}