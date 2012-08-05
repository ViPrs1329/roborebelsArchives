/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

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

    private int spinner_channel;
    private double spinnerSpeed = 0.0;
    private double directionMultiplier = 1.0;
    private Victor spinnerVictor;
    private RRXboxController xboxController;

    /**
     * @param xbc XBox controller
     * @param sc Spinner channel
     */
    RRGatherer(RRXboxController xbc, int spinnerChannel) {
        if (xbc == null || spinnerChannel <= 0) {
            throw new NullPointerException("Invalid argument provided to RRGatherer constructor");
        }
        xboxController = xbc;
        spinner_channel = spinnerChannel;
        spinnerVictor = new Victor(spinner_channel);
    }

    private void gatherInputStates() {
        boolean toggleDirection = xboxController.getButtonState_A();
        if (toggleDirection) {
            directionMultiplier = -1.0 * directionMultiplier;
        }
        spinnerSpeed = directionMultiplier * xboxController.getYAxisState_RightStick();
        RRLogger.logDebug(this.getClass(), "gatherInputStates()", "directionMultiplier="+directionMultiplier+", spinnerSpeed=" + spinnerSpeed);
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
