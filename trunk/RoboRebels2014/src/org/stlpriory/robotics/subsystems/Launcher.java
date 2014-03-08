/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.stlpriory.robotics.RobotMap;
import org.stlpriory.robotics.commands.CommandBase;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 * @author admin
 */
public class Launcher extends Subsystem {

    private Talon launch1;
    //private Talon launch2;
    private Solenoid valve1;
//    private Solenoid valve2;
    private static final double WINDING_SPEED = -1.0;
    private DigitalInput punterLimitSwitch;
    
    public Launcher() {
        super("Launcher");
        Debug.println("[Launcher Subsystem] Instantiating...");
       
        Debug.println(">>>>>>>>>>>>>  Go Robo Rebels");
        
        Debug.println("[Launcher Subsystem] Initializing speed controller to channel "
                + RobotMap.LAUNCHER_JAGUAR1_PWM_CHANNEL);
//        Debug.println("[Launcher Subsystem] Initializing speed controller to channel "
//                + RobotMap.LAUNCHER_JAGUAR2_PWM_CHANNEL);
        launch1 = new Talon(RobotMap.LAUNCHER_JAGUAR1_PWM_CHANNEL);
//        launch2 = new Talon(RobotMap.LAUNCHER_JAGUAR2_PWM_CHANNEL);

        Debug.println("[Launcher Subsystem] Initializing first compressor solenoid to channel "
                + RobotMap.LAUNCHER_VALVE1_CHANNEL);
        Debug.println("[Launcher Subsystem] Initializing second compressor solenoid to channel "
                + RobotMap.LAUNCHER_VALVE2_CHANNEL);
        
                       
        valve1 = new Solenoid(RobotMap.LAUNCHER_VALVE1_CHANNEL);
//        valve2 = new Solenoid(RobotMap.LAUNCHER_VALVE2_CHANNEL);
        
        Debug.println("[Launcher Subsystem] Initilizing limit switch for preventing retracting punter over limit");
        punterLimitSwitch = new DigitalInput(RobotMap.LAUNCHER_PUNTER_LIMIT_SWITCH_DIGITAL_IO_CHANNEL);

        
        Debug.println("[Launcher Subsystem] Instantiation complete.");
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public void disengageForShoot() {

        valve1.set(true);
//        valve2.set(false);
    }

    public void engageForLoad() {
//        valve2.set(true);
        valve1.set(false);
    }
    public boolean getValve1State() {
        return valve1.get();
    }
//    public boolean getValve2State() {
//        return valve2.get();
//    }
    public boolean isDisengagedForShoot() {
//        if ((getValve1State()) && (!getValve2State())) {
//            return true;
//        }
//        else if ((!getValve1State()) && (getValve2State())) {
//            return false;
//        }
//        else {
//            Debug.println("[Launcher Subsystem]  Error: both Solenoids in same state");
//            engageForLoad();
//            return false;
//        }
        return getValve1State();
            
    }
    public boolean isEngagedForLoad() {
        return !isDisengagedForShoot();
        
    }

    public void startWindingLauncher() {
        if (! isPunterLimitReached()){
            launch1.set(WINDING_SPEED);
            CommandBase.updateDriverStationLCD(2, 1, "Retracting launcher");
        } else {
            CommandBase.updateDriverStationLCD(2, 1, "Hit limit switch");
        }
        
//        launch2.set(windSpeed);
    }

    public void stopWindingLauncher() {
        launch1.set(0);
//        launch2.set(0);
    }
    
    public boolean isPunterLimitReached() {
        return punterLimitSwitch.get();
    }
    

}