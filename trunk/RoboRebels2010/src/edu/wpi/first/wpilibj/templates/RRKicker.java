/*
 * St. Louis Priory School Robotics Club
 * RoboRebels 2010
 *
 * This class implements the kicker object which is
 * in charge of encapsulating all kicker functionality.
 *
 * Maintainer:  Luc Bettaieb
 *
 * Objects needed:
 *
 *      - Solenoid (for releasing kicking mechanism), Pneumatic contollers,
 *        
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Victor;

public class RRKicker {
    private boolean isKicking;
    private Victor victor;
    
    public RRKicker(int channel)
    {
        victor = new Victor(channel);
    }

    public void set(boolean value)
    {
        isKicking = value;
    }
    
    public boolean get()
    {
        return isKicking;
    }
    public void kick()
    {
        if(isKicking)
        {
            victor.set(1.0);
        }
        else
        {
            victor.set(0.0);
        }
    }
}
