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
import edu.wpi.first.wpilibj.DigitalInput;


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
    
    Compressor      compressor;
    Solenoid        lockCylinderTail;
    Solenoid        lockCylinderPiston;
    Solenoid        shootingCylinderTail;
    Solenoid        shootingCylinderPiston;
    DigitalInput    lockCylinderSensor;

    //private RRKickerThread m_task;
    private long lastKickTime = 0;
    private boolean kickerRun = true;
    private boolean isLoaded = false;
    private boolean shortKick = false;

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
        System.out.println("RRKicker()");
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
    public void startUp() {
        if (! compressor.enabled()) {
            compressor.start();
            setupCylinders();
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



    public void enableShortKick()
    {
        shortKick = true;
    }

    public void enableLongKick()
    {
        shortKick = false;
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

        // This method should not be used anymore

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
        System.out.println( "kick()" );
        if ( Timer.getUsClock() - lastKickTime >= 250000 || lastKickTime == 0 )
        {
            try
            {
                System.out.println( "kick() - actually kicking" );
                expand(shootingCylinderTail, shootingCylinderPiston);
                Thread.sleep(250);
                Watchdog.getInstance().feed();
                compress(lockCylinderTail, lockCylinderPiston);
                Thread.sleep(350);
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
        System.out.println( "loadKicker()" );

        /*
        if ( shortKick )
        {
            
        }
        else
        {
            expand(lockCylinderTail, lockCylinderPiston);
        }
        */

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
//        System.out.println( "setupCylinders()" );

        try
        {
            compress(lockCylinderTail, lockCylinderPiston);
            Thread.sleep(250); // TODO: Check UPDATE: 7/7/10 - does this still need to be here, Derek?  It's awkwardly in my IDE. lol.
            compress(shootingCylinderTail, shootingCylinderPiston);
            isLoaded = false;
        }
        catch ( InterruptedException e )
        {
            System.out.println( e.toString() );
        }
    }

    private void lockCylinder(Solenoid s1, Solenoid s2)
    {
        s1.set(false);
        s2.set(false);
    }

    /*
     * This method compresses a cylinder with a relay.
     */
    private void compress(Solenoid s1, Solenoid s2)
    {
        s1.set(false);
        s2.set(true);
    }


    /*
     * This method compresses a cylinder with a relay.
     */
    private void expand(Solenoid s1, Solenoid s2)
    {
        s2.set(false);
        s1.set(true);
    }
}