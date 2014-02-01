/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.Solenoid;

/**
 *
 * @author William
 */
class GearBox {
    
    Solenoid valve;
    
    public GearBox(int channel) {
        valve = new Solenoid(channel);
    }
    
    //return the state of the valve
    public boolean getState() {
        return valve.get();
    }
    
    //shift gears
    public void shift() {
        boolean a = getState();
        valve.set(!a);
    }
    
}
