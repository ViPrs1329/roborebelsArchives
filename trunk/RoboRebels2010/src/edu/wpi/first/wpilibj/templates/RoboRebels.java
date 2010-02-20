
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
import edu.wpi.first.wpilibj.Joystick.ButtonType;
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
    double lastZValue;

    RRSpinner spinner;
    //RRPullup pullUP;
    RRDrive drive;

    /**
     * Constructor
     */
    public void RoboRebels()
    {
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

        // This was moved here because we were getting exceptions
        // whenever the robot was enabled, then disabled and then
        // enabled again
        spinner = new RRSpinner(5, 5, 100);
    }

    public void disabledInit()
    {
        spinner.rampDown();
    }

    public void autonomousInit()
    {
        System.out.println( "autonomousInit()" );
        //drive = new RRDrive(m_robotDrive);
        //autonomousStartTime = Timer.getUsClock();
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
     *
     * ---------------------
     * This is the most important method in this class
     * ---------------------
     */
    public void teleopPeriodic()
    {
        Watchdog.getInstance().feed();
        checkButtons();
        drive.drive(false);
        updateDSLCD();
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
        
    }

    /*
     * This method checks buttons and sets states accordingly
     */
    public void checkButtons()
    {


        //System.out.println( "checkButtons()" );

        if ( lastZValue != m_leftStick.getZ() && spinner.isSpinning() )
        {
            lastZValue = m_leftStick.getZ();
            spinner.setSpeedAndUpdateFromJoystick(lastZValue);
        }

        /*
         * Check trigger code out.  It gets a little jumpy
         * when the trigger is held down for more than a
         * microsecond.
         */


        if(m_leftStick.getTrigger())
        {
            //kicker.set((triggerPressed == true) ? false : true);

            if ( m_leftStick.getTrigger() && spinner.isSpinning() )
            {
                //spinner.set( false );
                spinner.rampDown();
            }
            else if ( m_leftStick.getTrigger() && ! spinner.isSpinning() )
            {
                //spinner.set( true );
                spinner.setSpeedFromJoystick(m_leftStick.getZ());
                spinner.rampUp();
            }
        }
    }
    /*public void pullUPcontrol()
        {
            if (m_leftStick.getButton(button3))
            {
            pullUP.extendArmStart ();
            }
            else
            {
            pullUP.extendArmStop();
            }
        }*/



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
    
    /*
     * Sends useful information to the LCD on the DriverStation
     */

    public void updateDSLCD()
    {
        m_dsLCD.println(DriverStationLCD.Line.kUser2, 1, "Spnr spd: " + Double.toString(spinner.getSpinnerSpeed()).substring(0, 3));
        m_dsLCD.println(DriverStationLCD.Line.kUser3, 1, "Pn Kr St: ");
        m_dsLCD.println(DriverStationLCD.Line.kUser4, 1, "Rbt spd : ");
        m_dsLCD.println(DriverStationLCD.Line.kUser5, 1, "Rbt slip: ");
        m_dsLCD.updateLCD();
    }

}