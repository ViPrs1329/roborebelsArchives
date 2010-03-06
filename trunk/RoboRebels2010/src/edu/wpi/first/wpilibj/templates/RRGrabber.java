/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author Dave
 */
public class RRGrabber
{
    private boolean isSpinning;
    private long lastSpinTime;

    private final double speed = 1.0;
    private Victor victor;

    public RRGrabber( int channel )
    {
        victor = new Victor( channel );
        lastSpinTime = Timer.getUsClock();
    }
    public void spin( boolean spinClockwise )
    {
        if (Timer.getUsClock() - lastSpinTime >= 300000 || lastSpinTime == 0)
        {
            isSpinning = true;
            if( spinClockwise )
            {
                setSpeed( speed );
            }
            else
            {
                setSpeed( -speed );
            }
            lastSpinTime = Timer.getUsClock();
            //System.out.println("Enabling spin");
        }
    }
    public void stop()
    {
        setSpeed( 0.0 );
        isSpinning = false;
    }
    private void setSpeed( double newSpeed )
    {
        victor.set( newSpeed );
    }
    
    public double getSpeed()
    {
        return victor.get();
    }

    public boolean isSpinning()
    {
        return isSpinning;
    }
}