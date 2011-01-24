
/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

/*
    Joystick button map



*/

/*
 * NOTES:
 *
 * - Watchdog is no longer required, however, we may want to use it
 *   if we feel that need for a that bit of safety.
 *
 * - There is code within the RobotDrive class which can handle
 *   Mecanum wheels!  This will help us out a lot.
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
import com.sun.squawk.util.MathUtils;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RoboRebels extends IterativeRobot {

    // Declare custom object vars
    RRMecanumDrive      mecanumDrive;

    TrackerDashboard    trackerDashboard = new TrackerDashboard();

    // Declare a variable to use to access the driver station object
    DriverStation       m_ds;                   // driver station object
    DriverStationLCD    m_dsLCD;                // driver station LCD object
    AxisCamera          cam;                    // camera object

    DigitalInput        partialLoadSensor;


    double                autonomousStartTime;    // holds the start time for autonomous mode


    Joystick            m_rightStick;		// joystick 1 (arcade stick or right tank stick)
    Joystick            m_leftStick;		// joystick 2 (tank left stick)
    Joystick            m_xboxStick;


    static final int    NUM_JOYSTICK_BUTTONS = 16;  // how many joystick buttons exist?
    static boolean      disabledStateBroadcasted = false;
    static boolean      teleopStateBroadcasted = false;
    static boolean      autonomousStateBroadcasted = false;

    double              kScoreThreshold = .01;      // used in circle tracking code; default = 0.01
    double              targetTolerance = 1.0;      // used for target tracking
    


    boolean             foundTarget = false,                // have we found the target yet?
                        autoDrive = false;                  // are we in autonomous drive mode?


    double              lastZValue;                         // last Z value for the dial on the joystick
    double              robotDriveSensitivity = 0.25;       // sensitivity of the RobotDrive object

    

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


        //Watchdog.getInstance().setExpiration(0.75);



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

  
        mecanumDrive = new RRMecanumDrive(3, 4,1,2, m_xboxStick);
    
        partialLoadSensor = new DigitalInput(2);
    }

    public void disabledInit()
    {
       

        teleopStateBroadcasted = false;
        autonomousStateBroadcasted = false;
    }

    public void autonomousInit()
    {
        System.out.println( "autonomousInit()" );

        disabledStateBroadcasted = false;
        teleopStateBroadcasted = false;

        // Get the time that the autonomous mode starts
        //autonomousStartTime = Timer.getUsClock();
        autonomousStartTime = Timer.getFPGATimestamp();
    }

    public void teleopInit()
    {
        System.out.println( "teleopInit()" );

        disabledStateBroadcasted = false;
        autonomousStateBroadcasted = false;

//        m_rightStick = new Joystick(2);
//        m_leftStick = new Joystick(1);
        m_xboxStick = new Joystick(1);


       

        /* Drive station code */
        m_ds = DriverStation.getInstance();
        m_dsLCD = DriverStationLCD.getInstance();

    }

    /**
     * This function is called periodically during autonomous
     *
     * Notes:
     *
     *  
     *
     */
    
    public void autonomousPeriodic()
    {
       

        processCamera();

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
        //Watchdog.getInstance().feed();

        if ( teleopStateBroadcasted == true )
        {
            System.out.println( "Teleop State" );
            teleopStateBroadcasted = false;
        }



       mecanumDrive.drive(m_xboxStick);
       
        checkButtons();
        updateDSLCD();
        processCamera();
    }

    /**
     * This function is called periodically during the disabled state
     *
     * What it needs to do:
     *
     *
     */
    public void disabledPeriodic()
    {
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
        {
            // drive the robot
        }
        else
        {
            // otherwise stop the robot
            mecanumDrive.stop();

        }
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
        System.out.println( "checkButtons()" );

        System.out.println( "LX: " + m_xboxStick.getRawAxis(1));
        System.out.flush();
        System.out.println( "LY: " + m_xboxStick.getRawAxis(2));
        System.out.flush();
        System.out.println( "RX: " + m_xboxStick.getRawAxis(4));
        System.out.flush();
        System.out.println( "RY: " + m_xboxStick.getRawAxis(5));
        System.out.flush();


    }

 



   
    
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
    {//TODO when enum is resolved, print the control configuration
       
       
        m_dsLCD.println(DriverStationLCD.Line.kUser3, 1, "Pn Kr St: ");
       // m_dsLCD.println(DriverStationLCD.Line.kUser4, 1, "Rbt spd : ");
        //m_dsLCD.println(DriverStationLCD.Line.kUser5, 1, "Rbt slip: ");
         m_dsLCD.println(DriverStationLCD.Line.kUser2, 1, "Control Mode: "+ mecanumDrive.getControlMode());
          m_dsLCD.println(DriverStationLCD.Line.kUser5, 1, "Xbox Trigger Value: "+(double)m_xboxStick.getRawAxis(3));

          m_dsLCD.updateLCD();

    }
/*
 * xbox buttons
 * 0
 * 1 A
 * 2 B
 * 3 X
 * 4 Y
 * 5 L Bumper
 * 6 R Bumper
 * 7 Back
 * 8 Start
 * 9 L Click
 */
 

}
