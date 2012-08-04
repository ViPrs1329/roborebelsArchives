/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;

/**
 * The gatherer class has the following components:
 * 
 * - The spinner
 * - The ball storage system
 * - The ball sensor system
 * 
 * 
 * Joystick Buttons/axis	Action	        This button layout is if the auto ball sensing is not functional

 *
 *
 * TODO:
 * 
 * - TEST!
 * - Implement ball sensor system
 * - Implement autonomous extensions
 * 
 * @author dmw
 */
public class RRGatherer {

    private final double SPINNER_MAX_SPEED = 0.5;
    private final static int SPINNER_FORWARD = 3;
    private final static int SPINNER_REVERSED = 3;
    private int spinner_channel;
    private double spinnerSpeed = 0.0;
    private Victor spinnerVictor;
    private Joystick m_xboxStick;

    /**
     * @param j Joystick to get inputs from
     * @param sc Spinner channel
     */
    RRGatherer(Joystick j, int spinnerChannel) {
        if (j == null || spinnerChannel <= 0) {
            throw new NullPointerException("Invalid argument provided to RRGatherer constructor");
        }
        m_xboxStick = j;
        spinner_channel = spinnerChannel;
        spinnerVictor = new Victor(spinner_channel);
    }

    private void gatherInputStates() {
        double spinnerForward = m_xboxStick.getRawAxis(SPINNER_FORWARD);
        double spinnerReverse = m_xboxStick.getRawAxis(SPINNER_REVERSED);
        RRLogger.logDebug(this.getClass(), "gatherInputStates()", "Fwd Spinner=" + spinnerForward + ", Rev Spinner=" + spinnerReverse);

        if (spinnerForward <= 1.0 && spinnerForward > 0.0) {
            // Left trigger pushed, spin forward
            spinnerSpeed = -1.0 * SPINNER_MAX_SPEED;
        } else if (spinnerReverse >= -1.0 && spinnerReverse < 0.0) {
            spinnerSpeed = SPINNER_MAX_SPEED;
        } else if (spinnerForward == 0.0 || spinnerReverse == 0.0) {
            spinnerSpeed = 0.0;
        }
    }

    public void gather() {
        gatherInputStates();
        setGathererSpeeds();
    }

    public void gatherAuton() {
        setGathererSpeeds();
    }

    private void setGathererSpeeds() {
        spinnerVictor.set(spinnerSpeed);
    }
}
