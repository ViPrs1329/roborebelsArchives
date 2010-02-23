
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
    String kickMethod; //either 'spin' or 'pneumatics'

    // Declare variables for the two joysticks being used
    Joystick m_rightStick;			// joystick 1 (arcade stick or right tank stick)
    Joystick m_leftStick;			// joystick 2 (tank left stick)


    static final int NUM_JOYSTICK_BUTTONS = 16;
    boolean[] m_rightStickButtonState = new boolean[(NUM_JOYSTICK_BUTTONS+1)];
    boolean[] m_leftStickButtonState = new boolean[(NUM_JOYSTICK_BUTTONS+1)];
    boolean triggerPressed;
    double lastZValue;
    double robotDriveSensitivity = 0.25;



    RRSpinner spinner;
    RRKicker kicker;
    RRPullup pullUP;
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

        // front left, rear left, front right, rear right

        /*
         *
         *
         *     ^
         *     |
         * +-------+
         * |J2   J4|
         *L|J1   J3|R
         * |       |
         * |       |
         * +-------+
         *
         * Match jag numbers with the dig i/o ports
         *
         */
        
        m_robotDrive = new RobotDrive(4, 3, 2, 1, robotDriveSensitivity);
        //m_robotDrive = new RobotDrive(2, 1, 4, 3, robotDriveSensitivity);

        // This was moved here because we were getting exceptions
        // whenever the robot was enabled, then disabled and then
        // enabled again

        kickMethod = "pneumatics";

        if ( kickMethod.equals("spin") )
        {
            spinner = new RRSpinner(5, 5, 25);
        }
        else if( kickMethod.equals("pneumatics") )
        {
            //Change these to correct channels.
            //In order: Pressure switch channel, compressor relay channel, driving cylinder relay channel,
            //locking cylinder relay channel, and shooting cylinder relay channel.
            kicker = new RRKicker(1, 1, 1, 2, 3, 4);
        }


        pullUP = new RRPullup(6, 7, 1.0, 0.2, 0.75);
    }

    public void disabledInit()
    {
        if ( kickMethod.equals("spin") )
            spinner.rampDown();
        else if ( kickMethod.equals("pneumatics") )
            kicker.shutDown();
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
        m_rightStick = new Joystick(2);
        m_leftStick = new Joystick(1);
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
        drive.drive(0.25, -0.2);
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

        if ( kickMethod.equals("spin") )
        {
            if ( lastZValue != m_leftStick.getZ() && spinner.isSpinning() )
            {
                lastZValue = m_leftStick.getZ();
                spinner.setSpeedAndUpdateFromJoystick(lastZValue);
            }
        }

        /*
         * Spinner and thepneumatic kicker are activated via the joystick trigger
         */

        if(m_leftStick.getTrigger())
        {
            if ( kickMethod.equals( "spin" ) )
            {
                if ( m_leftStick.getTrigger() && spinner.isSpinning() )
                {
                    //System.out.println( "rampDown()");
                    spinner.rampDown();
                }
                else if ( m_leftStick.getTrigger() && ! spinner.isSpinning() )
                {
                    //System.out.println( "rampUp()" );
                    spinner.setSpeedFromJoystick(m_leftStick.getZ());
                    spinner.rampUp();
                }
            }
            else if ( kickMethod.equals( "pneumatics" ) )
            {
                if ( kicker.isKickerReady() )
                {
                    kicker.kick();
                }
            }
        }


        // Arm extending code
        if (m_leftStick.getRawButton(3))
        {
            //System.out.println("***** Extending arm start");
            pullUP.extendArmStart();
        }

        if (m_leftStick.getRawButton(2))
        {
            //System.out.println("***** Retract arm start");
            pullUP.retractArmStart();
        }

        if ( !m_leftStick.getRawButton(3) && !m_leftStick.getRawButton(2) )
        {
            pullUP.extendArmStop();
        }

        
        // Wench handling
        if (m_leftStick.getRawButton(4))
        {
            //System.out.println("***** Winch wind start");
            pullUP.windWinchStart();
        }

        if (m_leftStick.getRawButton(5))
        {
            //System.out.println("***** Winch unwind start");
            pullUP.unwindWinchStart();
        }

        if ( !m_leftStick.getRawButton(4) && !m_leftStick.getRawButton(5) )
        {
            pullUP.windWinchStop();
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
        if ( kickMethod.equals("spin") )
        {
            m_dsLCD.println(DriverStationLCD.Line.kUser2, 1, "Spnr spd: " + Double.toString(spinner.getSpinnerSpeed()).substring(0, 3));
        }
        m_dsLCD.println(DriverStationLCD.Line.kUser3, 1, "Pn Kr St: ");
        m_dsLCD.println(DriverStationLCD.Line.kUser4, 1, "Rbt spd : ");
        m_dsLCD.println(DriverStationLCD.Line.kUser5, 1, "Rbt slip: ");
        m_dsLCD.updateLCD();
    }

}
