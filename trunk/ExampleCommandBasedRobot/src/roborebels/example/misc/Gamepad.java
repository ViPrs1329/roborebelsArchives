/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package roborebels.example.misc;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick;

/**
 *
 */
public class Gamepad extends Joystick implements Constants {

    /**
     * Gamepad contructor
     *     
* @param port
     */
    public Gamepad(int port) {
        super(port);
    }

    /**
     * @param port
     * @param numAxisTypes
     * @param numButtonTypes
     */
    public Gamepad(int port, int numAxisTypes, int numButtonTypes) {
        super(port, numAxisTypes, numButtonTypes);
    }

    /**
     * Get the button value for buttons 1 through 12.
     *     
* The buttons are returned in a single 16 bit value with one bit
     * representing the state of each button. The appropriate button is returned
     * as a boolean value.
     *     
* @param button The button number to be read.
     * @return The state of the button.
     */
    public boolean getButton(int button) {
        return super.getRawButton(button);
    }

    /**
     * For the current joystick, return the axis determined by the argument.
     *     
* This is for cases where the joystick axis is returned programatically,
     * otherwise one of the previous functions would be preferable (for example
     * getX()).
     *     
* @param axis The axis to read.
     * @return The value of the axis.
     */
    public double getAxis(int axis) {
        return scale(super.getRawAxis(axis));
    }

    /**
     * Get the X value of the joystick. This depends on the mapping of the
     * joystick connected to the current port.
     *     
* @param hand Unused
     * @return The X value of the joystick.
     */
    public double getX(Hand hand) {
        return scale(super.getX());
    }

    /**
     * Get the Y value of the joystick. This depends on the mapping of the
     * joystick connected to the current port.
     *     
* @param hand Unused
     * @return The Y value of the joystick.
     */
    public double getY(Hand hand) {
        return scale(super.getY());
    }

    /**
     * Get the Z value of the joystick. This depends on the mapping of the
     * joystick connected to the current port.
     *     
* @param hand Unused
     * @return The Z value of the joystick.
     */
    public double getZ(Hand hand) {
        return scale(super.getZ());
    }

    /**
     * Get the value of the throttle on the joystick where 0 being the bottom
     * and 1 being the top.
     *
     * @param axis The axis that the throttle is.
     * @return A scaled value of the throttle from 0.0 - 1.0.
     */
    public double getJoystickThrottle(int axis) {
        return scaleThrottle(-getAxis(axis));
    }

    /**
     * Scale the value of the throttle from 0.0 - 1.0 instead of -1.0 - 1.0
     *
     * @param x The throttle value
     * @return scaled throttle value
     */
    public double scaleThrottle(double x) {
        return (x + 1) / 2;
    }

    /**
     * Overwrite joystick values in a way that 0.0-1.0 is proportional to
     * 0.2-1.0
     *
     * @param x The joystick value that needs to be rounded up.
     */
    private double scale(double x) {
        if (Math.abs(x) < kJoystickThreshold) {
            return 0;
        }
        if (x > 0) {
            return (x - kJoystickThreshold) / (1 - kJoystickThreshold);
        }
        if (x < 0) {
            return (x + kJoystickThreshold) / (1 - kJoystickThreshold);
        }
        return 0;
    }
}
