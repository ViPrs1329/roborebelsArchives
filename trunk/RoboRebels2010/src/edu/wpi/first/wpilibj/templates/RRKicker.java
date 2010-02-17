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
    boolean isButtonPressed = false; //Boolean set to false, as not to start anything crazy.

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
     *
     *
     * 
     * QUESTION: Currently, the compressorLoop method is locked in an infinite
     * loop.  Remember that we are using a periodic calling convention for this
     * robot, so, unless this will be put into it's own thread this might not
     * work.  You may want to consider using states like in the spinner or
     * pullup object.               - Derek Ward
     *
     *
     *
     */


    /*
     *  FIXME: I'm not sure if this will work or if it what you're actually talking about.
     * -Luc Bettaieb
     */

    public void startUp()
    {
        while(true)
        {

            if (compressorCheck())
            {
                expand(drivingCylinder);
                kickerButtonCheck();
            }

        }

    }

    public boolean compressorCheck()
    {
        int p = 100; //Pressure variable
        //TODO:  This will change when we have the pressure sensor installed and applied.

            if (p < 90) //checks to see if the compressor is under 90 psi
            {
                compressorOn(); //turns the compressor on
                //wait however long it takes to get to a good pressure, or check the sensor continuously
                //see when we have enough.
                return false;
            }

            else if(p >= 115) //checks to see if the compressor is at or over 115 psi
            {
                compressorOff(); //turns the compressor off
                return true;
            }
        
            return false;

    }

    /*
     * The below method checks to see if a boolean variable (isButtonPressed) is true or false.
     * If it is true, it will set the canKick boolean variable to true, enabling the compressorLoop.
     * If it is not true, it will disable the compressor loop, or do nothing.
     *
     * -Luc Bettaieb
     */
    public void kickerButtonCheck() //Method to see if the kicker is ready or not with the button on the joystick
    {
        if (isButtonPressed == true) //Checks if the kicker is ready by seeing if the button is pressed or not
        {
            kick();
        }
            
    }

    
/*
 * This method goes through the kicking process.
 *
 * TODO:  There should be a wait function called in between the processes of expanding and compressing the cylinders
 * as to not cause a catastrophe.
 */
    public void kick()
    {
        expand(lockCylinder);
        compress(drivingCylinder);
        expand(shootingCylinder);
        compress(lockCylinder);
        compress(shootingCylinder);
        // expand(drivingCylinder);  This may not be needed
    }  

    /*
     * This method compresses a cylinder with a relay.
     */
    private void compress(Relay relay)
    {
        relay.set(Relay.Value.kOff);
        //TODO: this will possibly need to be changed
    }


    /*
     * This method compresses a cylinder with a relay.
     */
    private void expand(Relay relay)
    {
        relay.set(Relay.Value.kOn);
        //TODO: this will possibly need to be changed
    }


    /*
     * Checks to see if the compressor is off, and if it is off, it turns it on.
     */
    private void compressorOn()
    {
        if (isCompressorOn == false)
        {
            compressor.set(Relay.Value.kOn);
            //TODO: This will possibly need to be changed.
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
            //TODO: This will possibly need to be changed.
            isCompressorOn = false;
        }

    }
 
}