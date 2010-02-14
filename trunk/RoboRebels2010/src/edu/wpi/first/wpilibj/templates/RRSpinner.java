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
        System.out.println("set() - setting kicker state to: " + value);
        if (Timer.getUsClock()- lastSetTime>=250000 || lastSetTime == 0)
        {
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
            System.out.println("spin() - enabling");
            victor.set(1.0);
        }
        else
        {
            System.out.println("spin() - disabling");
            victor.set(0.0);
        }

    }
}
