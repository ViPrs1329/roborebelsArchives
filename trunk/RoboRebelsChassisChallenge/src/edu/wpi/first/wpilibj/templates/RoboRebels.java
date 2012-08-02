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
 * 10 R Stick Click
 * 
 * Axis
 * 
 *  ´1: Left Stick X Axis
-Left:Negative ; Right: Positive
´2: Left Stick Y Axis
-Up: Negative ; Down: Positive
´3: Triggers
-Left: Positive ; Right: Negative
´4: Right Stick X Axis
-Left: Negative ; Right: Positive
´5: Right Stick Y Axis
-Up: Negative ; Down: Positive
´6: Directional Pad (Not recommended, buggy)

 * 
 */

/*
 * 


 * NOTES:
 *
 *
 * - I have decided to clean up our code base a bit. - Derek W.
 * 
 * 
 * TODO:
 * 
 * - Migrate code out of RoboRebels.java into their respective classes
 * 
 * - All classees that depend on a Joystick should be passed joystick object(s)
 *   and handled within their classes
 * 
 */
package edu.wpi.first.wpilibj.templates;

import com.sun.squawk.util.MathUtils;

import edu.wpi.first.wpilibj.*;

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
    static DriverStationLCD m_dsLCD;                // driver station LCD object
    Joystick m_rightStick;           // joystick 1 (arcade stick or right tank stick)
    Joystick m_leftStick;            // joystick 2 (tank left stick)
    Joystick m_xboxStick;
    RRDrive drive;
    RRDriveThread driveThread;
    RRGatherer gatherer;
    ADXL345_I2C accel;
    RobotDrive m_robotDrive;
    RRButtonMap buttonMap;
    RRAutonomous autonomous;
    double autonomousStartTime;    // holds the start time for autonomous mode
    double robotDriveSensitivity = 0.25;       // sensitivity of the RobotDrive object
    boolean tankDrive = false;
    static double time_started_waiting;           // Time variables
    // PWM Channel constants
    final static int LEFT_DRIVE_CHANNEL = 1;
    final static int RIGHT_DRIVE_CHANNEL = 2;
    final static int SHOOTER_CHANNEL = 3;
    final static int TILT_CHANNEL = 7;
    final static int LAZY_SUSAN_CHANNEL = 8;
    final static int LOADER_CHANNEL = 5;
    final static int SPINNER_CHANNEL = 4;
    final static int BRIDGE_ARM_CHANNEL = 6;
    // Digital I/O constants
    final static int BOTTOM_BALL_SENSOR_CHANNEL = 1;
    final static int MIDDLE_BALL_SENSOR_CHANNEL = 2;
    final static int TOP_BALL_SENSOR_CHANNEL = 3;
    final static int TILT_LIMIT_SWITCH_CHANNEL = 4;
    final static boolean DEBUG_ON = false; //true;  // false;  //true;       // true;
    final static boolean MIN_DEBUG_ON = false;  // false;       // true;
    final static boolean TRACKER_DEBUG_ON = false;      //true;
    static final int NUM_JOYSTICK_BUTTONS = 16;  // how many joystick buttons exist?
    static boolean disabledStateBroadcasted = false;
    static boolean teleopStateBroadcasted = false;
    static boolean autonomousStateBroadcasted = false;
    int pwmTest = 0;
    boolean btnPressed = false;
    static boolean autonomous_complete = false;
    static boolean autonomous_mode_tracking = false;
    static boolean autonomous_tracking_failed = false;
    static boolean autonomous_mode = true;

    /*
     *          (\_/)
     *          (O.0)
     *           =o=
     *         (    ) <--- bunny ;)
     *          (  )
     *
     */
    /**
     * Constructor
     */
    public void RoboRebels() {
        RRLogger.logDebug(this.getClass(),"","RoboRebels()");

    }

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {

        RRLogger.logDebug(this.getClass(),"robotInit()","");

        m_dsLCD = DriverStationLCD.getInstance();

        m_leftStick = new Joystick(1);
        m_rightStick = new Joystick(2);
        m_xboxStick = new Joystick(3);
        RRLogger.logDebug(this.getClass(),"robotInit()","Joysticks set");

        buttonMap = new RRButtonMap(m_leftStick, m_rightStick, m_xboxStick);
        buttonMap.setControllers();
        RRLogger.logDebug(this.getClass(),"robotInit()","Button map");

        accel = new ADXL345_I2C(1, ADXL345_I2C.DataFormat_Range.k2G); // slot number is actually module number
        RRLogger.logDebug(this.getClass(),"robotInit()","accel");

        // ******************
        drive = new RRDrive(2, 1);
//        driveThread = new RRDriveThread();
//        driveThread.start();
//        drive = driveThread.getDrive();

        RRLogger.logDebug(this.getClass(),"robotInit()","Drive");

        // ******************
        gatherer = new RRGatherer(SPINNER_CHANNEL, LOADER_CHANNEL, BOTTOM_BALL_SENSOR_CHANNEL, MIDDLE_BALL_SENSOR_CHANNEL, TOP_BALL_SENSOR_CHANNEL);
        RRLogger.logDebug(this.getClass(),"robotInit()","Gatherer");

        // ********************
//        autonomous = new RRAutonomous(dipSwitch, tracker, shooter, sensor, gathererThread.getGatherer());
        autonomous = new RRAutonomous();

        RRLogger.logInfo(this.getClass(),"robotInit()","Robot Ready");
    }

    public void disabledInit() {
        teleopStateBroadcasted = false;
        autonomousStateBroadcasted = false;
    }

    public void autonomousInit() {
        RRLogger.logDebug(this.getClass(),"autonomousInit()","");

        disabledStateBroadcasted = false;
        teleopStateBroadcasted = false;

        // Get the time that the autonomous mode starts
        autonomousStartTime = Timer.getFPGATimestamp();

        autonomous_mode = true;
        autonomous_complete = false;

        autonomous.auton_init();
    }

    public void teleopInit() {

        disabledStateBroadcasted = false;
        autonomousStateBroadcasted = false;
        tankDrive = false;
        autonomous_mode = false;
        autonomous_mode_tracking = false;

        /* Drive station code */
        //m_ds = DriverStation.getInstance();
        //m_dsLCD = DriverStationLCD.getInstance();
        RRLogger.logDebug(this.getClass(),"teleopInit()","Initialization Complete!");
    }

    /**
     * This function is called periodically during autonomous
     *
     * Notes:
     *
     */
    public void autonomousPeriodic() {
        autonomous.auton_periodic();
    }

    /**
     * This function is called periodically during operator control
     *
     * ---------------------
     * This is the most important method in this class
     * ---------------------
     */
    public void teleopPeriodic() {
        if (teleopStateBroadcasted == true) {
            RRLogger.logDebug(this.getClass(),"teleopPeriodic()", "Teleop State" );
            teleopStateBroadcasted = false;
        }

        //***************
        if ( tankDrive == true ) {
            drive.drive(true);
            RRLogger.logDebug(this.getClass(),"teleopPeriodic()","Tank Drive");
        }
        else{
            drive.drive(false);
            RRLogger.logDebug(this.getClass(),"teleopPeriodic()","Arcade Drive");
        }
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
     * 
     * NOTE:  Input checking should be put into their respective classes.  For 
     * reference, see RRShooter.
     */
    public void checkButtons() {
        RRLogger.logDebug(this.getClass(),"checkButtons()", "checkButtons()" );
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
        if (m_leftStick.getRawButton(6) && btnPressed == false) {
        btnPressed = true;
        pwmTest++;

        if (currentPWM != null)
        currentPWM.setRaw(0);

        if (pwmTest == 8)
        pwmTest = 1;

        RRLogger.logDebug(this.getClass(),"checkButtons()","PWM #" + pwmTest);

        currentPWM = new PWM(pwmTest);
        currentPWM.setRaw(128);
        RRLogger.logDebug(this.getClass(),"checkButtons()","Pwm Test done with channel");
        }

        if (!m_leftStick.getRawButton(6) && btnPressed == true) {
        btnPressed = false;
        }
         *
         */
        /*
        RRLogger.logDebug(this.getClass(),"checkButtons()", "LX: " + m_xboxStick.getRawAxis(1));
        System.out.flush();
        RRLogger.logDebug(this.getClass(),"checkButtons()", "LY: " + m_xboxStick.getRawAxis(2));
        System.out.flush();
        RRLogger.logDebug(this.getClass(),"checkButtons()", "RX: " + m_xboxStick.getRawAxis(4));
        System.out.flush();
        RRLogger.logDebug(this.getClass(),"checkButtons()", "RY: " + m_xboxStick.getRawAxis(5));
        System.out.flush();
         */
    }

    public static void printLCD(int lineNum, String value) {
        DriverStationLCD.Line line = null;
        switch (lineNum) {
            case 2:
                line = DriverStationLCD.Line.kUser2;
                break;
            case 3:
                line = DriverStationLCD.Line.kUser3;
                break;
            case 4:
                line = DriverStationLCD.Line.kUser4;
                break;
            case 5:
                line = DriverStationLCD.Line.kUser5;
                break;
            case 6:
                line = DriverStationLCD.Line.kMain6;
                break;
        }
        m_dsLCD.println(line, 1, value);
        m_dsLCD.updateLCD();
    }
}
