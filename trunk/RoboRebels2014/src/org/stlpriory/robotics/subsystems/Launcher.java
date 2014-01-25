/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.stlpriory.robotics.RobotMap;

/**
 *
 * @author admin
 */
public class Launcher extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    Compressor compressor;
    
    Jaguar launch1;
    Jaguar launch2;
    

    
    Solenoid valve1;
    Solenoid valve2;
    public Launcher() {
        compressor = new Compressor(RobotMap.LAUNCHER_COMPRESSOR_DIGITALIO_CHANNEL, RobotMap.LAUNCHER_COMPRESSOR_RELAY_CHANNEL);
        
        launch1 = new Jaguar(RobotMap.LAUNCHER_JAGUAR1_PWM_CHANNEL);
        launch2 = new Jaguar(RobotMap.LAUNCHER_JAGUAR2_PWM_CHANNEL);
        
        
        
        valve1 = new Solenoid(RobotMap.LAUNCHER_VALVE1_CHANNEL);
        valve2 = new Solenoid(RobotMap.LAUNCHER_VALVE2_CHANNEL);
        
        compressor.start();

    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public void startSolenoid1() {
        valve2.set(false);
        valve1.set(true);
    }
    public void startSolenoid2() {
        valve1.set(false);
        valve2.set(true);
    }
    public void startMotors() {
        launch1.set(.6);
        launch2.set(.6);
        
    }
    public void stopMotors() {
        launch1.set(0);
        launch2.set(0);
    }
}