/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author Dave
 */
public class RRGrabber
{
    private boolean isSpinning;
    private boolean clockwise;

    private final int speed = 1;
    private Victor victor;

    public RRGrabber( int channel )
    {
        victor = new Victor( channel );
    }
    public void spin( boolean spinClockwise)
    {
        isSpinning = true;
        if( spinClockwise )
        {
            clockwise = true;
            setSpeed( speed );
        }
        else
        {
            clockwise = false;
            setSpeed( -speed );
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
}