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

    public static boolean DEBUG_ON = true;

    // Declare custom object vars
    RRDrive drive;
    RRAutonomous autonomous;
    RRGatherer gatherer;
    ADXL345_I2C accel;
    RobotDrive m_robotDrive;

    // Declare objects needed for the robot that might be used
    // in more than one location

    Joystick m_xboxStick;

    // Declare a variable to use to access the driver station object
    DriverStation       m_ds;                   // driver station object
    static DriverStationLCD    m_dsLCD;                // driver station LCD object

    // Misc variable declarations
    static final int    NUM_JOYSTICK_BUTTONS = 16;  // how many joystick buttons exist?
    static boolean      disabledStateBroadcasted = false;
    static boolean      teleopStateBroadcasted = false;
    static boolean      autonomousStateBroadcasted = false;

    /**
     * Constructor
     */
    public void RoboRebels() {
        RRLogger.logDebug(this.getClass(), "", "RoboRebels()");
    }

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {

        RRLogger.logDebug(this.getClass(), "robotInit()", "");
       m_dsLCD = DriverStationLCD.getInstance();
        m_xboxStick = new Joystick(3);
        RRLogger.logDebug(this.getClass(), "robotInit()", "Joysticks set");

        accel = new ADXL345_I2C(1, ADXL345_I2C.DataFormat_Range.k2G); // slot number is actually module number
        RRLogger.logDebug(this.getClass(), "robotInit()", "accel");

        // ******************
        drive = new RRDrive(2, 1);
        RRLogger.logDebug(this.getClass(), "robotInit()", "Drive");

        // ******************
        gatherer = new RRGatherer(3);
        RRLogger.logDebug(this.getClass(), "robotInit()", "Gatherer");

        // ********************
        autonomous = new RRAutonomous();

        RRLogger.logInfo(this.getClass(), "robotInit()", "Robot Ready");
    }

    public void disabledInit() {
        teleopStateBroadcasted = false;
        autonomousStateBroadcasted = false;
    }

    public void autonomousInit() {
        RRLogger.logDebug(this.getClass(), "autonomousInit()", "");

        disabledStateBroadcasted = false;
        teleopStateBroadcasted = false;

        autonomous.auton_init();
    }

    public void teleopInit() {
        disabledStateBroadcasted = false;
        autonomousStateBroadcasted = false;
        RRLogger.logDebug(this.getClass(), "teleopInit()", "Initialization Complete!");
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
            RRLogger.logDebug(this.getClass(), "teleopPeriodic()", "Teleop State");
            teleopStateBroadcasted = false;
        }

        // Using
        boolean tankDrive = false;
        drive.drive(tankDrive);
        RRLogger.logDebug(this.getClass(), "teleopPeriodic()", "Arcade Drive");
    }

    /**
     * This function is called periodically during the disabled state
     *
     * What it needs to do:
     *
     *
     */
    public void disabledPeriodic() {
        // The code below prevents a spam of "Disabled State" messages on the console
        if ( disabledStateBroadcasted == true ) {
            RRLogger.logDebug(this.getClass(), "disabledPeriodic()", "Disabled State");
            disabledStateBroadcasted = false;
        }
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
