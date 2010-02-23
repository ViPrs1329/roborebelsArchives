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
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

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
    
    Compressor compressor;
    Solenoid lockCylinderTail;
    Solenoid lockCylinderPiston;
    Solenoid shootingCylinderTail;
    Solenoid shootingCylinderPiston;

    boolean isCompressorOn = false; //boolean variable to check if the compressor is on or off
    boolean isButtonPressed = false; //Boolean set to false, as not to start anything crazy.
    private long lastKickTime = 0;

    /**
     * Create a new instance of the kicker class
     *
     * @param compChannel_1 Pressure switch channel
     * @param compChannel_2 Compressor relay channel
     * @param drivingChannel Driving cylinder relay channel
     * @param lockChannel Locking cylinder relay channel
     * @param shootingChannel Shooting cylinder relay channel
     */
    public RRKicker(int compChannel_1, int compChannel_2, int lockExpandChannel, int lockCompressChannel, int shootingExpandChannel, int shootingCompressChannel)
    {
        // TODO:  should we be using Solenoids instead of Relays?
        // The WPLib users guide (p. 34) suggests the use of Solenoids to simplify the
        // pneumatic system.  That we can control the pneumatic actuators directly
        // without the need for an additional relay. (In the past a Spike relay
        // was required along with a digital output port to control a pneumatics
        // component.)
        compressor = new Compressor(compChannel_1, compChannel_2);
        lockCylinderTail = new Solenoid(lockExpandChannel);
        lockCylinderPiston = new Solenoid(lockCompressChannel);
        shootingCylinderTail = new Solenoid(shootingExpandChannel);
        shootingCylinderPiston = new Solenoid(shootingCompressChannel);

        // Start up all systems associated with the kicking mechanism
        startUp();
    }

    /**
     * Startup the kicker.  This method will start the
     * compressor.  The compressor is stopped by default
     * and won't operate until it is started.
     */
    private void startUp() {
        if (! compressor.enabled()) {
            compressor.start();
        }
    }

    /**
     * Shutdown the kicker.  This method will stop the
     * compressor from turning on.
     */
    public void shutDown() {
        if (compressor.enabled()) {
            compressor.stop();
        }
    }

    /**
     * Return true if tthe system pressure is above the high
     * set point and the compressor is off.  This method
     * could be used to trigger a status light either on the
     * robot or the drivers control panel.
     *
     * @return true if the system is fully pressurized
     */
    public boolean isKickerReady() {
        // The Compressor class automatically creates a task that runs in the
        // background twice a second and turns the compressor on or off based
        // on the pressure switch value. If the system pressure is above the
        // high set point (Compressor.getPressureSwitchValue() == true), the
        // compressor turns off. If the pressure is below the low set point
        // (Compressor.getPressureSwitchValue() == false), the compressor
        // turns on.

        if (compressor.enabled()) {
            // If the pressure switch is above the high set point then we 
            // consider the system to be fully pressurized and ready to kick
            return compressor.getPressureSwitchValue();
        }
        return false;
    }

    /**
     * Execute a kick by proceeding through the sequence of steps on the 
     * pneumatic system required to perform the action:
     * <ol>
     * <li>extend the locking cylinder to engage the lock</li>
     * <li>compress the driving cylinder to disengage it</li>
     * <li>extend the shooting cylinder to load up the springs</li>
     * <li>compress the locking cylinder to fire the kicker </li>
     * <li>compress the shooting cylinder to reset the kicker</li>
     * <li>extend the driving cylinder to put the robot back in driving mode</li>
     * </ol>
     */
    public void kick() {
        // Progress through the steps needed to shoot.
        if (Timer.getUsClock() - lastKickTime >= 250000 || lastKickTime == 0)
        {
            expand(lockCylinderTail, lockCylinderPiston);
            expand(shootingCylinderTail, shootingCylinderPiston);

            compress(lockCylinderTail, lockCylinderPiston);
            compress(shootingCylinderTail, shootingCylinderPiston);
            lastKickTime = Timer.getUsClock();
        }
    }

        /*
     * This method compresses a cylinder with a relay.
     */
    private void compress(Solenoid s1, Solenoid s2)
    {
        //TODO: this will possibly need to be changed
        s1.set(false);
        s2.set(true);
    }


    /*
     * This method compresses a cylinder with a relay.
     */
    private void expand(Solenoid s1, Solenoid s2)
    {
        //TODO: this will possibly need to be changed
        s1.set(true);
        s2.set(false);
    }

    {/*
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

//    public void startUp()
//    {
//        while(true)
//        {
//
//            if (compressorCheck())
//            {
//                expand(drivingCylinder);
//                kickerButtonCheck();
//            }
//
//        }
//
//    }

//    public boolean compressorCheck()
//    {
//        int p = 100; //Pressure variable
//        //TODO:  This will change when we have the pressure sensor installed and applied.
//
//            if (p < 90) //checks to see if the compressor is under 90 psi
//            {
//                compressorOn(); //turns the compressor on
//                //wait however long it takes to get to a good pressure, or check the sensor continuously
//                //see when we have enough.
//                return false;
//            }
//
//            else if(p >= 115) //checks to see if the compressor is at or over 115 psi
//            {
//                compressorOff(); //turns the compressor off
//                return true;
//            }
//
//            return false;
//
//    }

    /*
     * The below method checks to see if a boolean variable (isButtonPressed) is true or false.
     * If it is true, it will set the canKick boolean variable to true, enabling the compressorLoop.
     * If it is not true, it will disable the compressor loop, or do nothing.
     *
     * -Luc Bettaieb
     */}

    
    {/*
 * This method goes through the kicking process.
 *
 * TODO:  There should be a wait function called in between the processes of expanding and compressing the cylinders
 * as to not cause a catastrophe.
 */
//    public void kick()
//    {
//        expand(lockCylinder);
//        compress(drivingCylinder);
//        expand(shootingCylinder);
//        compress(lockCylinder);
//        compress(shootingCylinder);
//        // expand(drivingCylinder);  This may not be needed
//    }


    /*
     * Checks to see if the compressor is off, and if it is off, it turns it on.
     */
//    private void compressorOn()
//    {
//        if (isCompressorOn == false)
//        {
//            compressor.set(Relay.Value.kOn);
//            //TODO: This will possibly need to be changed.
//            isCompressorOn = true;
//
//        }
//
//    }


    /*
     * Checks to see if the compressor is on, and if it is on, turns it off.
     */
//    private void compressorOff()
//    {
//        if (isCompressorOn == true)
//        {
//            compressor.set(Relay.Value.kOff);
//            //TODO: This will possibly need to be changed.
//            isCompressorOn = false;
//        }
//
//    }
    }
 
}