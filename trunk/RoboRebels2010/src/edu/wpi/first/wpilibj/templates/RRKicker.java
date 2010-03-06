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
 *      5 & 3
 *        
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.Joystick;


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
    Joystick controllingJoystick;

    private Thread m_task;
    private long lastKickTime = 0;
    private boolean kickerRun = true;
    private boolean isLoaded = false;

    /**
     * Create a new instance of the kicker class
     *
     * @param compChannel_1 Pressure switch channel
     * @param compChannel_2 Compressor relay channel
     * @param drivingChannel Driving cylinder relay channel
     * @param lockChannel Locking cylinder relay channel
     * @param shootingChannel Shooting cylinder relay channel
     */
    public RRKicker(int compChannel_1, int compChannel_2, int lockExpandChannel, int lockCompressChannel, int shootingExpandChannel, int shootingCompressChannel, Joystick j)
    {
        // TODO:  should we be using Solenoids instead of Relays?
        // The WPLib users guide (p. 34) suggests the use of Solenoids to simplify the
        // pneumatic system.  That we can control the pneumatic actuators directly
        // without the need for an additional relay. (In the past a Spike relay
        // was required along with a digital output port to control a pneumatics
        // component.)
        System.out.println("RRKicker()");
        compressor = new Compressor(compChannel_1, compChannel_2);
        lockCylinderTail = new Solenoid(lockExpandChannel);
        lockCylinderPiston = new Solenoid(lockCompressChannel);
        shootingCylinderTail = new Solenoid(shootingExpandChannel);
        shootingCylinderPiston = new Solenoid(shootingCompressChannel);

        // Start up all systems associated with the kicking mechanism
        startUp();
    }

    private class RRKickerThread extends Thread
    {
        RRKicker    kicker;
        boolean     triggerPressed = false,
                    kickerLoadedPressed = false,
                    kickerUnloadPressed = false;

        RRKickerThread(RRKicker k)
        {
            kicker = k;
        }

        public void run()
        {
            while (kickerRun) 
            {
                System.out.println( "RRKickerThread.run()");
                Watchdog.getInstance().feed();
                if(controllingJoystick.getTrigger() && triggerPressed == false)
                {
                    //System.out.println("checkButtons() - Trigger pressed | triggerPressed = " + triggerPressed);
                    triggerPressed = true;
                    if ( kicker.isKickerReady() && kicker.isKickerLoaded() )
                        {
                            System.out.println( "kicker.kick()" );
                            kicker.kick();
                        }
                }
                else if ( controllingJoystick.getTrigger() == false )       // check to see if the trigger has been depressed
                {
                    /*
                     * If so, the state is false
                     */
                    triggerPressed = false;
                }

                /* loads up the kicker (ie. gets it ready to kick) */
                if ( controllingJoystick.getRawButton(3) && kickerLoadedPressed == false )
                {
                    if ( kicker.isKickerLoaded() == false )
                    {
                        System.out.println( "kicker.loadKicker()" );
                        kicker.loadKicker();
                    }
                }
                else if ( controllingJoystick.getRawButton(3) == false )
                {
                    kickerLoadedPressed = false;
                }


                /*  safely unloads the kicker, without actually kicking */
                if ( controllingJoystick.getRawButton(4) && kickerUnloadPressed == false )
                {
                    System.out.println( "kicker.unloadKicker()" );
                    kicker.unloadKicker();
                    kickerUnloadPressed = true;
                }
                else
                {
                    kickerUnloadPressed = false;
                }
                Watchdog.getInstance().feed();
            }
        }
    }

    /**
     * Startup the kicker.  This method will start the
     * compressor.  The compressor is stopped by default
     * and won't operate until it is started.
     */
    private void startUp() {
        if (! compressor.enabled()) {
            compressor.start();
            setupCylinders();
        }

        // uncomment if you use the kicker thread
//        m_task = new RRKickerThread(this);
//        m_task.start();
    }

    /**
     * Shutdown the kicker.  This method will stop the
     * compressor from turning on.
     */
    public void shutDown() {
        if (compressor.enabled()) {
            compressor.stop();
        }

        // uncomment if you use the kicker thread
        //m_task.interrupt();
    }

    /**
     *
     */
    public void enable()
    {
        kickerRun = true;
    }

    /**
     *
     */
    public void disable()
    {
        kickerRun = false;
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

        /*
        if (compressor.enabled()) {
            // If the pressure switch is above the high set point then we 
            // consider the system to be fully pressurized and ready to kick
            return compressor.getPressureSwitchValue();
        }
        return false;
        */
        return true;
    }

    public boolean isKickerLoaded()
    {
        return isLoaded;
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
        if ( Timer.getUsClock() - lastKickTime >= 250000 || lastKickTime == 0 )
        {
            
            try
            {
                expand(shootingCylinderTail, shootingCylinderPiston);
                Thread.sleep(250); // TODO: Check
                Watchdog.getInstance().feed();
                //setupCylinders();
                compress(lockCylinderTail, lockCylinderPiston);
                Thread.sleep(250); // TODO: Check
                compress(shootingCylinderTail, shootingCylinderPiston);
                isLoaded = false;
                Watchdog.getInstance().feed();
            }
            catch ( InterruptedException e )
            {
                System.out.println( e.toString() );
            }

            
            lastKickTime = Timer.getUsClock();
        }
    }

    /**
     * Puts the kicker into a ready loaded state; ready to
     * be kicked.
     */
    public void loadKicker()
    {
        expand(lockCylinderTail, lockCylinderPiston);
        isLoaded = true;
    }

    /**
     * Safely unloads the kicker, without actually needing to kick.
     */
    public void unloadKicker()
    {
        compress(lockCylinderTail, lockCylinderPiston);
        isLoaded = false;
    }

    /**
     * Sets up the kicking cylinders into the default state:
     * in the compressed state (ie. not loaded)
     */
    public void setupCylinders()
    {
        try
        {
            compress(lockCylinderTail, lockCylinderPiston);
            Thread.sleep(250); // TODO: Check
            compress(shootingCylinderTail, shootingCylinderPiston);
            isLoaded = false;
        }
        catch ( InterruptedException e )
        {
            System.out.println( e.toString() );
        }
    }

        /*
     * This method compresses a cylinder with a relay.
     */
    private void compress(Solenoid s1, Solenoid s2)
    {
        //TODO: this will possibly need to be changed
//        System.out.println("compress() ["  + Timer.getUsClock() + "] " + s1 + " | " + s2);
//        System.out.println("           s1.get() = " + s1.get() );
        s1.set(false);
//        System.out.println("           s1.get() = " + s1.get() );
//        System.out.println("           s2.get() = " + s2.get() );
        s2.set(true);
//        System.out.println("           s2.get() = " + s2.get() );
    }


    /*
     * This method compresses a cylinder with a relay.
     */
    private void expand(Solenoid s1, Solenoid s2)
    {
        //TODO: this will possibly need to be changed
//        System.out.println("expand() [" + Timer.getUsClock() + "] " + s1 + " | " + s2);
//        System.out.println("           s2.get() = " + s2.get() );
        s2.set(false);
//        System.out.println("           s2.get() = " + s2.get() );
//        System.out.println("           s1.get() = " + s1.get() );
        s1.set(true);
//        System.out.println("           s1.get() = " + s1.get() );
    }
}