/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Joystick;


/**
 *
 * @author Matt
 */
public class RRDeployer {
     Victor deployMotor;
     private     Joystick    xboxStick;
     private    Joystick    rightStick;
     Timer timer;
     boolean isDeploying = false;
     boolean activate = false;
     boolean bisDeploying = false;
     boolean bactivate = false;
    //the channel is 8
    public RRDeployer(int channel){
       deployMotor = new Victor(channel);
       timer = new Timer();
       isDeploying = false;
    }

    public void assignJoystick(Joystick s){
        xboxStick = s;
    }

    public void assignRightJoystick(Joystick s){
        rightStick = s;
    }

    public void deploy(){
        //use right stick for minibot deployment
        if (rightStick.getRawButton(6)){
            deployMotor.set(-rightStick.getRawAxis(2)*.75);
        }
        else {
            deployMotor.set(0);
        }

      


        

     }
}
