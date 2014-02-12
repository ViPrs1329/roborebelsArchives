/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import org.stlpriory.robotics.RobotMap;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 * @author William
 */
class GearBox {
    
    public static Solenoid valve1;
    public static Solenoid valve2;
    
    public GearBox() {
        valve1 = new Solenoid(RobotMap.GEARBOX1_VALVE_CHANNEL);
        valve2 = new Solenoid(RobotMap.GEARBOX2_VALVE_CHANNEL);

    }
    
    //return the state of the valve
    public boolean getValve1State() {
        return valve1.get();
    }
    
    //shift gears
    public boolean GetValve2State() {
        return valve2.get();
    }
    public void shiftValve1() {
        boolean state = getValve1State();
        valve1.set(!state);
    }
    public void ShiftValve2() {
        boolean state = GetValve2State();
        valve2.set(!state);
    }
    public void shiftBoxes() {
        boolean state1 = getValve1State();
        boolean state2 = GetValve2State();
        if (state1 != state2) {
            shiftValve1();
            ShiftValve2();
        }
        else {
            Debug.println("[GearBoxes] Error, Both Solenoids on");
            shiftValve1();
        }
    }

}
