/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Solenoid;
import org.stlpriory.robotics.RobotMap;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 * @author William
 */
class GearBox {
    
    public static Solenoid box1;
    public static Solenoid box2;
    public static DriverStationLCD DR;
    
    public GearBox() {
        box1 = new Solenoid(RobotMap.GEARBOX1_VALVE_CHANNEL);
        box2 = new Solenoid(RobotMap.GEARBOX2_VALVE_CHANNEL);
        DR = DriverStationLCD.getInstance();

    }
    
    //return the state of the valve
    public boolean getBox1State() {
        return box1.get();
    }
    
    //shift gears
    public boolean getBox2State() {
        return box2.get();
    }
    public void shiftBox1() {
        boolean state = getBox1State();
        box1.set(!state);
    }
    public void shiftBox2() {
        boolean state = getBox2State();
        box2.set(!state);
    }
    public void shiftBoxes() {
        boolean state1 = getBox1State();
        boolean state2 = getBox2State();
        if (state1 == state2) {
            shiftBox1();
            shiftBox2();
        }
        else {
            Debug.println("[GearBoxes] Error, Boxes on Separate Gears");
            shiftBox1();
        }
    }
    public void printStates() {
        DR.println(DriverStationLCD.Line.kUser2, 0, "Box1 State: " + getBox1State());
        DR.println(DriverStationLCD.Line.kUser3, 0, "Box2 State: " + getBox2State());
    }
    
}
