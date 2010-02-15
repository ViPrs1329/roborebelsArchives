
/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

/**
 * Objects that would be important to develop:
 *
 *   - Kicker object
 *     * Will most likely use the following objects:
 *
 *   - Pulley object
 *     * Will most likely use the following objects:
 */

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RoboRebels extends IterativeRobot {

    // Declare variable for the robot drive system
    RobotDrive m_robotDrive;		// robot will use PWM 1-4 for drive motors

    // Declare a variable to use to access the driver station object
    DriverStation m_ds;                     // driver station object
    DriverStationLCD m_dsLCD;
    AxisCamera cam;

    long        autonomousStartTime;

    // Declare variables for the two joysticks being used
    Joystick m_rightStick;			// joystick 1 (arcade stick or right tank stick)
    Joystick m_leftStick;			// joystick 2 (tank left stick)

    static final int NUM_JOYSTICK_BUTTONS = 16;
    boolean[] m_rightStickButtonState = new boolean[(NUM_JOYSTICK_BUTTONS+1)];
    boolean[] m_leftStickButtonState = new boolean[(NUM_JOYSTICK_BUTTONS+1)];
    boolean triggerPressed;
    boolean readingTrigger;
    double lastZValue;

    RRSpinner spinner;
    RRDrive drive;

    /**
     * Constructor
     */
    public void RoboRebels()
    {
        /**
         * Set up the following:
         *
         *   - Create a new RobotDrive object specifying 4 motors
         *   - Grab an instance of the DriveStation object
         *   - Create Joystick objects which map to the approprite modules
         *   - Set up Joystick button map array
         */

        System.out.println( "RoboRebels()" );
        
    }

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
    {
        System.out.println( "robotInit()" );
        m_robotDrive = new RobotDrive(1, 2, 3, 4);
        spinner = new RRSpinner(5);
    }

    public void disabledInit()
    {

    }

    public void autonomousInit()
    {
        System.out.println( "autonomousInit()" );
        drive = new RRDrive(m_robotDrive);
        autonomousStartTime = Timer.getUsClock();
    }

    public void teleopInit()
    {
        System.out.println( "teleopInit()" );
        m_rightStick = new Joystick(1);
        m_leftStick = new Joystick(2);
        drive = new RRDrive( m_robotDrive, m_rightStick, m_leftStick );

        /* Drive station code */
        m_ds = DriverStation.getInstance();
        m_dsLCD = DriverStationLCD.getInstance();
        readingTrigger = false;

        /*
         * Camera code.  Uncomment when we get a working camera
         */

        //Timer.delay(5.0);
        //cam = AxisCamera.getInstance();
        //cam.writeResolution(AxisCamera.ResolutionT.k160x120);
        //cam.writeBrightness(0);
    }

    /**
     * This function is called periodically during autonomous
     *
     * Notes:
     *
     *   - We can use the RobotDrive.drive() method to drive the robot programatically
     *   - Use kicker object
     *
     */
    
    public void autonomousPeriodic()
    {
        /*
         * Negative direction moves left
         */
        Watchdog.getInstance().feed();
        drive.drive(-0.25, -0.2);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic()
    {
        Watchdog.getInstance().feed();
        checkButtons();
        drive.drive(false);
        //processCamera();
    }

    /**
     * This function is called periodically during the disabled state
     *
     * What it needs to do:
     *
     *   - Feed the Watchdog
     */
    public void disabledPeriodic()
    {
        Watchdog.getInstance().feed();
        //System.out.println("Disabled State");
    }

    /**
     * The VM will try to call this function as often as possible during the autonomous state
     *
     */
    public void autonomousContinuous()
    {

    }

    /**
     * The VM will try to call this function as often as possible during the teleop state
     *
     */
    public void teleopContinuous()
    {

    }

    /**
     * The VM will try to call this function as often as possible during the disbabled state
     */
    public void disabledContinuous()
    {
        //System.out.println("Disabled State");

    }

    public void checkButtons()
    {


        //System.out.println( "checkButtons()" );

        /*
         * Check trigger code out.  It gets a little jumpy
         * when the trigger is held down for more than a
         * microsecond.
         */
        if(m_leftStick.getTrigger())
        {
            //kicker.set((triggerPressed == true) ? false : true);

            if ( m_leftStick.getTrigger() && spinner.isKicking() )
            {
                //spinner.set( false );
                spinner.rampDown();
            }
            else if ( m_leftStick.getTrigger() && ! spinner.isKicking() )
            {
                //spinner.set( true );
                spinner.rampUp();
            }

            //m_dsLCD.println(DriverStationLCD.Line.kUser2, 1, "Left trigger pressed!");
            //m_dsLCD.updateLCD();
            //System.out.println("Left trigger pressed!");
        }

        if ( lastZValue != m_leftStick.getZ() )
        {
            lastZValue = m_leftStick.getZ();
            spinner.setSpeedFromJoystick(lastZValue);
        }
        //System.out.println("joystick.getZ() = " + m_leftStick.getZ() );
    }




    /*
     * This function is currently unneeded as when you
     * get a camera instance in the init function
     * the camera works automatcally.
     */
    public void processCamera()
    {
        //System.out.println("processCamera()");
        
        try
        {
                if (cam.freshImage()) {
                    //System.out.println("    - got a fresh image!");
                    ColorImage image = cam.getImage();
                    image.free();
                }
            } catch (NIVisionException ex) {
                ex.printStackTrace();
            } catch (AxisCameraException ex) {
                ex.printStackTrace();
        }
        
    }

}