/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Wrapper class for the XBox Controller to
 * simplify interaction with the device
 *
 * Xbox buttons/axis	Action
 * A                    Tilt down
 * B	                Loader down
 * X	                Loader up
 * Y	                Tilt up
 * L Bump	            Lazy Susan left
 * R Bump	            Lazy Susan right
 * Back	                Bridge arm up
 * Start	            Bridge arm down
 * L Click
 * R Click	            Auto track on, off
 * Left axis            Drive (arcade)
 * Right axis           Shooter speed control (one axis)
 * Left trigger         Spinner start, reverse, stop
 * Right trigger        Shoot on, off
 *
 *  *
 * Axis
 *
 *  ¥1: Left Stick X Axis
 *    -Left:Negative ; Right: Positive
 *  ¥2: Left Stick Y Axis
 *    -Up: Negative ; Down: Positive
 *  ¥3: Triggers
 *    -Left: Positive ; Right: Negative
 *  ¥4: Right Stick X Axis
 *    -Left: Negative ; Right: Positive
 *  ¥5: Right Stick Y Axis
 *  -Up: Negative ; Down: Positive
 *  ¥6: Directional Pad (Not recommended, buggy)
 *
 *
 */
public class RRXboxController {

    // Declare objects needed for the robot that might be used
    // in more than one location
    Joystick m_xboxStick;

    public RRXboxController(Joystick j) {
        m_xboxStick = j;
    }

    public double getXAxisState_LeftStick() {
        double value = m_xboxStick.getRawAxis(1);
        RRLogger.logDebug(this.getClass(), "getXAxisState_LeftStick()", "value="+value);
        return value;
    }

    public double getYAxisState_LeftStick() {
        double value = m_xboxStick.getRawAxis(2);
        RRLogger.logDebug(this.getClass(), "getYAxisState_LeftStick()", "value="+value);
        return value;
    }

    public double getXAxisState_RightStick() {
        double value = m_xboxStick.getRawAxis(4);
        RRLogger.logDebug(this.getClass(), "getXAxisState_RightStick()", "value="+value);
        return value;
    }

    public double getYAxisState_RightStick() {
        double value = m_xboxStick.getRawAxis(5);
        RRLogger.logDebug(this.getClass(), "getYAxisState_RightStick()", "value="+value);
        return value;
    }

    public boolean getButtonState_A() {
        boolean value = m_xboxStick.getRawButton(1);
        RRLogger.logDebug(this.getClass(), "getButtonState_A()", "value="+value);
        return value;
    }

    public boolean getButtonState_B() {
        boolean value = m_xboxStick.getRawButton(2);
        RRLogger.logDebug(this.getClass(), "getButtonState_B()", "value="+value);
        return value;
    }

    public boolean getButtonState_X() {
        boolean value = m_xboxStick.getRawButton(3);
        RRLogger.logDebug(this.getClass(), "getButtonState_X()", "value="+value);
        return value;
    }

    public boolean getButtonState_Y() {
        boolean value = m_xboxStick.getRawButton(4);
        RRLogger.logDebug(this.getClass(), "getButtonState_Y()", "value="+value);
        return value;
    }

    public boolean getButtonState_LeftBumper() {
        boolean value = m_xboxStick.getRawButton(5);
        RRLogger.logDebug(this.getClass(), "getButtonState_LeftBumper()", "value="+value);
        return value;
    }

    public boolean getButtonState_RightBumper() {
        boolean value = m_xboxStick.getRawButton(6);
        RRLogger.logDebug(this.getClass(), "getButtonState_RightBumper()", "value="+value);
        return value;
    }

    public boolean getButtonState_LeftStick() {
        boolean value = m_xboxStick.getRawButton(9);
        RRLogger.logDebug(this.getClass(), "getButtonState_LeftStick()", "value="+value);
        return value;
    }

    public boolean getButtonState_RightStick() {
        boolean value = m_xboxStick.getRawButton(10);
        RRLogger.logDebug(this.getClass(), "getButtonState_RightStick()", "value="+value);
        return value;
    }
}
