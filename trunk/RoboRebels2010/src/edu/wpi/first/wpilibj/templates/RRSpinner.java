/*
 * This functions
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Timer;


public class RRSpinner
{

    private boolean isSpinning;
    private Victor victor;
    private long lastSetTime;
    private long lastRampTime;
    private double currentSpeed;
    private int rampUpIncrement;
    private int rampDownIncrement;

    public RRSpinner(int channel, int rUI, int rDI)
    {
        victor = new Victor(channel);
        lastSetTime = 0;
        lastRampTime = 0;
        rampUpIncrement = 5;
        rampDownIncrement = 100;
        rampUpIncrement = rUI;
        rampDownIncrement = rDI;
    }

    /*
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
    */

    public boolean isSpinning()
    {
        return isSpinning;
    }

    /*
    public void spin()
    {

        if(isKicking)
        {
            victor.set(currentSpeed);
        }
        else
        {
            victor.set(0.0);
        }

    }
     * */

    public boolean setSpeed( double s )
    {
        if ( s >= 0.0 && s <= 1.0 )
        {
            currentSpeed = s;
            return true;
        }
        return false;
    }

    public boolean setSpeedFromJoystick( double j )
    {
        if ( j >= -1.0 && j <= 0.9921875 )
        {
            currentSpeed = (j + 1.0) / 1.9921875;
            return true;
        }
        return false;
    }

    /*
     * This is used for ramping up and down the spinner.
     * Currently the motor is making some weird noises
     * when we ramp it down.  We beleive that this may
     * be a problem with the motor.
     */

    public void rampUp()
    {
        double setSpeed = 0.0;

        // check to see when the last time a ramp command was issued and go ahead if it has been more than 0.25 seconds
        if (Timer.getUsClock() - lastRampTime >= 250000 || lastRampTime == 0)
        {
            System.out.println("rampUp() - rampUpIncrement = " + rampUpIncrement + " currentSpeed = " + currentSpeed);
            for ( int i = 0; i <= rampUpIncrement; i++ )
            {
                //System.out.println("-- i = " + i);
                setSpeed = i * (currentSpeed / (double) rampUpIncrement);
                System.out.println("rampUp()::setSpeed = " + setSpeed);
                victor.set( setSpeed );
            }
            lastRampTime = Timer.getUsClock();
            isSpinning = true;
        }
        else
        {
            System.out.println("rampUp() - ramp function called too soon!");
        }
    }

    public void rampDown()
    {
        double setSpeed = 0.0;
        if (Timer.getUsClock() - lastRampTime >= 250000 || lastRampTime == 0)
        {
            System.out.println("rampDown() - rampDownIncrement = " + rampDownIncrement + " currentSpeed = " + currentSpeed);
            for ( int i = rampDownIncrement; i > 0; i-- )
            {
                //System.out.println("-- i = " + i);
                setSpeed = i * (currentSpeed / (double) rampDownIncrement);
                System.out.println("rampDown()::setSpeed = " + setSpeed);
                victor.set( setSpeed );
            }
            victor.set(0.0);
            lastRampTime = Timer.getUsClock();
            isSpinning = false;
        }
        else
        {
            System.out.println("rampDown() - ramp function called too soon!");
        }
    }


    public void setSpeedAndUpdate(double s)
    {
        if ( setSpeed(s) )
        {
            System.out.println("setSpeedAndUpdate() - Setting speed to: " + s);
            victor.set(s);
        }
    }

    public void setSpeedAndUpdateFromJoystick(double j)
    {
        if ( setSpeedFromJoystick(j) )
        {
            System.out.println("setSpeedAndUpdateFromJoystick() - Setting speed to: " + currentSpeed);
            victor.set(currentSpeed);
        }
    }
}
