/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Timer;


public class RRSpinner
{

    private boolean isKicking;
    private Victor victor;
    private long lastSetTime;

    public RRSpinner(int channel)
    {
        victor = new Victor(channel);
        lastSetTime = 0;
    }

    public boolean set(boolean value)
    {
        if (Timer.getUsClock() - lastSetTime >= 250000 || lastSetTime == 0)
        {
            System.out.println("set() - setting kicker state to: " + value);
            isKicking = value;
            lastSetTime = Timer.getUsClock();
            return true;
        }
        return false;
    }

    public boolean get()
    {
        return isKicking;
    }
    public void spin()
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
