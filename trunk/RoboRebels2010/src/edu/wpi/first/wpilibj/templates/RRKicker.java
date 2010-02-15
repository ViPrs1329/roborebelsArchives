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
import edu.wpi.first.wpilibj.Relay;

public class RRKicker
{

    /* Threse are the objects for the compressor, driving cylinder, lock cylinder, and shooting cylinder spikes.
     * 1)The compressor is the air compressor which regulated the pressure, etc.
     * 2)The driving cylinder is the cylinder which moves the "boot" into a good position enabling the robot to go
     *   over the mound.
     * 3)The lock cylinder locks and unlockes the 'switch' which allows the springs to engage, making the kicker kick.
     * 4)The shooting cylinder drives the springs back, stretching them out, preparing them to be released by the
     *   'switch'
     */
    
    Relay compressor;
    Relay drivingCylinder;
    Relay lockCylinder;
    Relay shootingCylinder;

    boolean isCompressorOn = false; //boolean variable to check if the compressor is on or off

    //So these are the objects

    public RRKicker(int channel_1, int channel_2, int channel_3, int channel_4)
            //The constructor takes a channel for the location of the Spikes
    {
        compressor = new Relay(channel_1);
        drivingCylinder = new Relay(channel_2);
        lockCylinder = new Relay(channel_3);
        shootingCylinder = new Relay(channel_4);

        //And these are the locations of the Spikes which control the cylinders on the Sidecar
        //I've been told that there is a separate location for the Spikes on the board, so yeah.
    }

    /*
     * NOTE: I don't believe that we can use a main method in any
     * of our classes (unless you have been using this for on-
     * computer debugging; and if so does it work well?).  Perhaps
     * this code should be moved to the main Robot class,
     * RoboRebels.java.
     *
     * - Derek Ward
     */

    /*
    public static void main (String[]args)
    {
        RRKicker kicker = new RRKicker(1, 2, 3, 4);
        kicker.compressorLoop();

    }
    */


    /*
     * FIXME: 2/15/2010  The compressorLoop() method below continuously loops over
     * the entire kicking cycle which is not what we need for the competition.
     * We need to be able to trigger a kicking event from the console.  These
     * are the changes I think we need to make:
     * (1) Move the contents of the else block below into a new kick() method.
     * (2) Leave the logic in the compressorLoop() method related to continuously
     * checking the pressure levels in the tanks and turning the compressor on and off.
     * (3) Change the compressorOn() and compressorOff() methods to no longer use
     * the Relay class and instead make use of the edu.wpi.first.wpilibj.Compressor
     * class.
     * (4) Add a new kickerReady() method (?) that returns a boolean indicating if the
     * kicker/compressor has sufficient pressure to execute a kick.  We could use this
     * method trigger a light on the robot or on the console.
     *
     * QUESTION: Currently, the compressorLoop method is locked in an infinite
     * loop.  Remember that we are using a periodic calling convention for this
     * robot, so, unless this will be put into it's own thread this might not
     * work.   - Derek Ward
     *
     *
     *
     */
    public void compressorLoop()
    {
        int p = 50; //pressure variable

        expand(drivingCylinder); //Expands the driving cylinder so the robot can go over the bump

        while (true)
        {

            if (p < 40) //checks to see if the compressor is under 40 psi
            {
                compressorOn(); //turns the compressor on
            }

            else if(p >= 60) //checks to see if the compressor is at or over 60 psi
            {
                compressorOff(); //turns the compressor off
            }

            else
            {
                //These steps compress and expand the cylinders in the order needed to shoot.

                expand(lockCylinder);
                compress(drivingCylinder);
                expand(shootingCylinder);

                compress(lockCylinder);
                compress(shootingCylinder);

                expand(drivingCylinder);

            }

        }
    }

    private void compress(Relay relay) //Method to compress a cylinder with a relay
    {
        relay.set(Relay.Value.kOff);
        //TODO: this will possibly need to be changed
    }

    private void expand(Relay relay) //Method to expand a cylinder with a relay
    {
        relay.set(Relay.Value.kOn);
        //TODO: this will possibly need to be changed
    }

    /*
     * Checks to see if the compressor is off, and if it is off, it turns it on.
     */
    private void compressorOn() //checks if
    {
        if (isCompressorOn == false)
        {
            compressor.set(Relay.Value.kOn);
            isCompressorOn = true;
        }

    }

    /*
     * Checks to see if the compressor is on, and if it is on, turns it off.
     */
    private void compressorOff()
    {
        if (isCompressorOn == true)
        {
            compressor.set(Relay.Value.kOff);
            isCompressorOn = false;
        }

    }
 
}
