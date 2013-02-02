/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.misc;

/**
 *
 */
public class Constants {

    // Threshold below which joystick inputs will be ignored
    public static final double JOYSTICK_THRESHOLD = 0.2;

    /**
     * The servo angle, in degrees, used to load frisbee discs.
     *
     * Assume that the servo angle is linear with respect to the PWM value (big assumption, need to test).
     *
     * Servo angles that are out of the supported range of the servo simply "saturate" in that direction
     * In other words, if the servo has a range of (X degrees to Y degrees) than angles of less than X
     * result in an angle of X being set and angles of more than Y degrees result in an angle of Y being set.
     */
    public static final double MIN_LOADER_SERVO_ANGLE = -45.0;
    public static final double MAX_LOADER_SERVO_ANGLE = 65.0;
    public static final double SHOOTER_SPEED = .7;

}
