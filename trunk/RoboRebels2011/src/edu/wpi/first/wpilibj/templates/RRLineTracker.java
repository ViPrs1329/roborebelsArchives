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

   /*
    * Notes
    * -----
    *
    * If we want to keep the driving logic in RoboRebels.java, then we might
    * want to have this class only return a suggestion on what the motors
    * should be set to.
    *
    * Is it worth designing for a scenario where the robot is perpendicular
    * to the line?  This probably won't happen in autonomous, and would require
    * a decent amount of extra effort.
    */

    private DigitalInput left;
    private DigitalInput middle;
    private DigitalInput right;

    public RRLineTracker()
    {
        left   = new DigitalInput(1);
        middle = new DigitalInput(2);
        right  = new DigitalInput(3);
    }

   /*
    * Returns "left", "middle", or "right".
    * Assumes that only one can be active at any given time.
    * (If the line trackers are only going to be used for the autonomous
    * period, then this probably isn't a harmful assumption.)
    */
    public String activeSensor()
    {
        if (left.get())
            return "left";
        if (middle.get())
            return "middle";
        if (right.get())
            return "right";
        
        return "none";
    }
}
