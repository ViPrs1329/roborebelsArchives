/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.misc;

/**
 *
 * @author William
 * This Class is used to map the buttons on the xbox controller to various commands in OI.java
 */

/*
     * The bottons on the XBox controller follow this mapping
     * 1:  A
     * 2:  B
     * 3:  X
     * 4:  Y
     * 5:  Left Bumper
     * 6:  Right Bumper
     * 7:  Back
     * 8:  Start
     * 9:  Left thumbstick
     * 10: Right thumbstick
     *
     * The axis on the controller follow this mapping
     * (all output is between -1 to 1)
     * 1:  Left stick X axis  (left:negative, right:positve)
     * 2:  Left stick Y axis  (up:negative, down:positive)
     * 3:  Triggers           (left:positive, right:negative)
     * 4:  Right stick X axis (left:negative, right:positive)
     * 5:  Right stick Y axis (up:negative, down:positive)
     * 6:  Directional pad
     */
public class Keymap {
    
    public static final int LAUNCH_BUTTON_KEY_MAP = 6;
    public static final int RETRACT_BUTTON_KEY_MAP = 5;
    public static final int RESET_BUTTON_KEY_MAP = 3;
    public static final int SHIFT_BUTTON_KEY_MAP = 4;
    public static final int CLAW_EXPAND_RETRACT_BUTTON_KEY_MAP = 9;
    public static final int CLAW_START_STOP_BUTTON_KEY_MAP = 1;
    public static final int SENSOR_DISTANCE_BUTTON = 2;
    public static final int CLAW_WHEEL_EXPAND_RETRACT_BUTTON_KEY_MAP = 10;
    public static final int CLAW_LOW_GOAL_SHOOT_BUTTON_KEY_MAP = 8;
}
