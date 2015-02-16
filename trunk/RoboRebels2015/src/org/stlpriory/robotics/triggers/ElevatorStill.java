package org.stlpriory.robotics.triggers;

import org.stlpriory.robotics.Robot;
import org.stlpriory.robotics.utils.Keymap;

import edu.wpi.first.wpilibj.buttons.Trigger;

/**
 *
 */
public class ElevatorStill extends Trigger {
    
    public boolean get() {
    	boolean b1 = Robot.oi.getGamePad().getRawButton(Keymap.ELEVATOR_DOWN_BUTTON_KEY_MAP);
    	boolean b2 = Robot.oi.getGamePad().getRawButton(Keymap.ELEVATOR_UP_BUTTON_KEY_MAP);
        return (!b1&&!b2);
    }
}
