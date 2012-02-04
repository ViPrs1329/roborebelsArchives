
/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

/*
 * Joystick (xbox) button map
 * 0
 * 1 A
 * 2 B
 * 3 X
 * 4 Y
 * 5 L Bumper
 * 6 R Bumper
 * 7 Back
 * 8 Start
 * 9 L Stick Click
 */
/*
y4
x3  b2
a1


 * NOTES:
 *
 * - Watchdog is no longer required, however, we may want to use it
 *   if we feel that need for a that bit of safety.
 *
 * - There is code within the RobotDrive class which can handle
 *   Mecanum wheels!  This will help us out a lot.
 *
 * - Just for the sake of cleanliness, I think that we should
 *   delete the following modules:  RRKicker, RRGRabber, RRPullup and RRSpinner
 */
package edu.wpi.first.wpilibj.templates;



import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
//import edu.wpi.first.wpilibj.camera.AxisCamera;
//import edu.wpi.first.wpilibj.camera.AxisCameraException;
//import edu.wpi.first.wpilibj.image.*;
//import edu.wpi.first.wpilibj.image.NIVision.MeasurementType;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RoboRebels extends IterativeRobot {

    // Declare a variable to use to access the driver station object
    DriverStation m_ds;                   // driver station object
    DriverStationLCD m_dsLCD;                // driver station LCD object
    //AxisCamera cam;                    // camera object
    //CriteriaCollection cc;      // the criteria for doing the particle filter operation
    double autonomousStartTime;    // holds the start time for autonomous mode
    Joystick m_rightStick;		// joystick 1 (arcade stick or right tank stick)
    Joystick m_leftStick;		// joystick 2 (tank left stick)
    Joystick m_xboxStick;
    static final int NUM_JOYSTICK_BUTTONS = 16;  // how many joystick buttons exist?
    static boolean disabledStateBroadcasted = false;
    static boolean teleopStateBroadcasted = false;
    static boolean autonomousStateBroadcasted = false;
    double lastZValue;                         // last Z value for the dial on the joystick
    RRDrive drive;
    RobotDrive          m_robotDrive;
    RRTracker tracker = new RRTracker();
    double              robotDriveSensitivity = 0.25;       // sensitivity of the RobotDrive object
    boolean             tankDrive = false;



    /**
     * Constructor
     */
    public void RoboRebels() {
        System.out.println("RoboRebels()");

    }

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        System.out.println("robotInit()");
        m_robotDrive = new RobotDrive(4, 3, 2, 1, robotDriveSensitivity);

        

        //Watchdog.getInstance().setExpiration(0.75);



        // Camera init code
        //Timer.delay(5.0);
        //cam = AxisCamera.getInstance();


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

        //                              FL, FR, BL, BR

        m_leftStick = new Joystick(1);
        m_rightStick = new Joystick(2);
        m_xboxStick = new Joystick(3);//TODO test, check if problem is solved

        

//        cam = AxisCamera.getInstance();  // get an instance ofthe camera
//        cc = new CriteriaCollection();      // create the criteria for the particle filter
//        cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
//        cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);

        System.out.println("Robot Ready");
    }

    public void disabledInit() {
        teleopStateBroadcasted = false;
        autonomousStateBroadcasted = false;
    }

    public void autonomousInit() {
        System.out.println("autonomousInit()");

        disabledStateBroadcasted = false;
        teleopStateBroadcasted = false;

        // Get the time that the autonomous mode starts
        //autonomousStartTime = Timer.getUsClock();
        autonomousStartTime = Timer.getFPGATimestamp();
    }

    public void teleopInit() {
        System.out.println("teleopInit()");

        disabledStateBroadcasted = false;
        autonomousStateBroadcasted = false;
//        m_rightStick = new Joystick(2);
//        m_leftStick = new Joystick(1);




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
    public void autonomousPeriodic() {
        tracker.trackTarget();
    }

    /**
     * This function is called periodically during operator control
     *
     * ---------------------
     * This is the most important method in this class
     * ---------------------
     */
    public void teleopPeriodic() {
        if ( teleopStateBroadcasted == true )
        {
            System.out.println( "Teleop State" );
            teleopStateBroadcasted = false;
        }
        if ( tankDrive == true )
            drive.drive(true);
        else
            drive.drive(false);

    }

    /**
     * This function is called periodically during the disabled state
     *
     * What it needs to do:
     *
     *
     */
    public void disabledPeriodic() {
        //nothing right now
    }

    /**
     * The VM will try to call this function as often as possible during the autonomous state
     *
     */
    public void autonomousContinuous() {
        //nothing right now
    }

    /**
     * The VM will try to call this function as often as possible during the teleop state
     *
     */
    public void teleopContinuous() {
        
    }

    /**
     * The VM will try to call this function as often as possible during the disbabled state
     */
    public void disabledContinuous() {
    }

    /*
     * This method checks buttons and sets states accordingly
     */
    public void checkButtons() {
        //System.out.println( "checkButtons()" );
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


        /*
        System.out.println( "LX: " + m_xboxStick.getRawAxis(1));
        System.out.flush();
        System.out.println( "LY: " + m_xboxStick.getRawAxis(2));
        System.out.flush();
        System.out.println( "RX: " + m_xboxStick.getRawAxis(4));
        System.out.flush();
        System.out.println( "RY: " + m_xboxStick.getRawAxis(5));
        System.out.flush();
         */
    }
}
