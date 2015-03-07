package org.stlpriory.robotics.triggers;

import org.stlpriory.robotics.Robot;
import org.stlpriory.robotics.utils.Keymap;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Trigger;

/**
 *
 */

public class ElevatorStill extends Trigger {

	private Joystick j;

	public ElevatorStill(Joystick j) {
		super();
		this.j = j;
	}

	public boolean get() {
		boolean b1 = j.getRawButton(Keymap.ELEVATOR_DOWN_BUTTON_KEY_MAP);
		boolean b2 = j.getRawButton(Keymap.ELEVATOR_UP_BUTTON_KEY_MAP);
		return ((!b1 && !b2) && Robot.elevator.isElevatorPulsing() && !j.getRawButton(Keymap.TOGGLE_PULSE));
	}
}
