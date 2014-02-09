/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.stlpriory.robotics.RobotMap;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 * @author admin
 */
public class Launcher extends Subsystem {

    private Talon launch1;
    private Talon launch2;
    private Solenoid valve1;
    private Solenoid valve2;
    private Relay spike;

    public Launcher() {
        super("Launcher");
        Debug.println("[Launcher Subsystem] Instantiating...");
       
        Debug.println(">>>>>>>>>>>>>  Go Robo Rebels");
        
        Debug.println("[Launcher Subsystem] Initializing speed controller to channel "
                + RobotMap.LAUNCHER_JAGUAR1_PWM_CHANNEL);
        Debug.println("[Launcher Subsystem] Initializing speed controller to channel "
                + RobotMap.LAUNCHER_JAGUAR2_PWM_CHANNEL);
        launch1 = new Talon(RobotMap.LAUNCHER_JAGUAR1_PWM_CHANNEL);
        launch2 = new Talon(RobotMap.LAUNCHER_JAGUAR2_PWM_CHANNEL);

        Debug.println("[Launcher Subsystem] Initializing first compressor solenoid to channel "
                + RobotMap.LAUNCHER_VALVE1_CHANNEL);
        Debug.println("[Launcher Subsystem] Initializing second compressor solenoid to channel "
                + RobotMap.LAUNCHER_VALVE2_CHANNEL);
        
        Debug.println("[Launcher Subsystem] Initializing spike to PWM channel 7");
        spike = new Relay(7);

        valve1 = new Solenoid(RobotMap.LAUNCHER_VALVE1_CHANNEL);
        valve2 = new Solenoid(RobotMap.LAUNCHER_VALVE2_CHANNEL);

        
        Debug.println("[Launcher Subsystem] Instantiation complete.");
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public void extendPiston() {
        spike.set(Relay.Value.kOn);
        spike.setDirection(Relay.Direction.kForward);
        //spike.set(Relay.Value.kOn);
       /// valve1.set(true);
      //  valve2.set(false);
    }

    public void retractPiston() {
        spike.set(Relay.Value.kOn);
        spike.setDirection(Relay.Direction.kReverse);
       // spike.set(Relay.Value.kOn);
       // valve2.set(true);
       // valve1.set(false);
    }

    public void loadLauncher() {
        launch1.set(.6);
        launch2.set(.6);
    }

    public void fireLauncher() {
        launch1.set(0);
        launch2.set(0);
    }
    

}