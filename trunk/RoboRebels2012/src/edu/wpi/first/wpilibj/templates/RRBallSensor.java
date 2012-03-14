/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.DigitalInput;
/**
 *
 * @author alan
 */
public class RRBallSensor {
    
    private DigitalInput shootBallSensor;   // shooting ball sensor at top of loader
    private DigitalInput loadBallSensor;    // loading ball sensor at bottom of loader
 
    public void ballSensorInit(int i, int j)
    {
        shootBallSensor   = new DigitalInput(i);
        loadBallSensor = new DigitalInput(j);
    }
   
    //returns the values of the ball sensors
    
    public boolean getShootSensor() {
        return shootBallSensor.get();
    }
    public boolean getLoadSensor() {
        return loadBallSensor.get();
    }

}
