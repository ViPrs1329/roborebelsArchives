
/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

/*
    Joystick button map

trigger		-	kicks kicker
2		-	N/A
3		-	loads kicker
4		-	unloads kicker
5		-	N/A
6		-	extends arm
7		-	retracts arm
8		-	wind winch
9		-	unwind winch
10		-	change grabber direction
11		-	enables/disables grabber

 */

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.ButtonType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;

//it's a comment!
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RoboRebels extends IterativeRobot {

    // Declare custom object vars
    RobotDrive          m_robotDrive;		// robot will use PWM 1-4 for drive motors
    RRSpinner           spinner;                // spinner kicking device
    RRKicker            kicker;                 // pneumatic kicker device
    RRPullup            pullUP;                 // pullup device
    RRDrive             drive;                  // robot drive device
    RRGrabber           grabber;                // grabber device

    TrackerDashboard    trackerDashboard = new TrackerDashboard();

    // Declare a variable to use to access the driver station object
    DriverStation       m_ds;                   // driver station object
    DriverStationLCD    m_dsLCD;                // driver station LCD object
    AxisCamera          cam;                    // camera object

    DigitalInput        partialLoadSensor;


    long                autonomousStartTime;    // holds the start time for autonomous mode

    String              kickMethod;             // either 'spin' or 'pneumatics'

    Joystick            m_rightStick;		// joystick 1 (arcade stick or right tank stick)
    Joystick            m_leftStick;		// joystick 2 (tank left stick)


    static final int    NUM_JOYSTICK_BUTTONS = 16;  // how many joystick buttons exist?
    static boolean      disabledStateBroadcasted = false;
    static boolean      teleopStateBroadcasted = false;
    static boolean      autonomousStateBroadcasted = false;

    double              kScoreThreshold = .01;      // used in circle tracking code; default = 0.01
    double              targetTolerance = 1.0;      // used for target tracking

    double              autoDriveSpeed = 0.5,      // 0.0 - 1.0
                        autoTurnAngle = 0.3,
                        autoTurnDirection = 0.0;    // 1.0 for right, -1.0 for left
    

    boolean             triggerPressed = false,             // has the trigger been pressed?
                        kickerLoadedPressed = false,        // has the load kicker button been pressed?
                        kickerLoadPartialPressed = false,   // has the partial load kicker button been pressed?
                        kickerUnloadPressed = false,        // has the kicker unload button been pressed?
                        grabberEnabledPressed = false,      // has the enable grabber button been pressed?
                        grabberDirectionPressed = false;    // has the grabber direction change button been pressed?

    boolean             partialLoad = false;                // should we process a partial kicker load?

    boolean             foundTarget = false,                // have we found the target yet?
                        autoDrive = false;                  // are we in autonomous drive mode?

    boolean             tankDrive = false;

    double              lastZValue;                         // last Z value for the dial on the joystick
    double              robotDriveSensitivity = 0.25;       // sensitivity of the RobotDrive object

    boolean             grabberEnabled = false,             // is the grabber enabled?
                        grabberClockwise = false;            // what direction should it go?

    boolean             hasKicked = false;
    

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


        Watchdog.getInstance().setExpiration(0.75);

        kickMethod = "pneumatics";


        // Camera init code
        Timer.delay(5.0);
        cam = AxisCamera.getInstance();


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

        // This was moved here because we were getting exceptions
        // whenever the robot was enabled, then disabled and then
        // enabled again

        // Use PWM / Victor on channel 6 for arm, 7 for wench, 
        pullUP = new RRPullup(6, 7, 5.0, 0.54, 0.75);

        grabber = new RRGrabber( 8 );

        partialLoadSensor = new DigitalInput(2);
    }

    public void disabledInit()
    {
        if ( kickMethod.equals("spin") )
            if ( spinner != null )
                spinner.rampDown();
        else if ( kickMethod.equals("pneumatics") )
        {
            if ( kicker != null )
            {
//                System.out.println( "disabledInit() - Setting up kicker into a safe state" );
                kicker.setupCylinders();
                kicker.shutDown();
            }
        }

        grabberEnabled = false;

        teleopStateBroadcasted = false;
        autonomousStateBroadcasted = false;
    }

    public void autonomousInit()
    {
        System.out.println( "autonomousInit()" );

        disabledStateBroadcasted = false;
        teleopStateBroadcasted = false;

        grabberEnabled = true;
        hasKicked = false;
        
        initKicker();

        autonomousStartTime = Timer.getUsClock();
    }

    public void teleopInit()
    {
        System.out.println( "teleopInit()" );

        disabledStateBroadcasted = false;
        autonomousStateBroadcasted = false;

        m_rightStick = new Joystick(2);
        m_leftStick = new Joystick(1);
        if ( drive == null )
            drive = new RRDrive( m_robotDrive, m_rightStick, m_leftStick );

        /* Drive station code */
        m_ds = DriverStation.getInstance();
        m_dsLCD = DriverStationLCD.getInstance();

        if ( kickMethod.equals("spin") )
        {
            initSpinner();
        }
        else if( kickMethod.equals("pneumatics") )
        {
            initKicker();
        }
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
        long autoElapsedTime = Timer.getUsClock() - autonomousStartTime;
        

        Watchdog.getInstance().feed();

        processCamera();

        processGrabber();

        // drive for 1.5s, load the kicker
        if ( autoElapsedTime >= 0 && autoElapsedTime <= 1500000 )
        {
            System.out.println( "First pass" );
            //m_robotDrive.drive(autoDriveSpeed, 0.0);
            autoDrive = true;
            if ( kicker.isKickerLoaded() == false )
                kicker.loadKicker();
        }

        // stop the robot from driving after 1.5s
        if ( autoElapsedTime > 1500000 && autoElapsedTime <= 1750000 )
        {
            System.out.println( "Second pass" );
//            m_robotDrive.drive( 0.0, 0.0 );
            autoDrive = false;
        }

        // after the kicker has finished loading (after about 3.5s) kick
        if ( autoElapsedTime > 3500000 && autoElapsedTime <= 4000000 )
        {
            System.out.println( "Third pass" );
            if ( hasKicked == false )
            {
                kicker.kick();
                hasKicked = true;
            }
        }

        // after the kicker has kicked, load the kicker and drive forward again
        if ( autoElapsedTime > 4500000 && autoElapsedTime <= 5500000 )
        {
            System.out.println( "Fourth pass" );
            hasKicked = false;
            if ( kicker.isKickerLoaded() == false )
                kicker.loadKicker();
//            m_robotDrive.drive( autoDriveSpeed, 0.0 );
            autoDrive = true;
        }

        // stop robot from driving after 1s
        if ( autoElapsedTime > 5500000 && autoElapsedTime <= 5750000 )
        {
            System.out.println( "Fifth pass" );
//            m_robotDrive.drive( 0.0, 0.0 );
            autoDrive = false;
        }

        // kick again
        if ( autoElapsedTime > 8250000 && autoElapsedTime <= 9250000 )
        {
            System.out.println( "Sixth pass" );
            if ( hasKicked == false )
            {
                kicker.kick();
                hasKicked = true;
            }
        }

        if ( autoElapsedTime > 9500000 && autoElapsedTime <= 10250000 )
        {
            System.out.println( "Seventh pass" );
            hasKicked = false;
            if ( kicker.isKickerLoaded() == false )
                kicker.loadKicker();
//            m_robotDrive.drive( autoDriveSpeed, 0.0 );
            autoDrive = true;
        }

        if ( autoElapsedTime > 10250000 && autoElapsedTime <= 10500000 )
        {
            System.out.println( "Eighth pass" );
//            m_robotDrive.drive( 0.0, 0.0 );
            autoDrive = false;

        }

        if ( autoElapsedTime > 12500000 && autoElapsedTime <= 110000000 )
        {
            System.out.println( "Ninth pass" );
            if ( hasKicked == false )
            {
                kicker.kick();
                hasKicked = true;
            }
        }

        if ( autonomousStateBroadcasted == true )
        {
            System.out.println( "Autonomous State" );
            autonomousStateBroadcasted = false;
        }
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
        //System.out.println( "telopPeriodic()" );
        Watchdog.getInstance().feed();

        if ( teleopStateBroadcasted == true )
        {
            System.out.println( "Teleop State" );
            teleopStateBroadcasted = false;
        }

        checkButtons();
        if ( tankDrive == true )
            drive.drive(true);
        else
            drive.drive(false);
        updateDSLCD();
        processCamera();
        processGrabber();
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
        if ( kicker != null )
            kicker.setupCylinders();
        
        if ( disabledStateBroadcasted == true )
        {
            System.out.println( "Disabled State" );
            disabledStateBroadcasted = false;
        }
    }

    /**
     * The VM will try to call this function as often as possible during the autonomous state
     *
     */
    public void autonomousContinuous()
    {
        if ( autoDrive )
            m_robotDrive.drive(-1.0 * autoDriveSpeed, autoTurnDirection * autoTurnAngle);
        else
            m_robotDrive.drive(0.0, 0.0);
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
            if ( lastZValue != m_rightStick.getZ() && spinner.isSpinning() )
            {
                lastZValue = m_rightStick.getZ();
                spinner.setSpeedAndUpdateFromJoystick(lastZValue);
            }
        }

        /*
         * Spinner and the pneumatic kicker are activated via the joystick trigger
         *
         * If the joystick trigger has been pressed and the state is false
         * ...
         */

        // --------------------------------------
        // ------ joystick kicking code begin
        // ------ comment out if you use threads
        
        
        if(m_rightStick.getTrigger() && triggerPressed == false)
        {
            //System.out.println("checkButtons() - Trigger pressed | triggerPressed = " + triggerPressed);
            triggerPressed = true;


            if ( kickMethod.equals( "spin" ) )
            {
                if ( m_rightStick.getTrigger() && spinner.isSpinning() )
                {
                    //System.out.println( "rampDown()");
                    spinner.rampDown();
                }
                else if ( m_rightStick.getTrigger() && ! spinner.isSpinning() )
                {
                    //System.out.println( "rampUp()" );
                    spinner.setSpeedFromJoystick(m_rightStick.getZ());
                    spinner.rampUp();
                }
            }
            else if ( kickMethod.equals( "pneumatics" ) )
            {
                if ( kicker.isKickerReady() && kicker.isKickerLoaded() )
                {
                    grabber.stop();
                    kicker.kick();
                    grabber.spin( grabberClockwise );
                }
            }
        }
        else if ( m_rightStick.getTrigger() == false )       // check to see if the trigger has been depressed
        {
            
            triggerPressed = false;
        }

        // loads up the kicker (ie. gets it ready to kick)
        if ( m_rightStick.getRawButton(3) && kickerLoadedPressed == false )
        {
            if ( kickMethod.equals( "pneumatics" ) )
            {
                if ( kicker.isKickerLoaded() == false )
                {
                    kicker.loadKicker();
                }
            }
        }
        else if ( m_rightStick.getRawButton(3) == false )
        {
            kickerLoadedPressed = false;
        }


        //  safely unloads the kicker, without actually kicking
        if ( m_rightStick.getRawButton(4) && kickerUnloadPressed == false )
        {
            kicker.unloadKicker();
            kickerUnloadPressed = true;
        }
        else
        {
            kickerUnloadPressed = false;
        }



        
        // ------ comment out if you use threads
        // ------ joystick kicking code end
        // --------------------------------------


        if (m_rightStick.getZ() <= 0)
        {    // Logitech Attack3 has z-polarity reversed; up is negative
            // arcade mode
            tankDrive = false;
        }
        else
        {
            // tank drive
            tankDrive = true;
        }


        // Arm extending code
        if (m_rightStick.getRawButton(6))
        {
            //System.out.println("***** Extending arm start");
            pullUP.extendArmStart();
        }

        if (m_rightStick.getRawButton(7))
        {
            //System.out.println("***** Retract arm start");
            pullUP.retractArmStart();
        }

        if ( !m_rightStick.getRawButton(7) && !m_rightStick.getRawButton(6) )
        {
            //System.out.println("***** Extend or retract arm stop");
            pullUP.extendArmStop();
        }

        
        // Wench handling
        if (m_rightStick.getRawButton(8))
        {
            //System.out.println("***** Winch wind start");
            pullUP.windWinchStart();
        }

        if (m_rightStick.getRawButton(9))
        {
            //System.out.println("***** Winch unwind start");
            pullUP.unwindWinchStart();
        }

        if ( !m_rightStick.getRawButton(8) && !m_rightStick.getRawButton(9) )
        {
            //System.out.println("***** Wind or unwind wench stop");
            pullUP.windWinchStop();
        }

        // Grabber handling
        /*
         *  grabberEnabledPressed = false,
            grabberDirectionPressed = false;
         */

        // enable grabber
        if (m_rightStick.getRawButton(11) && grabberEnabledPressed == false)
        {
            if ( grabberEnabled )
                grabberEnabled = false;
            else
                grabberEnabled = true;

            grabberEnabledPressed = true;

            System.out.println( "grabber enable : " + grabberEnabled );
        }
        else if ( !m_rightStick.getRawButton(11))
        {
            grabberEnabledPressed = false;
        }

        // switch between clockwise or counter clockwise rotation
        if (m_rightStick.getRawButton(10) && grabberDirectionPressed == false)
        {
            if ( grabberClockwise )
                grabberClockwise = false;
            else
                grabberClockwise = true;

            grabberDirectionPressed = true;

            System.out.println( "grabber clockwise : " + grabberClockwise );
        }
        else if (!m_rightStick.getRawButton(10))
        {
            grabberDirectionPressed = false;
        }
    }

    /*
     * Handle grabber states
     */
    public void processGrabber()
    {
        if ( grabberEnabled )
        {
            //System.out.println("Setting grabber speed");
            grabber.spin( grabberClockwise );
        }
        else
        {
            grabber.stop();
        }
    }

    

    public void initSpinner()
    {
        if ( spinner == null )
                spinner = new RRSpinner(5, 5, 25);
    }

    public void initKicker()
    {
        //Change these to correct channels.
        //In order: Pressure switch channel, compressor relay channel, driving cylinder relay channel,
        //locking cylinder relay channel, and shooting cylinder relay channel.
        System.out.println("Making kicker");
        if ( kicker == null )
            kicker = new RRKicker(1, 1, 1, 2, 3, 4);
        else
            kicker.startUp();

    }


    /*
     * This function is currently unneeded as when you
     * get a camera instance in the init function
     * the camera works automatcally.
     */
    
    public void processCamera()
    {
//        System.out.println("processCamera()");

        try
        {
            if ( cam.freshImage() )
            {
                ColorImage image = cam.getImage();
                image.free();
            }
        }
        catch (NIVisionException ex)
        {
                ex.printStackTrace();
        }
        catch (AxisCameraException ex)
        {
                ex.printStackTrace();
        }
    }

        /*
        try {
                if (cam.freshImage()) {// && turnController.onTarget()) {
                    System.out.println("processCamera() - Got a fresh image");
                    //double gyroAngle = gyro.pidGet();
                    ColorImage image = cam.getImage();
                    Thread.yield();
                    Target[] targets = Target.findCircularTargets(image);
                    Watchdog.getInstance().feed();
                    Thread.yield();
                    image.free();
                    if (targets.length == 0 || targets[0].m_score < kScoreThreshold)
                    {
                        System.out.println("No target found");
                        Target[] newTargets = new Target[targets.length + 1];
                        newTargets[0] = new Target();
                        newTargets[0].m_majorRadius = 0;
                        newTargets[0].m_minorRadius = 0;
                        newTargets[0].m_score = 0;

                        for (int i = 0; i < targets.length; i++)
                        {
                            newTargets[i + 1] = targets[i];
                        }

                        autoDrive = true;
                        autoTurnDirection = 1.0;  // turn right
                        foundTarget = false;

                        trackerDashboard.updateVisionDashboard(0.0, 0.0, 0.0, 0.0, newTargets);
                    } 
                    else
                    {
                        Watchdog.getInstance().feed();
                        //System.out.println(targets[0]);
                        System.out.println("Target Angle: " + targets[0].getHorizontalAngle());
                        System.out.println("Target tolerance difference: " + (targets[0].getHorizontalAngle() - targetTolerance));
                        //turnController.setSetpoint(gyroAngle + targets[0].getHorizontalAngle());
                        //
                        // Use getHorizontalAngle() to determine if the robot is lined up with
                        // the target or not.  When it gets close to 0.0 it is lined up.  Use
                        // a tolerance!!!!
                        //

                        if ( targets[0].getHorizontalAngle() < targetTolerance && targets[0].getHorizontalAngle() > (-1.0 * targetTolerance) )
                        {
                            // if it is inside the target tolerance then stop
                            System.out.println("processCamera() - Found target.  IT IS CENTERED" );
                            autoDrive = false;
                            foundTarget = true;

                        }
                        else
                        {
                            // if it is outside the target tolerance then move it
                            System.out.println("processCamera() - Found target, but it is not centered");
                            autoDrive = true;
                            if ( targets[0].getHorizontalAngle() > 0.0 )
                                autoTurnDirection = 1.0;
                            else if ( targets[0].getHorizontalAngle() < 0.0 )
                                autoTurnDirection = -1.0;
                        }

                        trackerDashboard.updateVisionDashboard(0.0, 0.0, 0.0, targets[0].m_xPos / targets[0].m_xMax, targets);
                    }
                }
            } catch (NIVisionException ex) {
                ex.printStackTrace();
            } catch (AxisCameraException ex) {
                ex.printStackTrace();
            }
        */
    
    
    /*
     * Sends useful information to the LCD on the DriverStation
     */

    public void updateDSLCD()
    {
        m_dsLCD.println(DriverStationLCD.Line.kUser2, 1, "Kkr loaded: ");
        m_dsLCD.println(DriverStationLCD.Line.kUser3, 1, "Pn Kr St: ");
        m_dsLCD.println(DriverStationLCD.Line.kUser4, 1, "Rbt spd : ");
        m_dsLCD.println(DriverStationLCD.Line.kUser5, 1, "Rbt slip: ");
        m_dsLCD.updateLCD();
    }

}
