/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Victor;


public class RRSpinner
{

    private boolean isKicking;
    private Victor victor;

    public RRSpinner(int channel)
    {
        victor = new Victor(channel);
    }

    public void set(boolean value)
    {
        System.out.println("set() - setting kicker state to: " + value);
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
            System.out.println("kick() - enabling");
            victor.set(1.0);
        }
        else
        {
            System.out.println("kick() - disabling");
            victor.set(0.0);
        }

    }
}
