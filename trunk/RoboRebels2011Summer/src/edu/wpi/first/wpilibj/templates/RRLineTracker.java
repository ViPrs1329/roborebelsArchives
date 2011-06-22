/*
 * St. Louis Priory School Robotics Club
 * RoboRebels 2011
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;


public class RRLineTracker
{
   /*
    * Official FIRST Sample Code
    * https://gist.github.com/f06cd35355f51cf6c6c5
    *
    * Some ideas for how to organize a LineTracker class
    * http://www.chiefdelphi.com/forums/showpost.php?p=1020007&postcount=7
    */

    private DigitalInput left;
    private DigitalInput middle;
    private DigitalInput right;

    /**
     * Constructor
     * 
     * @param l Left digital input port number
     * @param m Middle digital input port number
     * @param r Right digital input port number
     */
    public RRLineTracker(int l, int m, int r)
    {
        left   = new DigitalInput(l);
        middle = new DigitalInput(m);
        right  = new DigitalInput(r);
    }

   /**
    * Returns "left", "middle", or "right".
    * Assumes that only one can be active at any given time.
    * (If the line trackers are only going to be used for the autonomous
    * period, then this probably isn't a harmful assumption.)
    * 
    * @return Returns a string representation of the 
    */
    public String activeSensor()
    {
        if (!left.get() && !middle.get() && !right.get())
            return "left-middle-right";
        if (!left.get() && !middle.get())
            return "left-middle";
        if (!right.get() && !middle.get())
            return "right-middle";
        if (!left.get() && !right.get())
            return "split";
        if (!left.get())
            return "left";
        if (!middle.get())
            return "middle";
        if (!right.get())
            return "right";
        
        return "none";
    }
    
    /**
     * Returns the value of the left line sensor
     * 
     * @return Returns a boolean of the left line sensor state
     */
    
    public boolean getL() {
        return left.get();
    }
    
    /**
     * Returns the value of the middle line sensor
     * 
     * @return Returns a boolean of the middle line sensor state
     */
    public boolean getM() {
        return middle.get();
    }
    
    /**
     * Returns the value of the right line sensor
     * 
     * @return Returns a boolean of the right line sensor state
     */
    
    public boolean getR() {
        return right.get();
    }

}
