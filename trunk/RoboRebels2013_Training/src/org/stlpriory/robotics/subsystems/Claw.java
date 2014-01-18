/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author John
 */
public class Claw extends Subsystem{
    
    protected static SpeedController loaderVictor;
    private static boolean loadBallIsFinished;
    
    public void initDefaultCommand() {
         // Set the default command for a subsystem here.
         //setDefaultCommand(new MySpecialCommand());
    } 
    public void loadBall(double speed){
        loaderVictor.set(speed);
    }
    public void unloadBall(double speed){
        loaderVictor.set(-speed);
    }
    public static boolean isLoadBallFinished() {
        return loadBallIsFinished;
    }

}