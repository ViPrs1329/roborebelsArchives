/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.stlpriory.robotics.RobotMap;

/**
 *
 * @author admin
 */
public class Kicker extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    Victor kick1;
    Victor kick2;
    Encoder encoder1;
    Encoder encoder2;
    DigitalInput limit;
    public Kicker() {
        kick1 = new Victor(RobotMap.KICKER_VICTOR1_PWM_CHANNEL);
        kick2 = new Victor(RobotMap.KICKER_VICTOR2_PWM_CHANNEL);
        
        encoder1 = new Encoder(RobotMap.KICKER_ENCODER1_ACHANNEL,RobotMap.KICKER_ENCODER1_BCHANNEL);
        encoder2 = new Encoder(RobotMap.KICKER_ENCODER2_ACHANEL,RobotMap.KICKER_ENCODER2_BCHANEL);
        
        limit = new DigitalInput(RobotMap.KICKER_LIMITSWITCH_CHANNEL);
    }
    public void startEncoders() {
        encoder1.start();
        encoder2.start();
    }
    
    public void stopEncoders() {
        encoder1.stop();
        encoder2.stop();
    }
    
    public void resetEncoders() {
        encoder1.reset();
        encoder2.reset();
    }
    
    public void startMotors() {
        kick1.set(.2);
        kick2.set(.2);
    }
    
    public void stopMotors() {
        kick1.set(0);
        kick2.set(0);
    }
    
    public void printEncoders() {
        System.out.println("Encoder1: " + encoder1.get());
        System.out.println("Encoder2: " + encoder2.get());
    }
    
    public void fullMotion() {
        resetEncoders();
        while ((encoder1.get() < 50) && (encoder2.get() < 50) ){
            startMotors();
        }
        if ((encoder1.get() >= 50) && (encoder2.get() >= 50)) {
            stopMotors();
        }
        else {
        }
    }
    
    public void kick() {
       
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}