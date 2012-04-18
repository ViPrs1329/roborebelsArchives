/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author dmw
 */
public class RRAction {

    int rrActionID;
    int buttonID;
    int axisID;
    Joystick js;

    public RRAction(int rraid, int bid, int aid, Joystick j) {
        rrActionID = rraid;
        buttonID = bid;
        axisID = aid;

        if (j != null) {
            js = j;
        } else {
            throw new NullPointerException("RRAction was passed a null Joystick object (j)! ");
        }
    }

    public boolean valueOf() {
        return js.getRawButton(buttonID);
    }

    public boolean getButtonState() {
        return js.getRawButton(buttonID);
    }

    public double getAxisState() {
        return js.getRawAxis(axisID);
    }
}
