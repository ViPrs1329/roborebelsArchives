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
//    public static Solenoid valve2;
    
    public GearBox() {
        valve1 = new Solenoid(RobotMap.GEARBOX1_VALVE_CHANNEL);
//        valve2 = new Solenoid(RobotMap.GEARBOX2_VALVE_CHANNEL);

    }
    
    //return the state of the valve
    private boolean getValve1State() {
        return valve1.get();
    }
    
    //shift gears
//    private boolean getValve2State() {
//        return valve2.get();
//    }
    public boolean isLowGear() {
//        if ((getValve1State()) && (!getValve2State())) {
//            return true;
//        }
//        else if((!getValve1State()) && (getValve2State())) {
//            return false;
//        }
//        else {
//            Debug.println("[Gearboxes] Error: Solenoids in same state");
//            shiftValve1();
//            if (getValve1State()) {
//                return true;
//            }
//            else {
//                return false;
//            }
//        }
        return !valve1.get();
    }
    
    public boolean isHighGear() {
        return !isLowGear();
    }
    private void shiftValve1() {
        boolean state = getValve1State();
        valve1.set(!state);
    }
//    private void shiftValve2() {
//        boolean state = getValve2State();
//        valve2.set(!state);
//    }
    public void shiftBoxes() {
//        boolean state1 = getValve1State();
//        boolean state2 = getValve2State();
//        if (state1 != state2) {
            shiftValve1();
//            shiftValve2();
//        }
//        else {
//            Debug.println("[GearBoxes] Error, Both Solenoids in same state");
//            shiftValve1();
//        }
    }
        
        public void setHighGear() {
            if (isHighGear()) {
                return;
            }
            shiftBoxes();

        }
        public void setLowGear() {
            if (isLowGear()) {
                return;
            }
            shiftBoxes();
        }
}


