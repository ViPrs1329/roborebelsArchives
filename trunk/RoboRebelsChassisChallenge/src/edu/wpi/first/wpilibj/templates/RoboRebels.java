/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;

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
    DriverStation m_ds;                   // driver station object
    static DriverStationLCD m_dsLCD;                // driver station LCD object

    // Misc variable declarations
    static final int NUM_JOYSTICK_BUTTONS = 16;  // how many joystick buttons exist?
    static boolean disabledStateBroadcasted = false;
    static boolean teleopStateBroadcasted = false;
    static boolean autonomousStateBroadcasted = false;

    /**
     * Constructor
     */
    public void RoboRebels() {
        RRLogger.logDebug(this.getClass(), "RoboRebels()", "");
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
        RRLogger.logDebug(this.getClass(), "robotInit()", "new ADXL345_I2C()");

        // ******************
        int leftMotorChannel  = 2;
        int rightMotorChannel = 1;
        drive = new RRDrive(m_xboxStick, leftMotorChannel, rightMotorChannel);
        RRLogger.logDebug(this.getClass(), "robotInit()", "new RRDrive()");

        // ******************
        int spinnerMotorChannel = 3;
        gatherer = new RRGatherer(m_xboxStick, spinnerMotorChannel);
        RRLogger.logDebug(this.getClass(), "robotInit()", "new RRGatherer()");

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
     */
    public void autonomousPeriodic() {
        autonomous.auton_periodic();
    }

    /**
     * This function is called periodically during operator control
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
        if (disabledStateBroadcasted == true) {
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
