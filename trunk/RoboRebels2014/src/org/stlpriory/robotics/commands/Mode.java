/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands;

import org.stlpriory.robotics.misc.Constants;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 * @author William
 */
public class Mode {
    
    private int state;
    
    public Mode() {
        Debug.println("[Mode] Preparing to initialize");
        
        Debug.println("[Mode] Setting State to Default");
        state = Constants.ROBOT_DEFAULT_MODE;
        
        Debug.println("[Mode] Initialization Complete");
    }
    public int getState() {
        return state;
    }
    public void setStateManual() {
        state = Constants.ROBOT_MANUAL_MODE;
    }
    public void setStateAutomatic() {
        state = Constants.ROBOT_AUTOMATIC_MODE;
    }
    
    public void toggleState() {
        if (state == Constants.ROBOT_MANUAL_MODE) {
            state = Constants.ROBOT_AUTOMATIC_MODE;
        }
        else if (state == Constants.ROBOT_AUTOMATIC_MODE) {
            state = Constants.ROBOT_MANUAL_MODE;
        }
        else {
            Debug.println("[Mode] Error: state is not known, setting to default");
            state = Constants.ROBOT_DEFAULT_MODE;
        }
    }
    
}
